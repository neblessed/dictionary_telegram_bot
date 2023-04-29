package exam;

import api_communication.CSV_handler.AddHandler;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ExamCounter {

    static final String directoryPath = "src/main/resources/user_exam";
    static List<String[]> wordsCollection = new ArrayList<>();

    public void addExamTracker(long chatID, String engWord, boolean result) {
        String rusWord = translateWordFromFile(engWord);
        String[] pairWord = new String[3];
        pairWord[0] = engWord;
        pairWord[1] = rusWord.trim();
        pairWord[2] = Boolean.toString(result);
        StringBuffer fileName = new StringBuffer();
        fileName.append("userExam");
        fileName.append(chatID);
        fileName.append(".txt");

        wordsCollection.add(pairWord);
        AddHandler.addCSV(directoryPath + "/" + fileName, wordsCollection);
        wordsCollection.clear();
    }

    public String translateWordFromFile(String endWord) {
        List<String[]> parsedEngWords = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/wordst.txt"))) {
            parsedEngWords = reader.readAll();

            for (String[] word : parsedEngWords) {
                if (endWord.equals(word[0])) {
                    return word[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getStatistics(long chatID) {
        String path = "src/main/resources/user_exam/userExam" + chatID + ".txt";
        int positiveAnswers = 0;
        int negativeAnswers = 0;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Правильных ответов : ");

        try (CSVReader br = new CSVReader(new FileReader(path))) {
            List<String[]> words = br.readAll();

            for (String[] word : words) {
                if (word[2].equals("false")) {
                    negativeAnswers++;
                } else {
                    positiveAnswers++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stringBuffer.append(positiveAnswers);
        stringBuffer.append("\n");
        stringBuffer.append("Не правильных ответов : ");
        stringBuffer.append(negativeAnswers);

        return stringBuffer.toString();
    }
}
