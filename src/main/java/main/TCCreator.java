package main;

import randomTestcase.CandidateSet;
import randomTestcase.TestCase;
import tools.*;

import java.util.ArrayList;

public class TCCreator {
    static ArrayList<SelectionMethod> removed_methods = new ArrayList<>();

    public static void create_testcase_files() {
        if(Settings.f_measure_evaluation){
            removed_methods = new ArrayList<>();
            int i = 0;
            while (removed_methods.size() != Settings.get_methods().size()) {
                write_and_check_test_files(i);
                i++;
            }
        }
        else {
            for (int i = 0; i<Settings.testcase_number; i++)
                write_test_files(i);
        }
    }

    private static void write_and_check_test_files(int i) {
        System.out.println(ConsoleColors.BLUE_BACKGROUND_BRIGHT + ConsoleColors.BLACK_BOLD + "testcase" + i + ": " +
                (Settings.get_methods().size() - removed_methods.size()) + " methods left." + ConsoleColors.RESET);
        double start_creating_candidates_time = System.currentTimeMillis();
        CandidateSet candidate_set = new CandidateSet();
        double end_creating_candidates_time = System.currentTimeMillis();
        double start_running_candidates = System.currentTimeMillis();
        for(TestCase c : candidate_set.C)
            c.run();
        double end_running_candidates = System.currentTimeMillis();
        for(SelectionMethod method : Settings.get_methods()){
            if(!method_is_removed(method)) {
                double start_testcase_selection_time = System.currentTimeMillis();
                select_testcase(candidate_set, method, i);
//                new elevatorMutated.TCRunner(Settings.get_testcase_path(method, i), Settings.get_output_path(method, i));
                // oracle
//                new elevator.TCRunner(Settings.get_testcase_path(method, i), Settings.get_oracle_path(method, i));
                try{
                    Evaluation.evaluation(Settings.get_output_path(method, i));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    removed_methods.add(method);
                }
                double end_testcase_selection_time = System.currentTimeMillis();
                method.add_execution_time(end_creating_candidates_time - start_creating_candidates_time);
                method.add_execution_time(end_testcase_selection_time - start_testcase_selection_time);
                if(method.is_running_all_candidates())
                    method.add_execution_time(end_running_candidates - start_running_candidates);
            }
        }
    }

    private static boolean method_is_removed(SelectionMethod method) {
        for(SelectionMethod m : removed_methods)
            if(m.get_name().equals(method.get_name()))
                return true;
        return false;
    }

    private static void select_testcase(CandidateSet candidate_set, SelectionMethod method, int i) {
        TestCase selected_testcase = method.best_candidate(candidate_set.C);
        method.add_execution_time(selected_testcase.execution_time); // add selected testcase runtime
        Terminal.echo(Settings.get_output_path(method, i), selected_testcase.result);
        String testcase_path = get_testcase_file_name(method.get_name(), i);
        TCWriter.write_into_txt_format(testcase_path, selected_testcase);
        ExecutionAnalysis.write_into_txt(method.get_name(), i);
    }

    private static String get_testcase_file_name(String method, int index) {
        return Settings.temp + method + Settings.testcases + Settings.test_file_name + index + Settings.testcase_format;
    }

    private static void write_test_files(int i) {
        CandidateSet candidate_set = new CandidateSet();
        for(SelectionMethod method : Settings.get_methods()) {
            select_testcase(candidate_set, method, i);
        }
    }
}
