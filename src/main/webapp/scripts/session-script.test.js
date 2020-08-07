import * as sessionscript from './session-script';
import { ServerClient } from './serverclient';
import { Session } from './session';
import fetch from 'jest-fetch-mock';
import { Attendee } from './attendee';

const getSessionSpy = 
    jest.spyOn(ServerClient.prototype, 'getSession').
        mockResolvedValue(new Session(
            'leee3414123', '1234', [], 'Bryan'));
const getSessionIdSpy = 
    jest.spyOn(Session.prototype, 'getSessionId').
        mockReturnValue('leee3414123');
const changeControllerToSpy = 
    jest.spyOn(ServerClient.prototype, 'changeControllerTo');
const urlParamSpy = 
    jest.spyOn(window.URLSearchParams.prototype, 'get').
        mockReturnValue('Jessica');
const buildAttendeeDivSpy =
    jest.spyOn(sessionscript.sessionScriptSpies, 'buildAttendeeDiv');
const notifyOfChangesToMembershipSpy =
    jest.spyOn(sessionscript.sessionScriptSpies,
        'notifyOfChangesToMembership');

fetch.enableMocks();

afterEach(() => {
  jest.clearAllMocks();
  fetch.resetMocks();
});

test('display none to block', () => {
  document.body.innerHTML = '<div id="container"></div>';
  const container = document.getElementById('container');
  const div = document.createElement('div');
  div.style.display = 'none';
  div.id = 'session-info-div';
  container.appendChild(div);
  sessionscript.openSessionInfo();
  expect(div.style.display).toEqual('block');
});

test('display block to none', () => {
  document.body.innerHTML = '<div id="container"></div>';
  const container = document.getElementById('container');
  const div = document.createElement('div');
  div.style.display = 'block';
  div.id = 'session-info-div';
  container.appendChild(div);
  sessionscript.closeParentDisplay(div);
  expect(container.style.display).toEqual('none');
});

test('change display using both functions - open then close', () => {
  document.body.innerHTML = '<div id="container"></div>';
  const container = document.getElementById('container');
  const div = document.createElement('div');
  div.style.display = 'none';
  div.id = 'session-info-div';
  container.appendChild(div);
  sessionscript.openSessionInfo();
  sessionscript.closeParentDisplay(div);
  expect(div.style.display).toEqual('block');
  expect(container.style.display).toEqual('none');
});

test('already opened', () => {
  document.body.innerHTML = '<div id="container"></div>';
  const container = document.getElementById('container');
  const div = document.createElement('div');
  div.style.display = 'block';
  div.id = 'session-info-div';
  container.appendChild(div);
  sessionscript.openSessionInfo();
  expect(div.style.display).toEqual('block');
});

test('tests copy and paste', () => {
  document.body.innerHTML = '<div id="container"></div>';
  const container = document.getElementById('container');
  const input = document.createElement('input');
  input.id = 'session-id-field';
  input.name = 'session-id';
  input.value = 'hello!';
  input.addEventListener('click', () => {
    sessionscript.copyTextToClipboard(input)
  });
  container.appendChild(input);
  document.execCommand = jest.fn();
  input.click();
  expect(document.execCommand).toHaveBeenCalledWith('copy');
});

test('tests setReadOnlyInputs()', () => {
  document.body.innerHTML = '';
  const sessionInfoInput = document.createElement('input');
  sessionInfoInput.id = 'session-info-input';
  const welcomeMessageInput = document.createElement('input');
  welcomeMessageInput.id = 'welcome-message-input';
  document.body.appendChild(sessionInfoInput);
  document.body.appendChild(welcomeMessageInput);
  sessionscript.setReadOnlyInputs('leee3414123');
  expect(sessionInfoInput.readOnly).toBe(true);
  expect(welcomeMessageInput.readOnly).toBe(true);
  expect(sessionInfoInput.value).toEqual('leee3414123');
  expect(welcomeMessageInput.value).toEqual('leee3414123');
});

test('addOnClickTo', () => {
  buildTestPage();
  const sessionInfoSpan = document.getElementById('session-info-span');
  const sessionInfoDiv = document.getElementById('session-info-div');
  const close = document.getElementsByClassName('close').item(0);
  const sessionIdInput =
      document.getElementsByClassName('session-id-input').item(0);
  sessionscript.addOnClickListenerToElements();
  sessionInfoSpan.click();
  expect(sessionInfoDiv.style.display).toEqual('block');
  close.click();
  expect(sessionInfoDiv.style.display).toEqual('none');
  document.execCommand = jest.fn();
  sessionIdInput.click();
  expect(document.execCommand).toHaveBeenCalledWith('copy');
});

test('adding an attendee div', () => {
  document.body.innerHTML = '';
  const sessionInfoAttendeeDiv =
      document.createElement('div');
  sessionInfoAttendeeDiv.id = 'session-info-attendees';
  document.body.appendChild(sessionInfoAttendeeDiv);
  sessionscript.buildAttendeeDiv('hello', 'Bryan'); 
  expect(sessionInfoAttendeeDiv.querySelector('h3').id).toEqual('hello');
});

