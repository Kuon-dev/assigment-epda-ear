package com.epda.factory;

import net.datafaker.Faker;
import com.epda.model.Pet;
import java.util.List;
import java.util.Random;

public class PetFactory {

    private static final Faker faker = new Faker();
    private static final List<String> petTypes = List.of("Dog", "Cat", "Bird", "Rabbit", "Fish");
    private static final Random random = new Random();

    public static Pet create() {
        Pet pet = new Pet();
        pet.setName(faker.name().firstName());
        pet.setType(petTypes.get(random.nextInt(petTypes.size())));
        pet.setBreed(faker.animal().name()); // This could be more specific based on the type, if desired
        pet.setAge(faker.number().numberBetween(1, 15));
        return pet;
    }
}
