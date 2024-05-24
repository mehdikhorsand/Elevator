package elevatorTraceable;

/**
 * This is the Floor Class. It consists of a list of the floors in the system
 * and each Floor object has its unique ID and an arrival sensor. Methods from
 * this class interact with the Elevator class in order to service requests from
 * users at a floor.
 */

public class Floor {

    private ArrivalSensor arrivalsensor;
    private Elevator elevator;

    /*
     * List of all the floors in the system
     */
    private static Floor[] allFloors = null;

    /*
     * Unique floor number, sequential integers 0 to (numFloors - 1)
     */
    private int floorID;
    /*
     * button set to true when a request for elevator going up has been
     * made
     */
    private boolean upButtonPressed = false;

    /*
     * button set to true when a request for elevator going down has been
     * made
     */
    private boolean downButtonPressed = false;

    /*
     * Constructor that initializes reference to class ArrivalSensor
     * and adds this floor object to the list of floors
     */
    public Floor(int floorID) {
        TCRunner.method_called();
        if (allFloors == null) {
            TCRunner.condition_covered();
            allFloors = new Floor[ElevatorGroup.numFloors];
        }
        arrivalsensor = new ArrivalSensor(this);
        this.floorID = floorID;
        Floor.allFloors[floorID] = this;
    }

    /*
     * @ return the reference to the desired
     * floor object from a list of floors in the system
     */
    public static Floor selectFloor(int floorID) {
        TCRunner.method_called();
        if ((floorID < ElevatorGroup.numFloors) && (floorID > -1)) {
            TCRunner.condition_covered();
            return allFloors[floorID];
        } else {
            TCRunner.condition_covered();
            return null;
        }
    }

    /*
     * @return number of floors in the system
     */
    public static int getNoFloors() {
        TCRunner.method_called();
        return ElevatorGroup.numFloors;
    }

    /*
     * Makes a request for an elevator to stop at this floor, heading up.
     * This method is called by FloorControl, to get an elevator to service
     * the request and add the stop to the elevator's list of stops
     *
     * @return the best elevator that can provide the service
     */
    public Elevator requestUp() {
        TCRunner.method_called();
        upButtonPressed = true;
        elevator = elevator.getBestElevator(floorID);
        if (floorID == ElevatorGroup.numFloors - 1) {
            TCRunner.condition_covered();
            TCRunner.print_output("No up requests are permitted at this floor.");
        } else if (elevator.getFloor().getFloorID() != this.getFloorID()) {
            TCRunner.condition_covered();
            TCRunner.print_output("Request for elevator going UP made at floor " + floorID);
            TCRunner.print_output("Best Elevator is: " + (elevator.getElevatorID() + 1));
            elevator.addStop(floorID, true);
            TCRunner.print_output("Stop added at " + floorID);
        } else {
            TCRunner.condition_covered();
            elevator.addStop(floorID, true);
        }
        return elevator;
    }

    /*
     * Makes a request for an elevator to stop at this floor, heading down.
     * This method is called by FloorControl, to get an elevator to service
     * the request and add the stop to the elevator's list of stops
     *
     * @return the best elevator that can provide the service
     */
    public Elevator requestDown() {
        TCRunner.method_called();
        downButtonPressed = true;
        elevator = elevator.getBestElevator(floorID);
        if (floorID == 0) {
            TCRunner.condition_covered();
            TCRunner.print_output("No down requests are permitted at this floor.");
        } else if (elevator.getFloor().getFloorID() != this.getFloorID()) {
            TCRunner.condition_covered();
            TCRunner.print_output("Request for elevator going DOWN made at floor " + floorID);
            TCRunner.print_output("Best Elevator is: " + (elevator.getElevatorID() + 1));
            elevator.addStop(floorID, true);
            TCRunner.print_output("Stop added at " + floorID);
        } else {
            TCRunner.condition_covered();
            elevator.addStop(floorID, true);
        }
        return elevator;
    }

    /*
     * @return true when a request for elevator going up has already been
     * made
     */
    public boolean requestUpMade() {
        TCRunner.method_called();
        if (upButtonPressed == true) {
            TCRunner.condition_covered();
            return true;
        } else {
            TCRunner.condition_covered();
            return false;
        }
    }

    /*
     * @return true when a request for elevator going down has already been
     * made
     */
    public boolean requestDownMade() {
        TCRunner.method_called();
        if (downButtonPressed == true) {
            TCRunner.condition_covered();
            return true;
        } else {
            TCRunner.condition_covered();
            return false;
        }
    }

    /*
     * Method called when a request for elevator going up has been serviced.
     * Resets the button for that floor.
     */
    public void requestUpServiced() {
        TCRunner.method_called();
        TCRunner.print_output("Elevator going UP has arrived at floor " + floorID + ".");
        upButtonPressed = false;
    }

    /*
     * Method called when a request for elevator going down has been serviced.
     * Resets the button for that floor.
     */
    public void requestDownServiced() {
        TCRunner.method_called();
        TCRunner.print_output("Elevator going DOWN has arrived at floor " + floorID + ".");
        downButtonPressed = false;
    }

    /*
     * @return the ID of the floor object
     */
    public int getFloorID() {
        TCRunner.method_called();
        return floorID;
    }

    /*
     * @return reference to the arrival sensor for the floor object
     */
    public ArrivalSensor getSensor() {
        TCRunner.method_called();
        return arrivalsensor;
    }

    public static void removeFloors() {
        TCRunner.method_called();
        allFloors = null;
    }
}
