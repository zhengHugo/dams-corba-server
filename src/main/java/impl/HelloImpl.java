package impl;

import HelloApp.HelloPOA;
import org.omg.CORBA.ORB;

public class HelloImpl extends HelloPOA {
  private ORB orb;

  public void setOrb(ORB orb) {
    this.orb = orb;
  }


  @Override
  public String sayHello() {
    return "Hello world!\n";
  }

  @Override
  public void shutdown() {
    orb.shutdown(false);
  }
}
