 /** 
  * The Poller class repeatedly execeutes a function passed in. 
  * By default, polling objects poll indefinitely over a
  * period of 30 seconds.
  * 
  * For example, if an API request is the pollingFunction that is 
  * passed in, and is one that contacts a servlet to doGet some value, a 
  * Poller object will repeatedly doGet the value, attaching it to one of
  * its fields which can then be retrieved with getResult(). 
  * The core nature is that it executes a function over and over.
  */ 
 class Poller {
  /**
   * Initializes a Poller object.
   * @param {function(): any} pollingFunction Represents the function 
   *    that is polled.
   * @param {number=} [pollingPeriodMs = 30000] Represents the 
   *    cadence at which the pollingFunction is called upon. 
   *    Must be provided in milliseconds and is by default
   *    30,000 milliseconds (or 30 seconds). 
   */
  constructor(pollingFunction, pollingPeriodMs = 30000) {
    /** 
     * Represents the output of the pollingFunction, is constantly
     * being updated.
     * @private {?Object} 
     */
    this.result_ = null;

    /**
     * @private {number}
     */
    this.pollingPeriodMs_ = pollingPeriodMs;

    /**
     * Represents the handler returned by the setTimeout that continues
     * to poll.
     * @private {?number}
     */
    this.setTimeoutId_ = 0;
    
    /**
     * @private {function(): any}
     */
    this.pollingFunction_ = pollingFunction;
  }

  /**
   * This method periodically executes (time dictated by pollingPeriodMs)
   * the pollingFunction, updating the result.
   * @private
   */ 
  poll_() {
    this.result_ = this.pollingFunction_();
    this.setTimeoutId_ = setTimeout(() => {
      this.poll_();
    }, this.pollingPeriodMs_);
  }

  /** 
   * This method begins polling. 
   */
  start() {
    this.poll_();
  }

  /** 
   * This method stops polling, calls clearTimeout on the current poll
   * function instance.
   */
  stop() {
    clearTimeout(this.setTimeoutId_);
  }

  /**
   * Returns the current result of the pollingFunction given how  
   * updated the poll is.
   * @return {?Object} The result.
   */
  getLastResult() {
    return this.result_;
  }
}

export { Poller };
