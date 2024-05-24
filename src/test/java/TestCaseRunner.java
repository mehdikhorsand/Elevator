import elevator.TCRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.Settings;
import tools.Evaluation;
import tools.Terminal;

// this TestCaseRunner class only runs for getting branch coverage and mutation coverage.
// for computing f-measure and f-time value, this class not used

public class TestCaseRunner{
    @Test
    public void tester () {
        for(int i = 0; i< Settings.testcase_number; i++) {
            // for automate testing...
            new TCRunner(Settings.get_target_testcase_path(i), Settings.get_target_output_path(i));
            // for manual testing...
//            new TCRunner("reported_result/report-0/RT/testcases/tc" + i + ".txt",
//                    "reported_result/report-0/RT/output/tc" + i + ".txt");
//            Evaluation.evaluation(Settings.get_target_output_path(i), Settings.get_target_oracle_path(i));
        }
    }

    @Before
    public void setup() {
        System.out.println("\n---------------------------------------------");
    }

    @After
    public void tearDown() {
        System.out.println("\n---------------------------------------------");
    }
}
