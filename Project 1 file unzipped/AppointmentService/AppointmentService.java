

import java.util.HashMap;
import java.util.Map;

public class AppointmentService {

    private final Map<String, Appointment> appointments = new HashMap<>();

    public void addAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment must not be null.");
        }
        if (appointments.containsKey(appointment.getAppointmentId())) {
            throw new IllegalArgumentException("An appointment with this ID already exists.");
        }
        appointments.put(appointment.getAppointmentId(), appointment);
    }

    public void deleteAppointment(String appointmentId) {
        if (!appointments.containsKey(appointmentId)) {
            throw new IllegalArgumentException("No appointment found with ID: " + appointmentId);
        }
        appointments.remove(appointmentId);
    }

    // Package-level accessor for testing
    Appointment getAppointment(String appointmentId) {
        return appointments.get(appointmentId);
    }
}