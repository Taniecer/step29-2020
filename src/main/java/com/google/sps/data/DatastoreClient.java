package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.google.sps.data.Attendee;
import com.google.sps.data.AttendeeInterface;
import com.google.sps.data.Session;
import com.google.sps.data.SessionInterface;
import com.google.sps.data.Instance;
import com.google.sps.data.InstanceInterface;

/** Class that reads from and writes to Datastore. */
public class DatastoreClient implements DatastoreClientInterface {
  private final DatastoreService datastoreService = 
    DatastoreServiceFactory.getDatastoreService();
  private final String INSTANCE_STATE_TERMINATED = "TERMINATED";

  public void insertOrUpdateAttendee(AttendeeInterface attendee) {
    datastoreService.put(attendee.toEntity());
  }

  public void insertOrUpdateInstance(InstanceInterface instance) {
    datastoreService.put(instance.toEntity());
  }

  public void insertOrUpdateSession(SessionInterface session) {
    datastoreService.put(session.toEntity());
  }

  public Optional<SessionInterface> getSession(String sessionId) {
    Query getSession = new Query(EntityConstants.SessionEntity.TABLE_NAME)
      .setFilter(new FilterPredicate(EntityConstants.SessionEntity.SESSION_ID,
      FilterOperator.EQUAL, sessionId));
    PreparedQuery pq = datastoreService.prepare(getSession);
    Entity sessionEntity = pq.asSingleEntity();
    return Optional.of(Session.fromEntity(sessionEntity));
  }

  public Optional<InstanceInterface> getInstance(String instanceName) {
    Query getInstance = new Query(EntityConstants.InstanceEntity.TABLE_NAME)
      .setFilter(new FilterPredicate
      (EntityConstants.InstanceEntity.INSTANCE_NAME, 
      FilterOperator.EQUAL, instanceName));
    PreparedQuery pq = datastoreService.prepare(getInstance);
    Entity instanceEntity = pq.asSingleEntity();
    return Optional.of(Instance.fromEntity(instanceEntity));
   }

  public Optional<AttendeeInterface> getAttendee
    (String screenName,String sessionId) {
      Filter filterByScreenName = new FilterPredicate
        (EntityConstants.AttendeeEntity.SCREEN_NAME, FilterOperator.EQUAL,
        screenName);
      Filter filterBySessionID = new FilterPredicate
        (EntityConstants.AttendeeEntity.SESSION_ID, FilterOperator.EQUAL, 
        sessionId);
      Filter filterByScreenNameAndSessionId = 
        CompositeFilterOperator.and(filterByScreenName, filterBySessionID);
      Query getAttendee = new Query
        (EntityConstants.AttendeeEntity.TABLE_NAME)
        .setFilter(filterByScreenNameAndSessionId);
      PreparedQuery pq = datastoreService.prepare(getAttendee);
      Entity attendeeEntity = pq.asSingleEntity();
      return Optional.of(Attendee.fromEntity(attendeeEntity));
   }

  public void deleteAttendee(String screenName, String sessionId) {
    Filter filterByScreenName = new FilterPredicate
      (EntityConstants.AttendeeEntity.SCREEN_NAME, FilterOperator.EQUAL,
       screenName);
    Filter filterBySessionID = new FilterPredicate
      (EntityConstants.AttendeeEntity.SESSION_ID, FilterOperator.EQUAL, 
      sessionId);
    Filter filterByScreenNameAndSessionId = 
      CompositeFilterOperator.and(filterByScreenName, filterBySessionID);
    Query getAttendee = new Query
      (EntityConstants.AttendeeEntity.TABLE_NAME)
      .setFilter(filterByScreenNameAndSessionId);
    PreparedQuery pq = datastoreService.prepare(getAttendee);
    Entity attendeeEntity = pq.asSingleEntity();
    datastoreService.delete(attendeeEntity.getKey());
    }

  public void deleteInstance(String instanceName) {
    Query getInstance = new Query(EntityConstants.InstanceEntity.TABLE_NAME)
      .setFilter(new FilterPredicate
      (EntityConstants.InstanceEntity.INSTANCE_NAME,
      FilterOperator.EQUAL, instanceName));
    PreparedQuery pq = datastoreService.prepare(getInstance);
    Entity instanceEntity = pq.asSingleEntity();
    datastoreService.delete(instanceEntity.getKey());
    }

  public void deleteSession(String sessionId) {
    Query getSession = new Query(EntityConstants.SessionEntity.TABLE_NAME)
      .setFilter(new FilterPredicate
      (EntityConstants.SessionEntity.SESSION_ID,
      FilterOperator.EQUAL, sessionId));
    PreparedQuery pq = datastoreService.prepare(getSession);
    Entity sessionEntity = pq.asSingleEntity();
    datastoreService.delete(sessionEntity.getKey());
    }

  public List<AttendeeInterface> getAttendeesInSession (String sessionId) {
    Query query = new Query(EntityConstants.AttendeeEntity.TABLE_NAME)
      .setFilter(new FilterPredicate
      (EntityConstants.AttendeeEntity.SESSION_ID, FilterOperator.EQUAL, 
      sessionId));
    PreparedQuery results = datastoreService.prepare(query);
    List<AttendeeInterface> attendeeList = new ArrayList<>();
    for (Entity attendeeEntity : results.asIterable()) {
      attendeeList.add(Attendee.fromEntity(attendeeEntity));
    }
    return attendeeList;
  }

  public List<InstanceInterface> getAvailableInstances() {
    Query query = new Query(EntityConstants.InstanceEntity.TABLE_NAME)
      .setFilter(new FilterPredicate(EntityConstants.InstanceEntity.STATE, 
      FilterOperator.EQUAL, INSTANCE_STATE_TERMINATED));
    List<InstanceInterface> instanceList = new ArrayList<>();
    PreparedQuery results = datastoreService.prepare(query);
    for (Entity instanceEntity : results.asIterable()) {
      instanceList.add(Instance.fromEntity(instanceEntity));
    }
    return instanceList;
  }
} 