/** Attendee Class */
class Attendee {
    constructor(sessionId, screenName, timeLastPolled) {
        /** @private {string} */
        this.sessionId_ = sessionId;
        /** @private {string} */
        this.screenName_ = screenName;
        /** @private {string} */
        this.timeLastPolled_ = timeLastPolled;  
        
    }
    getSessionId() {
        return this.sessionId_;
    }
    getScreenName() {
        return this.screenName_;
    }
    getTimeLastPolled() {
        return this.timeLastPolled_;
    }
    /**
    * @param {Attendee} obj An JSON object must be passed so that the function 
    can utilize the setters to set the variables of the object
    */ 
    static fromObject(obj) { 
        const attendee = new Attendee(obj.sessionId, obj.screenName, obj.timeLastPolled); 
        return attendee;
    }    
} 
export { Attendee };