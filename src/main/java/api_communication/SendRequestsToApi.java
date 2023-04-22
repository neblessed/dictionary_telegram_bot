package api_communication;

import config.BotProperties;
import io.restassured.response.Response;

import java.util.*;

import static io.restassured.RestAssured.given;

public class SendRequestsToApi extends BotProperties {
    static List<String> wordsCollection = new ArrayList<>(); //Коллекция слов
    static List<String> translatedWordsCollection = new ArrayList<>(); //Коллекция переведённых слов на русский

    //TODO Доработать парсинг слов для перевода из файла, а не брать слова с API
    public String getResultWordCollection(int wordsQuantity) {
        String resultWordKeyValue = "";
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
        Response response = given()
                .get("https://random-word-api.herokuapp.com/word?number=" + wordsQuantity)
                .then().extract().response();
        wordsCollection = Arrays.stream(response.asString().replace("[\"", "").replace("\"]", "").split("\",\"")).toList();
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
}
