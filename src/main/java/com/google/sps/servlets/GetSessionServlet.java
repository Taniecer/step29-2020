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
    AttendeeInterface updatedAttendee = new Attendee(sessionId, name, new Date());
    datastoreClient.insertOrUpdateAttendee(updatedAttendee);
    Optional<SessionInterface> session = datastoreClient.getSession(sessionId);
    if (session.isPresent()) {
      List<AttendeeInterface> listOfAttendees = datastoreClient.getAttendeesInSession(sessionId);
      Gson gson = new Gson();
      JsonElement jsonElement = gson.toJsonTree(session.get());
      jsonElement.getAsJsonObject().
          add("listOfAttendees", gson.toJsonTree(listOfAttendees).getAsJsonArray());
      String json = gson.toJson(jsonElement);
      response.setContentType("application/json;");
      response.getWriter().println(json);
    }
  }
}