package cobar_entity.patient;


import cobar_entity.appointment.AppointmentTypeDto;
import cobar_entity.appointment.AppointmentTypeDtoHelper;

/**
* patient/PatientPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from dams.idl
* Tuesday, March 8, 2022 3:01:23 PM EST
*/

public abstract class PatientPOA extends org.omg.PortableServer.Servant
 implements PatientOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("bookAppointment", new java.lang.Integer (0));
    _methods.put ("bookLocalAppointment", new java.lang.Integer (1));
    _methods.put ("getAppointmentSchedule", new java.lang.Integer (2));
    _methods.put ("getLocalAppointmentSchedule", new java.lang.Integer (3));
    _methods.put ("cancelAppointment", new java.lang.Integer (4));
    _methods.put ("cancelLocalAppointment", new java.lang.Integer (5));
    _methods.put ("swapAppointment", new java.lang.Integer (6));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // patient/Patient/bookAppointment
       {
         String patientId = in.read_string ();
         AppointmentTypeDto type = AppointmentTypeDtoHelper.read (in);
         String appointmentId = in.read_string ();
         boolean $result = false;
         $result = this.bookAppointment (patientId, type, appointmentId);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 1:  // patient/Patient/bookLocalAppointment
       {
         String patientId = in.read_string ();
         AppointmentTypeDto type = AppointmentTypeDtoHelper.read (in);
         String appointmentId = in.read_string ();
         boolean $result = false;
         $result = this.bookLocalAppointment (patientId, type, appointmentId);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 2:  // patient/Patient/getAppointmentSchedule
       {
         String patientId = in.read_string ();
         String $result = null;
         $result = this.getAppointmentSchedule (patientId);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // patient/Patient/getLocalAppointmentSchedule
       {
         String patientId = in.read_string ();
         String $result = null;
         $result = this.getLocalAppointmentSchedule (patientId);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:  // patient/Patient/cancelAppointment
       {
         String patientId = in.read_string ();
         AppointmentTypeDto type = AppointmentTypeDtoHelper.read (in);
         String appointmentId = in.read_string ();
         boolean $result = false;
         $result = this.cancelAppointment (patientId, type, appointmentId);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 5:  // patient/Patient/cancelLocalAppointment
       {
         String patientId = in.read_string ();
         AppointmentTypeDto type = AppointmentTypeDtoHelper.read (in);
         String appointmentId = in.read_string ();
         boolean $result = false;
         $result = this.cancelLocalAppointment (patientId, type, appointmentId);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 6:  // patient/Patient/swapAppointment
       {
         String patientId = in.read_string ();
         AppointmentTypeDto oldType = AppointmentTypeDtoHelper.read (in);
         String oldAppointmentId = in.read_string ();
         AppointmentTypeDto newType = AppointmentTypeDtoHelper.read (in);
         String newAppointmentId = in.read_string ();
         boolean $result = false;
         $result = this.swapAppointment (patientId, oldType, oldAppointmentId, newType, newAppointmentId);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:patient/Patient:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Patient _this() 
  {
    return PatientHelper.narrow(
    super._this_object());
  }

  public Patient _this(org.omg.CORBA.ORB orb) 
  {
    return PatientHelper.narrow(
    super._this_object(orb));
  }


} // class PatientPOA
