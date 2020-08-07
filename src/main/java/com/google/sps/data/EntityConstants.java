package com.google.sps.data;

/** Class that creates constants for tables. **/
public final class EntityConstants {

  /** Class that creates entity constants for the Attendee table. **/
  public final static class AttendeeEntity {
    public final static String TABLE_NAME = "Attendee";
    public final static String SESSION_ID = "SessionId";
    public final static String SCREEN_NAME = "ScreenName";
    public final static String TIME_LAST_POLLED = "TimeLastPolled";
  }

  /** Class that creates entity constants for the Instance table. **/
  public final static class InstanceEntity {
    public final static String TABLE_NAME = "Instance";
    public final static String INSTANCE_NAME = "InstanceName";
    public final static String STATE = "State";
    public final static String SESSION_ID = "SessionId";
  }

  /** Class that creates entity constants for the Session table. **/
  public final static class SessionEntity {
    public final static String TABLE_NAME = "Session";
    public final static String SESSION_ID = "SessionId";
    public final static String SCREEN_NAME_OF_CONTROLLER = "ScreenNameOfController";
    public final static String IP_OF_VM = "IpOfVM";
  }
}