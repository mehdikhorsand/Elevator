package randomTestcase;
import elevatorTraceable.TCRunner;
import randomRequest.*;
import tools.MyRandom;
import main.Settings;
import tools.activity.Action;
import tools.activity.Activity;
import tools.customList.MultiList;

import java.util.ArrayList;

public class TestCase {
    public int floor_quantity;
    public int elevator_quantity;
    public ArrayList<RequestInput> requests = new ArrayList<>();
    public MultiList<String> method_invocation_sequence;
    public MultiList<Activity> method_invocation_edge;
    public MultiList<Activity> method_and_condition_coverage;
    public MultiList<ArrayList<Action>> method_invocation_paths;
    public MultiList<ArrayList<Action>> value_quantification_paths;
    public MultiList<ArrayList<Action>> method_and_condition_coverage_paths;
    public String result;
    public double execution_time;

    public TestCase() {
//        floor_quantity = 2;
//        elevator_quantity = 1;
        floor_quantity = MyRandom.getInt(Settings.min_floor_qty, Settings.max_floor_qty);
        elevator_quantity = MyRandom.getInt(Settings.min_elevator_qty, Settings.max_elevator_qty);
        generate_random_requests();
//        set_execution_result();  // this function is not necessary for the elevator
    }

    private void generate_random_requests() {
        while (!finish_creating_new_request()) {
            if(MyRandom.getBoolean()) {
                requests.add(new RequestFromElevator(elevator_quantity, floor_quantity));
            }
            else {
                requests.add(new RequestFromFloor(floor_quantity));
            }
        }
    }

    private boolean finish_creating_new_request() {
        return requests.size() >= Settings.request_number;
    }

    public void run() {
        if (method_invocation_sequence == null) { // if it is not ran yet
            double start_run = System.currentTimeMillis();
            TCRunner.run(this);
            double end_run = System.currentTimeMillis();
            this.execution_time = end_run - start_run;
            this.method_invocation_sequence = TCRunner.method_invocation_sequence;
            this.method_invocation_edge = TCRunner.method_invocation_edge;
            this.method_and_condition_coverage = TCRunner.method_and_condition_coverage;
            this.method_invocation_paths = TCRunner.method_invocation_paths;
            this.value_quantification_paths = TCRunner.value_quantification_paths;
            this.method_and_condition_coverage_paths = TCRunner.method_and_condition_coverage_paths;
        }
    }

    // this function is not necessary for the elevator
//    public String run_this(){
//        String testcase_path = Settings.temp + Settings.test_file_name + Settings.testcase_format;
//        TCWriter.write_into_txt_format(testcase_path, this);
//        String tc_result = Terminal.run_oracle(testcase_path);
//        Terminal.rm(testcase_path);
//        return tc_result;
//    }

    // this function is not necessary for the elevator
//    public void set_execution_result(){
//        String tc_result = run_this();
//        set_TC_orders_result(tc_result);
//    }

    // this function is not necessary for the elevator
//    private void set_TC_orders_result(String tcResult) {
//        String[] orders_output = tcResult.split("OrderRs\t");
//        orders_output[0] = "";
//        for(int i=0; i<orders.size(); i++)
//            orders.get(i).set_order_result(orders_output[i], orders_output[i+1]);
//    }
}
