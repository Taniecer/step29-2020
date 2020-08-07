package com.google.sps.data;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.sps.servlets.JoinSessionServlet;
import com.google.appengine.api.datastore.Query;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JoinSessionServlet.class})
public class JoinSessionServletTest {
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final DatastoreClientInterface datastoreClient = 
      new DatastoreClient();
  private final JoinSessionServlet servlet = new JoinSessionServlet();

  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    MockitoAnnotations.openMocks(this);
    Date mockDate = new Date();
    PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(mockDate);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testInvalidSessionId() throws Exception {
    Mockito.when(request.getParameter("name")).thenReturn("Taniece");
    Mockito.when(request.getParameter("session-id")).thenReturn("12345");
    servlet.doPost(request, response);
    List<AttendeeInterface> attendeeList = 
        datastoreClient.getAttendeesInSession("12345");
    Assert.assertEquals(attendeeList.size(), 0);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

  @Test    
  public void testDoPostScreenNameNotUnique() throws Exception {
    Mockito.when(request.getParameter("name")).thenReturn("Taniece");
    Mockito.when(request.getParameter("session-id")).thenReturn("12345");
    AttendeeInterface attendee1 = new Attendee("12345", "Taniece", new Date());
    datastoreClient.insertOrUpdateAttendee(attendee1);
    AttendeeInterface attendee2 = 
        datastoreClient.getAttendee("Taniece", "12345").get();
    Assert.assertEquals(attendee1,attendee2);
    servlet.doPost(request, response);
    List<AttendeeInterface> attendeeList = 
        datastoreClient.getAttendeesInSession("12345");
    Assert.assertEquals(attendeeList.size(), 1);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
  }

  @Test
  public void testDoPostResponse() throws Exception {
    AttendeeInterface attendee1 = new Attendee("12345", "Taniece", new Date());
    datastoreClient.insertOrUpdateAttendee(attendee1);
    List<AttendeeInterface> attendeeList = 
        datastoreClient.getAttendeesInSession("12345");
    Assert.assertEquals(attendeeList.size(), 1);
    Mockito.when(request.getParameter("name")).thenReturn("Chris");
    Mockito.when(request.getParameter("session-id")).thenReturn("12345");
    servlet.doPost(request, response);
    List<AttendeeInterface> attendeeList2 = 
        datastoreClient.getAttendeesInSession("12345");
    Assert.assertEquals(attendeeList2.size(), 2);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    Mockito.verify(response).sendRedirect
        ("/session.html?session-id=12345&name=Chris");
  } 
} 