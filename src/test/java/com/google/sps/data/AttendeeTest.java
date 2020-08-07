package com.google.sps.data;

import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import org.junit.After;
import org.junit.Before;
import com.google.sps.data.Attendee;
import com.google.sps.data.AttendeeInterface;

/** Class that test the methods in the Attendee class. */
@RunWith(JUnit4.class)
public class AttendeeTest {
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
  public void testGetters() {
    // Creates test data. 
    Date date = new Date();
    AttendeeInterface attendee = new Attendee("12345", "Taniece", date);
    Assert.assertEquals(attendee.getSessionId(), "12345");
    Assert.assertEquals(attendee.getScreenName(), "Taniece");
    Assert.assertEquals(attendee.getTimeLastPolled(), date);
  }

  @Test
  public void testConversionBetweenEntityAndAttendee() {
    // Creates test data. 
    AttendeeInterface attendee = new Attendee("12345", "Taniece", new Date());
    Entity testEntity = attendee.toEntity();
    AttendeeInterface newAttendee = Attendee.fromEntity(testEntity);
    Assert.assertEquals(attendee, newAttendee);
  }

  @Test
  public void testEqualsMethod() {
    // Creates test data.
    AttendeeInterface attendee1 = new Attendee("12345", "Jasmine", new Date());
    AttendeeInterface attendee2 = new Attendee("54321", "Chris", new Date());
    Assert.assertFalse(attendee1.equals(attendee2));
  }
}