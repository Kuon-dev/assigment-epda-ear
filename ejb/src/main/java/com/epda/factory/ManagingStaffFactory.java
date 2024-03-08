package com.epda.factory;

import com.epda.model.ManagingStaff;

public class ManagingStaffFactory {

    public static ManagingStaff createManagingStaff() {
        ManagingStaff staff = new ManagingStaff();
        UserFactory.populateUserFields(staff);
        // Additional specific fields for ManagingStaff can be set here
        return staff;
    }
}
