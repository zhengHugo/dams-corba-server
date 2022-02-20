package impl;


import appointment.AppointmentType;
import patient.PatientPOA;

public class PatientImpl extends PatientPOA {

  @Override
  public boolean bookAppointment(String patientId, AppointmentType type, String appointmentId) {
    return false;
  }

  @Override
  public String getAppointmentSchedule(String patientId) {
    return null;
  }

  @Override
  public boolean cancelAppointment(String patientId, AppointmentType type, String appointmentId) {
    return false;
  }

  @Override
  public boolean swapAppointment(String patientId, AppointmentType oldType, String oldAppointmentId,
      AppointmentType newType, String newAppointmentId) {
    return false;
  }
}
