package elevatorTraceable;

/*
 * This class controls floors and their sensors. Whenever a request
 * has been made on a floor, the request is passed by the control
 * instance to the corresponding floor.
 *
 */
public class FloorControl {

    private Floor floor;
    private ArrivalSensor sensor;
    private Elevator elevator;

    public FloorControl(ArrivalSensor sensor) {
        TCRunner.method_called();
        this.sensor = sensor;
    }

    /*
     * Makes a request for an elevator to stop at this floor,
     * heading up. Executes RequestElevator Use Case for "up" direction.
     *
     * @ return the best elevator that can provide the service
     */
    public Elevator requestUp(int floorID) {
        TCRunner.method_called();
        floor = Floor.selectFloor(floorID);
        Elevator e = floor.requestUp();
        return e;
    }

    /*
     * Makes a request for an elevator to stop at this floor,
     * heading up. Executes RequestElevator Use Case for "Down" direction.
     *
     * @ return the best elevator that can provide the service
     */
    public Elevator requestDown(int floorID) {
        TCRunner.method_called();
        floor = Floor.selectFloor(floorID);
        Elevator e = floor.requestDown();
        return e;
    }

    /*
     * When this method is called, the sensor signals an approaching
     * elevator to stop, if a stop has been requested on that floor.
     *
     */
    public void stopAtThisFloor(int elevatorID, int floorID) {
        TCRunner.method_called();
        elevator = elevator.selectElevator(elevatorID);
        floor = Floor.selectFloor(floorID);
        sensor = floor.getSensor();
        boolean stop = sensor.stopAtThisFloor(elevatorID);

        if (stop == true) {
            TCRunner.condition_covered();
            if (floor.getFloorID() == 0) {
                TCRunner.condition_covered();
                floor.requestUpServiced();
            } else if (floor.getFloorID() == Floor.getNoFloors() - 1) {
                TCRunner.condition_covered();
                floor.requestDownServiced();
            } else {
                TCRunner.condition_covered();
                if (elevator.getDirection() == -1) {
                    TCRunner.condition_covered();
                    if (floor.requestDownMade() == true) {
                        TCRunner.condition_covered();
                        floor.requestDownServiced();
                    }
                } else if (elevator.getDirection() == 1) {
                    TCRunner.condition_covered();
                    if (floor.requestUpMade() == true) {
                        TCRunner.condition_covered();
                        floor.requestUpServiced();
                    }
                }
            }
        }
    }
}
