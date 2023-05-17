import api_communication.ParserHelper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import db.DBHandler;
import examStatistics.ExamStatistics;
import org.checkerframework.checker.units.qual.A;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.*;
import java.util.*;

import static api_communication.ParserHelper.*;

public class BotController extends TelegramLongPollingBot {
    private int limit = 10; //Лимит слов в день

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
        ExamStatistics examStatistics = new ExamStatistics();
        DBHandler dbHandler = new DBHandler();

        if (update.hasMessage()) {
            var msg = update.getMessage();
            var user = msg.getFrom();
            var id = user.getId();

            Long chat = msg.getChatId();
            long vadim_admin = 765707926;
            long sergei_admin = 351869653;

            long chatId = update.getMessage().getChatId();
            switch (msg.getText()) {
                case "/start" -> sendText(id, "Привет, воспользуйся меню 👇", Keyboards.mainMenu());
                case "Изучить слова 📚" -> {
                    sendText(id, "Подождите, Ваш запрос обрабатывается...", Keyboards.mainMenu());
                    sendText(id, new ParserHelper().getWordsPairs(update, id), Keyboards.mainMenu());
                    dbHandler.createTable();
                }
                case "Дневной лимит слов 📈" -> messagesClass.setWordsLimit(chatId);
                case "Запустить тестирование 🍀" -> {
                    examHandler.getChoice(id);
                    examStatistics.deleteStatistic(update);
                }
                case "Завершить тестирование досрочно 🏃‍♂️" -> {
                    sendText(id, examStatistics.getStatistics(id), Keyboards.mainMenu());
                    examStatistics.deleteStatistic(update);
                }
                case "Мои настройки ⚙" -> {
                    if (chat == vadim_admin || chat == sergei_admin) {
                        sendText(id, "Расширенная панель разработчика 🖥", Keyboards.adminMenu());
                    } else {
                        sendText(id, "В этом разделе ты можешь установить лимит слов или сбросить свой прогресс 👇", Keyboards.settingsMenu());
                    }
                }
                case "🎇 Добавить слова" -> {
                    sendText(id, "Для добавления слов присылайте их в формате: английское слово, перевод", Keyboards.adminMenu());
                }
                case "🎇 Очистить коллекцию" -> {
                    AdminHelper.clearWordsCollection();
                    sendText(id, "Коллекция слов очищена ✅", Keyboards.adminMenu());
                }
                case "Вернуться в меню 🏃‍♂️" -> sendText(id, "Воспользуйся меню 👇", Keyboards.mainMenu());
                case "Обнулить свой прогресс ♻" -> {
                    sendText(id, "Ваш прогресс обнулён 💿", Keyboards.mainMenu());
                    ParserHelper.replaceToNew(id);
                }
            }

            if ((chat == vadim_admin && msg.getText().contains(",")) || (chat == sergei_admin && msg.getText().contains(","))) {
                sendText(id, "👣 Размер коллекции слов: " + AdminHelper.appendNewWords(update.getMessage().getText()), Keyboards.adminMenu());
            }

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            long chatId = callbackQuery.getMessage().getChatId();

            switch (data) {
                case "five_btn" -> {
                    setLimit(findUserLimit(update, 5));
                    sendText(chatId, "Новый лимит установлен ✅", Keyboards.mainMenu());
                }
                case "fifteen_btn" -> {
                    setLimit(findUserLimit(update, 15));
                    sendText(chatId, "Новый лимит установлен ✅", Keyboards.mainMenu());
                }
                case "twenty_btn" -> {
                    setLimit(findUserLimit(update, 20));
                    sendText(chatId, "Новый лимит установлен ✅", Keyboards.mainMenu());
                }
                case "ten_btn" -> {
                    setLimit(findUserLimit(update, 10));
                    sendText(chatId, "Новый лимит установлен ✅", Keyboards.mainMenu());
                }
                case "btn_wrong1", "btn_wrong2", "btn_wrong3" -> {
                    sendText(chatId, "Не правильно ❌", Keyboards.examMenu());

                    String engWord = callbackQuery.getMessage().getText().split(":")[1].trim();
                    examStatistics.addExamTracker(chatId, engWord, false);
                    if (checkWordsInFileUserWords(chatId)) {
                        examHandler.getChoice(chatId);
                    } else {
                        sendText(chatId, "Тест завершён! Переходите к изучению новых слов ✅", Keyboards.mainMenu());
                        // examHandler.deleteFileWhenExamEnded(chatId);
                    }
                }
                default -> {
                    sendText(chatId, "Правильно 👍", Keyboards.examMenu());

                    String engWord = callbackQuery.getMessage().getText().split(":")[1].trim();
                    examStatistics.addExamTracker(chatId, engWord, true);
                    examHandler.deletePositiveChoisesFromFileUserWord(chatId, engWord);
                    if (checkWordsInFileUserWords(chatId)) {
                        examHandler.getChoice(chatId);
                    } else {
                        sendText(chatId, "Тест завершён! Переходите к изучению новых слов ✅", Keyboards.mainMenu());
                        //examHandler.deleteFileWhenExamEnded(chatId);
                    }
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

    public boolean checkWordsInFileUserWords(long chatID) {
        StringBuffer path = new StringBuffer();
        path.append("src/main/resources/user_words/wordsForExam");
        path.append(chatID);
        path.append(".csv");

        try (CSVReader br = new CSVReader(new FileReader(path.toString()))) {
            List<String[]> words = br.readAll();

            return !words.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
