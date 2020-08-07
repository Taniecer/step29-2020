package com.google.sps.servlets;

import com.google.sps.data.Attendee;
import com.google.sps.data.AttendeeInterface;
import com.google.sps.data.DatastoreClient;
import com.google.sps.data.DatastoreClientInterface;
import com.google.sps.data.SessionInterface;
import com.google.gson.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns a Session object */
@WebServlet("/get-session")
public class GetSessionServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreClientInterface datastoreClient = new DatastoreClient();
    String sessionId = 
        URLDecoder.decode(request.getParameter("session-id"), StandardCharsets.UTF_8);
    String name = URLDecoder.decode(request.getParameter("name"), StandardCharsets.UTF_8);
    Optional<SessionInterface> optionalSession; 
    try {
      optionalSession = datastoreClient.getSession(sessionId);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    // Updates the time last polled of the current attendee requesting the GetSessionServlet.
    AttendeeInterface updatedAttendee = 
        new Attendee(/**sessionId=*/sessionId, 
            /**screenName=*/name, /**timeLastPolled=*/new Date());
    datastoreClient.insertOrUpdateAttendee(updatedAttendee);
    if (!optionalSession.isPresent()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } else {
      List<AttendeeInterface> listOfAttendees = datastoreClient.getAttendeesInSession(sessionId);
      Gson gson = new Gson();
      JsonElement jsonElement = gson.toJsonTree(optionalSession.get());
      jsonElement.getAsJsonObject().
          add("listOfAttendees", gson.toJsonTree(listOfAttendees).getAsJsonArray());
      String json = gson.toJson(jsonElement);
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json;");
      response.getWriter().println(json);
    }
  }
}