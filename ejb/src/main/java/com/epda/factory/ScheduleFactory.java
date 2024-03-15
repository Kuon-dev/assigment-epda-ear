package com.epda.factory;

import com.epda.model.Schedule;
import com.epda.model.Veterinarian;
import com.epda.model.enums.TimeSlot;
import java.time.ZoneId;
// import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import net.datafaker.Faker;

public class ScheduleFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static Schedule create(Veterinarian veterinarian) {
        Schedule schedule = new Schedule();
        // randomly assign if a veterinarain or not
        schedule.setVeterinarian(veterinarian);
        // if (random.nextBoolean()) {
        //     schedule.setVeterinarian(veterinarian);
        // }
        schedule.setDate(
            faker
                .date()
                .future(365, TimeUnit.DAYS)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        );
        // either morning or afternoon
        schedule.setShift(
            TimeSlot.values()[random.nextInt(TimeSlot.values().length)]
        );
        // You should also initialize the createdAt and updatedAt fields
        return schedule;
    }
}
