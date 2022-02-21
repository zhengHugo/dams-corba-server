package cobar_entity.patient;

import cobar_entity.appointment.AppointmentTypeDto;

/**
 * patient/PatientOperations.java . Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from Patient.idl Sunday, February 20, 2022 6:15:22 PM EST
 */
public interface PatientOperations {
  boolean bookAppointment(String patientId, AppointmentTypeDto type, String appointmentId);

  boolean bookLocalAppointment(String patientId, AppointmentTypeDto type, String appointmentId);

  String getAppointmentSchedule(String patientId);

  String getLocalAppointmentSchedule(String patientId);

  boolean cancelAppointment(String patientId, AppointmentTypeDto type, String appointmentId);

  boolean swapAppointment(
      String patientId,
      AppointmentTypeDto oldType,
      String oldAppointmentId,
      AppointmentTypeDto newType,
      String newAppointmentId);
} // interface PatientOperations
