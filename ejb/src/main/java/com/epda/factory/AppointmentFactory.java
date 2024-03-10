package com.epda.factory;

import com.epda.model.Appointment;
import com.epda.model.Pet;
import com.epda.model.Veterinarian;
import com.epda.model.enums.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.datafaker.Faker;

public class AppointmentFactory {

    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private static TimeSlot[] timeSlots = TimeSlot.values();

    // private static final TimeSlot[] timeSlots = TimeSlot.values();
    private static final AppointmentStatus[] appointmentStatuses =
        AppointmentStatus.values();

    public static Appointment create(Pet pet, Veterinarian veterinarian) {
        Appointment appointment = new Appointment();
        // Timeslot will have no all day appointments
        timeSlots = Arrays.stream(timeSlots)
            .filter(timeSlot -> timeSlot != TimeSlot.ALL_DAY)
            .toArray(TimeSlot[]::new);
        // no all day appointments
        AppointmentStatus appointmentStatus =
            appointmentStatuses[random.nextInt(appointmentStatuses.length)];

        LocalDate date = LocalDate.parse(getAppointmentDate(appointmentStatus));
        LocalDateTime appointmentDateTime = LocalDateTime.of(
            date,
            LocalTime.MIDNIGHT
        );

        appointment.setPet(pet);
        appointment.setVeterinarian(veterinarian);
        appointment.setTimeSlot(timeSlots[random.nextInt(timeSlots.length)]);
        appointment.setStatus(appointmentStatus);
        appointment.setDiagnosis(faker.medical().symptoms());
        appointment.setPrognosis(faker.lorem().sentence());
        appointment.setAppointmentDate(appointmentDateTime);

        return appointment;
    }

    // check if the appointment status is completed or not, if not, set the appointment date to a future dateq

    private static String getAppointmentDate(
        AppointmentStatus appointmentStatus
    ) {
        LocalDateTime date;
        if (appointmentStatus == AppointmentStatus.COMPLETED) {
            // Ensures completed appointments are always before today.
            date = LocalDateTime.now()
                .minusDays(ThreadLocalRandom.current().nextInt(1, 31));
        } else {
            // For scheduled or cancelled appointments, ensures the dates are in the future.
            date = LocalDateTime.now()
                .plusDays(ThreadLocalRandom.current().nextInt(1, 31));
        }
        return date.format(DATE_FORMATTER);
    }
}
