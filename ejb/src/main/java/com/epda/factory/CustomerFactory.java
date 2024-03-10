package com.epda.factory;

import com.epda.model.Customer;
import net.datafaker.Faker;

public class CustomerFactory {

    private static final Faker faker = new Faker();

    public static Customer create() {
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        String phone = faker.phoneNumber().cellPhone();
        int age = faker.number().numberBetween(18, 100);
        return new Customer(name, email, phone, age);
    }
}
