import api_communication.SendRequestsToApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

import static config.BotProperties.*;

public class BotController extends TelegramLongPollingBot {
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

    static ReplyKeyboardMarkup setUpKeyboard() {
        //Использованные кнопки в меню
        var learnButton = "Изучить слова 📚";
        var dayWordsLimitButton = "Дневной лимит слов 📈";
        var runExamButton = "Запустить тестирование 🍀";

        // Создаю объект клавиатуры
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        // Создаю список строк с названиями кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(learnButton);
        keyboard.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(dayWordsLimitButton);
        keyboard.add(row2);
        KeyboardRow row3 = new KeyboardRow();
        row3.add(runExamButton);
        keyboard.add(row3);

        // Устанавливаю клавиатуру
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
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
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();

        switch (update.getMessage().getText()) {
            case "/start " -> sendText(id, "Привет, воспользуйся меню 👇", setUpKeyboard());
            //TODO заменить на выбрасываемые слова из коллекции (возможно concat key - value)
            case "Изучить слова 📚" -> sendText(id, "Слово для изучения: book - книга", setUpKeyboard());
            default ->  sendText(id, "Эта кнопка пока не работает 😢", setUpKeyboard());
        }
    }
}
