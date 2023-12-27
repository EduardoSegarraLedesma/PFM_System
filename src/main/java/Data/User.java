package Data;

public class User {

    private final String email;
    private final String userName;
    private final String personalID;
    private final String userPassword;
    private final String phone;

    public User(String email, String userName, String personalID, String userPassword, String phone) {
        this.email = email;
        this.userName = userName;
        this.personalID = personalID;
        this.userPassword = userPassword;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonalID() {
        return personalID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getPhone() {
        return phone;
    }
}