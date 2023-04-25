import api_communication.ParserHelper;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

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
        if (update.hasMessage()) {
            var msg = update.getMessage();
            var user = msg.getFrom();
            var id = user.getId();
            long chatId = update.getMessage().getChatId();
            switch (msg.getText()) {
                case "/start" -> sendText(id, "Привет, воспользуйся меню 👇", Keyboards.mainMenu());
                case "Изучить слова 📚" -> {
                    sendText(id, "Подождите, Ваш запрос обрабатывается...", Keyboards.mainMenu());
                    sendText(id, new ParserHelper().getWordsPairs(getLimit()), Keyboards.mainMenu());
                }
                case "Дневной лимит слов 📈" -> messagesClass.setWordsLimit(chatId);
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            long chatIdFromQuery = callbackQuery.getMessage().getChatId();
            switch (data) {
                case "five_btn" -> setLimit(5);
                case "fifteen_btn" -> setLimit(15);
                case "twenty_btn" -> setLimit(20);
                default -> setLimit(10);
            }
            sendText(chatIdFromQuery, "Новый лимит установлен ✅", Keyboards.mainMenu());
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