test('tests changeControllerTo() - controller clicks', () => {
  const attendeeDiv = document.createElement('div');
  const controllerToggle = 
      document.createElement('span');
  controllerToggle.addEventListener('click', event => {
    sessionscript.changeControllerTo(event, 'Jessica');
  }, false);
  const attendeeName = document.createElement('h3');
  attendeeName.id = 'Naomi';
  attendeeDiv.appendChild(controllerToggle);
  attendeeDiv.appendChild(attendeeName);
  document.body.appendChild(attendeeDiv);
  controllerToggle.click();
  expect(changeControllerToSpy).toBeCalledWith('Naomi');
});

test('tests changeControllerTo() - controller does not click', () => {
  const attendeeDiv = document.createElement('div');
  const controllerToggle = 
      document.createElement('span');
  controllerToggle.addEventListener('click', event => {
    sessionscript.changeControllerTo(event, 'Bob');
  }, false);
  const attendeeName =
      document.createElement('h3');
  attendeeName.id = 'Bob';
  attendeeDiv.appendChild(controllerToggle);
  attendeeDiv.appendChild(attendeeName);
  document.body.appendChild(attendeeDiv);
  controllerToggle.click();
  expect(changeControllerToSpy).toBeCalledTimes(0);
});

test('Tests to see if controller updates correctly UI wise', () => {
  document.body.innerHTML = '';
  const sessionInfoAttendeeDiv =
      document.createElement('div');
  sessionInfoAttendeeDiv.id = 'session-info-attendees';
  document.body.appendChild(sessionInfoAttendeeDiv);
  sessionscript.buildAttendeeDiv('Jessica', 'Jessica');
  sessionscript.buildAttendeeDiv('Bryan', 'Jessica');
  sessionscript.buildAttendeeDiv('Chris', 'Jessica');
  const urlParamSpy = 
      jest.spyOn(window.URLSearchParams.prototype, 'get').
          mockReturnValue('Bryan');
  sessionscript.updateController('Jessica');
  expect(sessionInfoAttendeeDiv.querySelector(`#${'Jessica'}`)
      .parentElement.querySelector('span').style.
          backgroundColor).toEqual('rgb(253, 93, 0)');
  expect(sessionInfoAttendeeDiv.querySelector(`#${'Bryan'}`)
      .parentElement.querySelector('span').style.
          backgroundColor).toEqual('rgb(255, 255, 255)');
  expect(sessionInfoAttendeeDiv.querySelector(`#${'Chris'}`)
      .parentElement.querySelector('span').style.
          backgroundColor).toEqual('rgb(255, 255, 255)');
});

test('tests updateSpanColor()', () => {
  document.body.innerHTML = '';
  const sessionInfoAttendeeDiv =
      document.createElement('div');
  sessionInfoAttendeeDiv.id = 'session-info-attendees';
  document.body.appendChild(sessionInfoAttendeeDiv);
  sessionscript.buildAttendeeDiv('Jessica', 'Jessica');
  sessionscript.buildAttendeeDiv('Bryan', 'Jessica');
  sessionscript.buildAttendeeDiv('Chris', 'Jessica');
  sessionscript.updateSpanColor(
      sessionInfoAttendeesDiv.querySelectorAll('span'));
  expect(sessionInfoAttendeeDiv.querySelector(`#${'Jessica'}`)
  .parentElement.querySelector('span').style.
      backgroundColor).toEqual('rgb(255, 255, 255)');
  expect(sessionInfoAttendeeDiv.querySelector(`#${'Bryan'}`)
    .parentElement.querySelector('span').style.
        backgroundColor).toEqual('rgb(255, 255, 255)');
  expect(sessionInfoAttendeeDiv.querySelector(`#${'Chris'}`)
    .parentElement.querySelector('span').style.
        backgroundColor).toEqual('rgb(255, 255, 255)');
});

test('tests updateCurrentControllerToggle', () => {
  document.body.innerHTML = '';
  const sessionInfoAttendeeDiv =
      document.createElement('div');
  sessionInfoAttendeeDiv.id = 'session-info-attendees';
  document.body.appendChild(sessionInfoAttendeeDiv);
  sessionscript.buildAttendeeDiv('Jessica', 'Jessica');
  sessionscript.updateCurrentControllerToggle('Jessica');
  expect(sessionInfoAttendeeDiv.querySelector(`#${'Jessica'}`)
  .parentElement.querySelector('span').style.
      backgroundColor).toEqual('rgb(253, 93, 0)');
})

test(`makes sure notifyOfChangesToMembership is
  correctly displaying message`, (done) => {
    const displayMessage = 'How are you ';
    document.body.innerHTML = '';
    const alertMembershipDiv =
        document.createElement('div');
    alertMembershipDiv.id = 'alert-membership';
    document.body.appendChild(alertMembershipDiv);
    sessionscript.notifyOfChangesToMembership(displayMessage);
    setTimeout(() => {
      expect(alertMembershipDiv.textContent).toEqual('How are you.');
      expect(alertMembershipDiv.className).toEqual('display-message');
      done();
    }, 2000);
    setTimeout(() => {
      expect(alertMembershipDiv.className).toEqual('');
      done();
    }, 6000);
});

