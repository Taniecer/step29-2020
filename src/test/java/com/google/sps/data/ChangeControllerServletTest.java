package com.google.sps.data;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.servlets.ChangeControllerServlet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ChangeControllerServletTest {
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final DatastoreClientInterface datastoreClient = new DatastoreClient();
  private final ChangeControllerServlet servlet = new ChangeControllerServlet();

  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    MockitoAnnotations.openMocks(this);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testDoPostOkResponse() throws IOException, ServletException {
    Mockito.when(request.getParameter("session-id")).thenReturn("EEEEE7");
    Mockito.when(request.getParameter("name")).thenReturn("Jessica");
    SessionInterface oldSession =
        new Session("EEEEE7", Optional.of("Bryan"), Optional.of("12.34.56.78"));
    SessionInterface updatedExpectedSession =
        new Session("EEEEE7", Optional.of("Jessica"), Optional.of("12.34.56.78"));
    datastoreClient.insertOrUpdateSession(oldSession);
    servlet.doPost(request, response);
    Assert.assertEquals(updatedExpectedSession, datastoreClient.getSession("EEEEE7").get());
    Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  public void testDoPostBadRequest() throws IOException, ServletException {
    Mockito.when(request.getParameter("session-id")).thenReturn("EEEE7");
    Mockito.when(request.getParameter("name")).thenReturn("Jessica");
    servlet.doPost(request, response);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }
}