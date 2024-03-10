package com.epda.factory;

import com.epda.model.Veterinarian;
import com.epda.model.enums.Expertise;
import java.util.ArrayList;
// import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class VeterinarianFactory {

    public static Veterinarian createVeterinarian() {
        Veterinarian veterinarian = new Veterinarian();
        UserFactory.populateUserFields(veterinarian);

        // Adjusted to generate a stream of indices from 0 (inclusive) to Expertise.values().length (exclusive)
        ArrayList<Expertise> expertises = ThreadLocalRandom.current()
            .ints(0, Expertise.values().length) // Now includes the possibility of selecting the last expertise
            .distinct()
            .limit(ThreadLocalRandom.current().nextInt(1, 4)) // Adjusted to ensure up to 3 unique expertises
            .mapToObj(i -> Expertise.values()[i])
            .collect(Collectors.toCollection(ArrayList::new));

        veterinarian.setExpertises(expertises);
        return veterinarian;
    }
}
