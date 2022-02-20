import HelloApp.Hello;
import HelloApp.HelloHelper;
import cobar_entity.admin.Admin;
import cobar_entity.admin.AdminHelper;
import impl.AdminImpl;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
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

//      HelloImpl helloImpl = new HelloImpl();
//      helloImpl.setOrb(orb);

      AdminImpl adminImpl = new AdminImpl();
      adminImpl.setOrb(orb);


//      org.omg.CORBA.Object ref = rootPoa.servant_to_reference(helloImpl);
//      Hello href = HelloHelper.narrow(ref);
      org.omg.CORBA.Object ref = rootPoa.servant_to_reference(adminImpl);
      Admin admin = AdminHelper.narrow(ref);

      org.omg.CORBA.Object nameService = orb.resolve_initial_references("NameService");
      NamingContextExt namingContextRef = NamingContextExtHelper.narrow(nameService);

      String name = "Admin";
      NameComponent[] path = namingContextRef.to_name(name);

      namingContextRef.rebind(path, admin);

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
