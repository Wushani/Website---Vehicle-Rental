// DatabaseInitializationService.java
package org.web.autolanka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.web.autolanka.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class DatabaseInitializationService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // No default users will be created
        System.out.println("Database initialization completed - no default users created");
    }
}