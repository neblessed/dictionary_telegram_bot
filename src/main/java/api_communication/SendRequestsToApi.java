package api_communication;

import config.BotProperties;
import io.restassured.response.Response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import static io.restassured.RestAssured.given;

public class SendRequestsToApi extends BotProperties {
    static List<String> wordsCollection = new ArrayList<>(); //Коллекция слов
    static List<String> translatedWordsCollection = new ArrayList<>(); //Коллекция переведённых слов на русский
    static Set<String> parsedEngWords = new HashSet<>();

    //TODO Доработать парсинг слов для перевода из файла, а не брать слова с API
    public String getResultWordCollection(int wordsQuantity) {
        String resultWordKeyValue = "";
        parseEngWordsFromFile();
        getRandomWords(wordsQuantity);
        translateWords(wordsCollection);
        //Объединение Слово - перевод в одну строку, для дальнейшего вывода в бота
        for (int i = 0; i < wordsQuantity; i++) {
            resultWordKeyValue = resultWordKeyValue.concat(wordsCollection.get(i) + " - " + translatedWordsCollection.get(i) + " \n");
        }
        //Очистка коллекции переводов
        translatedWordsCollection.clear();
        return resultWordKeyValue;
    }

    public static void getRandomWords(int wordsQuantity) {
        int count = 0;
        for (String element: parsedEngWords) {
            if (count < wordsQuantity){
                wordsCollection.add(element);
            }

        }
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

    public static void parseEngWordsFromFile(){
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/words.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                parsedEngWords.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
