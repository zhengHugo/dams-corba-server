package admin;

/**
* admin/AdminHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Patient.idl
* Sunday, February 20, 2022 12:41:36 AM EST
*/

public final class AdminHolder implements org.omg.CORBA.portable.Streamable
{
  public admin.Admin value = null;

  public AdminHolder ()
  {
  }

  public AdminHolder (admin.Admin initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = admin.AdminHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    admin.AdminHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return admin.AdminHelper.type ();
  }

}
