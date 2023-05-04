import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AdminHelper {
    static String path = "src/main/resources/wordst.txt";
    static List<String> blabla = new ArrayList<>();

    static void clearWordsCollection() {
        try {
            new FileWriter(path, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static int appendNewWords(String s) {
        try (CSVReader reader = new CSVReader(new FileReader(path));
             CSVWriter writer = new CSVWriter(new FileWriter(path, true))) {
            List<String[]> allData = reader.readAll();
            List<String> splitedList = List.of(s.split("\n"));
            for (int i = 0; i < splitedList.size(); i++) {
                String[] arr = splitedList.get(i).split(",");
                String[] main = {arr[0].trim(), arr[1].trim()};
                allData.add(main);
            }
            new FileWriter(path);
            writer.writeAll(allData, false);
            return allData.size();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
