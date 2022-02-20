package impl;

import cobar_entity.admin.AdminPOA;
import cobar_entity.appointment.AppointmentTypeDto;
import database.Database;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.ORB;

public class AdminImpl extends AdminPOA {

  private final Database database = Database.getInstance();
  private ORB orb;


  private static final Logger logger = LogManager.getLogger();

  public void setOrb(ORB orb) {
    this.orb = orb;
  }

  @Override
  public boolean addAppointment(String appointmentId, AppointmentTypeDto typeDto, int capacity) {
    AppointmentId id = new AppointmentId(appointmentId);
    AppointmentType type = AppointmentType.getByInt(typeDto.value());
    if (!database.findByTypeAndId(type, id).isPresent()) {
      database.add(id, type, capacity);
      logger.info(String.format("Appointment is added: %s, %s", type.toString(), id.getId()));
      return true;
    } else {
      logger.info(
          String.format(
              "Appointment is not added because appointment already exists with type: %s, id: %s",
              type, id.getId()));
      return false;
    }
  }

  @Override
  public String removeAppointment(String id, AppointmentTypeDto type) {
    AtomicReference<String> message = new AtomicReference<>();
    AppointmentType appointmentType = AppointmentType.getByInt(type.value());
    AppointmentId appointmentId = new AppointmentId(id);
    Optional<Appointment> appointmentOptional =
        database.findByTypeAndId(appointmentType, appointmentId);
    if (appointmentOptional.isPresent()) {
      Appointment appointment = appointmentOptional.get();
      List<PatientId> patientIds = appointment.getPatientIds();
      if (patientIds.size() > 0) {
        message.set(putPatientsAfterAppointment(patientIds, appointmentType, appointmentId));
      }
      database.remove(appointmentId, appointmentType);
      message.set("The target appointment is removed.");
      logger.info(message.get());
    } else {
      message.set("The target appointment does not exist.");
      logger.info(message.get());
    }
    return message.get();
  }

  @Override
  public String listAppointmentAvailability(AppointmentTypeDto type) {
    return null;
  }

  private synchronized String putPatientsAfterAppointment(
      List<PatientId> patientIds, AppointmentType type, AppointmentId id) {
    Optional<Appointment> targetAppointmentOptional = database.findByTypeAndId(type, id);
    String message = "";
    if (targetAppointmentOptional.isPresent()) {
      Optional<AppointmentId> nextAppointmentIdOptional = database.findNextAppointmentId(type, id);
      if (nextAppointmentIdOptional.isPresent()) {
        AppointmentId nextAppId = nextAppointmentIdOptional.get();
        Appointment nextApp = database.findByTypeAndId(type, nextAppId).get();
        if (patientIds.size() > nextApp.getRemainingCapacity()) {
          // next appointment cannot fit all patients; try put them to later ones
          int fromIndex = nextApp.getRemainingCapacity();
          nextApp.addPatients(patientIds.subList(0, fromIndex));
          message =
              String.format(
                  "Patient(s) %s are assigned to appointment %s:%s",
                  patientIds.subList(0, fromIndex), type, id);
          logger.info(message);
          putPatientsAfterAppointment(
              patientIds.subList(fromIndex, patientIds.size()), type, nextAppId);
        } else {
          nextApp.addPatients(patientIds);
          message =
              String.format(
                  "Patient(s) %s are assigned to appointment %s:%s", patientIds, type, id);
          logger.info(message);
        }
      } else {
        // this is the last appointment; no future appointment is available
        message =
            "Patient(s) %s are dropped from appointments because no future appointments are available";
        logger.warn(message);
      }
    }
    return message;
  }
}
