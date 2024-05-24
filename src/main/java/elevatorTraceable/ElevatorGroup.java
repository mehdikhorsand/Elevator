package elevatorTraceable;

/**
 * This class represents the facade of the Elevator system.
 * It is used to create the system, start its threads and shutdown the system.
 */
public class ElevatorGroup {

    public static ElevatorGroup theGroup;
    public static int numFloors;
    public static int numElevators;
    public static Thread[] elevatorThread;
    public static boolean[] threadStarted;
    public static Elevator[] e;
    public static FloorInterface[] fli;
    public static Floor[] floor;
    public static ElevatorInterface[] ebi;
    public static ArrivalSensor[] sensor;


    /*
     * @return the singleton instance of ElevatorGroup. Only one system
     * runs at a time.
     */
    public static ElevatorGroup getGroup(int el, int fl) {
        TCRunner.method_called();
        if (theGroup == null) {
            TCRunner.condition_covered();
            theGroup = new ElevatorGroup(el, fl);
            return theGroup;
        } else {
            TCRunner.condition_covered();
            return theGroup;
        }
    }

    /*
     * Constructor
     *
     * @param number of elevators, number of floors in the system
     */
    private ElevatorGroup(int el, int fl) {
        TCRunner.method_called();
        numFloors = fl;
        numElevators = el;
        elevatorThread = new Thread[numElevators];
        threadStarted = new boolean[numElevators];
        e = new Elevator[numElevators];
        floor = new Floor[numFloors];
        sensor = new ArrivalSensor[numFloors];
        ebi = new ElevatorInterface[numElevators];
        fli = new FloorInterface[numFloors];
    }

    /*
     * This method starts one elevator's thread
     */
    public void startThread(int threadNum) {
        TCRunner.method_called();
        TCRunner.print_output("Trying to start thread " + threadNum);
        if (threadStarted[threadNum] == false) {
            TCRunner.condition_covered();
            TCRunner.print_output("Running Thread #"+threadNum);
            elevatorThread[threadNum].start();
            threadStarted[threadNum] = true;
        }
    }

    /*
     * This method shutdown the system by killing its threads
     */
    public void stopGroup() {
        TCRunner.method_called();
        TCRunner.print_output("Shutting down the system.");
        for (int i = 0; i < numElevators; i++) {
            TCRunner.start_loop(2);
            if (threadStarted[i] == true) {
                TCRunner.condition_covered();
                threadStarted[i] = false;
                e[i].turnOff();
            }
        }
        TCRunner.end_loop(2);
        Floor.removeFloors();
        Elevator.removeAllElevators();
        theGroup = null;
    }

    /*
     * This method creates the objects in the system (elevators, floors, sensors ...) and starts the elevators threads
     */
    public void startGroup() {
        TCRunner.method_called();
        for (int i = 0; i < numFloors; i++) {
            TCRunner.start_loop(3);
            floor[i] = new Floor(i);
            sensor[i] = floor[i].getSensor();
            fli[i] = new FloorInterface(sensor[i]);
        }
        TCRunner.end_loop(3);
        for (int i = 0; i < numElevators; i++) {
            TCRunner.start_loop(4);
            e[i] = new Elevator();
            TCRunner.print_output("elevatorID = "+ e[i].getElevatorID());
            elevatorThread[i] = new Thread(e[i], "Elevator Thread " + i);
            ebi[i] = new ElevatorInterface(e[i]);
            theGroup.startThread(i);
        }
        TCRunner.end_loop(4);
    }

    /*
     * Simulates motor moving by sleeping the system for 2 seconds
     */
    public void motorMoving(int elevatorID, int direction, int currentFloor) {
        TCRunner.method_called();
        try {
            ElevatorGroup.elevatorDisplay(elevatorID + 1, "Moving from floor " + (currentFloor) + " in direction " + direction); //removed +1 from currentfloor
            ElevatorGroup.elevatorDisplay(elevatorID + 1, "Motor moving, sleeping to cause delay....");
            Thread.sleep(10);
        } catch (Exception e) {
            TCRunner.print_output("Exception in motorMoving.");
        }
        fli[currentFloor].stopAtThisFloor(elevatorID, currentFloor + direction);
    }

    /*
     * Used to show messages about status of elevators
     */
    public static void elevatorDisplay(int eid, String message) {
        TCRunner.method_called();
        TCRunner.print_output("elevator " + eid + ": " + message);
    }

    /*
     * @return floor interface associated with a specific floor
     */
    public FloorInterface getFloorInterface(int floorID) {
        TCRunner.method_called();
        if (floorID >= 0 && floorID < ElevatorGroup.numFloors) {
            TCRunner.condition_covered();
            return fli[floorID];
        }
        else {
            TCRunner.condition_covered();
            TCRunner.print_output("No Such floor.");
            return null;
        }
    }

    /*
     * @return elevator interface associated with a specific elevator
     */
    public ElevatorInterface getElevatorInterface(int elevatorID) {
        TCRunner.method_called();
        if (elevatorID >= 0 && elevatorID < ElevatorGroup.numElevators) {
            TCRunner.condition_covered();
            return ebi[elevatorID];
        }
        else {
            TCRunner.condition_covered();
            TCRunner.print_output("No Such elevator.");
            return null;
        }
    }
}//end class