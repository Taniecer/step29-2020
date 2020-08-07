package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import java.util.Optional;

/** Interface for the Instance class.*/
public interface InstanceInterface {
  // Getter methods
  String getInstanceName();
  String getState();
  Optional<String> getSessionId();
  // Setter methods
  void setState(String state);
  void setSessionId(String sessionId);
  // Compares an InstanceInterface to itself.
  boolean isEqualTo(InstanceInterface instance);
  // Returns a new Entity from an InstanceInterface object
  Entity toEntity();
}