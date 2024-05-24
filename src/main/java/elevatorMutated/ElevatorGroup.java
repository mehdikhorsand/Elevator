package elevatorMutated;

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
        if (theGroup == null) {
            theGroup = new ElevatorGroup(el, fl);
            return theGroup;
        } else
            return theGroup;
    }

    /*
     * Constructor
     *
     * @param number of elevators, number of floors in the system
     */
    private ElevatorGroup(int el, int fl) {
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
        TCRunner.print_output("Trying to start thread " + threadNum);
        if (threadStarted[threadNum] == false) {
            TCRunner.print_output("Running Thread #"+threadNum);
            elevatorThread[threadNum].start();
            threadStarted[threadNum] = true;
        }
    }

    /*
     * This method shutdown the system by killing its threads
     */
    public void stopGroup() {
        TCRunner.print_output("Shutting down the system.");
        for (int i = 0; i < numElevators; i++) {
            if (threadStarted[i] == true) {
                threadStarted[i] = false;
                e[i].turnOff();
            }
        }
        Floor.removeFloors();
        Elevator.removeAllElevators();
        theGroup = null;
    }

    /*
     * This method creates the objects in the system (elevators, floors, sensors ...) and starts the elevators threads
     */
    public void startGroup() {
        for (int i = 0; i < numFloors; i++) {
            floor[i] = new Floor(i);
            sensor[i] = floor[i].getSensor();
            fli[i] = new FloorInterface(sensor[i]);
        }
        for (int i = 0; i < numElevators; i++) {
            e[i] = new Elevator();
            TCRunner.print_output("elevatorID = "+ e[i].getElevatorID());
            elevatorThread[i] = new Thread(e[i], "Elevator Thread " + i);
            ebi[i] = new ElevatorInterface(e[i]);
            theGroup.startThread(i);
        }
    }

    /*
     * Simulates motor moving by sleeping the system for 2 seconds
     */
    public void motorMoving(int elevatorID, int direction, int currentFloor) {
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
        TCRunner.print_output("elevator " + eid + ": " + message);
    }

    /*
     * @return floor interface associated with a specific floor
     */
    public FloorInterface getFloorInterface(int floorID) {
        if (floorID >= 0 && floorID < ElevatorGroup.numFloors)
            return fli[floorID];
        else {
            TCRunner.print_output("No Such floor.");
            return null;
        }
    }

    /*
     * @return elevator interface associated with a specific elevator
     */
    public ElevatorInterface getElevatorInterface(int elevatorID) {
        if (elevatorID >= 0 && elevatorID < ElevatorGroup.numElevators)
            return ebi[elevatorID];
        else {
            TCRunner.print_output("No Such elevator.");
            return null;
        }
    }
}//end class