test(`A new member 
    -updateSessionAttendees`, () => {
      const expectedMessage =
          'The following people have joined the session: Miguel.\n';
      document.body.innerHTML = '';
      const sessionInfoAttendeeDiv =
          document.createElement('div');
      sessionInfoAttendeeDiv.id = 'session-info-attendees';
      document.body.appendChild(sessionInfoAttendeeDiv);
      const alertMembershipDiv =
          document.createElement('div');
      alertMembershipDiv.id = 'alert-membership';
      document.body.appendChild(alertMembershipDiv);
      sessionscript.updateSessionAttendees(['Jessica', 'Bryan', 
          'Miguel'], 'Jessica');
      expect(notifyOfChangesToMembershipSpy).
          toHaveBeenCalledWith(expectedMessage);
      expect(buildAttendeeDivSpy).toBeCalledTimes(3);
      expect(buildAttendeeDivSpy).toHaveBeenCalledWith('Jessica', 'Jessica');
      expect(buildAttendeeDivSpy).toHaveBeenCalledWith('Bryan', 'Jessica');
      expect(buildAttendeeDivSpy).toHaveBeenCalledWith('Miguel', 'Jessica');
});

test(`A member that has left
    -updateSessionAttendees`, () => {
      const expectedMessage =
          'The following people have left the session: Bryan.';
      document.body.innerHTML = '';
      const sessionInfoAttendeeDiv =
          document.createElement('div');
      sessionInfoAttendeeDiv.id = 'session-info-attendees';
      document.body.appendChild(sessionInfoAttendeeDiv);
      const alertMembershipDiv =
          document.createElement('div');
      alertMembershipDiv.id = 'alert-membership';
      document.body.appendChild(alertMembershipDiv);
      sessionscript.updateSessionAttendees(['Jessica'], 'Bryan');
      expect(notifyOfChangesToMembershipSpy).
         toHaveBeenCalledWith(expectedMessage);
      expect(buildAttendeeDivSpy).toBeCalledTimes(1);
      expect(buildAttendeeDivSpy).toHaveBeenCalledWith('Jessica', 'Bryan');
    });

test(`A new member + a lost member' + 
    '-updateSessionAttendees`, () => {
      const expectedMessage =
          'The following people have joined the session: Miguel.' + 
              '\nThe following people have left the session: Bryan.';
      document.body.innerHTML = '';
      const sessionInfoAttendeeDiv =
          document.createElement('div');
      sessionInfoAttendeeDiv.id = 'session-info-attendees';
      document.body.appendChild(sessionInfoAttendeeDiv);
      const alertMembershipDiv =
          document.createElement('div');
      alertMembershipDiv.id = 'alert-membership';
      document.body.appendChild(alertMembershipDiv);
      sessionscript.updateSessionAttendees(['Jessica', 'Miguel'], 'Jessica');
      expect(notifyOfChangesToMembershipSpy).
          toHaveBeenCalledWith(expectedMessage);
      expect(buildAttendeeDivSpy).toBeCalledTimes(2);
      expect(buildAttendeeDivSpy).toBeCalledWith('Jessica', 'Jessica');
      expect(buildAttendeeDivSpy).toBeCalledWith('Miguel', 'Jessica');
});

test(`no update 
    '-updateSessionAttendees`, () => {
      document.body.innerHTML = '';
      const sessionInfoAttendeeDiv =
          document.createElement('div');
      sessionInfoAttendeeDiv.id = 'session-info-attendees';
      document.body.appendChild(sessionInfoAttendeeDiv);
      sessionscript.updateSessionAttendees(['Jessica', 'Bryan'], 'Bryan');
      expect(buildAttendeeDivSpy).toBeCalledWith('Jessica', 'Bryan');
      expect(buildAttendeeDivSpy).toBeCalledWith('Bryan', 'Bryan');
});

test('We can check if correct errors are thrown - connectCallback', () => {
      try {
        sessionscript.connectCallback();
      } catch (e) {
        expect(e.message).toBe('Unimplemented');
      }
});

test('We can check if correct errors are thrown - disconnectCallback', () => {
      try {
        sessionscript.disconnectCallback();
      } catch (e) {
        expect(e.message).toBe('Unimplemented');
      }
});

/**
 * Builds a mini-webpage to be used to test addOnClickListenerToElements.
 * Adds elements with specific ids/class names that the session-script 
 * function adds onClick listeners to.
 */
function buildTestPage() {
  document.body.innerHTML = '';
  const sessionInfoSpan = document.createElement('span');
  sessionInfoSpan.id = 'session-info-span';
  const sessionInfoDiv = document.createElement('div');
  sessionInfoDiv.id = 'session-info-div';
  const close = document.createElement('span');
  close.className = 'close';
  sessionInfoDiv.appendChild(close);
  const sessionIdInput = document.createElement('input');
  sessionIdInput.className = 'session-id-input';
  document.body.appendChild(sessionInfoDiv);
  document.body.appendChild(sessionInfoSpan);
  document.body.appendChild(sessionIdInput);
}
