package com.epda.factory;

import com.epda.model.User;
import com.epda.model.enums.AccountStatus;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.concurrent.ThreadLocalRandom;
// import java.util.stream.Collectors;
import java.util.Random;
import net.datafaker.Faker;

public class UserFactory {

    private static final Faker faker = new Faker();
    private static AccountStatus[] accountStatus = AccountStatus.values();
    private static final Random random = new Random();

    public static void populateUserFields(User user) {
        user.setName(faker.name().fullName());
        user.setPhone(faker.phoneNumber().phoneNumber());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password());

        user.setStatus(accountStatus[(random.nextInt(accountStatus.length))]);
    }

    // If direct instantiation of User is allowed and makes sense in your model
    public static User createUser() {
        User user = new User(); // Adjust if User is abstract; this line is illustrative
        populateUserFields(user);
        return user;
    }
}
