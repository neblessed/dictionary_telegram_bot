import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Messages extends BotController {
    public void setWordsLimit(long chatId) {
        SendMessage msg = new SendMessage();
        msg.setChatId(String.valueOf(chatId));
        msg.setText("Выберите желаемое количество слов в день:");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;


        var five = new InlineKeyboardButton();
        five.setText("5");
        five.setCallbackData("five_btn");
        var ten = new InlineKeyboardButton();
        ten.setText("10");
        ten.setCallbackData("ten_btn");
        var fifteen = new InlineKeyboardButton();
        fifteen.setText("15");
        fifteen.setCallbackData("fifteen_btn");
        var twenty = new InlineKeyboardButton();
        twenty.setText("20");
        twenty.setCallbackData("twenty_btn");

        rowInLine = List.of(five, ten, fifteen, twenty);
        rowsInLine.add(rowInLine);

        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        msg.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
