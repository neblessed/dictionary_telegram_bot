package api_communication;

import config.BotProperties;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

import static io.restassured.RestAssured.given;

public class ParserHelper extends BotProperties {
    static List<String> wordsCollection = new ArrayList<>(); //Коллекция слов
    static List<String> translatedWordsCollection = new ArrayList<>(); //Коллекция переведённых слов на русский

    public String getWordsPairs(int limit) {
        String resultWordKeyValue = "";
        parseWordsFromFile(limit); //Парсинг из файла
        translateWords(wordsCollection); //Перевод слов

        //Объединение Слово - перевод в одну строку, для дальнейшего вывода в бота
        for (int i = 0; i < limit; i++) {
            resultWordKeyValue = resultWordKeyValue
                    .concat(wordsCollection.get(i) + " - " + translatedWordsCollection.get(i) + "\n");
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
}
