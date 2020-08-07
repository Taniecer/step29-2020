package com.google.sps.data;

import java.util.Date;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.DatastoreClientInterface;
import com.google.sps.data.DatastoreClient;
import com.google.sps.data.BackgroundTaskManagerInterface;

/** 
* Implements the interface for the BackgroundTaskManager class. 
*/ 
public class BackgroundTaskManager implements BackgroundTaskManagerInterface {
   
    @Override
    public void updateInstances() {
        throw new RuntimeException("Unimplemented");
    } 

    @Override
    public void replaceFaultyInstances() {
        throw new RuntimeException("Unimplemented");
    }

    @Override
    public void deleteInactiveAttendees() {
        throw new RuntimeException("Unimplemented");
    }

}