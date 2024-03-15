package com.epda.factory;

import com.epda.model.Pet;
import com.epda.model.enums.Expertise;
import java.util.List;
import java.util.Random;
import net.datafaker.Faker;

public class PetFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    private static final List<Expertise> expertiseList = List.of(
        Expertise.values()
    );

    public static Pet create() {
        Pet pet = new Pet();
        pet.setName(faker.name().firstName());
        pet.setType(
            expertiseList.get(random.nextInt(expertiseList.size())).toString()
        );
        pet.setBreed(faker.animal().name()); // This could be more specific based on the type, if desired
        pet.setAge(faker.number().numberBetween(1, 15));
        return pet;
    }
}
