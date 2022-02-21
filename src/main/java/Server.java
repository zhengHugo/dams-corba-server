import HelloApp.Hello;
import HelloApp.HelloHelper;
import cobar_entity.admin.Admin;
import cobar_entity.admin.AdminHelper;
import cobar_entity.patient.Patient;
import cobar_entity.patient.PatientHelper;
import common.GlobalConstants;
import impl.AdminImpl;
import impl.PatientImpl;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

public class Server {
  public static void main(String[] args) {
    ORB orb = ORB.init(args, null);
    try {
      POA rootPoa = (POA) orb.resolve_initial_references("RootPOA");
      rootPoa.the_POAManager().activate();

      org.omg.CORBA.Object nameService = orb.resolve_initial_references("NameService");
      NamingContextExt namingContextRef = NamingContextExtHelper.narrow(nameService);

      AdminImpl adminImpl = new AdminImpl();
      org.omg.CORBA.Object adminRef = rootPoa.servant_to_reference(adminImpl);
      Admin admin = AdminHelper.narrow(adminRef);
      namingContextRef.rebind(
          namingContextRef.to_name("Admin" + GlobalConstants.thisCity.code), admin);

      PatientImpl patientImpl = new PatientImpl();
      org.omg.CORBA.Object patientRef = rootPoa.servant_to_reference(patientImpl);
      Patient patient = PatientHelper.narrow(patientRef);
      namingContextRef.rebind(
          namingContextRef.to_name("Patient" + GlobalConstants.thisCity.code), patient);

      System.out.println("HelloServer ready and waiting...");

      orb.run();

    } catch (InvalidName
        | AdapterInactive
        | WrongPolicy
        | ServantNotActive
        | org.omg.CosNaming.NamingContextPackage.InvalidName
        | CannotProceed
        | NotFound e) {
      e.printStackTrace();
    }
  }
}
