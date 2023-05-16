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

            String sqlCreateUsers = "create table if not exists users (id INTEGER PRIMARY KEY, chat_id INTEGER);";
            statement.execute(sqlCreateUsers);
            System.out.println("Создана таблица users!");

            String sqlCreatePairWords = "create table if not exists base_words (id INTEGER PRIMARY KEY, " +
                                                                                "engWord TEXT NOT NULL, " +
                                                                                "rusWord TEXT NOT NULL);";
            statement.execute(sqlCreatePairWords);
            System.out.println("Создана таблица pairWords!");

            /*String sqlCreateUserExam = "create table if not exists user_exam (id INTEGER PRIMARY KEY, " +
                                                                                "user_id INTEGER, " +
                                                                                "pair_word INTEGER," +
                                                                                "result BOOLEAN," +
                                                                                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                                                                                "FOREIGN KEY(pair_word) REFERENCES base_words(id) ON DELETE CASCADE);";
            statement.execute(sqlCreateUserExam);
            System.out.println("Создана таблица userExam!");*/

            String sqlCreateUserWord = "create table if not exists user_exam (id INTEGER PRIMARY KEY, " +
                                                                                "user_id INTEGER, " +
                                                                                "pair_word INTEGER," +
                                                                                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                                                                                "FOREIGN KEY(pair_word) REFERENCES base_words(id) ON DELETE CASCADE);";
            statement.execute(sqlCreateUserWord);
            System.out.println("Создана таблица userWord!");

            String sqlCreateUserLimit = "create table if not exists user_limit (id INTEGER PRIMARY KEY, " +
                                                                                "user_id INTEGER, " +
                                                                                "limit INTEGER," +
                                                                                "FOREIGN KEY(user_id) REFERENCES users (id)); ";

            statement.execute(sqlCreateUserLimit);
            System.out.println("Создана таблица userLimit!");

            String sqlCreateUserStats = "create table if not exists user_stats (id integer primary key, " +
                                                                                "user_id INTEGER, " +
                                                                                "right_choise INTEGER," +
                                                                                "wrong_choise INTEGER," +
                                                                                "FOREIGN KEY(user_id) REFERENCES users (id));";

            statement.execute(sqlCreateUserStats);
            System.out.println("Создана таблица userStats!");
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserDB(int chaID) {
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

    public int getUserIdDB(int chaID, Statement statement) {
        int id = 0;

        try {
            Class.forName("org.sqlite.JDBC");
            String sql = "select id from user where chat_id = ("+ chaID +");";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }

            resultSet.close();

            return id;
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserLimitDB(int chaID, int limit) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:WordOfTheDayBot.s3db");

            System.out.println("База Подключена!");

            statement = connection.createStatement();
            int id = getUserIdDB(chaID, statement);
            String sql = "INSERT INTO user_limit (user_id, limit) VALUES (" + id + " , " + limit + ");";
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
