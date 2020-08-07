package com.google.sps.data;
import java.util.Optional;
import java.util.List;

/** 
* An Interface that describes the different types of 
* background work that can be performed on entities of the datastore. 
*/
public interface BackgroundTaskManagerInterface { 

    /** 
    * Checks VM status to make sure they are healthy.  
    */
    public void updateInstances();
    
    /**
    * If attendee hasn't been polled, they will be 
    * removed from the session and the datastore. 
    */
    public void deleteInactiveAttendees();

    /** 
    * List all sessions and if an instance is not running 
    * for an active session, a replacement will be found or created. 
    */
    public void replaceFaultyInstances();
}