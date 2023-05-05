import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboards {
    static ReplyKeyboardMarkup mainMenu() {
        //Использованные кнопки в меню
        var learnButton = "Изучить слова 📚";
        var runExamButton = "Запустить тестирование 🍀";
        var settingsButton = "Мои настройки ⚙";

        // Создаю объект клавиатуры
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        // Создаю список строк с названиями кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(learnButton);
        keyboard.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(runExamButton);
        keyboard.add(row2);
        KeyboardRow row3 = new KeyboardRow();
        row3.add(settingsButton);
        keyboard.add(row3);

        // Устанавливаю клавиатуру
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup examMenu() {
        //Использованные кнопки в меню
        var abortExam = "Завершить тестирование досрочно 🏃‍♂️";

        // Создаю объект клавиатуры
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        // Создаю список строк с названиями кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(abortExam);
        keyboard.add(row1);

        // Устанавливаю клавиатуру
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup settingsMenu() {
        //Использованные кнопки в меню
        var wordLimit = "Дневной лимит слов 📈";
        var resetProgress = "Обнулить свой прогресс ♻";
        var goBack = "Вернуться в меню 🏃‍♂️";

        // Создаю объект клавиатуры
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        // Создаю список строк с названиями кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(wordLimit);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(resetProgress);
        KeyboardRow row3 = new KeyboardRow();
        row3.add(goBack);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        // Устанавливаю клавиатуру
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup adminMenu() {
        //Использованные кнопки в меню
        var wordLimit = "Дневной лимит слов 📈";
        var resetProgress = "Обнулить свой прогресс ♻";
        var goBack = "Вернуться в меню 🏃‍♂️";
        var addWords = "🎇 Добавить слова";
        var removeAllWords = "🎇 Очистить коллекцию";

        // Создаю объект клавиатуры
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        // Создаю список строк с названиями кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(wordLimit);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(resetProgress);
        KeyboardRow row3 = new KeyboardRow();
        row3.add(goBack);
        KeyboardRow row4 = new KeyboardRow();
        row4.add(addWords);
        row4.add(removeAllWords);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        // Устанавливаю клавиатуру
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
