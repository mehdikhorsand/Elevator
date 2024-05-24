package tools;
import main.Settings;
import randomRequest.RequestInput;
import randomTestcase.TestCase;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class TCWriter {

    static PrintWriter writer;

    public static void write_into_txt_format(String file_name, TestCase tc) {
        try {
            writer = new PrintWriter(Settings.project_location + file_name, "UTF-8");
            write_start_group(tc);
            write_requests(tc);
            write_stop_group(tc);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void write_start_group(TestCase tc) {
        writer.println("StartGroup\t" + tc.floor_quantity + "\t" + tc.elevator_quantity);
    }

    private static void write_stop_group(TestCase tc) {
        writer.print("StopGroup");
    }

    private static void write_requests(TestCase tc) {
        for(RequestInput request : tc.requests) {
            writer.println(request.get_text());
        }
    }
}
