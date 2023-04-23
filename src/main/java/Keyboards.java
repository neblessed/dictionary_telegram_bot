import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboards {
    static ReplyKeyboardMarkup mainMenu() {
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

    static ReplyKeyboardMarkup wordLimit() {
        //Использованные кнопки в меню
        var fiveWords = "5 слов";
        var tenWords = "10 слов";
        var fifteenWords = "15 слов";
        var twentyWords = "20 слов";

        // Создаю объект клавиатуры
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        // Создаю список строк с названиями кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(fiveWords);
        row1.add(tenWords);
        keyboard.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(fifteenWords);
        row2.add(twentyWords);
        keyboard.add(row2);
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Вернуться в меню ⚡");
        keyboard.add(row3);

        // Устанавливаю клавиатуру
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public static InlineKeyboardMarkup inlineWordLimit() {
        // Создаем список кнопок
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton fiveWords = new InlineKeyboardButton();
        fiveWords.setCallbackData("five");
        fiveWords.setText("5");
        InlineKeyboardButton tenWords = new InlineKeyboardButton();
        fiveWords.setCallbackData("ten");
        fiveWords.setText("10");
        InlineKeyboardButton fifteenWords = new InlineKeyboardButton();
        fiveWords.setCallbackData("fifteen");
        fiveWords.setText("15");
        InlineKeyboardButton twentyWords = new InlineKeyboardButton();
        fiveWords.setCallbackData("twenty");
        fiveWords.setText("20");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));
        return markup;
    }
}
