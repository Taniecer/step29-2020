import { NoVNCClient } from './novncclient.js';

test('We can check if remoteToSession works correctly', () => {
    const div = document.createElement('div');
    const novncClient = 
        new NoVNCClient(testConnectCallback, testDisconnectCallback, div);
    novncClient.remoteToSession('123456.12', 'EEEEE7');
    expect(novncClient.rfbConnection_.target).toEqual(div);
    expect(novncClient.rfbConnection_.viewOnly).toBe(true);
});

test('We can check if disconnect works - no object', () => {
  const disconnectSpy = jest.spyOn(RFB.prototype, 'disconnect');
  const div = document.createElement('div');
  const novncClient = 
      new NoVNCClient(testConnectCallback, testDisconnectCallback, div);
  novncClient.disconnect();
  expect(disconnectSpy).toHaveBeenCalledTimes(0);
});

test('We can check if disconnect works - with object', () => {
  const disconnectSpy = jest.spyOn(RFB.prototype, 'disconnect');
  const div = document.createElement('div');
  const novncClient = 
      new NoVNCClient(testConnectCallback, testDisconnectCallback, div);
  novncClient.remoteToSession('123456.12', 'EEEEE7');
  novncClient.disconnect();
  expect(disconnectSpy).toHaveBeenCalledTimes(1);
});

test('We can check if setViewOnly works - no object', () => {
  const viewOnlySpy = jest.spyOn(RFB.prototype, 'viewOnly', 'set');
  const div = document.createElement('div');
  const novncClient = 
      new NoVNCClient(testConnectCallback, testDisconnectCallback, div);
  novncClient.setViewOnly(false);
  expect(viewOnlySpy).toHaveBeenCalledTimes(0);
});

test('We can check if setViewOnly works - with object', () => {
  const viewOnlySpy = jest.spyOn(RFB.prototype, 'viewOnly', 'set');
  const div = document.createElement('div');
  const novncClient = 
      new NoVNCClient(testConnectCallback, testDisconnectCallback, div);
  novncClient.remoteToSession('123456.12', 'EEEEE7');
  novncClient.setViewOnly(false);
  expect(viewOnlySpy).toHaveBeenCalledWith(false);
  expect(novncClient.rfbConnection_.viewOnly).toBe(false);
});

function testConnectCallback() {
  const hello = 'Hello!';
}

function testDisconnectCallback() {
  const goodbye = 'Goodbye!';
}


