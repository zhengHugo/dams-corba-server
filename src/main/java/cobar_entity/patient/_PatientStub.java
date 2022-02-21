package cobar_entity.patient;

import cobar_entity.appointment.AppointmentTypeDto;
import cobar_entity.appointment.AppointmentTypeDtoHelper;

/**
 * patient/_PatientStub.java . Generated by the IDL-to-Java compiler (portable), version "3.2" from
 * Patient.idl Sunday, February 20, 2022 6:15:22 PM EST
 */
public class _PatientStub extends org.omg.CORBA.portable.ObjectImpl implements Patient {

  public boolean bookAppointment(String patientId, AppointmentTypeDto type, String appointmentId) {
    org.omg.CORBA.portable.InputStream $in = null;
    try {
      org.omg.CORBA.portable.OutputStream $out = _request("bookAppointment", true);
      $out.write_string(patientId);
      AppointmentTypeDtoHelper.write($out, type);
      $out.write_string(appointmentId);
      $in = _invoke($out);
      boolean $result = $in.read_boolean();
      return $result;
    } catch (org.omg.CORBA.portable.ApplicationException $ex) {
      $in = $ex.getInputStream();
      String _id = $ex.getId();
      throw new org.omg.CORBA.MARSHAL(_id);
    } catch (org.omg.CORBA.portable.RemarshalException $rm) {
      return bookAppointment(patientId, type, appointmentId);
    } finally {
      _releaseReply($in);
    }
  } // bookAppointment

  public boolean bookLocalAppointment(
      String patientId, AppointmentTypeDto type, String appointmentId) {
    org.omg.CORBA.portable.InputStream $in = null;
    try {
      org.omg.CORBA.portable.OutputStream $out = _request("bookLocalAppointment", true);
      $out.write_string(patientId);
      AppointmentTypeDtoHelper.write($out, type);
      $out.write_string(appointmentId);
      $in = _invoke($out);
      boolean $result = $in.read_boolean();
      return $result;
    } catch (org.omg.CORBA.portable.ApplicationException $ex) {
      $in = $ex.getInputStream();
      String _id = $ex.getId();
      throw new org.omg.CORBA.MARSHAL(_id);
    } catch (org.omg.CORBA.portable.RemarshalException $rm) {
      return bookLocalAppointment(patientId, type, appointmentId);
    } finally {
      _releaseReply($in);
    }
  } // bookLocalAppointment

  public String getAppointmentSchedule(String patientId) {
    org.omg.CORBA.portable.InputStream $in = null;
    try {
      org.omg.CORBA.portable.OutputStream $out = _request("getAppointmentSchedule", true);
      $out.write_string(patientId);
      $in = _invoke($out);
      String $result = $in.read_string();
      return $result;
    } catch (org.omg.CORBA.portable.ApplicationException $ex) {
      $in = $ex.getInputStream();
      String _id = $ex.getId();
      throw new org.omg.CORBA.MARSHAL(_id);
    } catch (org.omg.CORBA.portable.RemarshalException $rm) {
      return getAppointmentSchedule(patientId);
    } finally {
      _releaseReply($in);
    }
  } // getAppointmentSchedule

  public String getLocalAppointmentSchedule(String patientId) {
    org.omg.CORBA.portable.InputStream $in = null;
    try {
      org.omg.CORBA.portable.OutputStream $out = _request("getLocalAppointmentSchedule", true);
      $out.write_string(patientId);
      $in = _invoke($out);
      String $result = $in.read_string();
      return $result;
    } catch (org.omg.CORBA.portable.ApplicationException $ex) {
      $in = $ex.getInputStream();
      String _id = $ex.getId();
      throw new org.omg.CORBA.MARSHAL(_id);
    } catch (org.omg.CORBA.portable.RemarshalException $rm) {
      return getLocalAppointmentSchedule(patientId);
    } finally {
      _releaseReply($in);
    }
  } // getLocalAppointmentSchedule

  public boolean cancelAppointment(
      String patientId, AppointmentTypeDto type, String appointmentId) {
    org.omg.CORBA.portable.InputStream $in = null;
    try {
      org.omg.CORBA.portable.OutputStream $out = _request("cancelAppointment", true);
      $out.write_string(patientId);
      AppointmentTypeDtoHelper.write($out, type);
      $out.write_string(appointmentId);
      $in = _invoke($out);
      boolean $result = $in.read_boolean();
      return $result;
    } catch (org.omg.CORBA.portable.ApplicationException $ex) {
      $in = $ex.getInputStream();
      String _id = $ex.getId();
      throw new org.omg.CORBA.MARSHAL(_id);
    } catch (org.omg.CORBA.portable.RemarshalException $rm) {
      return cancelAppointment(patientId, type, appointmentId);
    } finally {
      _releaseReply($in);
    }
  } // cancelAppointment

  public boolean swapAppointment(
      String patientId,
      AppointmentTypeDto oldType,
      String oldAppointmentId,
      AppointmentTypeDto newType,
      String newAppointmentId) {
    org.omg.CORBA.portable.InputStream $in = null;
    try {
      org.omg.CORBA.portable.OutputStream $out = _request("swapAppointment", true);
      $out.write_string(patientId);
      AppointmentTypeDtoHelper.write($out, oldType);
      $out.write_string(oldAppointmentId);
      AppointmentTypeDtoHelper.write($out, newType);
      $out.write_string(newAppointmentId);
      $in = _invoke($out);
      boolean $result = $in.read_boolean();
      return $result;
    } catch (org.omg.CORBA.portable.ApplicationException $ex) {
      $in = $ex.getInputStream();
      String _id = $ex.getId();
      throw new org.omg.CORBA.MARSHAL(_id);
    } catch (org.omg.CORBA.portable.RemarshalException $rm) {
      return swapAppointment(patientId, oldType, oldAppointmentId, newType, newAppointmentId);
    } finally {
      _releaseReply($in);
    }
  } // swapAppointment

  // Type-specific CORBA::Object operations
  private static String[] __ids = {"IDL:patient/Patient:1.0"};

  public String[] _ids() {
    return (String[]) __ids.clone();
  }

  private void readObject(java.io.ObjectInputStream s) throws java.io.IOException {
    String str = s.readUTF();
    String[] args = null;
    java.util.Properties props = null;
    org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
    try {
      org.omg.CORBA.Object obj = orb.string_to_object(str);
      org.omg.CORBA.portable.Delegate delegate =
          ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
      _set_delegate(delegate);
    } finally {
      orb.destroy();
    }
  }

  private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
    String[] args = null;
    java.util.Properties props = null;
    org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
    try {
      String str = orb.object_to_string(this);
      s.writeUTF(str);
    } finally {
      orb.destroy();
    }
  }
} // class _PatientStub