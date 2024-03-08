package com.epda.factory;

import com.epda.model.Appointment;
import com.epda.model.Pet;
import com.epda.model.Veterinarian;
import com.epda.model.enums.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.datafaker.Faker;

public class AppointmentFactory {

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
        LocalDateTime appointmentDate = getAppointmentDate(appointmentStatus);

        appointment.setPet(pet);
        appointment.setVeterinarian(veterinarian);
        appointment.setTimeSlot(timeSlots[random.nextInt(timeSlots.length)]);
        appointment.setStatus(appointmentStatus);
        appointment.setDiagnosis(faker.medical().symptoms());
        appointment.setPrognosis(faker.lorem().sentence());
        appointment.setAppointmentDate(appointmentDate);

        return appointment;
    }

    // check if the appointment status is completed or not, if not, set the appointment date to a future dateq
    private static LocalDateTime getAppointmentDate(
        AppointmentStatus appointmentStatus
    ) {
        if (appointmentStatus == AppointmentStatus.COMPLETED) {
            // Ensures completed appointments are always before today.
            // Randomly subtracts a number of days between 1 and 30 from the current date.
            return LocalDateTime.now().minusDays(random.nextInt(30) + 1);
        } else {
            // For scheduled or cancelled appointments, ensures the dates are in the future.
            // Randomly adds a number of days between 1 and 30 to the current date.
            return LocalDateTime.now().plusDays(random.nextInt(30) + 1);
        }
    }
}
