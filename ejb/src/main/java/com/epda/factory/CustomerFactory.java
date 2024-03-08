package com.epda.factory;

import com.epda.model.Customer;
import net.datafaker.Faker;

public class CustomerFactory {

    private static final Faker faker = new Faker();

    public static Customer create() {
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        return new Customer(name, email);
    }
}
