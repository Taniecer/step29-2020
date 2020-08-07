package com.google.sps.data;

import com.google.api.services.compute.model.Operation;
import java.io.IOException;
import java.util.Optional;

/** Interface for the ComputerEngineClient class. */
public interface ComputeEngineClientInterface {
  /** 
   * Creates a new Instance.
   * @param {String} instanceName - the name of the new Instance.
   * @param {String} vncServerPassword - the password needed to access 
   *    to vnc server.
   */
  void createInstance(String instanceName, String vncServerPassword) 
    throws Exception;
    
  /**
   * Stops an Instance.
   * @param {String} instanceName - the name of the instance that will 
   *    be stopped.
   */
  void stopInstance(String instanceName) throws Exception;

  /** 
   * Restart an Instance that was stopped.
   * @param {String} instanceName - the name of the Instance.
   * @param {String} sessionId - the session associated with this instance. 
   */
  void restartInstance(String instanceName, String vncServerPassword) 
    throws Exception;

  /**
   * Deletes an Instance.
   * @param {String} instanceName - the name of the instance that will 
   *    be deleted.
   */
  void deleteInstance(String instanceName) throws Exception;
}