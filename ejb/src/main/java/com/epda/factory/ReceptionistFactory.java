package com.epda.factory;

import com.epda.model.Receptionist;

public class ReceptionistFactory {

    public static Receptionist createReceptionist() {
        Receptionist receptionist = new Receptionist();
        UserFactory.populateUserFields(receptionist);
        // Additional specific fields for Receptionist can be set here if any
        return receptionist;
    }
}
