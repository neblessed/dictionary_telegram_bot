## 🌎 Телеграм бот для изучения английских слов

Данный бот призван помочь в изучениии новых английских слов. Пользователю предлагается подборка слов для изучения, а так же функционал запуска экзамена по выданной ранее подборке слов.

### Возможности бота:
![image](https://user-images.githubusercontent.com/110935510/234935773-f4509819-14bc-41f6-8091-70750c7252c2.png)


#### 1. Выдача слов по кнопке "Изучить слова 📚"
Бот обрабатывает файл words.txt и присылает то количество слов, которое установлено пользователем (см. пункт 2).
Налажено общение с Google Translate API, который обрабатывает поступающие слова из файла и парсит их в коллекцию с переводами.

После выдачи слов пользователю - они помещаются в .csv-файл в формате "слово, перевод" для дальнейшей выдачи через кнопку "Запустить тестирование" (см. пункт 3).
![image](https://user-images.githubusercontent.com/110935510/234936212-6138027e-59d9-4674-97ea-579aafde4c42.png)
![image](https://user-images.githubusercontent.com/110935510/234936390-ffabdae9-c68f-4453-b9ba-255b393ec092.png)

#### 2. Установка дневного лимита слов по кнопке "Дневной лимит слов 📈" 
После нажатия на кнопку пользователю присылается сообщение с прикрепленными кнопками:
![image](https://user-images.githubusercontent.com/110935510/234936532-22083db6-0248-46c9-94fd-7acf469f7ea5.png)

При выборе любого лимита через нажатие, боту приходит информация о callbackData (id нажатой кнопки), которую бот обрабатывает следующим образом:
В существующем файле (user_limits/limits.csv) считывает всё содержимое в коллекцию типа List<String[]>, и проходится по каждой записи,
сверяя chatId и новый лимит. Если записи нет - записывает её, если запись есть и новый установленный лимит соответствует уже записанному - 
возвращает этот лимит, а если запись имеется, но записанный лимит и устанавливаемый - не соответствуют друг другу, то обновляет запись.

![image](https://user-images.githubusercontent.com/110935510/234937479-7ec8f99a-f83b-473f-af16-e95c255349a9.png)

После успешной установки лимита - пользователю приходит ответ от бота: "Новый лимит установлен ✅"

#### 3. Прохождение итогового тестирования по кнопке "Запустить тестирование 🍀"
Бот взаимодействует с ранее описанным .csv-файлом для выдачи для проверки именно тех слов, которые ранее выдавались пользователю.

Сообщение приходит в таком формате: Английское слово и 4 кнопки (слова переводы, один из которых верный). Сверка правильного ответа происходит благодаря подставляемому в кнопку-перевод callbackData, что позволяет динамично проверять ответы пользователя на правильность.

![image](https://user-images.githubusercontent.com/110935510/234935542-dd6f9dfc-8ce0-4fdf-991d-291de3a9d874.png)

- Если пользователь отвечает не правильно, то бот присылает сообщение "Не правильный ответ"
- Если пользователь отвечает верно, то данная пара (слово-перевод) удаляется из файла и в при последующем экзаменее не предлагается.

При прохождении тестирования пользователю доступна кнопка "Завершить тестирование досрочно 🏃‍♂️", она позволяет прервать тестирования и зафиксировать текущий прогресс.
#### После прохождения тестирования пользователю предоставляется статистика:

![image](https://user-images.githubusercontent.com/110935510/235336337-a75c3185-17d4-4c0d-91c5-7187d65e2052.png)


