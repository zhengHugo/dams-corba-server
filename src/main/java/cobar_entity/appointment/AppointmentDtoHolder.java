package cobar_entity.appointment;

/**
 * appointment/AppointmentDtoHolder.java . Generated by the IDL-to-Java compiler (portable), version
 * "3.2" from Patient.idl Sunday, February 20, 2022 6:15:22 PM EST
 */
public final class AppointmentDtoHolder implements org.omg.CORBA.portable.Streamable {
  public AppointmentDto value = null;

  public AppointmentDtoHolder() {}

  public AppointmentDtoHolder(AppointmentDto initialValue) {
    value = initialValue;
  }

  public void _read(org.omg.CORBA.portable.InputStream i) {
    value = AppointmentDtoHelper.read(i);
  }

  public void _write(org.omg.CORBA.portable.OutputStream o) {
    AppointmentDtoHelper.write(o, value);
  }

  public org.omg.CORBA.TypeCode _type() {
    return AppointmentDtoHelper.type();
  }
}