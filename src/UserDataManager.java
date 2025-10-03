import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDataManager {
    private static final String FILE_PATH = "user_database.txt";
    private List<User> users;

    public UserDataManager() {
        this.users = new ArrayList<>();
        loadUsersFromFile();
    }

    // Parsing File
    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Map<String, String> userData = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    // This is the end of a user record. Create the User object.
                    if (!userData.isEmpty()) {
                        User user = new User(
                                userData.get("email"),
                                userData.get("username"),
                                userData.get("password_hash"),
                                userData.get("account_type"),
                                userData.get("timestamp")
                        );
                        users.add(user);
                        userData.clear(); // Reset for the next user
                    }
                } else {
                    String[] parts = line.split(": ", 2);
                    if (parts.length == 2) {
                        userData.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Writing File
    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                writer.write("timestamp: " + user.getCreationTimestamp());
                writer.newLine();
                writer.write("account_type: " + user.getAccountType());
                writer.newLine();
                writer.write("email: " + user.getEmail());
                writer.newLine();
                writer.write("username: " + user.getUsername());
                writer.newLine();
                writer.write("password_hash: " + user.getHashedPassword());
                writer.newLine();
                writer.write("---");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmailTaken(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    public boolean isUsernameTaken(String username) {
        return users.stream().anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    public void addUser(String email, String username, String password, String accountType) {
        String hashedPassword = hashPassword(password);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        User newUser = new User(email, username, hashedPassword, accountType, timestamp);
        this.users.add(newUser);
        saveUsersToFile();
    }

    public boolean verifyUser(String usernameOrEmail, String password) {
        String hashedPassword = hashPassword(password);
        Optional<User> foundUser = users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(usernameOrEmail) || user.getEmail().equalsIgnoreCase(usernameOrEmail))
                .findFirst();

        return foundUser.isPresent() && foundUser.get().getHashedPassword().equals(hashedPassword);
    }
}