import { Attendee } from './attendee.js';

/** Attendee Class */
class Session { 
    constructor(sessionId, ipOfVM, listOfAttendees, screenNameOfController) { 
        /** @private {string} */
        this.sessionId_ = sessionId ;

        /** @private {string} */
        this.ipOfVM_ = ipOfVM;

        /** @private {Array} */
        this.listOfAttendees_ = listOfAttendees;

        /** @private  {string} */
        this.screenNameOfController_ = screenNameOfController ; 
    
    } 

    getSessionId() {
        return this.sessionId_;
    } 

    getIpOfVM() {
        return this.ipOfVM_;
    } 

    getListOfAttendees() { 
       return this.listOfAttendees_;    
    }
    
    getScreenNameOfController() {
       return this.screenNameOfController_; 
    }
    /**
    * @param {Session} obj An Json Object must be passed so that the function 
     can utilize the setters to set the variables of the object into class Session
    */    
    static fromObject(obj) {   
        const session = new Session();
        session.sessionId_ = obj.sessionId;
        session.ipOfVM_ = obj.ipOfVM.value;
        session.listOfAttendees_ = [];
        obj.listOfAttendees.forEach(attendee => {
          session.listOfAttendees_.push(Attendee.fromObject(attendee));
        });
        session.screenNameOfController_ = obj.screenNameOfController.value;
        return session;
    }
    
}
export { Session };