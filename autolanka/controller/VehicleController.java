package org.web.autolanka.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.web.autolanka.entity.Vehicle;
import org.web.autolanka.entity.VehicleOwner;
import org.web.autolanka.service.AuthService;
import org.web.autolanka.service.VehicleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/vehicle")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private AuthService authService;
    
    // Helper method to ensure uploads directory exists
    private void ensureUploadsDirectoryExists() {
        try {
            Path uploadsDir = Paths.get(System.getProperty("user.dir"), "uploads");
            if (!Files.exists(uploadsDir)) {
                Files.createDirectories(uploadsDir);
                System.out.println("Created uploads directory: " + uploadsDir.toString());
            } else {
                System.out.println("Uploads directory already exists: " + uploadsDir.toString());
            }
        } catch (Exception e) {
            System.err.println("Error creating uploads directory: " + e.getMessage());
        }
    }
    
    // Helper method to check if any photo in the array is non-empty
    private boolean hasNonEmptyPhoto(MultipartFile[] photos) {
        if (photos == null) {
            return false;
        }
        for (MultipartFile photo : photos) {
            if (photo != null && !photo.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    // Helper method to extract file name from path
    private String extractFileNameFromPath(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        // Extract the file name from the path (e.g., /uploads/license_12345_filename.jpg -> license_12345_filename.jpg)
        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < path.length() - 1) {
            return path.substring(lastSlashIndex + 1);
        }
        return path;
    }
    
    // Helper method to delete a file from the uploads folder
    private void deleteUploadFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return;
        }
        
        try {
            String fileName = extractFileNameFromPath(filePath);
            if (fileName != null) {
                // Use the current working directory + uploads folder
                Path path = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
                System.out.println("Attempting to delete file: " + path.toString());
                if (Files.exists(path)) {
                    Files.delete(path);
                    System.out.println("Successfully deleted file: " + path.toString());
                } else {
                    System.out.println("File not found, skipping deletion: " + path.toString());
                }
            }
        } catch (Exception e) {
            // Log the error but don't fail the operation
            System.err.println("Error deleting file: " + filePath + " - " + e.getMessage());
        }
    }
    
    // Helper method to delete multiple photo files
    private void deletePhotoFiles(String photoPaths) {
        if (photoPaths == null || photoPaths.isEmpty()) {
            return;
        }
        
        try {
            String[] paths = photoPaths.split(",");
            for (String path : paths) {
                String fileName = extractFileNameFromPath(path.trim());
                if (fileName != null) {
                    // Use the current working directory + uploads folder
                    Path filePath = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
                    System.out.println("Attempting to delete photo file: " + filePath.toString());
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                        System.out.println("Successfully deleted photo file: " + filePath.toString());
                    } else {
                        System.out.println("Photo file not found, skipping deletion: " + filePath.toString());
                    }
                }
            }
        } catch (Exception e) {
            // Log the error but don't fail the operation
            System.err.println("Error deleting photo files: " + photoPaths + " - " + e.getMessage());
        }
    }
    
    // Helper method to create a Vehicle object from form parameters
    private Vehicle createVehicleFromParams(String vehicleNumber, String make, String model, Integer registerYear, 
                                          String vehicleType, Integer seatCapacity, java.math.BigDecimal rentalPricePerDay,
                                          String color, String fuelType, String transmission, String licenseNumber, 
                                          String insuranceDetails, String availabilityStatus, 
                                          String maintenanceRecords) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(vehicleNumber);
        vehicle.setMake(make);
        vehicle.setModel(model);
        vehicle.setRegisterYear(registerYear);
        vehicle.setVehicleType(vehicleType);
        vehicle.setSeatCapacity(seatCapacity);
        vehicle.setRentalPricePerDay(rentalPricePerDay);
        vehicle.setColor(color);
        vehicle.setFuelType(fuelType);
        vehicle.setTransmission(transmission);
        vehicle.setLicenseNumber(licenseNumber);
        if (insuranceDetails != null) {
            vehicle.setInsuranceDetails(insuranceDetails);
        }
        if (availabilityStatus != null) {
            vehicle.setAvailabilityStatus(availabilityStatus);
        }
        if (maintenanceRecords != null) {
            vehicle.setMaintenanceRecords(maintenanceRecords);
        }
        return vehicle;
    }
    
    // Helper method to add owner information to model
    private void addOwnerInfoToModel(HttpSession session, Model model) {
        // Always ensure owner info attributes are set
        model.addAttribute("ownerName", "");
        model.addAttribute("ownerEmail", "");
        model.addAttribute("ownerRole", "");
        
        // Debug information about session
        String userType = (String) session.getAttribute("userType");
        Long userId = (Long) session.getAttribute("userId");
        
        if (userType == null || userId == null) {
            model.addAttribute("ownerName", "No session data: userType=" + userType + ", userId=" + userId);
            model.addAttribute("ownerEmail", "N/A");
            model.addAttribute("ownerRole", "No Session");
            return;
        }
        
        if (!"vehicle_owner".equals(userType)) {
            model.addAttribute("ownerName", "Not a vehicle owner: " + userType);
            model.addAttribute("ownerEmail", "N/A");
            model.addAttribute("ownerRole", userType);
            return;
        }
        
        // Find the vehicle owner by user ID, not owner ID
        Optional<VehicleOwner> ownerOpt = authService.findVehicleOwnerByUserId(userId);
        if (ownerOpt.isPresent()) {
            VehicleOwner owner = ownerOpt.get();
            if (owner.getUser() != null) {
                model.addAttribute("ownerName", owner.getUser().getFullName());
                model.addAttribute("ownerEmail", owner.getUser().getEmail());
                model.addAttribute("ownerRole", "Vehicle Owner");
            } else {
                model.addAttribute("ownerName", "User data not found for owner ID: " + userId);
                model.addAttribute("ownerEmail", "N/A");
                model.addAttribute("ownerRole", "Unknown");
            }
        } else {
            // Debug information if owner not found
            model.addAttribute("ownerName", "Owner record not found for User ID: " + userId);
            model.addAttribute("ownerEmail", "N/A");
            model.addAttribute("ownerRole", "Unknown");
        }
    }
    
    // Show vehicle add/edit form
    @GetMapping("/add")
    public String showAddVehicleForm(HttpSession session, Model model) {
        // Check if user is logged in as vehicle owner
        String userType = (String) session.getAttribute("userType");
        if (!"vehicle_owner".equals(userType)) {
            return "redirect:/vehicle-owner-login";
        }
        
        // Add owner information to the model
        addOwnerInfoToModel(session, model);
        
        model.addAttribute("vehicle", new Vehicle());
        return "vehicle-add";
    }
    
    // Handle vehicle submission
    @PostMapping("/save")
    public String saveVehicle(@RequestParam String vehicleNumber,
                             @RequestParam String make,
                             @RequestParam String model,
                             @RequestParam Integer registerYear,
                             @RequestParam String vehicleType,
                             @RequestParam Integer seatCapacity,
                             @RequestParam java.math.BigDecimal rentalPricePerDay,
                             @RequestParam String color,
                             @RequestParam String fuelType,
                             @RequestParam String transmission,
                             @RequestParam String licenseNumber,
                             @RequestParam(required = false) String insuranceDetails,
                             @RequestParam(required = false) String availabilityStatus,
                             @RequestParam(required = false) String maintenanceRecords,
                             @RequestParam("photos") MultipartFile[] photos,
                             @RequestParam("licenseDocument") MultipartFile licenseDocument,
                             @RequestParam(value = "insuranceDocument", required = false) MultipartFile insuranceDocument,
                             HttpSession session, 
                             Model modelObj) {
        // Check if user is logged in as vehicle owner
        String userType = (String) session.getAttribute("userType");
        if (!"vehicle_owner".equals(userType)) {
            modelObj.addAttribute("errorMessage", "Access denied. Please log in as a vehicle owner.");
            return "redirect:/vehicle-owner-login";
        }
        
        try {
            // Ensure uploads directory exists
            ensureUploadsDirectoryExists();
            
            // Get owner ID from session (this is the user ID)
            Long userId = (Long) session.getAttribute("userId");
            Optional<VehicleOwner> ownerOpt = authService.findVehicleOwnerByUserId(userId);
            
            if (ownerOpt.isPresent()) {
                VehicleOwner owner = ownerOpt.get();
                Vehicle vehicle = new Vehicle();
                vehicle.setOwner(owner);
                vehicle.setVehicleNumber(vehicleNumber);
                vehicle.setMake(make);
                vehicle.setModel(model);
                vehicle.setRegisterYear(registerYear);
                vehicle.setVehicleType(vehicleType);
                vehicle.setSeatCapacity(seatCapacity);
                vehicle.setRentalPricePerDay(rentalPricePerDay);
                vehicle.setColor(color);
                vehicle.setFuelType(fuelType);
                vehicle.setTransmission(transmission);
                vehicle.setLicenseNumber(licenseNumber);
                if (insuranceDetails != null) {
                    vehicle.setInsuranceDetails(insuranceDetails);
                }
                if (availabilityStatus != null && !availabilityStatus.isEmpty()) {
                    vehicle.setAvailabilityStatus(availabilityStatus);
                } else {
                    vehicle.setAvailabilityStatus("AVAILABLE");
                }
                if (maintenanceRecords != null) {
                    vehicle.setMaintenanceRecords(maintenanceRecords);
                }
                
                // Handle license document upload
                if (licenseDocument == null || licenseDocument.isEmpty()) {
                    modelObj.addAttribute("errorMessage", "License document is required.");
                    modelObj.addAttribute("vehicle", createVehicleFromParams(vehicleNumber, make, model, registerYear, vehicleType, seatCapacity, rentalPricePerDay, color, fuelType, transmission, licenseNumber, insuranceDetails, availabilityStatus, maintenanceRecords));
                    addOwnerInfoToModel(session, modelObj);
                    return "vehicle-add";
                }
                
                String fileName = "license_" + System.currentTimeMillis() + "_" + licenseDocument.getOriginalFilename();
                Path path = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, licenseDocument.getBytes());
                vehicle.setLicenseDocumentPath("/uploads/" + fileName);
                
                // Handle insurance document upload
                if (insuranceDocument != null && !insuranceDocument.isEmpty()) {
                    fileName = "insurance_" + System.currentTimeMillis() + "_" + insuranceDocument.getOriginalFilename();
                    path = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
                    Files.createDirectories(path.getParent());
                    Files.write(path, insuranceDocument.getBytes());
                    vehicle.setInsuranceDocumentPath("/uploads/" + fileName);
                }
                
                // Handle photo uploads
                StringBuilder photoPaths = new StringBuilder();
                for (MultipartFile photo : photos) {
                    if (photo != null && !photo.isEmpty()) {
                        // Save photo to disk (in a real app, you'd want to store in a proper location)
                        fileName = "vehicle_" + System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                        path = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
                        Files.createDirectories(path.getParent());
                        Files.write(path, photo.getBytes());
                        
                        if (photoPaths.length() > 0) {
                            photoPaths.append(",");
                        }
                        photoPaths.append("/uploads/").append(fileName);
                    }
                }
                
                if (photoPaths.length() > 0) {
                    vehicle.setPhotoPaths(photoPaths.toString());
                }
                
                // Save vehicle
                vehicleService.saveVehicle(vehicle);
                
                modelObj.addAttribute("successMessage", "Vehicle added successfully!");
                modelObj.addAttribute("vehicle", new Vehicle()); // Reset form
            } else {
                modelObj.addAttribute("errorMessage", "Vehicle owner not found.");
                modelObj.addAttribute("vehicle", new Vehicle()); // Reset form
            }
        } catch (IOException e) {
            modelObj.addAttribute("errorMessage", "Error uploading documents: " + e.getMessage());
            modelObj.addAttribute("vehicle", new Vehicle()); // Reset form
        } catch (Exception e) {
            modelObj.addAttribute("errorMessage", "Error saving vehicle: " + e.getMessage());
            modelObj.addAttribute("vehicle", new Vehicle()); // Reset form
        }
        
        // Add owner information to the model for display after submission
        addOwnerInfoToModel(session, modelObj);
        
        return "vehicle-add";
    }
    
    // View all vehicles for the owner
    @GetMapping("/my-vehicles")
    public String viewMyVehicles(HttpSession session, Model model) {
        // Check if user is logged in as vehicle owner
        String userType = (String) session.getAttribute("userType");
        if (!"vehicle_owner".equals(userType)) {
            return "redirect:/vehicle-owner-login";
        }
        
        Long userId = (Long) session.getAttribute("userId");
        Optional<VehicleOwner> ownerOpt = authService.findVehicleOwnerByUserId(userId);
        if (ownerOpt.isPresent()) {
            Long ownerId = ownerOpt.get().getOwnerId();
            List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
            model.addAttribute("vehicles", vehicles);
        }
        
        // Add owner information for display
        addOwnerInfoToModel(session, model);
        
        return "vehicle-list";
    }
    
    // Edit vehicle form
    @GetMapping("/edit/{id}")
    public String showEditVehicleForm(@PathVariable Long id, HttpSession session, Model model) {
        // Check if user is logged in as vehicle owner
        String userType = (String) session.getAttribute("userType");
        if (!"vehicle_owner".equals(userType)) {
            return "redirect:/vehicle-owner-login";
        }
        
        Optional<Vehicle> vehicleOpt = vehicleService.getVehicleById(id);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            // Check if this vehicle belongs to the current owner
            Long userId = (Long) session.getAttribute("userId");
            Optional<VehicleOwner> ownerOpt = authService.findVehicleOwnerByUserId(userId);
            if (ownerOpt.isPresent()) {
                Long ownerId = ownerOpt.get().getOwnerId();
                if (vehicle.getOwner().getOwnerId().equals(ownerId)) {
                    model.addAttribute("vehicle", vehicle);
                    
                    // Add owner information for display
                    addOwnerInfoToModel(session, model);
                    
                    return "vehicle-add"; // Reuse the add form for editing
                }
            }
        }
        
        return "redirect:/vehicle/my-vehicles";
    }
    
    // Update vehicle
    @PostMapping("/update/{id}")
    public String updateVehicle(@PathVariable Long id, 
                               @RequestParam String vehicleNumber,
                               @RequestParam String make,
                               @RequestParam String model,
                               @RequestParam Integer registerYear,
                               @RequestParam String vehicleType,
                               @RequestParam Integer seatCapacity,
                               @RequestParam java.math.BigDecimal rentalPricePerDay,
                               @RequestParam String color,
                               @RequestParam String fuelType,
                               @RequestParam String transmission,
                               @RequestParam String licenseNumber,
                               @RequestParam(required = false) String insuranceDetails,
                               @RequestParam(required = false) String availabilityStatus,
                               @RequestParam(required = false) String maintenanceRecords,
                               @RequestParam(value = "photos", required = false) MultipartFile[] photos,
                               @RequestParam(value = "licenseDocument", required = false) MultipartFile licenseDocument,
                               @RequestParam(value = "insuranceDocument", required = false) MultipartFile insuranceDocument,
                               HttpServletRequest request,
                               HttpSession session, 
                               Model modelObj) {
        // Check if user is logged in as vehicle owner
        String userType = (String) session.getAttribute("userType");
        if (!"vehicle_owner".equals(userType)) {
            modelObj.addAttribute("errorMessage", "Access denied. Please log in as a vehicle owner.");
            return "redirect:/vehicle-owner-login";
        }
        
        try {
            // Ensure uploads directory exists
            ensureUploadsDirectoryExists();
            
            // Check if this vehicle belongs to the current owner
            Long userId = (Long) session.getAttribute("userId");
            Optional<VehicleOwner> ownerOpt = authService.findVehicleOwnerByUserId(userId);
            Optional<Vehicle> vehicleOpt = vehicleService.getVehicleById(id);
            
            if (ownerOpt.isPresent() && vehicleOpt.isPresent()) {
                Vehicle vehicle = vehicleOpt.get();
                Long ownerId = ownerOpt.get().getOwnerId();
                if (vehicle.getOwner().getOwnerId().equals(ownerId)) {
                    // Update vehicle details
                    vehicle.setVehicleNumber(vehicleNumber);
                    vehicle.setMake(make);
                    vehicle.setModel(model);
                    vehicle.setRegisterYear(registerYear);
                    vehicle.setVehicleType(vehicleType);
                    vehicle.setSeatCapacity(seatCapacity);
                    vehicle.setRentalPricePerDay(rentalPricePerDay);
                    vehicle.setColor(color);
                    vehicle.setFuelType(fuelType);
                    vehicle.setTransmission(transmission);
                    vehicle.setLicenseNumber(licenseNumber);
                    if (insuranceDetails != null) {
                        vehicle.setInsuranceDetails(insuranceDetails);
                    }
                    if (availabilityStatus != null && !availabilityStatus.isEmpty()) {
                        vehicle.setAvailabilityStatus(availabilityStatus);
                    } else if (availabilityStatus == null || availabilityStatus.isEmpty()) {
                        // Keep existing status if not provided
                    }
                    if (maintenanceRecords != null) {
                        vehicle.setMaintenanceRecords(maintenanceRecords);
                    }
                    
                    // Handle license document upload if provided
                    if (licenseDocument != null && !licenseDocument.isEmpty()) {
                        // Delete the existing license document file if it exists
                        if (vehicle.getLicenseDocumentPath() != null && !vehicle.getLicenseDocumentPath().isEmpty()) {
                            deleteUploadFile(vehicle.getLicenseDocumentPath());
                        }
                        
                        String fileName = "license_" + System.currentTimeMillis() + "_" + licenseDocument.getOriginalFilename();
                        Path path = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
                        Files.createDirectories(path.getParent());
                        Files.write(path, licenseDocument.getBytes());
                        vehicle.setLicenseDocumentPath("/uploads/" + fileName);
                    } 
                    // If removeLicense parameter is present, remove existing license
                    else if (request.getParameter("removeLicense") != null) {
                        // Delete the existing license document file if it exists
                        if (vehicle.getLicenseDocumentPath() != null && !vehicle.getLicenseDocumentPath().isEmpty()) {
                            deleteUploadFile(vehicle.getLicenseDocumentPath());
                        }
                        vehicle.setLicenseDocumentPath(""); // Clear the path
                    }
                    
                    // Handle insurance document upload if provided
                    if (insuranceDocument != null && !insuranceDocument.isEmpty()) {
                        // Delete the existing insurance document file if it exists
                        if (vehicle.getInsuranceDocumentPath() != null && !vehicle.getInsuranceDocumentPath().isEmpty()) {
                            deleteUploadFile(vehicle.getInsuranceDocumentPath());
                        }
                        
                        String fileName = "insurance_" + System.currentTimeMillis() + "_" + insuranceDocument.getOriginalFilename();
                        Path path = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
                        Files.createDirectories(path.getParent());
                        Files.write(path, insuranceDocument.getBytes());
                        vehicle.setInsuranceDocumentPath("/uploads/" + fileName);
                    }
                    // If removeInsurance parameter is present, remove existing insurance
                    else if (request.getParameter("removeInsurance") != null) {
                        // Delete the existing insurance document file if it exists
                        if (vehicle.getInsuranceDocumentPath() != null && !vehicle.getInsuranceDocumentPath().isEmpty()) {
                            deleteUploadFile(vehicle.getInsuranceDocumentPath());
                        }
                        vehicle.setInsuranceDocumentPath(""); // Clear the path
                    }
                    
                    // Handle photo uploads if provided
                    if (hasNonEmptyPhoto(photos)) {
                        // Delete existing photo files
                        if (vehicle.getPhotoPaths() != null && !vehicle.getPhotoPaths().isEmpty()) {
                            deletePhotoFiles(vehicle.getPhotoPaths());
                        }
                        
                        // Upload new photos
                        StringBuilder photoPaths = new StringBuilder();
                        for (MultipartFile photo : photos) {
                            if (photo != null && !photo.isEmpty()) {
                                String fileName = "vehicle_" + System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                                Path path = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
                                Files.createDirectories(path.getParent());
                                Files.write(path, photo.getBytes());
                                
                                if (photoPaths.length() > 0) {
                                    photoPaths.append(",");
                                }
                                photoPaths.append("/uploads/").append(fileName);
                            }
                        }
                        
                        if (photoPaths.length() > 0) {
                            vehicle.setPhotoPaths(photoPaths.toString());
                        }
                    }
                    // If removePhotos parameter is present, remove all existing photos
                    else if (request.getParameter("removePhotos") != null && "true".equals(request.getParameter("removePhotos"))) {
                        // Delete existing photo files
                        if (vehicle.getPhotoPaths() != null && !vehicle.getPhotoPaths().isEmpty()) {
                            deletePhotoFiles(vehicle.getPhotoPaths());
                        }
                        vehicle.setPhotoPaths(""); // Clear the paths
                    }
                    
                    // Save updated vehicle
                    vehicleService.updateVehicle(id, vehicle);
                    
                    modelObj.addAttribute("successMessage", "Vehicle updated successfully!");
                    modelObj.addAttribute("vehicle", vehicle);
                } else {
                    modelObj.addAttribute("errorMessage", "You don't have permission to edit this vehicle.");
                }
            } else {
                modelObj.addAttribute("errorMessage", "Vehicle not found.");
            }
        } catch (IOException e) {
            modelObj.addAttribute("errorMessage", "Error uploading documents: " + e.getMessage());
            // Add the vehicle data back to the model so the form is populated
            Optional<Vehicle> vehicleOpt = vehicleService.getVehicleById(id);
            if (vehicleOpt.isPresent()) {
                modelObj.addAttribute("vehicle", vehicleOpt.get());
                addOwnerInfoToModel(session, modelObj);
                return "vehicle-add";
            }
            return "redirect:/vehicle/my-vehicles";
        } catch (Exception e) {
            modelObj.addAttribute("errorMessage", "Error updating vehicle: " + e.getMessage());
            // Add the vehicle data back to the model so the form is populated
            Optional<Vehicle> vehicleOpt = vehicleService.getVehicleById(id);
            if (vehicleOpt.isPresent()) {
                modelObj.addAttribute("vehicle", vehicleOpt.get());
                addOwnerInfoToModel(session, modelObj);
                return "vehicle-add";
            }
            return "redirect:/vehicle/my-vehicles";
        }
        
        // Add owner information for display
        addOwnerInfoToModel(session, modelObj);
        
        return "vehicle-add";
    }
    
    // View available vehicles for customers
    @GetMapping("/search")
    public String searchVehicles(@RequestParam(required = false) String pickupLocation,
                                @RequestParam(required = false) String pickupDate,
                                @RequestParam(required = false) String dropoffDate,
                                @RequestParam(required = false) String vehicleType,
                                Model model) {
        System.out.println("VehicleController: Searching vehicles with params - pickupLocation: " + pickupLocation + 
                          ", pickupDate: " + pickupDate + ", dropoffDate: " + dropoffDate + ", vehicleType: " + vehicleType);
        
        // Get only fully approved vehicles (both insurance and staff) that are available
        List<Vehicle> vehicles = vehicleService.getFullyApprovedVehicles();
        vehicles = vehicles.stream()
                .filter(v -> "AVAILABLE".equals(v.getAvailabilityStatus()))
                .collect(Collectors.toList());
        
        // Apply filters if provided
        if (vehicleType != null && !vehicleType.isEmpty()) {
            vehicles = vehicles.stream()
                    .filter(v -> vehicleType.equals(v.getVehicleType()))
                    .collect(Collectors.toList());
        }
        
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("pickupLocation", pickupLocation);
        model.addAttribute("pickupDate", pickupDate);
        model.addAttribute("dropoffDate", dropoffDate);
        model.addAttribute("vehicleType", vehicleType);
        
        System.out.println("VehicleController: Found " + vehicles.size() + " vehicles");
        return "customer-vehicle-search";
    }
    
    // Show delete confirmation page
    @GetMapping("/delete/{id}")
    public String showDeleteVehicleForm(@PathVariable Long id, HttpSession session, Model model) {
        try {
            // Check if user is logged in as vehicle owner
            String userType = (String) session.getAttribute("userType");
            Long userId = (Long) session.getAttribute("userId");
            
            // If user is not logged in, redirect to login page
            if (userType == null || userId == null) {
                model.addAttribute("errorMessage", "Please log in as a vehicle owner to delete vehicles.");
                return "redirect:/vehicle-owner-login";
            }
            
            // If user is logged in but not as a vehicle owner, show error
            if (!"vehicle_owner".equals(userType)) {
                model.addAttribute("errorMessage", "Access denied. Only vehicle owners can delete vehicles.");
                // Redirect to appropriate dashboard based on user type
                switch (userType) {
                    case "admin":
                        return "redirect:/home/admin";
                    case "customer":
                        return "redirect:/home/customer";
                    case "staff":
                        return "redirect:/home/staff";
                    case "insurance_officer":
                        return "redirect:/home/insurance-officer";
                    case "financial_executive":
                        return "redirect:/home/financial-executive";
                    default:
                        return "redirect:/";
                }
            }
            
            // Find the vehicle owner
            Optional<VehicleOwner> ownerOpt = authService.findVehicleOwnerByUserId(userId);
            if (!ownerOpt.isPresent()) {
                model.addAttribute("errorMessage", "Vehicle owner record not found. Please contact support.");
                return "redirect:/vehicle-owner-login";
            }
            
            // Find the vehicle
            Optional<Vehicle> vehicleOpt = vehicleService.getVehicleById(id);
            if (!vehicleOpt.isPresent()) {
                model.addAttribute("errorMessage", "Vehicle not found.");
                // Reload the list of vehicles for the owner
                Long ownerId = ownerOpt.get().getOwnerId();
                List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
                model.addAttribute("vehicles", vehicles);
                addOwnerInfoToModel(session, model);
                return "vehicle-list";
            }
            
            // Check if the vehicle belongs to this owner
            Vehicle vehicle = vehicleOpt.get();
            Long ownerId = ownerOpt.get().getOwnerId();
            if (!vehicle.getOwner().getOwnerId().equals(ownerId)) {
                model.addAttribute("errorMessage", "Access denied. You don't own this vehicle.");
                // Reload the list of vehicles for the owner
                List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
                model.addAttribute("vehicles", vehicles);
                addOwnerInfoToModel(session, model);
                return "vehicle-list";
            }
            
            // Add vehicle to model for confirmation page
            model.addAttribute("vehicle", vehicle);
            
            // Add owner information for display
            addOwnerInfoToModel(session, model);
            
            return "vehicle-delete";
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error showing delete vehicle form: " + e.getMessage());
            e.printStackTrace();
            
            // Add error message to model
            model.addAttribute("errorMessage", "An error occurred while loading the delete confirmation page. Please try again.");
            
            try {
                // Try to reload the list of vehicles for the owner
                String userType = (String) session.getAttribute("userType");
                if ("vehicle_owner".equals(userType)) {
                    Long userId = (Long) session.getAttribute("userId");
                    Optional<VehicleOwner> ownerOpt = authService.findVehicleOwnerByUserId(userId);
                    if (ownerOpt.isPresent()) {
                        Long ownerId = ownerOpt.get().getOwnerId();
                        List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
                        model.addAttribute("vehicles", vehicles);
                    }
                }
            } catch (Exception ex) {
                // Ignore errors in error handling
            }
            
            // Add owner information for display
            addOwnerInfoToModel(session, model);
            
            return "vehicle-list";
        }
    }
    
    // Process vehicle deletion
    @PostMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Long id, HttpSession session, Model model) {
        System.out.println("Starting vehicle deletion process for ID: " + id);
        try {
            // Check if user is logged in as vehicle owner
            String userType = (String) session.getAttribute("userType");
            Long userId = (Long) session.getAttribute("userId");
            
            System.out.println("User type: " + userType + ", User ID: " + userId);
            
            // If user is not logged in, redirect to login page
            if (userType == null || userId == null) {
                System.out.println("User not logged in, redirecting to login");
                model.addAttribute("errorMessage", "Please log in as a vehicle owner to delete vehicles.");
                return "redirect:/vehicle-owner-login";
            }
            
            // If user is logged in but not as a vehicle owner, show error
            if (!"vehicle_owner".equals(userType)) {
                System.out.println("User is not a vehicle owner, redirecting to appropriate page");
                model.addAttribute("errorMessage", "Access denied. Only vehicle owners can delete vehicles.");
                // Redirect to appropriate dashboard based on user type
                switch (userType) {
                    case "admin":
                        return "redirect:/home/admin";
                    case "customer":
                        return "redirect:/home/customer";
                    case "staff":
                        return "redirect:/home/staff";
                    case "insurance_officer":
                        return "redirect:/home/insurance-officer";
                    case "financial_executive":
                        return "redirect:/home/financial-executive";
                    default:
                        return "redirect:/";
                }
            }
            
            // Find the vehicle owner
            Optional<VehicleOwner> ownerOpt = authService.findVehicleOwnerByUserId(userId);
            if (!ownerOpt.isPresent()) {
                System.out.println("Vehicle owner not found for user ID: " + userId);
                model.addAttribute("errorMessage", "Vehicle owner record not found. Please contact support.");
                return "redirect:/vehicle-owner-login";
            }
            
            // Find the vehicle
            Optional<Vehicle> vehicleOpt = vehicleService.getVehicleById(id);
            if (!vehicleOpt.isPresent()) {
                System.out.println("Vehicle not found with ID: " + id);
                model.addAttribute("errorMessage", "Vehicle not found.");
                // Reload the list of vehicles for the owner
                Long ownerId = ownerOpt.get().getOwnerId();
                List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
                model.addAttribute("vehicles", vehicles);
                addOwnerInfoToModel(session, model);
                return "vehicle-list";
            }
            
            // Check if the vehicle belongs to this owner
            Vehicle vehicle = vehicleOpt.get();
            Long ownerId = ownerOpt.get().getOwnerId();
            System.out.println("Vehicle owner ID: " + vehicle.getOwner().getOwnerId() + ", Expected owner ID: " + ownerId);
            
            if (!vehicle.getOwner().getOwnerId().equals(ownerId)) {
                System.out.println("Access denied - vehicle doesn't belong to this owner");
                model.addAttribute("errorMessage", "Access denied. You don't own this vehicle.");
                // Reload the list of vehicles for the owner
                List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
                model.addAttribute("vehicles", vehicles);
                addOwnerInfoToModel(session, model);
                return "vehicle-list";
            }
            
            // Log the deletion for debugging
            System.out.println("Deleting vehicle ID: " + id);
            System.out.println("Vehicle details: " + vehicle.getVehicleNumber() + " - " + vehicle.getVehicleType());
            
            // Delete associated files before deleting the vehicle
            try {
                // Delete license document
                if (vehicle.getLicenseDocumentPath() != null && !vehicle.getLicenseDocumentPath().isEmpty()) {
                    System.out.println("Deleting license document: " + vehicle.getLicenseDocumentPath());
                    deleteUploadFile(vehicle.getLicenseDocumentPath());
                }
                
                // Delete insurance document
                if (vehicle.getInsuranceDocumentPath() != null && !vehicle.getInsuranceDocumentPath().isEmpty()) {
                    System.out.println("Deleting insurance document: " + vehicle.getInsuranceDocumentPath());
                    deleteUploadFile(vehicle.getInsuranceDocumentPath());
                }
                
                // Delete photo files
                if (vehicle.getPhotoPaths() != null && !vehicle.getPhotoPaths().isEmpty()) {
                    System.out.println("Deleting photo files: " + vehicle.getPhotoPaths());
                    deletePhotoFiles(vehicle.getPhotoPaths());
                }
            } catch (Exception e) {
                System.err.println("Error deleting vehicle files: " + e.getMessage());
                e.printStackTrace();
                // Continue with vehicle deletion even if file deletion fails
            }
            
            // Delete the vehicle from database
            System.out.println("Deleting vehicle from database with ID: " + id);
            vehicleService.deleteVehicle(id);
            System.out.println("Vehicle deleted from database");
            model.addAttribute("successMessage", "Vehicle deleted successfully!");
            
            // Reload the list of vehicles for the owner
            System.out.println("Reloading vehicle list for owner ID: " + ownerId);
            List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
            System.out.println("Number of vehicles after deletion: " + (vehicles != null ? vehicles.size() : "null"));
            model.addAttribute("vehicles", vehicles);
            
            // Add owner information for display
            addOwnerInfoToModel(session, model);
            
            System.out.println("Returning to vehicle-list page");
            return "vehicle-list";
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error deleting vehicle: " + e.getMessage());
            e.printStackTrace();
            
            // Add error message to model
            model.addAttribute("errorMessage", "An error occurred while deleting the vehicle. This vehicle may have associated bookings or insurance records that need to be handled first. Please contact support.");
            
            try {
                // Try to reload the list of vehicles for the owner
                String userType = (String) session.getAttribute("userType");
                if ("vehicle_owner".equals(userType)) {
                    Long userId = (Long) session.getAttribute("userId");
                    Optional<VehicleOwner> ownerOpt = authService.findVehicleOwnerByUserId(userId);
                    if (ownerOpt.isPresent()) {
                        Long ownerId = ownerOpt.get().getOwnerId();
                        List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
                        model.addAttribute("vehicles", vehicles);
                    }
                }
            } catch (Exception ex) {
                // Ignore errors in error handling
                System.err.println("Error in error handling: " + ex.getMessage());
            }
            
            // Add owner information for display
            addOwnerInfoToModel(session, model);
            
            return "vehicle-list";
        }
    }
}