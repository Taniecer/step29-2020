import { Session } from './session.js';

test('Check for correct value of sessionId', () => {
    const sessionId = "LYW23902"; 
    const ipOfVM = "122.01.231.25"; 
    const listOfAttendees = ["Jasmine", "Taniece", "Chris"];
    const screenNameofController = "Jaz";
    const sess = new Session(sessionId, ipOfVM, listOfAttendees, screenNameofController);  
    const id = sess.getSessionId(); 
    const ip = sess.getIpOfVM();
    const list = sess.getListOfAttendees();
    const name = sess.getScreenNameOfController();
    expect(id).toBe(sessionId); 
    expect(ip).toBe(ipOfVM);
    expect(list).toBe(listOfAttendees);
    expect(name).toBe(screenNameOfController);
}); 

test.only('Check for correct value of fromObject', () => {
    const object = JSON.parse(
      '{"sessionId":"EEEEE7","screenNameOfController":{"value":"Jessica"}'+
      ',"ipOfVM":{"value":"12.34.56.78"},"listOfAttendees":[{"sessionId":'+
      '"EEEEE7","screenName":"Jessica","timeLastPolled":'+
      '"Aug 3, 2020, 9:38:40 PM"}]}'); 
    const newSession = Session.fromObject(object);
    expect(newSession.getSessionId()).toBe(object.sessionId);
});