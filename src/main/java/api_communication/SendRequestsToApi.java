package api_communication;

import io.restassured.response.Response;
import java.util.*;
import static io.restassured.RestAssured.given;

public class SendRequestsToApi {
    Map<String, String> dictionary = new HashMap<>(); //Коллекция слово-перевод
    static List<String> wordsCollection = new ArrayList<>();
    static List<String> translatedWordsCollection = new ArrayList<>();

    private static final String TRANSLATION_KEY = "AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw";

    public static void main(String[] args) {
        //для теста
        getRandomWords(5);
        translateWords(wordsCollection);
        System.out.println(wordsCollection);
        System.out.println(translatedWordsCollection);
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
                .get("https://translation.googleapis.com/language/translate/v2?key=" + TRANSLATION_KEY + "&q=" + word + "&source=en&target=ru")
                .then().extract().jsonPath().getString("data.translations.translatedText").replace("[", "").replace("]", "");
    }
}
