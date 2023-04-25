import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Messages extends BotController {
    public void setWordsLimit(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;

        InlineKeyboardButton five = InlineKeyboardButton.builder()
                .text("5")
                .callbackData("five_btn")
                .build();

        InlineKeyboardButton ten = InlineKeyboardButton.builder()
                .text("10")
                .callbackData("ten_btn")
                .build();

        InlineKeyboardButton fifteen = InlineKeyboardButton.builder()
                .text("15")
                .callbackData("fifteen_btn")
                .build();

        InlineKeyboardButton twenty = InlineKeyboardButton.builder()
                .text("20")
                .callbackData("twenty_btn")
                .build();

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
