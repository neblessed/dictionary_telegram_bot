package db;

import java.sql.*;

public class DBHandler {

    private Connection connection;
    private Statement statement;

    public void createTable() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:WordOfTheDayBot.s3db");

            System.out.println("База Подключена!");

            statement = connection.createStatement();

            String sqlCreateUsers = "create table if not exists users (id integer primary key, chat_id int);";
            statement.execute(sqlCreateUsers);
            System.out.println("Создана таблица users!");

            String sqlCreatePairWords = "create table if not exists pair_words (id integer primary key, " +
                                                                                "engWord TEXT NOT NULL, " +
                                                                                "rusWord TEXT NOT NULL);";
            statement.execute(sqlCreatePairWords);
            System.out.println("Создана таблица pairWords!");

            String sqlCreateUserExam = "create table if not exists user_exam (id integer primary key, " +
                                                                                "FOREIGN KEY(user_id) REFERENCES users (id), " +
                                                                                "FOREIGN KEY(pair_word) REFERENCES pair_words (id)," +
                                                                                "result BOOLEAN);";
            statement.execute(sqlCreateUserExam);
            System.out.println("Создана таблица userExam!");

            String sqlCreateUserWord = "create table if not exists user_word (id integer primary key, " +
                                                                                "FOREIGN KEY(user_id) REFERENCES users (id), " +
                                                                                "FOREIGN KEY(pair_word) REFERENCES pair_words (id));";
            statement.execute(sqlCreateUserWord);
            System.out.println("Создана таблица userWord!");

            String sqlCreateUserLimit = "create table if not exists user_limit (id integer primary key, " +
                                                                                "FOREIGN KEY(user_id) REFERENCES users (id), " +
                                                                                "limit int);";
            statement.execute(sqlCreateUserLimit);
            System.out.println("Создана таблица userLimit!");

            String sqlCreateUserStats = "create table if not exists user_stats (id integer primary key, " +
                                                                                "FOREIGN KEY(user_id) REFERENCES users (id), " +
                                                                                "right_choise int," +
                                                                                "wrong_choise int);";
            statement.execute(sqlCreateUserStats);
            System.out.println("Создана таблица userStats!");
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertUserToDB(int chaID) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:WordOfTheDayBot.s3db");

            System.out.println("База Подключена!");

            statement = connection.createStatement();
            String sql = "INSERT INTO 'users' ('chatID') VALUES ("+ chaID +");";
            statement.execute(sql);

            System.out.println("Пользователь добавлен : " + chaID);

            statement.close();
            System.out.println("Закрытие statement!");
            connection.close();
            System.out.println("Закрытие connection!");
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
