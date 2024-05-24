import elevatorInstrumented.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ElevatorTest {

    @Test
    public void testElevatorGroup () throws InterruptedException {
        // RequestUp    _fromFloor#
        // RequestDown  _fromFloor#
        // RequestStop  _elevator#  _floor#
        // StartGroup    _elevator#  _floor#
        // StopGroup
        Recorder recorder = Recorder.getInstance();
        recorder.startRecording();
        ElevatorGroup eg = ElevatorGroup.getGroup(3, 5);
        eg.startGroup();
        FloorInterface fi7 = eg.getFloorInterface(7);
        FloorInterface fi_1 = eg.getFloorInterface(-1);
        FloorInterface fi3 = eg.getFloorInterface(3);
        Thread.sleep(4);
        fi3.requestDown(5);
        Thread.sleep(4);
        fi3.requestDown(4);
        Thread.sleep(4);
        FloorInterface fi4 = eg.getFloorInterface(4);
        Thread.sleep(4);
        fi4.requestUp(2);
        Thread.sleep(4);
        fi4.requestDown(5);
        Thread.sleep(4);
        ElevatorInterface e1 = eg.getElevatorInterface(2);
        Thread.sleep(4);
        e1.requestStop(4);
        Thread.sleep(4);
        e1.requestStop(5);
        Thread.sleep(4);
        e1.requestStop(1);
        Thread.sleep(1000);
        eg.stopGroup();
        recorder.printReport();
    }

    @Test
    public void testElevatorGroup1 () throws InterruptedException {
        ElevatorGroup eg = ElevatorGroup.getGroup(1, 2);
        eg.startGroup();
        eg.getFloorInterface(0).requestDown(0);
        Thread.sleep(1000);
        eg.stopGroup();
    }

    @Test
    public void test10 () throws InterruptedException {
        ElevatorGroup eg = ElevatorGroup.getGroup(1, 15);
        eg.startGroup();
        Thread.sleep(4);
        eg.getFloorInterface(3).requestUp(3);
        Thread.sleep(4);
        eg.getFloorInterface(1).requestDown(1);
        Thread.sleep(4);
        eg.getFloorInterface(2).requestUp(2);
        Thread.sleep(4);
        eg.getFloorInterface(3).requestUp(3);
        Thread.sleep(4);
        eg.getElevatorInterface(0).requestStop(0);
        Thread.sleep(4);
        eg.getFloorInterface(11).requestUp(11);
        Thread.sleep(4);
        eg.getElevatorInterface(0).requestStop(0);
        Thread.sleep(4);
        eg.getFloorInterface(9).requestDown(9);
        Thread.sleep(4);
        eg.getFloorInterface(5).requestUp(5);
        Thread.sleep(4);
        eg.getElevatorInterface(0).requestStop(0);
        Thread.sleep(4);
        Thread.sleep(1000);
        eg.stopGroup();
    }

    @Test
    public void test5 () throws InterruptedException {
        ElevatorGroup eg = ElevatorGroup.getGroup(1, 7);
        eg.startGroup();
        Thread.sleep(4);
        eg.getFloorInterface(5).requestUp(5);
        Thread.sleep(4);
        eg.getElevatorInterface(0).requestStop(0);
        Thread.sleep(4);
        eg.getElevatorInterface(0).requestStop(0);
        Thread.sleep(4);
        eg.getElevatorInterface(0).requestStop(0);
        Thread.sleep(4);
        eg.getElevatorInterface(0).requestStop(0);
        Thread.sleep(4);
        Thread.sleep(1000);
        eg.stopGroup();
    }

    @Test
//    public void testTCRunner() {
//        new TCRunner("reported_result/report-0/RT/testcases/tc0.txt",
//                "reported_result/report-0/RT/output/tc0.txt");
//    }

    @Before
    public void setup() {
        System.out.println("---------------------------------------------");
    }

    @After
    public void tearDown() {
        System.out.println("---------------------------------------------");
    }
}
