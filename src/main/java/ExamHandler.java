import api_communication.ParserHelper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class ExamHandler {

    private final ParserHelper parserHelper = new ParserHelper();
    private final Messages messages = new Messages();

    public boolean getChoice(long chatId) {
        List<String> pairs = setRightChoice(chatId);
        String fileName = "userWords" + chatId + ".csv";
        // Создание объекта File для проверки наличия файла
        File file = new File("src/main/resources/user_words/", fileName);
        boolean foundFile = false;

        if (file.exists() && !file.isDirectory()) {
            foundFile = true;
            List<String> wrongWord = setWrongWords(chatId);
            String rightChoice = pairs.get(1);
            String rightChoiceTranslation = pairs.get(0);

            messages.setFourChoicesToExam(chatId, rightChoice, rightChoiceTranslation, wrongWord);
        }

        return foundFile;
    }

    public List<String> setWrongWords(long chatId) {
        try (CSVReader br = new CSVReader(new FileReader("src/main/resources/user_words/userWords" + chatId + ".csv"))) {
            List<String> wrongWord = new ArrayList<>();
            List<String[]> word = br.readAll();

            for (int i = 0; i < 3; i++) {
                wrongWord.add(word.get(new Random().nextInt(word.size()))[1]);
            }

            return wrongWord;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<String> setRightChoice(long chatId) {
        List<String> wordPairs;
        String path = "src/main/resources/user_words/userWords" + chatId + ".csv";
        try (CSVReader br = new CSVReader(new FileReader(path));
             CSVWriter writer = new CSVWriter(new FileWriter(path, true))) {
            List<String[]> word = br.readAll();
            //создаю рандомный индекс заранее
            int randomIndex = new Random().nextInt(word.size());
            wordPairs = List.of(word.get(randomIndex)[0], word.get(randomIndex)[1]);
            //удаляю пару по индексу
            word.remove(randomIndex);
            //Очищаю файл
            new FileWriter(path, false);
            //Записываю новую коллекцию в этот же файл
            writer.writeAll(word, false);
            return wordPairs;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
