package com.epda.factory;

import com.epda.model.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import net.datafaker.Faker;

public class UserFactory {

    private static final Faker faker = new Faker();

    public static void populateUserFields(User user) {
        user.setName(faker.name().fullName());
        user.setPhone(faker.phoneNumber().phoneNumber());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password());
        // user.setCreatedAt(
        //     LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        // );
        // user.setUpdatedAt(
        //     LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        // );
    }

    // If direct instantiation of User is allowed and makes sense in your model
    public static User createUser() {
        User user = new User(); // Adjust if User is abstract; this line is illustrative
        populateUserFields(user);
        return user;
    }
}
