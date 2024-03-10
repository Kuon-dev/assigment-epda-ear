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

        // Randomly assign 1 to 3 unique expertises
        ArrayList<Expertise> expertises = ThreadLocalRandom.current()
            .ints(1, Expertise.values().length)
            .distinct() // This still ensures uniqueness
            .limit(ThreadLocalRandom.current().nextInt(1, 4))
            .mapToObj(i -> Expertise.values()[i - 1])
            .collect(Collectors.toCollection(ArrayList::new)); // Collect to an ArrayList

        veterinarian.setExpertises(expertises);
        return veterinarian;
    }
}
