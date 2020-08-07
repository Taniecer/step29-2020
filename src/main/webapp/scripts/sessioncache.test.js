import { SessionCache } from './sessioncache.js';
import { Session } from './session.js';
import fetch from 'jest-fetch-mock';

fetch.enableMocks();
jest.setTimeout(40000);

const mockFetchResponse = 
'{"sessionId":"EEEE7","screenNameOfController":{"value":"chris"}'+
',"ipOfVM":{"value":"1.2.3.4.5.6.7"},"listOfAttendees":[{"sessionId":'+
'"EEEE7","screenName":"chris","timeLastPolled":'+
'"Aug 3, 2020, 9:38:40 PM"}, {"sessionId":'+
'"EEEE7","screenName":"bryan","timeLastPolled":'+
'"Aug 3, 2020, 9:38:40 PM"} ]}'; 

afterEach(() => {    
  jest.clearAllMocks();
  fetch.resetMocks();
});

test('Test to see if stop is working correctly!', (done) => {
  fetch.mockResponse(mockFetchResponse);
  const cache = new SessionCache(testSessionRequest, 1000);
  cache.start();
  setTimeout(async () => {
    cache.stop();
    await expect(cache.getSession()).
        resolves.toEqual(Session.fromObject(
            JSON.parse(mockFetchResponse)));
    done();
  }, 30000);
});

test('Checks continuation of refreshing - no stop', (done) => {
  fetch.mockResponse(mockFetchResponse);
  const cache = new SessionCache(testSessionRequest, 1000);
  cache.start();
  setTimeout(async () => {
    await expect(cache.getSession()).
        resolves.toEqual(Session.fromObject(
            JSON.parse(mockFetchResponse)));
    done();
  }, 5000);
});

test('stopping before starting', async () => {
  fetch.mockResponse(mockFetchResponse);
  const cache = new SessionCache(testSessionRequest, 1000);
  cache.stop();
  await expect(cache.getSession()).
      rejects.toThrowError('No contact with server.');
});

test('starting up, immediately stopping', async () => {
  fetch.mockResponse(mockFetchResponse);
  const cache = new SessionCache(testSessionRequest, 1000);
  cache.start();
  cache.stop();
  await expect(cache.getSession()).
      resolves.toEqual(Session.fromObject(JSON.parse(mockFetchResponse)));
});

test('starting up after stopping', (done) => {
  fetch.mockResponse(mockFetchResponse);
  const cache = new SessionCache(testSessionRequest, 1000);
  cache.start();
  cache.stop();
  cache.start();
  setTimeout(async () => {
    await expect(cache.getSession()).
        resolves.toEqual(
            Session.fromObject(JSON.parse(mockFetchResponse))); 
    done();
  }, 5000);
});

test('retrieving info after starting immediately', async () => {
  fetch.mockResponse(mockFetchResponse);
  const cache = new SessionCache(testSessionRequest, 1000);
  cache.start();
  await expect(cache.getSession()).
      resolves.toEqual(Session.fromObject(JSON.parse(mockFetchResponse)));
});

async function testSessionRequest() {
  const response =
      await fetch('https://api.exchangeratesapi.io/latest?base=USD');
  return await response.json();
}