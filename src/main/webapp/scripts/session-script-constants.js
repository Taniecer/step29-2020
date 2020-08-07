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
 * Represents (in miliseconds) the cadence at which the Session is
 * refreshed. 
 * @type {number}
 */
const SESSION_REFRESH_CADENCE_MS = 30000;

/**
 * Represents (in miliseconds) how long the message that alerts users
 * of any membership changes in the session is displayed. 
 * @type {number}
 */
const MESSAGE_DURATION_MS = 4000;

/**
 * Represents (in miliseconds) the cadence at which attempts to try and re-
 * establish contact with the server if lost.
 * @type {number}
 */
const SERVER_RECONNECT_CADENCE_MS = 15000;

export { URL_PARAM_KEY, SESSION_REFRESH_CADENCE_MS, MESSAGE_DURATION_MS, 
    SERVER_RECONNECT_CADENCE_MS };