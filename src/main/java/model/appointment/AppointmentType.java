package model.appointment;

public enum AppointmentType{
  Physician(),
  Surgeon(),
  Dental();

  AppointmentType(){
  }

  public static AppointmentType getByInt(int value) {
    if (value == 0) return Physician;
    else if (value == 1) return Surgeon;
    else if (value == 2) return Dental;
    else return null;
  }
}
