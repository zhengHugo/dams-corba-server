package impl;


import cobar_entity.appointment.AppointmentTypeDto;
import cobar_entity.patient.PatientPOA;

public class PatientImpl extends PatientPOA {

  @Override
  public boolean bookAppointment(String patientId, AppointmentTypeDto type, String appointmentId) {
    return false;
  }

  @Override
  public String getAppointmentSchedule(String patientId) {
    return null;
  }

  @Override
  public boolean cancelAppointment(String patientId, AppointmentTypeDto type,
      String appointmentId) {
    return false;
  }

  @Override
  public boolean swapAppointment(String patientId, AppointmentTypeDto oldType,
      String oldAppointmentId, AppointmentTypeDto newType, String newAppointmentId) {
    return false;
  }
}
