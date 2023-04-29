import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class ExamHandler {

    private final Messages messages = new Messages();

    public void getChoice(long chatId) {
        List<String> pairs = setRightChoice(chatId);
        if (!pairs.isEmpty()) {
            String fileName = "userWords" + chatId + ".csv";
            // Создание объекта File для проверки наличия файла
            File file = new File("src/main/resources/user_words/", fileName);

            if (file.exists() && !file.isDirectory()) {
                List<String> wrongWord = setWrongWords(chatId);
                String rightChoice = pairs.get(1);
                String rightChoiceTranslation = pairs.get(0);

                messages.setFourChoicesToExam(chatId, rightChoice, rightChoiceTranslation, wrongWord);
            }
        }
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
        Messages messageClass = new Messages();
        List<String> wordPairs;
        String path = "src/main/resources/user_words/userWords" + chatId + ".csv";
        File file = new File(path);
        if (file.exists()) {
            try (CSVReader br = new CSVReader(new FileReader(path));
                 CSVWriter writer = new CSVWriter(new FileWriter(path, true))) {

                List<String[]> word = br.readAll();
                if (word.size() > 0) {
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            messageClass.sendText(chatId, "Вы ещё не получили ни одного слова.\b Нажмите на кнопку 'Изучить слова 📚'.", Keyboards.mainMenu());

        return new ArrayList<>();
    }
}
