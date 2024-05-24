package tools;

import elevator.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class SourceDetector {
    public static ArrayList<String> get_src_methods_name() {
        ArrayList<String> output = new ArrayList<>();
        for(Class src_class : get_src_classes()) {
            Method[] methods = src_class.getDeclaredMethods();
            for(Method method : methods) {
                output.add(src_class.getName().replace(src_class.getPackageName() + ".", "") + "." + method.getName());
            }
        }
        return output;
    }

    public static ArrayList<Class> get_src_classes() {
        ArrayList<Class> src_classes = new ArrayList<>();
        src_classes.add(ArrivalSensor.class);
        src_classes.add(Elevator.class);
        src_classes.add(ElevatorControl.class);
        src_classes.add(ElevatorGroup.class);
        src_classes.add(ElevatorInterface.class);
        src_classes.add(Floor.class);
        src_classes.add(FloorControl.class);
        src_classes.add(FloorInterface.class);
        return src_classes;
    }
}
