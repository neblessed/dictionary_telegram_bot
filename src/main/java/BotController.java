import api_communication.ParserHelper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import exam.ExamCounter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.*;
import java.util.*;

import static api_communication.ParserHelper.*;

public class BotController extends TelegramLongPollingBot {
    private int limit = 10; //Лимит слов в день
    private int limitExam = 15; //Лимит экзамена

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new BotController());
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    public void sendText(Long who, String what, ReplyKeyboardMarkup replyKeyboard) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Кому сообщение
                .text(what)
                .replyMarkup(replyKeyboard)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Messages messagesClass = new Messages();
        ExamHandler examHandler = new ExamHandler();
        ExamCounter examCounter = new ExamCounter();

        if (update.hasMessage()) {
            var msg = update.getMessage();
            var user = msg.getFrom();
            var id = user.getId();
            long chatId = update.getMessage().getChatId();
            switch (msg.getText()) {
                case "/start" -> sendText(id, "Привет, воспользуйся меню 👇", Keyboards.mainMenu());
                case "Изучить слова 📚" -> {
                    sendText(id, "Подождите, Ваш запрос обрабатывается...", Keyboards.mainMenu());
                    sendText(id, new ParserHelper().getWordsPairs(update, id), Keyboards.mainMenu());
                }
                case "Дневной лимит слов 📈" -> messagesClass.setWordsLimit(chatId);
                case "Запустить тестирование 🍀" -> examHandler.getChoice(id);
                case "Завершить тестирование досрочно 🏃‍♂️" ->
                        sendText(id, examCounter.getStatistics(id), Keyboards.mainMenu());

            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            long id = callbackQuery.getMessage().getChatId();

            switch (data) {
                case "five_btn" -> {
                    setLimit(findUserLimit(update, 5));
                    sendText(id, "Новый лимит установлен ✅", Keyboards.mainMenu());
                }
                case "fifteen_btn" -> {
                    setLimit(findUserLimit(update, 15));
                    sendText(id, "Новый лимит установлен ✅", Keyboards.mainMenu());
                }
                case "twenty_btn" -> {
                    setLimit(findUserLimit(update, 20));
                    sendText(id, "Новый лимит установлен ✅", Keyboards.mainMenu());
                }
                case "ten_btn" -> {
                    setLimit(findUserLimit(update, 10));
                    sendText(id, "Новый лимит установлен ✅", Keyboards.mainMenu());
                }
                case "btn_wrong1", "btn_wrong2", "btn_wrong3" -> {
                    sendText(id, "Не правильно ❌", Keyboards.examMenu());

                    String engWord = callbackQuery.getMessage().getText().split(":")[1].trim();

                    examCounter.addExamTracker(id, engWord, false);
                    // messagesClass.deleteRecentExamMessage(update);

                    examHandler.getChoice(id);
                }
                default -> {
                    sendText(id, "Правильно 👍", Keyboards.examMenu());

                    String engWord = callbackQuery.getMessage().getText().split(":")[1].trim();

                    examCounter.addExamTracker(id, engWord, true);
                    // messagesClass.deleteRecentExamMessage(update);

                    examHandler.getChoice(id);
                }
            }
        }
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int findUserLimit(Update update, int limit) {
        String fileName = "src/main/resources/users_limits/limits.csv";
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String[] data = {String.valueOf(chatId), String.valueOf(limit)};

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName, true));
             CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> allData = reader.readAll();
            for (String[] element : allData) {
                if (Arrays.equals(element, data)) {
                    return Integer.parseInt(element[1]);
                } else if (element[0].equals(data[0]) && !element[1].equals(data[1])) {
                    element[1] = data[1];
                    new FileWriter(fileName, false);
                    writer.writeAll(allData, false);
                    return Integer.parseInt(element[1]);
                }
            }
            writer.writeNext(data, false);
            return Integer.parseInt(data[1]);
        } catch (CsvException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
