package impl;

import cobar_entity.admin.Admin;
import cobar_entity.admin.AdminHelper;
import cobar_entity.admin.AdminPOA;
import cobar_entity.appointment.AppointmentTypeDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.GlobalConstants;
import database.Database;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import model.appointment.Appointment;
import model.appointment.AppointmentAvailability;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class AdminImpl extends AdminPOA {

  private final Database database = Database.getInstance();

  private static final Logger logger = LogManager.getLogger();

  private final Gson gson = new Gson();
  private final Type appointmentAvailabilityListType =
      new TypeToken<List<AppointmentAvailability>>() {}.getType();

  public void setOrb(ORB orb) {
  }

  @Override
  public boolean addAppointment(String appointmentId, AppointmentTypeDto typeDto, int capacity) {
    AppointmentId id = new AppointmentId(appointmentId);
    AppointmentType type = AppointmentType.getByInt(typeDto.value());
    if (!database.findByTypeAndId(type, id).isPresent()) {
      database.add(id, type, capacity);
      logger.info(String.format("Appointment is added: %s, %s", type, id.getId()));
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
  public String listAppointmentAvailability(AppointmentTypeDto typeDto) {
    ORB orb = ORB.init();
    Admin adminRemote1 = null;
    Admin adminRemote2 = null;

    try {
      org.omg.CORBA.Object nameService = orb.resolve_initial_references("NameService");
      NamingContextExt namingContextExt = NamingContextExtHelper.narrow(nameService);
      switch (GlobalConstants.thisCity) {
        case Montreal:
          adminRemote1 = AdminHelper.narrow(namingContextExt.resolve_str("AdminQUE"));
          adminRemote2 = AdminHelper.narrow(namingContextExt.resolve_str("AdminSHE"));
          break;
        case Quebec:
          adminRemote1 = AdminHelper.narrow(namingContextExt.resolve_str("AdminMTL"));
          adminRemote2 = AdminHelper.narrow(namingContextExt.resolve_str("AdminSHE"));
          break;
        case Sherbrooke:
          adminRemote1 = AdminHelper.narrow(namingContextExt.resolve_str("AdminMTL"));
          adminRemote2 = AdminHelper.narrow(namingContextExt.resolve_str("AdminQue"));
          break;
      }
      List<AppointmentAvailability> availabilities =
          gson.fromJson(listLocalAppointmentAvailability(typeDto), appointmentAvailabilityListType);

    } catch (InvalidName
        | org.omg.CosNaming.NamingContextPackage.InvalidName
        | CannotProceed
        | NotFound e) {
      e.printStackTrace();
    }

    List<AppointmentAvailability> availabilities =
        new ArrayList<>(
            gson.fromJson(
                listLocalAppointmentAvailability(typeDto), appointmentAvailabilityListType));
    assert adminRemote1 != null;
    availabilities.addAll(
        gson.fromJson(
            adminRemote1.listLocalAppointmentAvailability(typeDto),
            appointmentAvailabilityListType));
    assert adminRemote2 != null;
    availabilities.addAll(
        gson.fromJson(
            adminRemote2.listLocalAppointmentAvailability(typeDto),
            appointmentAvailabilityListType));
    return gson.toJson(availabilities);
  }

  @Override
  public String listLocalAppointmentAvailability(AppointmentTypeDto typeDto) {
    AppointmentType type = AppointmentType.fromDto(typeDto);
    List<AppointmentAvailability> availabilities =
        database.findAllByType(type).stream()
            .map(
                appointment ->
                    new AppointmentAvailability(
                        appointment.getAppointmentId().getId(), appointment.getRemainingCapacity()))
            .collect(Collectors.toList());
    return gson.toJson(availabilities);
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
