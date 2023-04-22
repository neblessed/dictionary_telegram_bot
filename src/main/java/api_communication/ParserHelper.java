package api_communication;

import config.BotProperties;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;

public class ParserHelper extends BotProperties {
    static List<String> wordsCollection = new ArrayList<>(); //Коллекция слов
    static List<String> translatedWordsCollection = new ArrayList<>(); //Коллекция переведённых слов на русский
    static List<String> parsedEngWords = new ArrayList<>();

    //TODO Доработать парсинг слов для перевода из файла, а не брать слова с API
    public String getResultWordCollection(int wordsQuantity) {
        String resultWordKeyValue = "";
        parseWordsFromFile(wordsQuantity); //Парсинг из файла
        translateWords(wordsCollection); //Перевод слов

        //Объединение Слово - перевод в одну строку, для дальнейшего вывода в бота
        for (int i = 0; i < wordsQuantity; i++) {
            resultWordKeyValue = resultWordKeyValue.concat(wordsCollection.get(i) + " - " + translatedWordsCollection.get(i) + " \n");
        }

        //Парсинг в файл двух коллекций
        parseToExamFile("src/main/resources/exam.csv", wordsCollection, translatedWordsCollection);

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
        //TODO дописать парсер, сейчас он зачищает имеющиеся файлы, а должен добавлять туда значения
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(path));
            writer.writeNext(words.toArray(new String[0]));
            writer.writeNext(translatedWords.toArray(new String[0]));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
