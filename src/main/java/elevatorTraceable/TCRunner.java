package elevatorTraceable;

import main.Settings;
import randomTestcase.TestCase;
import tools.ConsoleColors;
import tools.TCWriter;
import tools.Terminal;
import tools.activity.Action;
import tools.activity.Activity;
import tools.activity.Path;
import tools.customList.MultiList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class TCRunner {
    static String destination_file_path = null;
    ElevatorGroup elevatorGroup;
    static FileWriter writer;
    public static MultiList<String> method_invocation_sequence;
    public static MultiList<Activity> method_invocation_edge;
    public static MultiList<Activity> method_and_condition_coverage;
    public static MultiList<ArrayList<Action>> method_invocation_paths;
    public static MultiList<ArrayList<Action>> value_quantification_paths;
    public static MultiList<ArrayList<Action>> method_and_condition_coverage_paths;

    public TCRunner(String input_file_path, String output_file_path) {
        method_invocation_sequence = new MultiList<>();
        method_invocation_edge = new MultiList<>();
        method_and_condition_coverage = new MultiList<>();
        method_invocation_paths = new MultiList<>();
        value_quantification_paths = new MultiList<>();
        method_and_condition_coverage_paths = new MultiList<>();
        TCRunner.destination_file_path = output_file_path;
        try {
            Scanner scanner = new Scanner(new File(input_file_path));
            writer = new FileWriter(output_file_path);
            while (scanner.hasNextLine()) {
                execute_line(scanner.nextLine());
            }
            scanner.close();
            writer.close();
            // reset_elevator();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void reset_elevator() {
//        Order.reset_order_counter();
//        Broker.list = new ArrayList<>();
//        Shareholder.list = new ArrayList<>();
    }

    public void execute_line(String line) throws InterruptedException {
        String[] rq_args = Arrays.stream(line.replace("\t", " ")
                .split(" ")).filter(s -> !s.isEmpty()).toArray(String[]::new);
        String request = rq_args[0];
        String[] args = Arrays.copyOfRange(rq_args, 1, rq_args.length);
        switch(request) {
            case "StartGroup":
                elevatorGroup = ElevatorGroup.getGroup(Integer.parseInt(args[1]), Integer.parseInt(args[0]));
                elevatorGroup.startGroup();
                Thread.sleep(10);
                break;
            case "RequestUp":
                elevatorGroup.getFloorInterface(Integer.parseInt(args[0])).requestUp(Integer.parseInt(args[0]));
                Thread.sleep(10);
                break;
            case "RequestDown":
                elevatorGroup.getFloorInterface(Integer.parseInt(args[0])).requestDown(Integer.parseInt(args[0]));
                Thread.sleep(10);
                break;
            case "RequestStop":
                elevatorGroup.getElevatorInterface(Integer.parseInt(args[1])).requestStop(Integer.parseInt(args[0]));
                Thread.sleep(10);
                break;
            case "StopGroup":
                Thread.sleep(100);
                int counter = 0;
                for(Elevator elevator : ElevatorGroup.e) {
                    while(elevator.nStops > 0 || elevator.state != Elevator.IDLE) {
                        if(counter == 200) {
                            System.out.println(ConsoleColors.PURPLE + "running time limit reached." + ConsoleColors.RESET);
                            elevator.nStops = 0;
                            elevator.state = Elevator.IDLE;
                            break;
                        }
                        Thread.sleep(5);
                        counter ++;
                    }
                }
                elevatorGroup.stopGroup();
//                for(Thread th : ElevatorGroup.elevatorThread) {
//                    if(th.isAlive())
//                        th.stop();
//                }
                break;
        }
    }

    public static void print_output(String output) {
        try{
            if(writer != null)
                writer.write(output + "\n");
            else
                System.out.println(ConsoleColors.CYAN + output + ConsoleColors.RESET);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void method_called() {
        Throwable stack = new Throwable();
        String class_name = stack.getStackTrace()[1].getClassName();
        String method_name = stack.getStackTrace()[1].getMethodName();
        method_invocation_sequence.add(class_name.split("\\.")[1] + "." + method_name);
        Activity activity = Activity.method_called(stack);
        method_invocation_edge.add(activity);
        method_and_condition_coverage.add(activity);
        ArrayList<Action> path = Path.get_method_invocation_path(stack);
        method_invocation_paths.add(path);
        method_and_condition_coverage_paths.add(path);
    }

    public static void method_finished() {
        Throwable stack = new Throwable();
        Activity activity = Activity.method_finished(stack);
        method_invocation_edge.add(activity);
        method_and_condition_coverage.add(activity);
    }

    public static void start_loop(int loop_index) {
        Throwable stack = new Throwable();
        Activity activity = Activity.start_loop(stack, loop_index);
        method_invocation_edge.add(activity);
        method_and_condition_coverage.add(activity);
        condition_covered2(stack);
    }

    public static void end_loop(int loop_index) {
        Throwable stack = new Throwable();
        Activity activity = Activity.end_loop(stack, loop_index);
        method_invocation_edge.add(activity);
        method_and_condition_coverage.add(activity);
        condition_covered2(stack);
    }

    public static void condition_covered() {
        Throwable stack = new Throwable();
        method_and_condition_coverage.add(Activity.condition_covered(stack));
        condition_covered2(stack);
    }

    public static void condition_uncovered() {
        Throwable stack = new Throwable();
        condition_covered2(stack);
    }

    public static void condition_covered2(Throwable stack) {
        method_and_condition_coverage_paths.add(Path.get_condition_coverage_path(stack));
    }

    public static void run(TestCase testcase) {
        System.out.println(ConsoleColors.CYAN + "Running TestCase: " + testcase + ConsoleColors.RESET);
        String src_path = Settings.temp + Settings.test_file_name + Settings.testcase_format;
        String des_path = Settings.temp + Settings.output + Settings.testcase_format;
        TCWriter.write_into_txt_format(src_path, testcase);
        new TCRunner(src_path, des_path);
        testcase.result = Terminal.cat(des_path);
        Terminal.rm(src_path);
        Terminal.rm(des_path);
    }
}
