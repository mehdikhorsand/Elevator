package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Evaluation {
    public static String read_file(String file_address) {
        File file = new File(file_address);
        try{
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\\Z");
            return scanner.next();
        } catch (FileNotFoundException e) {
            return "";
        }
    }

    public static void evaluation(String output_location) throws Exception {
        String output = read_file(output_location);
        String error = "Mutant has been discovered!";
        if (output.contains(error)) {
            throw new Exception(ConsoleColors.RED_BOLD + ConsoleColors.BLACK_BACKGROUND + error + ConsoleColors.RESET);
        }
    }

    public static void evaluation(String output_location, String oracle_location) throws Exception {
        String output = read_file(output_location);
        String oracle = read_file(oracle_location);
        String error_msg = "";

        int elevatorNumber = count_substring(oracle, "New Elevator,");
        error_msg += evaluate(output, oracle, "Best Elevator is:");
        for(int e=1; e<=elevatorNumber; e++) {
            String template = "elevator " + e + ": ";
            error_msg += evaluate(output, oracle, template + "Stop requested at floor");
            error_msg += evaluate(output, oracle, template + "Stopped");
            error_msg += evaluate(output, oracle, template + "Door");
            error_msg += evaluate(output, oracle, template + "Moving");
            error_msg += evaluate(output, oracle, template + "Motor moving");
            error_msg += evaluate(output, oracle, template + "Motor stopped");
        }

        if (!error_msg.equals(""))
            throw new Exception("\nerror_messages:\n" + ConsoleColors.RED_BOLD + ConsoleColors.BLACK_BACKGROUND +
                    error_msg + ConsoleColors.RESET);
    }

    public static int count_substring(String string, String substring) {
        return find(string, substring).length;
    }

    public static String evaluate(String output_res, String oracle_res, String template) {
        String[] oracle = find(oracle_res, template);
        String[] output = find(output_res, template);
        int iteration = Math.max(oracle.length, output.length);
        for(int i=0; i<iteration; i++){
            if(i < output.length && i < oracle.length){
                if(!Objects.equals(oracle[i], output[i])){
                    return error_template(oracle[i], output[i]);
                }
            } else if (i < oracle.length) {
                return error_template(oracle[i], "Nothing");
            } else{
                return error_template("Nothing", output[i]);
            }
        }
        return "";
    }

    public static String error_template (String expected, String output) {
        return "\nError!" +
                "\nexpected:   " + expected +
                "\noutput:     " + output + "\n";
    }

    public static String[] find(String testcase_output, String template) {
        String[] split_output = testcase_output.split(template);
        for(int i=1; i<split_output.length; i++){
            split_output[i] = template + split_output[i].split("\n")[0];
        }
        return Arrays.copyOfRange(split_output, 1, split_output.length);
    }
}
