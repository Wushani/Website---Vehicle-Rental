// UserType.java
package org.web.autolanka.enums;

public enum UserType {
    ADMIN("Administrator"),
    CUSTOMER("Customer"),
    STAFF("Company Staff"),
    VEHICLE_OWNER("Vehicle Owner"),
    INSURANCE_OFFICER("Insurance Officer"),
    FINANCIAL_EXECUTIVE("Financial Executive");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
