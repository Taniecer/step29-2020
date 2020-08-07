package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import java.util.Optional;
import com.google.sps.data.EntityConstants;
import java.util.List;

/** Class that represents a session. */
public class Session implements SessionInterface {

  // The id used to identify the session. 
  private final String sessionId;

  // The screen name of the user with the controller. 
  private Optional<String> screenNameOfController;

  // The ip of the VM assigned to this session. 
  private Optional<String> ipOfVM;

  /** Initializes a Session object
   * @param {String} sessionID - the id used to identify a session.
   * @param {Optional<String>} screenNameOfController - the screen name
   *    of the attendee with the controller. 
   * @param {Optional<String>} ipOfVM - the ip of the VM assigned to 
   *    the session.
   */
  public Session (String sessionId, Optional<String> screenNameOfController,
    Optional<String> ipOfVM) {
      this.sessionId = sessionId;
      this.screenNameOfController = screenNameOfController;
      this.ipOfVM = ipOfVM;
  }

  public String getSessionId() {
    return sessionId;
  }

  public Optional<String> getScreenNameOfController() {
    return screenNameOfController;
  }

  public Optional<String> getIpOfVM() {
    return ipOfVM;
  }

  public void setScreenNameOfController (String screenNameOfController) {
      this.screenNameOfController = Optional.of(screenNameOfController);
  }

  public void setIpOfVM(String ipOfVM) {
    this.ipOfVM = Optional.of(ipOfVM);
  }

  @Override
  public boolean equals(Object obj) {
    // Self check
    if (this == obj) {
      return true;   
    }
    // Null check 
    if (obj == null) {
      return false;  
    }
    // Type check
    if(obj.getClass() != getClass()) {
      return false; 
    }
    return this.isEqualTo((Session) obj);
  }

  /** Compares a SessionInterface to itself. */
  public boolean isEqualTo(SessionInterface session) {
    // Field comparison
    return sessionId.equals(session.getSessionId())
        && screenNameOfController.equals(session.getScreenNameOfController())
        && ipOfVM.equals(session.getIpOfVM());
  }
  
  /** Returns a new Entity of kind "Session" from a Session object. */
  public Entity toEntity() {
    Entity sessionEntity = 
        new Entity(/*kind=*/EntityConstants.SessionEntity.TABLE_NAME, 
        /*keyName=*/this.sessionId);
    sessionEntity.setProperty 
        (EntityConstants.SessionEntity.SESSION_ID, this.sessionId);
    if (this.getScreenNameOfController().isPresent()) {
      sessionEntity.setProperty
        (EntityConstants.SessionEntity.SCREEN_NAME_OF_CONTROLLER, 
        (this.screenNameOfController.get()));
    }
    if (this.getIpOfVM().isPresent()) {
      sessionEntity.setProperty
        (EntityConstants.SessionEntity.IP_OF_VM, (this.ipOfVM.get()));
    }
    return sessionEntity;
  }

  /**
   * Returns a new Session from an entity of kind "Session".
   * @param {Entity} sessionEntity - entity of kind "Session" with various 
   *     properties similar to the fields of a session object.
   */
  public static Session fromEntity(Entity sessionEntity) {
    String sessionId = (String) sessionEntity.getProperty
        (EntityConstants.SessionEntity.SESSION_ID);  
    Optional<String> screenNameOfController = Optional.empty();
    if (sessionEntity.hasProperty
      (EntityConstants.SessionEntity.SCREEN_NAME_OF_CONTROLLER)) {
        screenNameOfController= Optional.of((String) sessionEntity.getProperty
          (EntityConstants.SessionEntity.SCREEN_NAME_OF_CONTROLLER));
    }
    Optional<String> ipOfVM = Optional.empty();
    if (sessionEntity.hasProperty(EntityConstants.SessionEntity.IP_OF_VM)) {
       ipOfVM = Optional.of((String) sessionEntity.getProperty
        (EntityConstants.SessionEntity.IP_OF_VM));
    }
    return new Session(sessionId, screenNameOfController, ipOfVM);
  }
}