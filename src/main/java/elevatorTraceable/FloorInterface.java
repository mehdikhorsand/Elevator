package elevatorTraceable;

/**
 * This class represents the Input Interface for interaction with the
 * floor requests. When the user requests an up or down going elevator
 * the messages are sent to this class which calls methods from Floor
 * Control to deal with the request.
 */

public class FloorInterface {

    FloorControl fc;
    ArrivalSensor sensor;

    /*
     * Constructor
     */
    public FloorInterface(ArrivalSensor sensor) {
        TCRunner.method_called();
        this.sensor = sensor; //a sensor that determines if an elevator needs to stop when reaching a floor
    }

    /*
     * Makes a request for an elevator to stop at this floor,
     * heading up. Triggers RequestElevator Use Case for "up" direction.
     *
     * @return the best elevator that can provide the service
     */
    public Elevator requestUp(int floorID) {
        TCRunner.method_called();
        Elevator e = null;
        if (floorID >= 0 && floorID < ElevatorGroup.numFloors) {
            TCRunner.condition_covered();
            fc = new FloorControl(sensor);
            e = fc.requestUp(floorID);
            fc = null;
        } else {
            TCRunner.condition_covered();
            TCRunner.print_output("No such floor " + floorID + ".");
        }
        return e;
    }

    /*
     * Makes a request for an elevator to stop at this floor,
     * heading up. Triggers RequestElevator Use Case for "Down" direction.
     *
     * @return the best elevator that can provide the service
     */
    public Elevator requestDown(int floorID) {
        TCRunner.method_called();
        Elevator e = null;
        if (floorID >= 0 && floorID < ElevatorGroup.numFloors) {
            TCRunner.condition_covered();
            fc = new FloorControl(sensor);
            e = fc.requestDown(floorID);
            fc = null;
        } else {
            TCRunner.condition_covered();
            TCRunner.print_output("No such floor " + floorID + ".");
        }
        return e;
    }

    /*
     * When this method is called, the sensor signals an approaching
     * elevator to stop, if a stop has been requested on that floor.
     *
     */
    public void stopAtThisFloor(int elevatorID, int floorID) {
        TCRunner.method_called();
        fc = new FloorControl(sensor);
        fc.stopAtThisFloor(elevatorID, floorID);
        fc = null;
    }

    /*
     * @return the floor associated with this interface
     */
    public Floor getFloor() {
        TCRunner.method_called();
        return sensor.getTheFloor();
    }
}

