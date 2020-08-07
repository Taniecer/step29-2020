import { Attendee } from './attendee.js';

test('Check for correct value of screenName', () => {
    const expectedScreenName = "Jaz";
    const expectedId = "JTK614";
    const expectedTimePolled = "2 minutes ago";
    const attendee = new Attendee(expectedId, expectedScreenName, expectedTimePolled);
    const screenName = attendee.getScreenName();
    const id = attendee.getSessionId();
    const timePolled = attendee.getTimeLastPolled();
    expect(screenName).toBe(expectedScreenName); 
    expect(id).toBe(expectedId);
    expect(timePolled).toBe(expectedTimePolled);
}); 

test('Check for correct value of fromObject', () => { 
    var obj = {  
        sessionId: "JTK614", 
        screenName:"Jaz",
        timeLastPolled: "2 minutes ago"
    };
    const newAttendee = Attendee.fromObject(obj);
    expect(newAttendee.getSessionId()).toBe(obj.sessionId);
});