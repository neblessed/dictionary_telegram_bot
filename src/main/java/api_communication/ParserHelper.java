package api_communication;

import api_communication.CSV_handler.CreateHandler;
import api_communication.CSV_handler.NotifyHandler;
import config.BotProperties;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;

public class ParserHelper extends BotProperties {
    static List<String> wordsCollection = new ArrayList<>(); //Коллекция слов
    static List<String> translatedWordsCollection = new ArrayList<>(); //Коллекция переведённых слов на русский
    static final CreateHandler createHandler = new CreateHandler();
    static final NotifyHandler notifyHandler = new NotifyHandler();
    static final String directoryPath = "src/main/resources/userWords";

    public String getWordsPairs(int wordsQuantity, long id) {
        String resultWordKeyValue = "";
        parseWordsFromFile(wordsQuantity); //Парсинг из файла
        translateWords(wordsCollection); //Перевод слов

        //Объединение Слово - перевод в одну строку, для дальнейшего вывода в бота
        for (int i = 0; i < wordsQuantity; i++) {
            resultWordKeyValue = resultWordKeyValue.concat(wordsCollection.get(i) + " - " + translatedWordsCollection.get(i) + " \n");
        }

        //Парсинг в файл двух коллекций
        String fileName = id + ".csv";
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

    public static void translateWords(List<String> words) {
        for (String word : words) {
            translatedWordsCollection.add(translate(word));
        }
    }

    public static String translate(String word) {
        return given()
                .get("https://translation.googleapis.com/language/translate/v2?key=" + TRANSLATION_API_KEY + "&q=" + word + "&source=en&target=ru")
                .then().extract().jsonPath().getString("data.translations.translatedText").replace("[", "").replace("]", "");
    }

    public static void parseWordsFromFile(int wordsQuantity) {
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

}
