package com.google.sps.data;

import java.util.Date;
import com.google.appengine.api.datastore.Entity;
import java.util.Optional;
import java.util.List;

/** Interface for the DatastoreClient class. */
public interface DatastoreClientInterface {
  /**
   * Adds an AttendeeInterface object to the Datastore. If object already
   * exist, the old one is replaced.  
   * @param {AttendeeInterface} attendee - the object that will be inserted
   *    or update in datastore
   */
  void insertOrUpdateAttendee(AttendeeInterface attendee);

  /**
   * Adds an InstanceInterface object to the Datastore. If object already
   * exist, the old one is replaced. 
   * @param {InstanceInterface} instance - the object that will be inserted
   *    or update in datastore
   */
  void insertOrUpdateInstance(InstanceInterface instance);

  /** 
   * Adds an SessionInterface object to the Datastore. If object already 
   * exist, the old one is replaced. 
   * @param {SessionInterface} session - the object that will be inserted
   *    or update in datastore
   */
  void insertOrUpdateSession(SessionInterface session);

   /** 
    * Returns the SessionInterface object associated with the given sessionId. 
    * If return Optional is empty, then no object exists with given parameter.
    * @param {String} sessionId - the id used to look up the Session.
    */
  Optional<SessionInterface> getSession(String sessionId);

  /**
   * Returns the InstanceInterface object associated with the given parameter. 
   * If return Optional is empty, then that instanceName does not exists.
   * @param {String} instanceName - the name used to look up the Instance.
   */
  Optional<InstanceInterface> getInstance(String instanceName);

  /**
   * Returns the AttendeeInterface object associated with the given screenName.
   * If return Optional is empty, then no object exists with given parameter.
   * @param {String} screenName - the name used to look up the Attendeee.
   * @param {String} sessionId - the session the attendee is attached to.
   */ 
  Optional<AttendeeInterface> getAttendee(String screenName, String sessionId);

  /**
   * Deletes an Attendee from Datastore. 
   * @param {String} screenName - the name used to look up the Attendee.
   * @param {String} sessionId - the session the attendee is attached to.
   */
  void deleteAttendee(String screenName, String sessionId);

  /**
   * Deletes an Instance from Datastore. 
   * @param {String} instanceName - the name used to look up the Instance.
   */
  void deleteInstance(String instanceName);

  /**
   * Deletes an Session from Datastore. 
   * @param {String} sessionId - the id used to look up the Session.
   */
  void deleteSession(String sessionId);

  /**
   * Returns a list of attendees in a session. 
   * @param {String} sessionId - the id used to identify what session 
   *    the attendee is in.
   */
  List<AttendeeInterface> getAttendeesInSession(String sessionId);

  /* Returns a list of terminated instances.  */
  List<InstanceInterface> getAvailableInstances();
}