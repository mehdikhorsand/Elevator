package elevatorTraceable;

/*
 * This class receives requests for service and dispatches them
 * to the elevator associated with the control object
 */
public class ElevatorControl {

    private Elevator myElevator;
    private ElevatorInterface ei;

    /*
     * Constructor
     */
    public ElevatorControl(Elevator elevator) {
        TCRunner.method_called();
        myElevator = elevator;
        ei = ElevatorInterface.getFromList(this);
    }

    /*
     * Constructor
     */
    public ElevatorControl(int EID) {
        TCRunner.method_called();
        myElevator = Elevator.selectElevator(EID);
        ei = ElevatorInterface.getFromList(this);
    }

    /*
     * Requests to stop the elevator upon arrival at a floor for which a stop is requested
     */
    public void stopElevator() {
        TCRunner.method_called();
        myElevator.stopElevator();
    }

    /*
     * Simulates opening elevator's door
     */
    public void openDoor() {
        TCRunner.method_called();
        myElevator.openDoor();
    }

    /*
     * Simulates closing elevator's door
     */
    public void closeDoor() {
        TCRunner.method_called();
        myElevator.closeDoor();
    }

    /*
     * Requests a stop at a specified floor
     */
    public void requestStop(int floor) {
        TCRunner.method_called();
        myElevator.addStop(floor, true);
    }

    /*
     * @return the associated elevator
     */
    public Elevator getElevator() {
        TCRunner.method_called();
        return myElevator;
    }

    /*
     * Simulates motor moving down
     */
    public void motorMoveDown() {
        TCRunner.method_called();
        ei.motorMoveDown();
    }

    /*
     * Simulates motor moving up
     */
    public void motorMoveUp() {
        TCRunner.method_called();
        ei.motorMoveUp();
    }

    /*
     * Simulates stopping motor
     */
    public void motorStop() {
        TCRunner.method_called();
        ei.motorStop();
    }
}
