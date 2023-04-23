import api_communication.ParserHelper;
import com.fasterxml.jackson.core.exc.InputCoercionException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import static api_communication.ParserHelper.*;

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
    public void onUpdateReceived(Update update)  {
        Messages messagesClass = new Messages();
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();
        int limit = 3;
        switch (msg.getText()) {
            case "/start" -> sendText(id, "Привет, воспользуйся меню 👇", Keyboards.mainMenu());
            case "Изучить слова 📚" -> {
                sendText(id, "Подождите, Ваш запрос обрабатывается...", Keyboards.mainMenu());
                sendText(id, new ParserHelper().getWordsPairs(limit, id), Keyboards.mainMenu());
            }
            //TODO придумать реализацию обработки нажатия и назначения нового лимита
            case "Дневной лимит слов 📈" -> {
                messagesClass.setWordsLimit(id, update);
                System.out.println(update.hasCallbackQuery());
            }
        }
        System.out.println(update.getChosenInlineQuery());
    }
}
