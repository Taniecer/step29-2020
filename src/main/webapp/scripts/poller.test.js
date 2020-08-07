import { Poller } from './poller';

jest.setTimeout(15000);

// Keeps track on the number of times the testPollingFunction
// is executed.
let pollCounter = 0;

afterEach(() => {    
  pollCounter = 0;
});

test('Test to see if stop is working correctly!', (done) => {
  const poll = new Poller(testPollingFunction, 1000);
  poll.start();
  setTimeout(() => {
    poll.stop();
    expect(pollCounter).toBeGreaterThan(9);
    expect(poll.getLastResult()).toBe(true);   
    done();
  }, 10000);
});

test('We can check if poll() is called correct amount' + 
    'of times + correct return value', (done) => {
      const poll = new Poller(testPollingFunction, 1000);
      poll.start();
      setTimeout(() => {
        expect(pollCounter).toBeGreaterThan(4);
        expect(poll.getLastResult()).toBe(true);   
        done();
      }, 5000);
});

test('stopping before starting', () => {
  const poll = new Poller(testPollingFunction, 1000);
  poll.stop();
  expect(pollCounter).toBe(0);
  expect(poll.getLastResult()).toBeNull();
});

test('starting up, immediately stopping', () => {
  const poll = new Poller(testPollingFunction, 1000);
  poll.start();
  poll.stop();
  expect(pollCounter).toBe(1);
  expect(poll.getLastResult()).toBe(true); 
});

test('starting up after stopping', (done) => {
  const poll = new Poller(testPollingFunction, 1000);
  poll.start();
  poll.stop();
  poll.start();
  setTimeout(() => {
    expect(pollCounter).toBeGreaterThan(10);
    expect(poll.getLastResult()).toBe(true);   
    done();
  }, 5000);
});

function testPollingFunction() {
  pollCounter++;
  return true;
}
