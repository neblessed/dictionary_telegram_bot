import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

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

    public void setFourChoicesToExam(long chatId,
                                     String rusWord,
                                     String engWord,
                                     List<String> wrongWord) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        rowInLine.add(InlineKeyboardButton.builder()
                .text(wrongWord.get(0).toLowerCase())
                .callbackData("btn_wrong1")
                .build());
        rowInLine.add(InlineKeyboardButton.builder()
                .text(wrongWord.get(1).toLowerCase())
                .callbackData("btn_wrong3")
                .build());
        rowInLine.add(InlineKeyboardButton.builder()
                .text(wrongWord.get(2).toLowerCase())
                .callbackData("btn_wrong3")
                .build());
        rowInLine.add(InlineKeyboardButton.builder()
                .text(rusWord.toLowerCase())
                .callbackData("rightChoice")
                .build());
        Collections.shuffle(rowInLine);
        rowsInLine.add(rowInLine);
        inlineKeyboardMarkup.setKeyboard(rowsInLine);

        SendMessage msg = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Как переводится: " + engWord)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteRecentExamMessage(Update update) {
        String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        try {
            execute(DeleteMessage.builder().chatId(chatId).messageId(messageId).build()); // отправляем запрос на удаление сообщения
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
