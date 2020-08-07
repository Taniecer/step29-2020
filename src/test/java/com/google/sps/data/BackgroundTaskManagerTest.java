package com.google.sps;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import org.junit.After;
import org.junit.Before;
import com.google.sps.data.BackgroundTaskManager; 
import com.google.sps.data.BackgroundTaskManagerInterface;
import java.util.Optional;

/** Class that tests the BackgroundTaskManager. */
@RunWith(JUnit4.class)
public class BackgroundTaskManagerTest {
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
  public void updateInstanceTest() {
    BackgroundTaskManagerInterface backgroundManager = new BackgroundTaskManager();  
    try {
        backgroundManager.updateInstances();
    } catch(RuntimeException e) {
        Assert.assertEquals(e.getMessage(), "Unimplemented");
    }
  } 

  @Test
  public void deleteInactiveAttendeesTest() {
    BackgroundTaskManagerInterface backgroundManager = new BackgroundTaskManager();
    try {
        backgroundManager.deleteInactiveAttendees();
    } catch(RuntimeException e) {
        Assert.assertEquals(e.getMessage(), "Unimplemented");
    } 

  } 
  
  @Test
  public void replaceFaultyInstancesTest() {
    BackgroundTaskManagerInterface backgroundManager = new BackgroundTaskManager();
    try {
        backgroundManager.replaceFaultyInstances();
    } catch(RuntimeException e) {
        Assert.assertEquals(e.getMessage(), "Unimplemented");
    }
  }  
}
