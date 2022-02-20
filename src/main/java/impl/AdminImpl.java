package impl;


import admin.AdminPOA;
import appointment.AppointmentType;

public class AdminImpl extends AdminPOA {


  @Override
  public boolean addAppointment(String appointmentId, AppointmentType type, int capacity) {
    return false;
  }

  @Override
  public String removeAppointment(String appointmentId, AppointmentType type) {
    return null;
  }

  @Override
  public String listAppointmentAvailability(AppointmentType type) {
    return null;
  }
}
