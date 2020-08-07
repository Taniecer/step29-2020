package com.google.sps.data;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import org.junit.After;
import org.junit.Before;
import com.google.sps.data.Session;
import com.google.sps.data.SessionInterface;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

/** Class that tests the methods in the Session class. */
@RunWith(JUnit4.class)
public class SessionTest {
  private final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
   helper.setUp();
  }

  @After
  public void tearDown() {
   helper.tearDown();
  }

  @Test
  public void testGetter() {
    // Creates test data. 
    Optional<String> controller = Optional.of("Taniece");
    Optional<String> ipOfVm = Optional.of("123.123.12.1");
    SessionInterface session = new Session("12345", controller, ipOfVm);
    Assert.assertEquals(session.getSessionId(), "12345");
    Assert.assertEquals(session.getScreenNameOfController(), controller);
    Assert.assertEquals(session.getIpOfVM(), ipOfVm);
  }

  @Test
  public void testSetter() {
    // Creates test data. 
    Optional<String> controller = Optional.of("Taniece");
    Optional<String> ipOfVm = Optional.of("123.123.12.1");
    SessionInterface session = new Session("12345", controller, ipOfVm);
    session.setScreenNameOfController("Jasmine");
    session.setIpOfVM("321.123.12.1");
    Assert.assertEquals(session.getSessionId(), "12345");
    Assert.assertEquals(session.getScreenNameOfController().get(), "Jasmine");
    Assert.assertEquals(session.getIpOfVM().get(), "321.123.12.1");
  }
  
  // Test if Optional variables are converted properly.
  @Test
  public void testOptionalValues() {
    // Creates test data. 
    Optional<String> controller = Optional.of("Taniece");
    Optional<String> ipOfVm = Optional.of("123.123.12.1");
    SessionInterface session = new Session("12345", controller, ipOfVm);
    Assert.assertEquals(session.getScreenNameOfController().get(), "Taniece");
    Assert.assertEquals(session.getIpOfVM().get(), "123.123.12.1");
  }

  @Test
  public void testConversionBetweenEntityAndSession() {
    // Creates test data. 
    Optional<String> controller = Optional.of("Taniece");
    Optional<String> ipOfVm = Optional.of("123.123.12.1");
    SessionInterface session = new Session("12345", controller, ipOfVm);
    Entity sessionEntity = session.toEntity();
    SessionInterface newSession = Session.fromEntity(sessionEntity);
    Assert.assertEquals(session, newSession);
  }

  @Test
  public void testEqualsMethod() {
    // Creates test data.
    Optional<String> controller1 = Optional.of("Jasmine");
    Optional<String> ipOfVm1 = Optional.of("123.321.12.1");
    SessionInterface session1 = 
        new Session("12345", controller1, ipOfVm1);
    Optional<String> controller2 = Optional.of("Chris");
    Optional<String> ipOfVm2 = Optional.of("123.321.12.1");
    SessionInterface session2 = 
        new Session("54321", controller2, ipOfVm2);
    Assert.assertFalse(session1.equals(session2));
  }
}