package impl;

import cobar_entity.appointment.AppointmentTypeDto;
import cobar_entity.patient.Patient;
import cobar_entity.patient.PatientHelper;
import cobar_entity.patient.PatientPOA;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.GlobalConstants;
import database.Database;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class PatientImpl extends PatientPOA {

  private final Database database = Database.getInstance();
  private static final Logger logger = LogManager.getLogger();

  private final Gson gson = new Gson();
  private final Type appointmentListType = new TypeToken<List<Appointment>>() {}.getType();

  private final ORB orb;

  public PatientImpl(ORB orb) {
    this.orb = orb;
  }

  @Override
  public synchronized boolean bookAppointment(
      String patientIdStr, AppointmentTypeDto typeDto, String appointmentIdStr) {

    PatientId patientId = new PatientId(patientIdStr);
    AppointmentType type = AppointmentType.getByInt(typeDto.value());
    AppointmentId appointmentId = new AppointmentId(appointmentIdStr);
    Patient patientRemote = null;

    // init remote client
    try {
      org.omg.CORBA.Object nameService = orb.resolve_initial_references("NameService");
      NamingContextExt namingContextExt = NamingContextExtHelper.narrow(nameService);
      patientRemote =
          PatientHelper.narrow(
              namingContextExt.resolve_str("Patient" + appointmentId.getCity().code));

    } catch (InvalidName
        | org.omg.CosNaming.NamingContextPackage.InvalidName
        | CannotProceed
        | NotFound e) {
      e.printStackTrace();
    }

    List<Appointment> patientAppointments =
        gson.fromJson(getAppointmentSchedule(patientIdStr), appointmentListType);

    if (patientAppointments.stream()
        .filter(app -> app.getType().equals(type))
        .map(app -> app.getAppointmentId().getDateString())
        .collect(Collectors.toList())
        .contains(appointmentId.getDateString())) {
      logger.info(
          String.format(
              "The patient %s already has an appointment %s - %s. Booking not proceeded",
              patientId, type, appointmentId));
      return false;
    }
    if (GlobalConstants.thisCity.equals(appointmentId.getCity())) {
      // patient book an appointment in its own city
      return bookLocalAppointment(patientIdStr, typeDto, appointmentIdStr);
    } else {
      // call bookLocalAppointment at the city according to the appointment
      // check the 3 times/week booking limit first
      DateTimeFormatter formatter = DateTimeFormat.forPattern("ddMMyy");
      int thisWeek = DateTime.parse(appointmentId.getDateString(), formatter).getWeekOfWeekyear();
      assert patientRemote != null;
      long allAppsInThisWeekCount =
          patientAppointments.stream()
              .filter(
                  app ->
                      DateTime.parse(app.getAppointmentId().getDateString(), formatter)
                              .getWeekOfWeekyear()
                          == thisWeek)
              .count();
      List<Appointment> localPatientAppointments =
          gson.fromJson(getLocalAppointmentSchedule(patientIdStr), appointmentListType);
      long localAppsInThisWeekCount =
          localPatientAppointments.stream()
              .filter(
                  app ->
                      DateTime.parse(app.getAppointmentId().getDateString(), formatter)
                              .getWeekOfWeekyear()
                          == thisWeek)
              .count();
      long nonlocalAppCount = allAppsInThisWeekCount - localAppsInThisWeekCount;
      if (nonlocalAppCount < 3) {
        logger.info(
            String.format(
                "Call server %s to book appointment for %s, %s - %s",
                appointmentId.getCity().code, patientId, type, appointmentId));
        return patientRemote.bookLocalAppointment(patientIdStr, typeDto, appointmentIdStr);
      } else {
        logger.info(
            String.format(
                "Non-local appointments over limit. Cannot book for %s, %s - %s",
                patientId, type, appointmentId));
        return false;
      }
    }
  }

  @Override
  public boolean bookLocalAppointment(
      String patientIdStr, AppointmentTypeDto typeDto, String appointmentIdStr) {
    PatientId patientId = new PatientId(patientIdStr);
    AppointmentType type = AppointmentType.getByInt(typeDto.value());
    AppointmentId appointmentId = new AppointmentId(appointmentIdStr);

    Optional<Appointment> appointmentOptional = database.findByTypeAndId(type, appointmentId);
    if (appointmentOptional.isPresent()) {
      Appointment appointment = appointmentOptional.get();
      if (appointment.addPatient(patientId) && database.update(appointment, type)) {
        logger.info(
            String.format(
                "Book appointment success: %s, %s - %s",
                patientId.getId(), type, appointmentId.getId()));
        return true;
      } else {
        logger.info(
            String.format(
                "Cannot book appointment: %s, %s - %s",
                patientId.getId(), type, appointmentId.getId()));
        return false;
      }
    } else {
      logger.info(String.format("The appointment %s - %s doesn't exist.", type, appointmentId));
      return false;
    }
  }

  @Override
  public String getAppointmentSchedule(String patientIdStr) {
    Patient patientRemote1 = null;
    Patient patientRemote2 = null;

    try {
      org.omg.CORBA.Object nameService = orb.resolve_initial_references("NameService");
      NamingContextExt namingContextExt = NamingContextExtHelper.narrow(nameService);
      switch (GlobalConstants.thisCity) {
        case Montreal:
          patientRemote1 = PatientHelper.narrow(namingContextExt.resolve_str("PatientQUE"));
          patientRemote2 = PatientHelper.narrow(namingContextExt.resolve_str("PatientSHE"));
          break;
        case Quebec:
          patientRemote1 = PatientHelper.narrow(namingContextExt.resolve_str("PatientMTL"));
          patientRemote2 = PatientHelper.narrow(namingContextExt.resolve_str("PatientSHE"));
          break;
        case Sherbrooke:
          patientRemote1 = PatientHelper.narrow(namingContextExt.resolve_str("PatientMTL"));
          patientRemote2 = PatientHelper.narrow(namingContextExt.resolve_str("PatientQUE"));
          break;
      }
    } catch (InvalidName
        | org.omg.CosNaming.NamingContextPackage.InvalidName
        | CannotProceed
        | NotFound e) {
      e.printStackTrace();
    }

    PatientId patientId = new PatientId(patientIdStr);
    List<Appointment> allAppointment =
        new ArrayList<>(
            gson.fromJson(getLocalAppointmentSchedule(patientIdStr), appointmentListType));
    assert patientRemote1 != null;
    allAppointment.addAll(
        gson.fromJson(
            patientRemote1.getLocalAppointmentSchedule(patientIdStr), appointmentListType));
    assert patientRemote2 != null;
    allAppointment.addAll(
        gson.fromJson(
            patientRemote2.getLocalAppointmentSchedule(patientIdStr), appointmentListType));
    return gson.toJson(allAppointment);
  }

  @Override
  public String getLocalAppointmentSchedule(String patientId) {
    List<Appointment> appointmentList = database.findAllByPatientId(new PatientId(patientId));
    return gson.toJson(appointmentList);
  }

  @Override
  public boolean cancelAppointment(
      String patientIdStr, AppointmentTypeDto type, String appointmentIdStr) {
    AppointmentId appointmentId = new AppointmentId(appointmentIdStr);
    if (appointmentId.getCity().equals(GlobalConstants.thisCity)) {
      cancelLocalAppointment(patientIdStr, type, appointmentIdStr);
    } else {
      Patient patientRemote;
      try {
        org.omg.CORBA.Object nameService = orb.resolve_initial_references("NameService");
        NamingContextExt namingContextExt = NamingContextExtHelper.narrow(nameService);
        patientRemote =
            PatientHelper.narrow(
                namingContextExt.resolve_str("Patient" + appointmentId.getCity().code));
        if (patientRemote.cancelLocalAppointment(patientIdStr, type, appointmentIdStr)) {
          logger.info(
              String.format(
                  "Cancel appointment success: %s, %s - %s", patientIdStr, type, appointmentId));
          return true;
        } else {
          logger.info(
              String.format(
                  "The patient %s doesn't have an appointment with %s - %s",
                  patientIdStr, type, appointmentId));
          return false;
        }
      } catch (InvalidName
          | org.omg.CosNaming.NamingContextPackage.InvalidName
          | CannotProceed
          | NotFound e) {
        e.printStackTrace();
      }

    }
    return false;
  }

  @Override
  public synchronized boolean cancelLocalAppointment(
      String patientIdStr, AppointmentTypeDto typeDto, String appointmentIdStr) {
    AppointmentId appointmentId = new AppointmentId(appointmentIdStr);
    AppointmentType type = AppointmentType.getByInt(typeDto.value());
    PatientId patientId = new PatientId(patientIdStr);

    Optional<Appointment> appointmentOptional = database.findByTypeAndId(type, appointmentId);
    if (appointmentOptional.isPresent()) {
      Appointment appointment = appointmentOptional.get();
      if (appointment.removePatient(patientId)) {
        database.update(appointment, type);
        logger.info(
            String.format(
                "Cancel appointment success: %s, %s - %s", patientId, type, appointmentId));
        return true;
      } else {
        logger.info(
            String.format(
                "The patient %s doesn't have an appointment with %s - %s",
                patientId, type, appointmentId));
        return false;
      }
    } else {
      logger.info(String.format("The appointment %s doesn't exist", appointmentId));
      return false;
    }
  }

  @Override
  public boolean swapAppointment(
      String patientIdStr,
      AppointmentTypeDto oldType,
      String oldAppointmentId,
      AppointmentTypeDto newType,
      String newAppointmentId) {
    if (cancelAppointment(patientIdStr, oldType, oldAppointmentId)) {
      if (bookAppointment(patientIdStr, newType, newAppointmentId)) {
        logger.info(
            String.format(
                "Swapped appointment for %s: from %s - %s to %s - %s",
                patientIdStr, oldType, oldAppointmentId, newType, newAppointmentId));
        return true;
      } else {
        logger.info(
            String.format(
                "Cannot book new appointment for %s: %s - %s. Swap not proceeded",
                patientIdStr, newType, newAppointmentId));
        bookAppointment(patientIdStr, oldType, oldAppointmentId);
        return false;
      }
    } else {
      logger.info(
          String.format(
              "Cannot cancel old appointment for %s: %s - %s. Swap not proceeded",
              patientIdStr, oldType, oldAppointmentId));
      return false;
    }
  }
}
