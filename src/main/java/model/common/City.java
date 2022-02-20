package model.common;

public enum City {
  Montreal("MTL"),
  Quebec("QUE"),
  Sherbrooke("SHE"),
  ;

  public final String code;

  City(String code) {
    this.code = code;
  }

  public static City fromCode(String code) {
    switch (code) {
      case "MTL":
        return Montreal;
      case "QUE":
        return Quebec;
      case "SHE":
        return Sherbrooke;
      default:
        throw new IllegalArgumentException(code);
    }
  }
}
