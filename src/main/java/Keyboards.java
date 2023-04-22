import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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
        var fiveWords = "5";
        var tenWords = "10";
        var fifteenWords = "15";
        var twentyWords = "20";

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
}
