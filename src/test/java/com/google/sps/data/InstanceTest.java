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
import com.google.sps.data.Instance;
import com.google.sps.data.InstanceInterface;
import java.util.Optional;

/** Class that test the methods in the Instance class. */
@RunWith(JUnit4.class)
public class InstanceTest {
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
    Optional<String> sessionId = Optional.of("12345");
    InstanceInterface instance = new Instance("vm1","Running", sessionId);
    Assert.assertEquals(instance.getInstanceName(), "vm1");
    Assert.assertEquals(instance.getState(), "Running");
    Assert.assertEquals(instance.getSessionId(), sessionId);
  }

  @Test
  public void testSetters() {
    // Creates test data. 
    Optional<String> sessionId = Optional.of("12345");
    InstanceInterface instance = new Instance("vm1","Running", sessionId);
    instance.setState("Stopped");
    instance.setSessionId("54321");
    Assert.assertEquals(instance.getInstanceName(), "vm1");
    Assert.assertEquals(instance.getState(), "Stopped");
    Assert.assertEquals(instance.getSessionId().get(), "54321");
  }

  @Test
  public void testConversionBetweenEntityAndInstance() {
    // Creates test data. 
    Optional<String> sessionId = Optional.of("12345");
    InstanceInterface instance = new Instance("vm1", "Running", sessionId);
    Entity instanceEntity = instance.toEntity();
    Instance newInstance = Instance.fromEntity(instanceEntity);
    Assert.assertEquals(instance, newInstance);  
  }

  @Test
  public void testEqualsMethod() {
    // Creates test data.
    Optional<String> sessionId1 = Optional.of("12345");
    InstanceInterface instance1 = new Instance("vm1", "Staging", sessionId1);
    Optional<String> sessionId2 = Optional.of("54321");
    InstanceInterface instance2 = new Instance("vm2", "Terminated", sessionId2);
    Assert.assertFalse(instance1.equals(instance2));
  }
}