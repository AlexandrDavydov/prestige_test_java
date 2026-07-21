package com.prestige.adapters;

import com.prestige.models.Card;
import com.prestige.models.Coach;
import com.prestige.models.Lesson;
import com.prestige.models.LessonTemplate;
import com.prestige.models.Student;

import com.prestige.config.TestConfig;
import java.sql.*;

public class DbAdapter implements AutoCloseable {
    private final Connection connection;

    public DbAdapter() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(TestConfig.getDbUrl());
            connection.setAutoCommit(false);
            //System.out.println("Подключение к SQLite установлено");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Не удалось подключиться к SQLite", e);
        }
    }

    public int addLessonTemplate(LessonTemplate lessonTemplate) {
        String sql = "INSERT INTO lesson_templates (template_name, coach_id, student_ids) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lessonTemplate.getTemplateName());
            stmt.setInt(2, lessonTemplate.getCoachId());
            stmt.setString(3, lessonTemplate.getStudentsIds());

            int insertedRows = stmt.executeUpdate();
            connection.commit();

            int id = getLastInsertedId();
            lessonTemplate.setId(id);
            return id;
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при добавлении шаблона занятия", e);
        }
    }

    public void deleteLessonTemplateByName(String templateName) {
        String sql = "DELETE FROM lesson_templates WHERE template_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, templateName);
            int deletedRows = stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при удалении шаблона занятия", e);
        }
    }

    public int addLesson(Lesson lesson) {
        String sql = "INSERT INTO lessons (lesson_name, date, coach_id, student_ids, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lesson.getLessonName());
            stmt.setString(2, lesson.getDate());
            stmt.setInt(3, lesson.getCoachId());
            stmt.setString(4, lesson.getStudentIds());
            stmt.setString(5, lesson.getStatus());

            stmt.executeUpdate();
            connection.commit();

            int id = getLastInsertedId();
            lesson.setId(id);
            return id;
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при добавлении занятия", e);
        }
    }

    public void deleteLessonByName(String lessonName) {
        String sql = "DELETE FROM lessons WHERE lesson_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lessonName);
            int deletedRows = stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при удалении занятия занятия", e);
        }
    }

    public int addStudent(Student student) {
        int id;
        String sql = "INSERT INTO students (last_name, first_name, middle_name, contacts, birthday, lessons_count, additional_info) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getLastName());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getMiddleName());
            stmt.setString(4, student.getContacts());
            stmt.setString(5, student.getBirthday());
            stmt.setInt(6, student.getLessonsCount());
            stmt.setString(7, student.getAdditionalInfo());

            int insertedRows = stmt.executeUpdate();
            connection.commit();
            //System.out.println("Добавлено записей в SQLite: " + insertedRows);

            id = getLastInsertedId();
            //System.out.println("Присвоен ID студенту: " + id);

        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при добавлении студента: " + e.getMessage(), e);
        }
        return id;
    }

    public void deleteStudentById(int studentId) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при удалении студента по ID", e);
        }
    }

    public void deleteStudents(String studentIds) {
        String[] ids = studentIds.split(",");
        for (String id : ids) {
            deleteStudentById(Integer.parseInt(id.trim()));
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
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при удалении студента", e);
        }
    }

    public int addCoach(Coach coach) {
        int id;
        String sql = "INSERT INTO coaches (last_name, first_name, middle_name, contacts, birthday, lessons_count, lessons_paid, student_payment, additional_info) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, coach.getLastName());
            stmt.setString(2, coach.getFirstName());
            stmt.setString(3, coach.getMiddleName());
            stmt.setString(4, coach.getContacts());
            stmt.setString(5, coach.getBirthday());
            stmt.setInt(6, coach.getLessonsCount());
            stmt.setInt(7, coach.getLessonsPaid());
            stmt.setDouble(8, coach.getStudentPayment());
            stmt.setString(9, coach.getAdditionalInfo());

            int insertedRows = stmt.executeUpdate();
            connection.commit();
            id = getLastInsertedId();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при добавлении тренера", e);
        }
        return id;
    }

    public void deleteCoachById(int coachId) {
        String sql = "DELETE FROM coaches WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, coachId);
            int deletedRows = stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при удалении тренера по ID", e);
        }
    }

    public void deleteCoachByFullName(String lastName, String firstName, String middleName) {
        String sql = "DELETE FROM coaches WHERE last_name = ? AND first_name = ? AND middle_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setString(3, middleName);
            int deletedRows = stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при удалении тренера", e);
        }
    }

    public int addCard(Card card) {
        int id;
        String sql = "INSERT INTO cards (name, price, lessons_count, duration, color, status, creation_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.getName());
            stmt.setDouble(2, card.getPrice());
            stmt.setInt(3, card.getLessonsCount());
            stmt.setString(4, card.getDuration());
            stmt.setString(5, card.getColor());
            stmt.setString(6, card.getStatus());
            stmt.setString(7, card.getCreationDate());

            int insertedRows = stmt.executeUpdate();
            connection.commit();
            System.out.println("Добавлено записей в SQLite: " + insertedRows);

            id = getLastInsertedId();
            card.setId(id);
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при добавлении абонемента", e);
        }
        return id;
    }

    public void deleteCardByName(String name) {
        String sql = "DELETE FROM cards WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            int deletedRows = stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при удалении абонемента", e);
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

    private int getLastInsertedId() {
        String sql = "SELECT last_insert_rowid()";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new RuntimeException("Не удалось получить последний ID");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении последнего ID", e);
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