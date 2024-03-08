package com.epda.factory;

import com.epda.model.Veterinarian;

public class VeterinarianFactory {

    public static Veterinarian createVeterinarian() {
        Veterinarian veterinarian = new Veterinarian();
        UserFactory.populateUserFields(veterinarian);
        // Additional specific fields for Veterinarian can be set here if any
        return veterinarian;
    }
}
