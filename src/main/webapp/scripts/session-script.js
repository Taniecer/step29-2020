import { ServerClient } from './serverclient.js';

/**
 * Represents the URLSearchParams the client is in, 
 * holds information such as the session ID and 
 * the screen name of the current user.
 * @type {URLSearchParams}
 */
let urlParameters;

/**
 * Represents the ServerClient object responsible for
 * keeping up-to-date with the current session and handles many
 * of the client-to-server interactions, like changing the controller.
 * @type {ServerClient}
 */
let serverClient;

/**
 * Surrounds the noVNC library, providing many of its functionality in the
 * context necessary for Virtual Movie Night. Allows for remoting into a
 * session, handles disconnecting and connecting, and allows one to change
 * who can interact with the virtual machines.
 * @type {NoVNCClient}
 */
let novncClient;

/**
 * This object represents the two keys that are a part 
 * of the URLSearchParams of the given session. They convey the current
 * screen name of the current user and the session-id they are in.
 * @type {object}
 */
const URL_PARAM_KEY = {
  SCREEN_NAME: 'name',
  SESSION_ID: 'session-id'
};

/**
 * Represents the current state of the novncClient in terms of whether or
 * not it is connected.
 * @type {boolean}
 */
let isConnected = false;

/**
 * Represents (in miliseconds) the cadence at which the session is
 * refreshed. 
 * @type {number}
 */
const SESSION_REFRESH_CADENCE_MS = 30000;

/**
 * This waits until the webpage loads and then it calls the
 * anonymous function, which calls main.
 */
window.onload = function() { main(); }

/**
 * function main() connects the client to a session and begins many of
 * the behind the scenes operations, like caching.
 */
function main() {
  urlParameters = new URLSearchParams(window.location.search);
  serverClient = new ServerClient(urlParameters);
  novncClient = new NoVNCClient(
      connectCallback, disconnectCallback, 
          document.getElementById('session-screen'));
  addOnClickListenerToElements();
  serverClient.getSession().then(session => {
    setReadOnlyInputs(session.getSessionId());
  }).catch(error => {
    window.alert('No contact with the server!');
  });
}

/**
 * Adds an onclick event listener to some of the elements on the
 * in-session webpage.
 */
function addOnClickListenerToElements() {
  document.getElementById('session-info-span').addEventListener('click', 
      openSessionInfo);
  document.querySelectorAll('.close').forEach(element => {
    element.addEventListener('click', event => {
      closeParentDisplay(event.target);
    });
  });
  document.querySelectorAll('.session-id-input').forEach(element => {
    element.addEventListener('click', event => {
      copyTextToClipboard(event.target);
    });
  });
}

/**
 * function setReadOnlyInputs() changes the two inputs,
 * one on the welcome message and the other in the session 
 * information div, to show the session ID and then changes them
 * to read only (meaning they cannot be changed once set).
 * @param {string} sessionId
 */
function setReadOnlyInputs(sessionId) {
  const /** HTMLElement */ sessionInfoInput = 
      document.getElementById('session-info-input');
  sessionInfoInput.value = sessionId;
  sessionInfoInput.readOnly = true;
  const /** HTMLElement */ welcomeMessageInput = 
      document.getElementById('welcome-message-input');
  welcomeMessageInput.value = sessionId;
  welcomeMessageInput.readOnly = true;
}

/**
 * function buildAttendeeDiv() adds the div element containing
 * all the elements representing an attendee to the session info
 * attendees div.
 * @param {string} nameOfAttendee name of attendee to build
 * @param {string} controller name of the controller of the session
 */
function buildAttendeeDiv(nameOfAttendee, controller) {
  const /** HTMLElement */ sessionInfoAttendeesDiv =
      document.getElementById('session-info-attendees');
  const /** HTMLDivElement */ attendeeDiv = document.createElement('div');
  attendeeDiv.className = 'attendee-div'
  const /** HTMLSpanElement */ controllerToggle = 
      document.createElement('span');
  controllerToggle.className = 'controller-toggle';
  controllerToggle.addEventListener('click', event => {
    changeControllerTo(event, controller);
  }, /**AddEventListenerOptions=*/false);
  const /** HTMLHeadingElement */ attendeeName =
      document.createElement('h3');
  attendeeName.innerHTML = nameOfAttendee;
  attendeeName.className = 'attendee-name'
  attendeeName.id = nameOfAttendee;
  attendeeDiv.appendChild(controllerToggle);
  attendeeDiv.appendChild(attendeeName);
  sessionInfoAttendeesDiv.appendChild(attendeeDiv);
}

/**
 * If the current controller of the session clicks on the controller 
 * toggle, their controller status is revoked and the server is updated
 * with information on the new controller.
 * @param {MouseEvent} event the event that captures what was clicked on
 * @param {string} controller name of the controller of the session
 */
function changeControllerTo(event, controller) {
  if (urlParameters.get(URL_PARAM_KEY.SCREEN_NAME) === controller) {
    try {
      serverClient.changeControllerTo(/**newControllerName=*/
          event.target.parentElement.querySelector('h3').id);
    } catch (e) {
      window.alert('No contact with the server!');
    }
  }
}

/**
 * function openSessionInfo() displays the div container
 * that has information about the session.
 */
function openSessionInfo() {
  document.getElementById('session-info-div').style.display = 'block'; 
}

/**
 * function closeParentDisplay() changes the display of the 
 * parent of the element passed in to 'none'.
 * @param {HTMLElement} element
 */
function closeParentDisplay(element) {
  element.parentElement.style.display = 'none';
}

/**
 * function copyTextToClipboard() copies the text of the element passed
 * in into the clipboard.
 * @param {HTMLInputElement} element
 */
function copyTextToClipboard(element) {
  element.select();
  document.execCommand('copy');
}

export { openSessionInfo, closeParentDisplay, copyTextToClipboard, 
  addOnClickListenerToElements, setReadOnlyInputs, buildAttendeeDiv,
  changeControllerTo };
