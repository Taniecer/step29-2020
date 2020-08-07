package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Attendee;
import java.util.Date;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.DatastoreClientInterface;
import com.google.sps.data.DatastoreClient;
import com.google.sps.data.AttendeeInterface;
import com.google.sps.data.Attendee;

/** 
 * Servlet that checks if given session Id exists and screen name is unique 
 * before storing users data in datastore and redirected them to the in
 * session webpage.  
 */
@WebServlet("/join-session")
public class JoinSessionServlet extends HttpServlet { 
  private static final String SCREEN_NAME_PARAM_KEY = "name";
  private static final String SESSION_ID_PARAM_KEY = "session-id";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
        DatastoreClientInterface datastoreClient = new DatastoreClient();
        String screenName = request.getParameter(SCREEN_NAME_PARAM_KEY);
        String sessionId = request.getParameter(SESSION_ID_PARAM_KEY);
        Date timeLastPolled = new Date();
        List<AttendeeInterface> attendeeList = 
            datastoreClient.getAttendeesInSession(sessionId);
        if (attendeeList.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        else {
            if (alreadyExists(screenName, attendeeList)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            AttendeeInterface attendee = 
                new Attendee(sessionId, screenName, timeLastPolled);
            datastoreClient.insertOrUpdateAttendee(attendee);
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/session.html?session-id=" + sessionId +
                "&name=" + screenName); 
        }
    }
    
    // Checks if a screenName already exists in a session.
    private boolean alreadyExists
        (String screenName, List<AttendeeInterface> attendeeList) {
            for (int i = 0; i<attendeeList.size(); i++) {
                if (attendeeList.get(i).getScreenName().equals(screenName)) {
                    return true;
                }
            }
            return false;
    }
}       