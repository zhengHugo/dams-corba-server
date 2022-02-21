package cobar_entity.appointment;

/**
 * appointment/AppointmentTypeDtoHelper.java . Generated by the IDL-to-Java compiler (portable),
 * version "3.2" from dams.idl Sunday, February 20, 2022 6:15:22 PM EST
 */
public abstract class AppointmentTypeDtoHelper {
  private static String _id = "IDL:appointment/AppointmentTypeDto:1.0";

  public static void insert(org.omg.CORBA.Any a, AppointmentTypeDto that) {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
    a.type(type());
    write(out, that);
    a.read_value(out.create_input_stream(), type());
  }

  public static AppointmentTypeDto extract(org.omg.CORBA.Any a) {
    return read(a.create_input_stream());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;

  public static synchronized org.omg.CORBA.TypeCode type() {
    if (__typeCode == null) {
      __typeCode =
          org.omg
              .CORBA
              .ORB
              .init()
              .create_enum_tc(
                  AppointmentTypeDtoHelper.id(),
                  "AppointmentTypeDto",
                  new String[] {"Physician", "Surgeon", "Dental"});
    }
    return __typeCode;
  }

  public static String id() {
    return _id;
  }

  public static AppointmentTypeDto read(org.omg.CORBA.portable.InputStream istream) {
    return AppointmentTypeDto.from_int(istream.read_long());
  }

  public static void write(org.omg.CORBA.portable.OutputStream ostream, AppointmentTypeDto value) {
    ostream.write_long(value.value());
  }
}
