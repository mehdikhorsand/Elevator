package elevatorTraceable;

import java.util.Enumeration;
import java.util.Vector;

/**
 * This class represents one elevator in the system. An elevator is a thread
 * that receives requests from its button panel and from floors.
 */


public class Elevator implements Runnable {

    private static Vector allElevators = new Vector();
    private int elevatorID;
    private boolean running;

    // direction in which the elevator is heading (1 for Up and -1 for Down; 0 when elevator is idle)
    public int direction;

    // An elevator can be in one of 4 states (IDLE, PREPARE, MOVING and FINDNEXT)
    public int state;

    // The floor on which the elevator is currently
    private Floor currentFloor;

    // The stops that an elevator has to serve
    private boolean[] stops;
    public int nStops;
    private boolean motorMoving;
    private boolean doorOpen;

    public final static int IDLE = 0;
    public final static int PREPARE = 1;
    public final static int MOVING = 2;
    public final static int FINDNEXT = 3;


    //  Constructor
    public Elevator() {
        TCRunner.method_called();
        stops = new boolean[Floor.getNoFloors()];
        int newIndex = allElevators.size();
        this.elevatorID = newIndex;
        TCRunner.print_output("New Elevator, ID #" + elevatorID);
        allElevators.add(this);
        this.direction = 0; /* 0 when idle, 1 for up direction and -1 for down direction */
        currentFloor = Floor.selectFloor(0); //New Elevators start at the bottom.
        state = Elevator.IDLE;
        nStops = 0;
        doorOpen = true;
        motorMoving = false;
        running = true;

        //Define the stops, based on the Floors.
        for (int i = 0; i < Floor.getNoFloors(); i++) {
            TCRunner.start_loop(0);
            Floor floor = Floor.selectFloor(i);
            stops[floor.getFloorID()] = false;
        }
        TCRunner.end_loop(0);
    }

    /**
     * @return an elevator corresponding to a unique ID
     */
    public static Elevator selectElevator(int elevatorID) {
        TCRunner.method_called();
        return (Elevator) allElevators.elementAt(elevatorID);
    }

    /**
     * Called by a Floor wanting an elevator to service a request,
     * this method returns the best elevator for the job - an idle
     * Elevator is preferred, then a moving elevator headed towards
     * this floor and a moving elevator headed away if nothing else
     * is available.
     */
    public static Elevator getBestElevator(int floorID) {
        TCRunner.method_called();
        //A simple bubble sort
        Elevator bestElevator = null;
        for (Enumeration e = allElevators.elements(); e.hasMoreElements(); ) {  //Loop through all elevators
            TCRunner.start_loop(1);
            Elevator testElevator = (Elevator) e.nextElement();
            if (bestElevator == null) {
                TCRunner.condition_covered();
                bestElevator = testElevator;
            }
            else if (bestElevator.getState() != Elevator.IDLE && testElevator.getState() == Elevator.IDLE) {
                TCRunner.condition_covered();
                bestElevator = testElevator; //Idle is better than anything else
            }
            else if (bestElevator.getState() == Elevator.IDLE && testElevator.getState() == Elevator.IDLE) {//both Idle;
                TCRunner.condition_covered();
                int bestDistance = Math.abs(bestElevator.getFloor().getFloorID() - floorID);
                int testDistance = Math.abs(testElevator.getFloor().getFloorID() - floorID);
                if (testDistance < bestDistance) {
                    TCRunner.condition_covered();
                    bestElevator = testElevator;  //If test is closer, test is now best.
                }
            } else if (bestElevator.getState() != Elevator.IDLE && testElevator.getState() != Elevator.IDLE) { //if both are active
                TCRunner.condition_covered();
                if ((testElevator.getFloor().getFloorID() - floorID) * testElevator.direction <= 0) { //if test is heading in the right direction or is here
                    TCRunner.condition_covered();
                    if ((bestElevator.getFloor().getFloorID() - floorID) * bestElevator.direction > 0) {//Best is heading away
                        TCRunner.condition_covered();
                        bestElevator = testElevator;
                    } else { // Both heading towards this floor
                        TCRunner.condition_covered();
                        int bestDistance = Math.abs(bestElevator.getFloor().getFloorID() - floorID);
                        int testDistance = Math.abs(testElevator.getFloor().getFloorID() - floorID);
                        if (testDistance == bestDistance)
                            TCRunner.print_output("Mutant has been discovered!");
                        if (testDistance < bestDistance) {
                            TCRunner.condition_covered();
                            bestElevator = testElevator;  //If test is closer, test is now best.
                        }
                    }
                } // End if test is heading this way
            } // end if both are active
        }  //End enumeration loop.
        TCRunner.end_loop(1);
        return bestElevator;
    }

