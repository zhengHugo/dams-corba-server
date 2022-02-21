package cobar_entity.appointment;

/**
 * appointment/AppointmentDto.java . Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from dams.idl Sunday, February 20, 2022 6:15:22 PM EST
 */
public final class AppointmentDto implements org.omg.CORBA.portable.IDLEntity {
  public String id = null;
  public int capacity = (int) 0;
  public String patientIds[] = null;
  public AppointmentTypeDto type = null;

  public AppointmentDto() {} // ctor

  public AppointmentDto(String _id, int _capacity, String[] _patientIds, AppointmentTypeDto _type) {
    id = _id;
    capacity = _capacity;
    patientIds = _patientIds;
    type = _type;
  } // ctor
} // class AppointmentDto
