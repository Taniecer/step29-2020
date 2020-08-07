package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import java.util.Optional;
import com.google.sps.data.EntityConstants;

/** Class that represents an instance. */
public class Instance implements InstanceInterface {
  
  // The name of this instance.
  private final String instanceName;

  // The state this instance is in. 
  // https://cloud.google.com/compute/docs/instances/instance-life-cycle
  private String state;

  // The session associated with this instance. If sessionId is not present,
  // then the VM is not being used for a session.
  private Optional<String> sessionId;

  /** Initializes an Instance object
   * @param {String} instanceName - the name of the instance.
   * @param {String} state - the state the instance is in.
   * @param {Optional<String>} sessionId - the session id 
   *    associated with the instance. 
   */
  public Instance(String instanceName, String state, 
  Optional<String> sessionId) {
    this.instanceName = instanceName;
    this.state = state;
    this.sessionId = sessionId;
  }

  public String getInstanceName() {
    return instanceName;
  } 

  public String getState() {
    return state;
  }

  public Optional<String> getSessionId() {
    return sessionId;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = Optional.of(sessionId);
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
    return this.isEqualTo((Instance) obj);
  }

  /** Compares an InstanceInterface to itself. */
  public boolean isEqualTo(InstanceInterface instance) {
    // Field comparison
    return instanceName.equals(instance.getInstanceName())
        && sessionId.equals(instance.getSessionId());
  }

  /** Returns a new Entity of kind "Instance" from an Instance object. */
  public Entity toEntity() {
    Entity instanceEntity = 
        new Entity(/*kind=*/EntityConstants.InstanceEntity.TABLE_NAME,
        /*keyName=*/this.instanceName);
    instanceEntity.setProperty
        (EntityConstants.InstanceEntity.INSTANCE_NAME, this.instanceName);
    instanceEntity.setProperty
        (EntityConstants.InstanceEntity.STATE, this.state);
    if (this.getSessionId().isPresent()) {
      instanceEntity.setProperty
        (EntityConstants.InstanceEntity.SESSION_ID, this.sessionId.get());
    }
    return instanceEntity;
  }

   /**
   * Returns a new Instance from an entity of kind "Instance".
   * @param {Entity} instanceEntity - entity of kind "Instance" with 
   *    various properties similar to the fields of a instance object.
   */
  public static Instance fromEntity(Entity instanceEntity) {
    String instanceName = 
        (String) instanceEntity.getProperty
        (EntityConstants.InstanceEntity.INSTANCE_NAME);
    String state = 
        (String) instanceEntity.getProperty
        (EntityConstants.InstanceEntity.STATE);
    Optional<String> sessionId = Optional.empty();
    if (instanceEntity.hasProperty
      (EntityConstants.InstanceEntity.SESSION_ID)) {
        sessionId = Optional.of((String) instanceEntity.getProperty
        (EntityConstants.InstanceEntity.SESSION_ID));
    }  
    return new Instance(instanceName, state, sessionId);
  }
}