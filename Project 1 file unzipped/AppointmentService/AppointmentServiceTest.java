

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

public class AppointmentServiceTest {

    private AppointmentService service;

    private Date futureDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    @BeforeEach
    void setUp() {
        service = new AppointmentService();
    }

    @Test
    void testAddAppointment() {
        Appointment appt = new Appointment("APT100", futureDate(), "Follow-up visit with specialist.");
        service.addAppointment(appt);
        assertNotNull(service.getAppointment("APT100"));
    }

    @Test
    void testAddDuplicateAppointmentIdThrows() {
        Appointment appt1 = new Appointment("APT101", futureDate(), "First appointment entry.");
        Appointment appt2 = new Appointment("APT101", futureDate(), "Duplicate ID appointment.");
        service.addAppointment(appt1);
        assertThrows(IllegalArgumentException.class, () ->
            service.addAppointment(appt2)
        );
    }

    @Test
    void testAddMultipleUniqueAppointments() {
        Appointment appt1 = new Appointment("APT201", futureDate(), "Dentist appointment next week.");
        Appointment appt2 = new Appointment("APT202", futureDate(), "Physical therapy session scheduled.");
        service.addAppointment(appt1);
        service.addAppointment(appt2);
        assertNotNull(service.getAppointment("APT201"));
        assertNotNull(service.getAppointment("APT202"));
    }

    @Test
    void testDeleteAppointment() {
        Appointment appt = new Appointment("APT300", futureDate(), "Appointment to be deleted soon.");
        service.addAppointment(appt);
        service.deleteAppointment("APT300");
        assertNull(service.getAppointment("APT300"));
    }

    @Test
    void testDeleteNonExistentAppointmentThrows() {
        assertThrows(IllegalArgumentException.class, () ->
            service.deleteAppointment("GHOST999")
        );
    }

    @Test
    void testDeleteOneDoesNotAffectOthers() {
        Appointment appt1 = new Appointment("APT401", futureDate(), "Keep this appointment as is.");
        Appointment appt2 = new Appointment("APT402", futureDate(), "This one will be removed.");
        service.addAppointment(appt1);
        service.addAppointment(appt2);
        service.deleteAppointment("APT402");
        assertNotNull(service.getAppointment("APT401"));
        assertNull(service.getAppointment("APT402"));
    }
}