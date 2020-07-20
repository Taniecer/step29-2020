import { SessionCache } from './sessioncache';
import { Session } from './session';
import fetch from 'jest-fetch-mock';

fetch.enableMocks();
jest.setTimeout(40000);

const setTimeoutSpy = jest.spyOn(window, 'setTimeout');
const clearTimeoutSpy = jest.spyOn(window, 'clearTimeout');
const testParams =  new URLSearchParams('?session-id=EEEE7&name=chris');
const expectedResult = Session.fromObject({
  sessionID: 'EEEE7',
  controller: 'chris',
  ipOfVM: '1.2.3.4.5.6.7',
  listOfAttendes: ['chris', 'bryan']
});

afterEach(() => {    
  jest.clearAllMocks();
  fetch.resetMocks();
});

test('Test to see if stop is working correctly!', (done) => {
  fetch.mockResponse(JSON.stringify(expectedResult));
  const cache = new SessionCache(testParams, 1000);
  cache.start();
  setTimeout(async () => {
    cache.stop();
    expect(clearTimeoutSpy).toHaveBeenCalledTimes(1);
    expect(setTimeoutSpy.mock.calls.length).toBeGreaterThan(3);
    await expect(cache.getSession()).
        resolves.toEqual(expectedResult);
    done();
  }, 30000);
});

test('Checks continuation of refreshing - no stop', (done) => {
  fetch.mockResponse(JSON.stringify(expectedResult));
  const cache = new SessionCache(testParams, 1000);
  cache.start();
  setTimeout(async () => {
    expect(clearTimeoutSpy).toHaveBeenCalledTimes(0);
    expect(setTimeoutSpy.mock.calls.length).toBeGreaterThan(1);
    await expect(cache.getSession()).
        resolves.toEqual(expectedResult);
    done();
  }, 5000);
});

test('stopping before starting', async () => {
  fetch.mockResponse(JSON.stringify(expectedResult));
  const cache = new SessionCache(testParams, 1000);
  cache.stop();
  expect(clearTimeoutSpy).toHaveBeenCalledTimes(1);
  expect(setTimeoutSpy).toHaveBeenCalledTimes(0);
  await expect(cache.getSession()).
      rejects.toThrowError('No contact with server.');
});

test('starting up, immediately stopping', async () => {
  fetch.mockResponse(JSON.stringify(expectedResult));
  const cache = new SessionCache(testParams, 1000);
  cache.start();
  cache.stop();
  expect(clearTimeoutSpy).toHaveBeenCalledTimes(1);
  expect(setTimeoutSpy).toHaveBeenCalledTimes(1);
  await expect(cache.getSession()).
      resolves.toEqual(expectedResult);
});

test('starting up after stopping', (done) => {
  fetch.mockResponse(JSON.stringify(expectedResult));
  const cache = new SessionCache(testParams, 1000);
  cache.start();
  cache.stop();
  cache.start();
  setTimeout(async () => {
    expect(clearTimeoutSpy).toHaveBeenCalledTimes(1);
    expect(setTimeoutSpy.mock.calls.length).toBeGreaterThanOrEqual(3); 
    await expect(cache.getSession()).
        resolves.toEqual(expectedResult); 
    done();
  }, 5000);
});

test('retrieving info after starting immediately', async () => {
  fetch.mockResponse(JSON.stringify(expectedResult));
  const cache = new SessionCache(testParams, 1000);
  cache.start();
  await expect(cache.getSession()).
      resolves.toEqual(expectedResult);
});
