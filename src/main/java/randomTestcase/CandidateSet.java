package randomTestcase;

import main.Settings;
import tools.ConsoleColors;

public class CandidateSet {
    public final TestCase[] C;

    public CandidateSet () {
        System.out.println(ConsoleColors.YELLOW_BOLD + "Creating new candidates ..." + ConsoleColors.RESET);
        C = new TestCase[Settings.candidate_set_size];
        for (int i=0; i<Settings.candidate_set_size; i++){
            C[i] = new TestCase();
        }
    }
}
