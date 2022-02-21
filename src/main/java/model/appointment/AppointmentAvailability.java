package model.appointment;

public class AppointmentAvailability {
  private final String appointmentId;
  private final int availability;

  public AppointmentAvailability(String appointmentId, int availability) {
    this.appointmentId = appointmentId;
    this.availability = availability;
  }

  public String getAppointmentId() {
    return appointmentId;
  }

  public int getAvailability() {
    return availability;
  }
}
