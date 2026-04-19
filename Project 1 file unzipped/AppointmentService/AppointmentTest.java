

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

public class AppointmentTest {

    private Date futureDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    private Date pastDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return cal.getTime();
    }

    @Test
    void testValidAppointmentCreation() {
        Appointment appt = new Appointment("APT001", futureDate(), "Annual checkup with primary care doctor.");
        assertEquals("APT001", appt.getAppointmentId());
        assertNotNull(appt.getAppointmentDate());
        assertEquals("Annual checkup with primary care doctor.", appt.getDescription());
    }

    @Test
    void testAppointmentIdNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Appointment(null, futureDate(), "Valid description here.")
        );
    }

    @Test
    void testAppointmentIdTooLong() {
        assertThrows(IllegalArgumentException.class, () ->
            new Appointment("12345678901", futureDate(), "Valid description here.")
        );
    }

    @Test
    void testAppointmentIdExactly10Characters() {
        Appointment appt = new Appointment("1234567890", futureDate(), "Valid description.");
        assertEquals("1234567890", appt.getAppointmentId());
    }

    @Test
    void testAppointmentIdNotUpdatable() {
        boolean hasSetterMethod = false;
        try {
            Appointment.class.getMethod("setAppointmentId", String.class);
            hasSetterMethod = true;
        } catch (NoSuchMethodException e) {
            hasSetterMethod = false;
        }
        assertFalse(hasSetterMethod, "Appointment ID should not be updatable.");
    }

    @Test
    void testAppointmentDateNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Appointment("APT002", null, "Valid description here.")
        );
    }

    @Test
    void testAppointmentDateInPast() {
        assertThrows(IllegalArgumentException.class, () ->
            new Appointment("APT003", pastDate(), "Valid description here.")
        );
    }

    @Test
    void testAppointmentDateInFuture() {
        Appointment appt = new Appointment("APT004", futureDate(), "Valid description here.");
        assertNotNull(appt.getAppointmentDate());
    }

    @Test
    void testDescriptionNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Appointment("APT005", futureDate(), null)
        );
    }

    @Test
    void testDescriptionTooLong() {
        String longDesc = "A".repeat(51);
        assertThrows(IllegalArgumentException.class, () ->
            new Appointment("APT006", futureDate(), longDesc)
        );
    }

    @Test
    void testDescriptionExactly50Characters() {
        String maxDesc = "A".repeat(50);
        Appointment appt = new Appointment("APT007", futureDate(), maxDesc);
        assertEquals(50, appt.getDescription().length());
    }
}