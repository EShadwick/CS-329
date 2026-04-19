

import java.util.Date;

public class Appointment {

    private final String appointmentId;
    private Date appointmentDate;
    private String description;

    public Appointment(String appointmentId, Date appointmentDate, String description) {
        // Validate appointmentId
        if (appointmentId == null || appointmentId.length() > 10) {
            throw new IllegalArgumentException("Appointment ID must not be null and must be 10 characters or fewer.");
        }

        // Validate appointmentDate
        if (appointmentDate == null) {
            throw new IllegalArgumentException("Appointment date must not be null.");
        }
        if (appointmentDate.before(new Date())) {
            throw new IllegalArgumentException("Appointment date must not be in the past.");
        }

        // Validate description
        if (description == null || description.length() > 50) {
            throw new IllegalArgumentException("Description must not be null and must be 50 characters or fewer.");
        }

        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.description = description;
    }

    // Getters
    public String getAppointmentId() {
        return appointmentId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public String getDescription() {
        return description;
    }

    // Setters (ID is intentionally omitted — not updatable)
    public void setAppointmentDate(Date appointmentDate) {
        if (appointmentDate == null) {
            throw new IllegalArgumentException("Appointment date must not be null.");
        }
        if (appointmentDate.before(new Date())) {
            throw new IllegalArgumentException("Appointment date must not be in the past.");
        }
        this.appointmentDate = appointmentDate;
    }

    public void setDescription(String description) {
        if (description == null || description.length() > 50) {
            throw new IllegalArgumentException("Description must not be null and must be 50 characters or fewer.");
        }
        this.description = description;
    }
}