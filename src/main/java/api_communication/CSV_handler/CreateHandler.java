package api_communication.CSV_handler;

import com.opencsv.CSVWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CreateHandler {

//    public void createCVS(String path, List<String> words, List<String> translatedWords) {
//        try (CSVWriter writer = new CSVWriter(new FileWriter(path, true))) {
//            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader());
//            csvPrinter.printRecord(words.toArray(new String[0]));
//            csvPrinter.printRecord(translatedWords.toArray(new String[0]));
//            csvPrinter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
