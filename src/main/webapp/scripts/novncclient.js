import RFB from 'https://cdn.jsdelivr.net/npm/@novnc/novnc@1.1.0/core/rfb.js';

/**
 * The NoVNCClient class essentially wraps the RFB object of the noVNC
 * library, takes care of remoting, what happens if someone connects, 
 * and what happens if someone disconnects. The class also allows for 
 * additional functionality that are specific to the scope of Virtual Movie
 * Night, like changing the viewOnly property of the RFB object (or all
 * owing who is able to controll).
 */
class NoVNCClient {
  /**
   * Initalizes a NoVNCClient object.
   * @param {function(): void} connectCallback This function is called 
   *    once the rfbConnection connects.
   * @param {function(): void} disconnectCallback This function is called
   *    once the rfbConnection disconnects.
   * @param {HTMLElement} rfbConnectionElement A block HTMLElement that 
   *    specifies where the RFB object should attach itself.
   */
  constructor(connectCallback, disconnectCallback, rfbConnectionElement) {
    /**
     * Represents the current noVNC RFB object of the session. The RFB
     * object is defined as the single connection to the VNC server of the
     * VM assigned to the session. The interface of the noVNC client 
     * consists of this single RFB object and all of the functionality of
     * the library resides in this object. 
     * @private {RFB}
     */
    this.rfbConnection_ = null; 

    /** 
     * @private {function(): void}
     */
    this.connectCallback_ = connectCallback;

    /** 
     * @private {function(): void}
     */
    this.disconnectCallback_ = disconnectCallback;

    /**
     * @private {HTMLElement}
     */
    this.rfbConnectionElement_ = rfbConnectionElement;
  }

  /**
   * Method remoteToSession() uses the noVNC library
   * in order to connect to a session.
   * @param {string} ipOfVM the ip adress of the VM assigned to the 
   *    session.
   * @param {string} sessionId represents the id of the current session.
   */
  remoteToSession(ipOfVM, sessionId) {
    const /** string */ url = `wss://${ipOfVM}:6080`;
    this.rfbConnection_ = new RFB(this.rfbConnectionElement_, url,
        { credentials: { password: sessionId } });
    this.rfbConnection_.addEventListener('connect', this.connectCallback_);
    this.rfbConnection_.addEventListener(
        'disconnect', this.disconnectCallback_);
    this.rfbConnection_.viewOnly = true;
  }

  /**
   * Method disconnect() disconnects from the server.
   */
  disconnect() {
    if(this.rfbConnection_) {
      this.rfbConnection_.disconnect();
    }
  }

  /**
   * Method setViewOnly() changes the viewOnly property of the current
   * rfbConnection to be what is passed in.
   * @param {boolean} viewOnly is a boolean indicating if any events
   *    (e.g. key presses or mouse movement) should be prevented from 
   *    being sent to the server.
   */
  setViewOnly(viewOnly) {
    if(this.rfbConnection_) {
      this.rfbConnection_.viewOnly = viewOnly;
    }
  }
}

export { NoVNCClient }

