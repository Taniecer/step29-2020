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
import com.google.sps.servlets.GetSessionServlet;
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
@PrepareForTest({GetSessionServlet.class})
public class GetSessionServletTest {
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final DatastoreClientInterface datastoreClient = new DatastoreClient();
  private final GetSessionServlet servlet = new GetSessionServlet();

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

  public void testDoGetOkResponse() throws Exception {
    SessionInterface expectedSession =
        new Session("EEEEE7", Optional.of("Jessica"), Optional.of("12.34.56.78"));
    AttendeeInterface expectedAttendee =
        new Attendee("EEEEE7", "Jessica", new Date());
    datastoreClient.insertOrUpdateSession(expectedSession);
    datastoreClient.insertOrUpdateAttendee(expectedAttendee);
    Mockito.when(request.getParameter("session-id")).thenReturn("EEEEE7");
    Mockito.when(request.getParameter("name")).thenReturn("Jessica");
    List<AttendeeInterface> listOfAttendees = Arrays.asList(expectedAttendee);
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(expectedSession);
    jsonElement.getAsJsonObject().
        add("listOfAttendees", gson.toJsonTree(listOfAttendees).getAsJsonArray());
    String expectedJson = gson.toJson(jsonElement);
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    Mockito.when(response.getWriter()).thenReturn(pw);
    servlet.doGet(request, response);
    String actualJson = sw.getBuffer().toString().trim();
    Assert.assertEquals(expectedJson, actualJson);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  public void testDoGetBadRequest() throws Exception {
    Mockito.when(request.getParameter("session-id")).thenReturn("EEEE7");
    Mockito.when(request.getParameter("name")).thenReturn("Jessica");
    servlet.doGet(request, response);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }
}