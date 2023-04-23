import org.checkerframework.checker.units.qual.C;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Messages extends BotController {
    public void setWordsLimit(long chatId, Update update) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;

        InlineKeyboardButton five = new InlineKeyboardButton();
        five.setText("5");
        five.setCallbackData("five_btn");

        InlineKeyboardButton ten = new InlineKeyboardButton();
        ten.setText("10");
        ten.setCallbackData("ten_btn");

        InlineKeyboardButton fifteen = InlineKeyboardButton.builder()
                .text("15")
                .callbackData("fifteen_btn")
                .build();

        InlineKeyboardButton twenty = new InlineKeyboardButton();
        twenty.setText("20");
        twenty.setCallbackData("twenty_btn");

        rowInLine = List.of(five, ten, fifteen, twenty);
        rowsInLine.add(rowInLine);

        inlineKeyboardMarkup.setKeyboard(rowsInLine);

        SendMessage msg = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Выберите желаемое количество слов в день:")
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
