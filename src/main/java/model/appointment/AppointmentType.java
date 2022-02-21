package model.appointment;

import cobar_entity.appointment.AppointmentTypeDto;

public enum AppointmentType {
  Physician,
  Surgeon,
  Dental;

  AppointmentType() {}

  public static AppointmentType getByInt(int value) {
    if (value == 0) return Physician;
    else if (value == 1) return Surgeon;
    else if (value == 2) return Dental;
    else return null;
  }

  public static AppointmentType fromDto(AppointmentTypeDto dto) {
    return getByInt(dto.value());
  }

  public AppointmentTypeDto toDto() {
    switch (this) {
      case Physician:
        return AppointmentTypeDto.from_int(0);
      case Surgeon:
        return AppointmentTypeDto.from_int(1);
      case Dental:
        return AppointmentTypeDto.from_int(2);
    }
    return null;
  }
}
