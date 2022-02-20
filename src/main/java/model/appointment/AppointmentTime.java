package model.appointment;

public enum AppointmentTime {
  Morning("M"),
  Afternoon("A"),
  Evening("E"),
  ;

  public final String code;

  AppointmentTime(String code) {
    this.code = code;
  }

  public static AppointmentTime fromCode(String code) {
    switch (code) {
      case "M":
        return Morning;
      case "A":
        return Afternoon;
      case "E":
        return Evening;
      default:
        throw new IllegalArgumentException(code);
    }
  }
}
