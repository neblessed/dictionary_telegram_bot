package api_communication;

import com.opencsv.CSVWriter;
import org.telegram.telegrambots.meta.api.objects.Update;
import api_communication.CSV_handler.AddHandler;
import config.BotProperties;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static io.restassured.RestAssured.given;

public class ParserHelper extends BotProperties {
    static List<String[]> wordsCollection = new ArrayList<>(); //Коллекция слов
    static List<String> translatedWordsCollection = new ArrayList<>(); //Коллекция переведённых слов на русский
    static final String directoryWithExamWords = "src/main/resources/user_words";
    static final String directoryWithWords = "src/main/resources/words/";

    public String getWordsPairs(Update update, long id) {
        createIfFileNotExist(id, "wordst.txt");

        String resultWordKeyValue = "";
        parseWordsFromFile(parseUserLimit(update)); //Парсинг из файла

        //Объединение Слово - перевод в одну строку, для дальнейшего вывода в бота
        for (int i = 0; i < parseUserLimit(update); i++) {
            resultWordKeyValue = resultWordKeyValue
                    .concat(wordsCollection.get(i)[0] + " - " + wordsCollection.get(i)[1] + "\n");
        }

        //Путь до файла
        StringBuffer path = new StringBuffer();
        path.append(directoryWithExamWords);
        path.append("/");
        path.append("wordsForExam");
        path.append(id);
        path.append(".csv");

        //Создание файла в директории user_words
        AddHandler.addCSV(path.toString(), wordsCollection);

        //Очистка коллекций со словами после выдачи пользователю
        wordsCollection.clear();
        translatedWordsCollection.clear();
        return resultWordKeyValue;
    }

    public String translate(String word) {
        return given()
                .get("https://translation.googleapis.com/language/translate/v2?key=" + TRANSLATION_API_KEY + "&q=" + word + "&source=en&target=ru")
                .then().extract().jsonPath()
                .getString("data.translations.translatedText")
                .replace("[", "")
                .replace("]", "");
    }

    public void parseWordsFromFile(int wordsQuantity) {
        List<String[]> parsedEngWords = new ArrayList<>(); //Коллекция спарсеных слов
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/wordst.txt"))) {
            parsedEngWords = reader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Генерация случайного числа для взятия по индексу рандомно
        for (int i = 0; i < wordsQuantity; i++) {
            int number = new Random().nextInt(parsedEngWords.size());
            wordsCollection.add(parsedEngWords.get(number));
        }
    }

    public static int parseUserLimit(Update update) {
        long chatId = update.getMessage().getChatId();
        String fileName = "src/main/resources/users_limits/limits.csv";
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> allData = reader.readAll();
            for (String[] element : allData) {
                if (element[0].equals(String.valueOf(chatId))) {
                    return Integer.parseInt(element[1]);
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        return 10;
    }

    public static void createIfFileNotExist(long id, String catchFromFile) {
        try {
            List<Path> directory = Files.walk(Paths.get(directoryWithWords))
                    .filter(x -> x.getFileName().toString()
                            .contains(String.valueOf(id))).toList();

            if (directory.isEmpty()) {
                CSVReader reader = new CSVReader(new FileReader("src/main/resources/" + catchFromFile));
                List<String[]> listToRelocate = reader.readAll();
                CSVWriter writer = new CSVWriter(new FileWriter(directoryWithWords + "collection" + id + ".csv"));
                writer.writeAll(listToRelocate, false);
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public static void replaceToNew(long id) {
        try {
            List<Path> containFiles = Files.walk(Path.of(directoryWithWords)).toList();
            containFiles = containFiles.stream().filter(x -> x.getFileName().toString().contains(String.valueOf(id))).toList();
            Files.delete(containFiles.get(0));
            createIfFileNotExist(id, "wordst.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
