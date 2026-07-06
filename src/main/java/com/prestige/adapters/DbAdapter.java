package com.prestige.adapters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbAdapter implements AutoCloseable {
    private final Connection connection;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/ALEKSANDER/PycharmProjects/prestige/instance/db.db"; // путь к вашей БД

    public DbAdapter() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            System.out.println("Подключение к SQLite установлено");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Не удалось подключиться к SQLite", e);
        }
    }

    public void deleteStudentByFullName(String lastName, String firstName, String middleName) {
        String sql = "DELETE FROM students WHERE last_name = ? AND first_name = ? AND middle_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setString(3, middleName);
            int deletedRows = stmt.executeUpdate();
            connection.commit();
            System.out.println("Удалено записей из SQLite: " + deletedRows);
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при удалении студента", e);
        }
    }

    public Long findStudentId(String lastName, String firstName, String middleName) {
        String sql = "SELECT id FROM students WHERE last_name = ? AND first_name = ? AND middle_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setString(3, middleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске студента", e);
        }
    }

    public boolean studentExists(String lastName, String firstName, String middleName) {
        String sql = "SELECT COUNT(*) FROM students WHERE last_name = ? AND first_name = ? AND middle_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setString(3, middleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке существования студента", e);
        }
    }

    /**
     * Очищает все тестовые данные (студенты, созданные за последние N минут)
     * В SQLite нет INTERVAL, используем datetime
     */
    public void cleanupTestData(int minutes) {
        String sql = "DELETE FROM students WHERE created_at > datetime('now', '-' || ? || ' minutes')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, minutes);
            int deletedRows = stmt.executeUpdate();
            connection.commit();
            System.out.println("Очищено тестовых записей из SQLite: " + deletedRows);
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при очистке тестовых данных", e);
        }
    }

    /**
     * Получает количество студентов в БД
     */
    public int getStudentsCount() {
        String sql = "SELECT COUNT(*) FROM students";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при подсчете студентов", e);
        }
    }

    private void rollback() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при откате транзакции: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Соединение с SQLite закрыто");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }
}