package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import java.util.Optional;
import java.util.List;

/** Interface for the Session class. */
public interface SessionInterface {
  // Getter methods
  String getSessionId();
  Optional<String> getScreenNameOfController();
  Optional<String> getIpOfVM();
  // Setter methods
  void setScreenNameOfController(String screenNameOfController);
  void setIpOfVM(String ipOfVM);
  // Compares a SessionInterface to itself. 
  boolean isEqualTo(SessionInterface session);
  // Returns a new Entity from a SessionInterface object.
  Entity toEntity();
}