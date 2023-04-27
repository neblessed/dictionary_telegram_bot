import api_communication.ParserHelper;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class ExamHandler {

    private final ParserHelper parserHelper = new ParserHelper();
    private final Messages messages = new Messages();

    public boolean getСhoice(long chatId) {

        String fileName = "userWords" + chatId + ".csv";
        // Создание объекта File для проверки наличия файла
        File file = new File("src/main/resources", fileName);
        boolean foundFile = false;

        if (file.exists() && !file.isDirectory()) {
            foundFile = true;
            List<String> wrongWord = setWrongWords();
            String rightChoice = setRightChoice(chatId);
            String rightChoiceTranslation = parserHelper.translate(rightChoice);

            messages.setFourСhoicesToExam(chatId, rightChoice, rightChoiceTranslation, wrongWord);
        }

        return foundFile;
    }

    public List<String> setWrongWords() {
        try (CSVReader br = new CSVReader(new FileReader("src/main/resources/words.txt"))) {
            List<String> wrongWord = new ArrayList<>();
            List<String[]> word = br.readAll();

            for (int i = 0; i < 3; i++) {
                wrongWord.add(Arrays.toString(word.get(new Random().nextInt(1000))));
            }

            return wrongWord;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public String setRightChoice(long chatId) {
        try (CSVReader br = new CSVReader(new FileReader("src/main/resources/userWords" + chatId + ".csv"))) {
            List<String[]> word = br.readAll();

            return word.get(new Random().nextInt(word.size()))[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
