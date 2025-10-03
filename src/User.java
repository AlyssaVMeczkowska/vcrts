public class User {
    private String email;
    private String username;
    private String hashedPassword;
    private String accountType;
    private String creationTimestamp;

    public User(String email, String username, String hashedPassword, String accountType, String creationTimestamp) {
        this.email = email;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.accountType = accountType;
        this.creationTimestamp = creationTimestamp;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }
}