package elevator;

import tools.ConsoleColors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class TCRunner {
    static String destination_file_path = null;
    ElevatorGroup elevatorGroup;
    static FileWriter writer;

    public TCRunner(String input_file_path, String output_file_path) {
        TCRunner.destination_file_path = output_file_path;
        try {
            Scanner scanner = new Scanner(new File(input_file_path));
            writer = new FileWriter(output_file_path);
            while (scanner.hasNextLine()) {
                execute_line(scanner.nextLine());
            }
            scanner.close();
            writer.close();
            reset_elevator();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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
//                Thread.sleep(500);
                for(Elevator elevator : ElevatorGroup.e) {
                    while(elevator.nStops > 0 || elevator.state != Elevator.IDLE)
                        Thread.sleep(5);
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
                System.out.println(output + ConsoleColors.CYAN);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
