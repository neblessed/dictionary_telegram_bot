package api_communication;

import api_communication.CSV_handler.CreateHandler;
import api_communication.CSV_handler.AddHandler;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import api_communication.CSV_handler.AddHandler;
import config.BotProperties;
import com.opencsv.CSVWriter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.*;
import java.util.*;

import static io.restassured.RestAssured.given;

public class ParserHelper extends BotProperties {
    static List<String> wordsCollection = new ArrayList<>(); //Коллекция слов
    static List<String> translatedWordsCollection = new ArrayList<>(); //Коллекция переведённых слов на русский
    static final CreateHandler createHandler = new CreateHandler();
    static final AddHandler notifyHandler = new AddHandler();
    static final String directoryPath = "src/main/resources";

    public String getWordsPairs(Update update, long id) {
        String resultWordKeyValue = "";
        parseWordsFromFile(parseUserLimit(update)); //Парсинг из файла
        translateWords(wordsCollection); //Перевод слов

        //Объединение Слово - перевод в одну строку, для дальнейшего вывода в бота
        for (int i = 0; i < parseUserLimit(update); i++) {
            resultWordKeyValue = resultWordKeyValue
                    .concat(wordsCollection.get(i) + " - " + translatedWordsCollection.get(i) + "\n");
        }

        //Парсинг в файл двух коллекций
        String fileName = "userWords" + id + ".csv";
        // Создание объекта File для проверки наличия файла
        File file = new File(directoryPath, fileName);

        if (file.exists() && !file.isDirectory()) {
            createHandler.createCVS(directoryPath + fileName, wordsCollection, translatedWordsCollection);
        } else {
            notifyHandler.notifyCSV(directoryPath + fileName, wordsCollection, translatedWordsCollection);
        }

        //Очистка коллекций со словами
        wordsCollection.clear();
        translatedWordsCollection.clear();
        return resultWordKeyValue;
    }

    public void translateWords(List<String> words) {
        for (String word : words) {
            translatedWordsCollection.add(translate(word));
        }
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
        List<String> parsedEngWords = new ArrayList<>(); //Коллекция спарсеных слов
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/words.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                parsedEngWords.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Генерация случайного числа для взятия по индексу рандомно
        for (int i = 0; i < wordsQuantity; i++) {
            int number = new Random().nextInt(parsedEngWords.size());
            wordsCollection.add(parsedEngWords.get(number));
        }
    }

    public static void parseToExamFile(String path, List<String> words, List<String> translatedWords) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(path, true));
            for (int i = 0; i < words.size(); i++) {
                writer.append(words.get(i)).append(", ").append(translatedWords.get(i));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
