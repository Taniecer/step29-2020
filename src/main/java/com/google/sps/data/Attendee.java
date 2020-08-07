package com.google.sps.data;

import java.util.Date;
import com.google.appengine.api.datastore.Entity;
import com.google.sps.data.EntityConstants;

/** Class that represents a user in the session as an attendee. */
public class Attendee implements AttendeeInterface {

  // The id of the session this attendee is in.
  private final String sessionId;

  // The screen name of this attendee.
  private final String screenName;

  // Time the attendee last polled the server.
  private Date timeLastPolled;
  
  /** Initializes an Attendee object
   * @param {String} sessionId - the session the attendee is attached to.
   * @param {String} screenName - the screen name of the attendee. 
   * @param {Date} timeLastPolled - the time attendee was last polled.
   */
  public Attendee (String sessionId, String screenName, Date timeLastPolled) {
    this.sessionId = sessionId;
    this.screenName = screenName;
    this.timeLastPolled = timeLastPolled;
  }

  public String getSessionId() {
    return sessionId;
  }

  public String getScreenName() {
    return screenName;
  }

  public Date getTimeLastPolled() {
    return timeLastPolled;
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
    return this.isEqualTo((Attendee) obj);
  }

  /** Compares an AttendeeInterface to itself. */
  public boolean isEqualTo(AttendeeInterface attendee) {
    // Field comparison
    return sessionId.equals(attendee.getSessionId())
        && screenName.equals(attendee.getScreenName())
        && timeLastPolled.equals(attendee.getTimeLastPolled());   
  }

  /** Returns a new Entity of kind "Attendee" from an Attendee object. */
  public Entity toEntity() {
    Entity attendeeEntity = 
        new Entity(/*kind=*/EntityConstants.AttendeeEntity.TABLE_NAME, 
        /*keyName=*/this.screenName + this.sessionId);
    attendeeEntity.setProperty 
        (EntityConstants.AttendeeEntity.SESSION_ID, this.sessionId);
    attendeeEntity.setProperty
        (EntityConstants.AttendeeEntity.SCREEN_NAME, this.screenName);
    attendeeEntity.setProperty
        (EntityConstants.AttendeeEntity.TIME_LAST_POLLED, this.timeLastPolled);
    return attendeeEntity;
  }

  /**
   * Returns a new Attendee from an entity of kind "Attendee".
   * @param {Entity} attendeeEntity - entity of kind "Attendee" with various 
   *     properties similar to the fields of a attendee object.
   */
  public static Attendee fromEntity(Entity attendeeEntity) {
    String sessionId = (String) attendeeEntity.getProperty
        (EntityConstants.AttendeeEntity.SESSION_ID);    
    String screenName = (String) attendeeEntity.getProperty
        (EntityConstants.AttendeeEntity.SCREEN_NAME);
    Date timeLastPolled = (Date) attendeeEntity.getProperty
        (EntityConstants.AttendeeEntity.TIME_LAST_POLLED);
    return new Attendee(sessionId, screenName, timeLastPolled);
  }
}