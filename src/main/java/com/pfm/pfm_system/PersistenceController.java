package com.pfm.pfm_system;

import Data.User;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import java.security.SecureRandom;
import java.sql.*;

@Component
public class PersistenceController {

    private static PersistenceController instance = null;
    private User user;

    private PersistenceController() {

    }

    public static PersistenceController getInstance() {
        if (instance == null)
            instance = new PersistenceController();
        return instance;
    }

    public User createAccount(String email, String userName, String personalID, String password, String phone) throws SQLException {
        // #( UserID, Email, userName, personalID, userPassword, phone)
        Connection connection = UsersDB().getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO Users "
                + "VALUES ("
                + "'" + email + "',"
                + "'" + userName + "',"
                + "'" + personalID + "',"
                + "'" + password + "',"
                + "'" + phone + "');");
        setUser(statement, email);
        try {
            connection = InvestmentDB().getConnection();
            statement = connection.createStatement();
            statement.execute("INSERT INTO Users VALUES ('" + getUser().getPersonalID() + "',0.0);");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Hola soy yo ---- \n\n\n\n");
            deleteAccount();
            cleanUser();
        }
        connection.close();
        return getUser();
    }

    public User logIn(String email, String password) throws SQLException {
        Connection connection = UsersDB().getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT userPassword FROM Users"
                + " WHERE email = '" + email + "';");
        result.next();
        if (password.equals(result.getString("userPassword"))) {
            setUser(statement, email);
            connection.close();
            return getUser();
        } else throw new SQLException();
    }

    public String resetPassword(String email, String userName, String personalID) throws SQLException {
        Connection connection = UsersDB().getConnection();
        Statement statement = connection.createStatement();
        String reset = generateRandomString(20);
        int changes = statement.executeUpdate("UPDATE Users SET userPassword = '" + reset + "' "
                + "WHERE email = '" + email + "'"
                + "AND userName = '" + userName + "'"
                + "AND personalID = '" + personalID + "';");
        connection.close();
        if (changes == 1)
            return reset;
        else throw new SQLException();
    }

    public void updateConfiguration(String email, String name, String password, String phone) throws SQLException {
        Connection connection = UsersDB().getConnection();
        if (!password.equals(getUser().getUserPassword())) {
            updatePassword(password);
            setUser(connection.createStatement(), getUser().getEmail());
        }
        if (!name.equals(getUser().getUserName())) {
            updateName(name);
            setUser(connection.createStatement(), getUser().getEmail());
        }
        if (!phone.equals(getUser().getPhone())) {
            updatePhone(phone);
            setUser(connection.createStatement(), getUser().getEmail());
        }
        if (!email.equals(getUser().getEmail())) {
            updateEmail(email);
            setUser(connection.createStatement(), email);
        }
        connection.close();
    }

    public void deleteAccount() throws SQLException {
        Connection connection = UsersDB().getConnection();
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM Users WHERE email = '" + getUser().getEmail() + "';");
        connection = InvestmentDB().getConnection();
        statement = connection.createStatement();
        statement.execute("DELETE FROM Users WHERE Id = '" + getUser().getEmail() + "';");
        statement.close();
        cleanUser();
    }

    private void setUser(Statement statement, String email) throws SQLException {
        // #( Email, userName, personalID, userPassword, phone)
        ResultSet result = statement.executeQuery(
                "SELECT email, userName, personalID, userPassword, phone FROM Users" +
                        " WHERE email = '" + email + "';");
        result.next();
        user = new User(result.getString(1),
                result.getString(2),
                result.getString(3),
                result.getString(4),
                result.getString(5));
        result.close();
    }

    public User getUser() {
        return user;
    }

    public void cleanUser() {
        user = null;
    }

    // ----------------- SUPPORT FUNCTIONS ----------------- //
    private DataSource UsersDB() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://southeast-asia-s3rv3r.database.windows.net:1433;database=PFM_System_Users");
        dataSource.setUsername("TongjiStudent");
        dataSource.setPassword("Tongji_Root");
        return dataSource;
    }

    private DataSource InvestmentDB() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://southeast-asia-s3rv3r.database.windows.net:1433;database=PFM_System_Investment");
        dataSource.setUsername("TongjiStudent");
        dataSource.setPassword("Tongji_Root");
        return dataSource;
    }

    private void updateEmail(String newEmail) throws SQLException {
        update("email", newEmail);
    }

    private void updatePassword(String newPassword) throws SQLException {
        update("userPassword", newPassword);
    }

    private void updateName(String newName) throws SQLException {
        update("userName", newName);
    }

    private void updatePhone(String newPhone) throws SQLException {
        update("phone", newPhone);
    }

    private void update(String attribute, String newValue) throws SQLException {
        Connection connection = UsersDB().getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE Users SET " + attribute + " = '" + newValue + "' WHERE email ='" + getUser().getEmail() + "';");
        connection.close();
    }

    public static String generateRandomString(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
}