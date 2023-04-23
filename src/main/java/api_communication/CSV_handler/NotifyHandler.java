package api_communication.CSV_handler;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class NotifyHandler {

    public static void notifyCSV(String path, List<String> words, List<String> translatedWords) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(path)))
        {
            writer.writeNext(words.toArray(new String[0]), true);
            writer.writeNext(translatedWords.toArray(new String[0]), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