    /**
     * Called by ArrivalSensor to tell the elevator that it's reached a new floor.
     *
     * @return true if the elevator has to stop, and false otherwise
     */
    public boolean notifyNewFloor(Floor newFloor) {
        TCRunner.method_called();
        currentFloor = newFloor;
        ElevatorGroup.elevatorDisplay(elevatorID + 1, "Reached floor " + (newFloor.getFloorID())); //removed +1 from floor
        if (stops[newFloor.getFloorID()] == true) {
            TCRunner.condition_covered();
            stopElevator();
            return true;
        } else if ((newFloor.requestUpMade()) && (direction == 1)) {
            TCRunner.condition_covered();
            stopElevator();
            return true;
        } else if ((newFloor.requestDownMade()) && (direction == -1)) {
            TCRunner.condition_covered();
            stopElevator();
            return true;
        } else {
            TCRunner.condition_covered();
            if (direction == 1) {
                TCRunner.condition_covered();
                motorMoveUp();
            } else {
                TCRunner.condition_covered();
                motorMoveDown();
            }
            return false;
        } //end else
    }

    /**
     * Simulates elevator's motor movement down
     */
    private void motorMoveDown() {
        TCRunner.method_called();
        ElevatorControl ec = new ElevatorControl(this);
        motorMoving = true;
        ec.motorMoveDown();
        ec = null;
    }

    /**
     * Simulates elevator's motor movement up
     */
    private void motorMoveUp() {
        TCRunner.method_called();
        ElevatorControl ec = new ElevatorControl(this);
        motorMoving = true;
        ec.motorMoveUp();
        ec = null;
    }

    /**
     * If there is a floor with a request in the current direction, move one floor in the current direction.
     * Else reverse direction and check again.
     */
    public void moveElevator() {
        TCRunner.method_called();
        /* Three-phase movement cycle.
         * Prepare/move/check.
         * Does not loop here, since it returns control to run()
         * which calls this again if it hasn't set itself to Idle.
         */

        //  PREPARE:
        if (this.state == PREPARE) {
            TCRunner.condition_covered();
            ElevatorGroup.elevatorDisplay(elevatorID + 1, "Door closed.");
            closeDoor();
            this.state = MOVING;
            if (direction == 1) {
                TCRunner.condition_covered();
                motorMoveUp();
            } else {
                TCRunner.condition_covered();
                motorMoveDown();
            }
        }

        // MOVING:
        while (this.state == MOVING) {
            TCRunner.start_loop(6);
            //Do nothing - we're waiting to be interrupted by ArrivalSensor telling us we've reached a new floor.
            try {
                Thread.sleep(3);
            } catch (Exception e) {
            }
        }
        TCRunner.end_loop(6);
        getNextDestination();
    }

    /**
     * @return floor id to service or -1 if no more requests
     */
    public int getNextDestination() {
        TCRunner.method_called();
        while (this.state == FINDNEXT) {
            TCRunner.start_loop(7);
            if (nStops == 0) {
                TCRunner.condition_covered();
                this.state = Elevator.IDLE;
                direction = 0;
                ElevatorGroup.elevatorDisplay(elevatorID + 1, "All stops handled.  Idling.");
                return -1;
            } else {
                TCRunner.condition_covered();
                int stopToCheck = currentFloor.getFloorID() + direction;
                while (Floor.selectFloor(stopToCheck) != null && state == FINDNEXT) {
                    TCRunner.start_loop(8);
                    if (stops[stopToCheck] == true) {
                        TCRunner.condition_covered();
                        this.state = PREPARE;
                        ElevatorGroup.elevatorDisplay(elevatorID + 1, "Next Stop = floor " + (stopToCheck));
                        return stopToCheck;
                    } else {
                        TCRunner.condition_covered();
                        stopToCheck += direction;
                    }
                }
                TCRunner.end_loop(8);
                if (Floor.selectFloor(stopToCheck) == null && state == FINDNEXT) { //If we ran out of floors before finding a stop
                    TCRunner.condition_covered();
                    direction = -direction; //reverse direction
                }
            }
        }
        TCRunner.end_loop(7);
        return -1;
    }

