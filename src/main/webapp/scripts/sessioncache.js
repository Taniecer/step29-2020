import { Poller } from './poller.js';
import { Session } from './session.js';

/** 
 * SessionCache bridges the gap between the client and server.
 * Allows client to indirectly stay in contact with the server,
 * encapsulating many of the contact points away from the client.
 * 
 * SessionCache looks for information about the current session and
 * updates the server with information about each client in return (like
 * date last contacted).
 */
class SessionCache {
  /**
   * Initalizes a SessionCache object.
   * @param {function(): Promise<any>} sessionRequest Represents
   *    the fetch api request responsible for contacting the server
   *    to retrieve the Session object.
   * @param {number=} [refreshCadenceMs = 30000] Represents the cadence at
   *    which the Session object is refreshed. By default, the rate is
   *    30,000 milliseconds (or 30 seconds).  
   */
  constructor(sessionRequest, refreshCadenceMs = 30000) {
    /** 
     * Poller responsible for contacting the server to retrieve the Session
     * object.
     * @private {Poller} 
     */
    this.sessionPoller_ = 
        new Poller(sessionRequest, refreshCadenceMs);
  }

  /** 
   * This method begins polling for the Session object.
   */
  start() {
    this.sessionPoller_.start();
  }

  /** 
   * This method stops polling for the Session object.
   */
  stop() {
    this.sessionPoller_.stop();
  }

  /**
   * Returns a promise containing the Session object, given how updated the 
   * cache is in refreshing.
   * @return {Promise} The Promise object
   */
  async getSession() {
    const /** ?Object */ session =
        await this.sessionPoller_.getLastResult();
    return new Promise((resolve, reject) => {
      if(session) {
        resolve(Session.fromObject(session));
      } else {
        reject(new Error('No contact with server.'));
      }
    });
  }
}

export { SessionCache };
