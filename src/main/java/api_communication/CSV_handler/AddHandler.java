package api_communication.CSV_handler;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AddHandler {

    public static void addCSV(String path, List<String[]> words) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(path, true))) {
            for (int i = 0; i < words.size(); i++) {
                String[] arr = words.get(i);
                writer.writeNext(arr, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