    /**
     * When an elevator reaches a stop, it stops its motor, open the door and removes the served stop from the list of stops
     */
    public void stopElevator() {
        TCRunner.method_called();
        state = FINDNEXT;
        addStop(currentFloor.getFloorID(), false); //to remove stop from list of stops
        motorStop();
        ElevatorGroup.elevatorDisplay(elevatorID + 1, "Stopped.  ");
        ElevatorGroup.elevatorDisplay(elevatorID + 1, "Door open.");
        openDoor();
    }

    /**
     * Simulates motor stopping
     */
    private void motorStop() {
        TCRunner.method_called();
        ElevatorControl ec = new ElevatorControl(this);
        motorMoving = false;
        ec.motorStop();
        ec = null;
    }

    /**
     * Simulates elevator's door opening
     */
    public void openDoor() {
        TCRunner.method_called();
        ElevatorGroup.elevatorDisplay(elevatorID + 1, "Door is open on floor " + getFloor().getFloorID() + ".");
        try {
            doorOpen = true;
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Simulates elevator's door closing
     */
    public void closeDoor() {
        TCRunner.method_called();
        ElevatorGroup.elevatorDisplay(elevatorID + 1, "Door is closed on floor " + getFloor().getFloorID() + ".");
        try {
            doorOpen = false;
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }
    }

    /**
     * A stop is added to the list of elevator stops whenever a stop is requested
     * from the elevator or a request from floor has been directed to the elevator
     * after selecting it as the best elevator to do the job
     *
     * @param floorID: the floor where to stop or to remove a stop after reaching it
     *                 stopState: true if a stop is added, false if a stop at a floor is to be
     *                 removed after reaching the floor
     */
    public void addStop(int floorID, boolean stopState) {
        TCRunner.method_called();
        if (stopState) {
            TCRunner.condition_covered();
            if(!stops[floorID]) {
                TCRunner.condition_covered();
                if ((floorID != currentFloor.getFloorID()) || (this.getState() == Elevator.MOVING)) {
                    TCRunner.condition_covered();
                    nStops++;
                    stops[floorID] = true;
                    if (this.state == Elevator.IDLE) {
                        TCRunner.condition_covered();
                        if (this.currentFloor.getFloorID() < floorID) { // If the requested floor is above the current one
                            TCRunner.condition_covered();
                            direction = 1;
                            this.state = PREPARE;   // Start the motion cycle
                        } else {
                            TCRunner.condition_covered();
                            direction = -1;
                            this.state = PREPARE;
                        }
                    }
                } else {
                    TCRunner.condition_covered();
                    ElevatorGroup.elevatorDisplay(elevatorID + 1, "Elevator is at requested floor.");
                }
            }
        } else {
            TCRunner.condition_covered();
            if(stops[floorID]) {
                TCRunner.condition_covered();
                stops[floorID] = false;
                nStops--;
            }
        }
    }

    public void run() {
//        TCRunner.method_called();  // this cause Index Out Of Bound Exception in TCRunner.method_called
        //ElevatorGroup.elevatorDisplay(elevatorID + 1, "running.");
        while (running) {
            try {
                if (this.state == Elevator.IDLE && this.nStops == 0) {
                    Thread.sleep(1);
                    //If you're idle, sleep for a short time.
                } else {
                    moveElevator();
                }
            } catch (Exception e) { //If you're interrupted, wake up and do nothing.
            }
        }
    }

    /**
     * @return the direction in which the elevator is heading
     */
    public int getDirection() {
        TCRunner.method_called();
        return direction;
    }

    /**
     * @return the state of the elevator
     */
    public int getState() {
        TCRunner.method_called();
        return state;
    }

    /**
     * @return the current floor on which the elevator is
     */
    public Floor getFloor() {
        TCRunner.method_called();
        return currentFloor;
    }

    /**
     * @return the elevator's unique ID
     */
    public int getElevatorID() {
        TCRunner.method_called();
        return elevatorID;
    }

//    /**
//     * @return true if elevator's door is open and false otherwise
//     */
//    public boolean getDoorOpen() {
//        return doorOpen;
//    }
//
//    /**
//     * @return true if elevator's motor is moving and false otherwise
//     */
//    public boolean getMotorMoving() {
//        return motorMoving;
//    }
//
//    /**
//     * @param i floor number
//     * @return true if there is a stop requested for floor i
//     */
//    public boolean getStop(int i) {
//        return stops[i];
//    }
//
//    public int getNumberOfStops() {
//        return nStops;
//    }

    public static void removeAllElevators() {
        TCRunner.method_called();
        allElevators.removeAllElements();
    }

    public void turnOff() {
        TCRunner.method_called();
        running = false;
    }
}
