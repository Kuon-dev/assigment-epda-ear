package com.epda.factory;

import com.epda.model.Veterinarian;
import com.epda.model.enums.Expertise;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class VeterinarianFactory {

    private static final Random random = new Random();

    public static Veterinarian createVeterinarian() {
        Veterinarian veterinarian = new Veterinarian();
        UserFactory.populateUserFields(veterinarian);

        List<Expertise> expertises = generateRandomExpertises(1, 5); // Each vet has 1 to 5 areas of expertise
        veterinarian.setExpertises(expertises);

        return veterinarian;
    }

    private static List<Expertise> generateRandomExpertises(int min, int max) {
        List<Expertise> selectedExpertises = new ArrayList<>(
            EnumSet.allOf(Expertise.class)
        );
        Collections.shuffle(selectedExpertises); // Randomize the order of expertise
        int numberOfExpertises = min + random.nextInt(max - min + 1); // Determine how many expertises to assign (between min and max, inclusively)

        // Return a sublist containing a random selection of expertises
        return selectedExpertises.subList(0, numberOfExpertises);
    }
}
