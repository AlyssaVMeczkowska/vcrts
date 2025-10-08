package data;

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
import model.User;

public class UserDataManager {
    private static final String FILE_PATH = "data/user_database.txt";
    private List<User> users;

    public UserDataManager() {
        this.users = new ArrayList<>();
        loadUsersFromFile();
    }

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
                    if (!userData.isEmpty()) {
                        boolean hasAgreedToTerms = userData.getOrDefault("has_agreed_to_terms", "false").equalsIgnoreCase("true");

                        // Determine the ID from either owner_id or client_id key
                        int id = 0;
                        if (userData.containsKey("owner_id")) {
                            id = Integer.parseInt(userData.get("owner_id"));
                        } else if (userData.containsKey("client_id")) {
                            id = Integer.parseInt(userData.get("client_id"));
                        }

                        User user = new User(
                                id,
                                userData.get("first_name"),
                                userData.get("last_name"),
                                userData.get("email"),
                                userData.get("username"),
                                userData.get("phone_number"),
                                userData.get("password_hash"),
                                userData.get("account_type"),
                                userData.get("timestamp"),
                                hasAgreedToTerms
                        );
                        users.add(user);
                        userData.clear();
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

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                if ("Owner".equals(user.getAccountType())) {
                    writer.write("owner_id: " + user.getId());
                } else {
                    writer.write("client_id: " + user.getId());
                }
                writer.newLine();
                writer.write("timestamp: " + user.getCreationTimestamp());
                writer.newLine();
                writer.write("account_type: " + user.getAccountType());
                writer.newLine();
                writer.write("first_name: " + user.getFirstName());
                writer.newLine();
                writer.write("last_name: " + user.getLastName());
                writer.newLine();
                writer.write("email: " + user.getEmail());
                writer.newLine();
                writer.write("username: " + user.getUsername());
                writer.newLine();
                writer.write("phone_number: " + user.getPhoneNumber());
                writer.newLine();
                writer.write("password_hash: " + user.getHashedPassword());
                writer.newLine();
                writer.write("has_agreed_to_terms: " + user.hasAgreedToTerms());
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
    
    private int getNextOwnerId() {
        return users.stream()
                .filter(user -> "Owner".equals(user.getAccountType()))
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }

    private int getNextClientId() {
        return users.stream()
                .filter(user -> "Client".equals(user.getAccountType()))
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }

    public void addUser(String firstName, String lastName, String email, String username, String phoneNumber, String password, String accountType, boolean hasAgreedToTerms) {
        int newId;
        if ("Owner".equals(accountType)) {
            newId = getNextOwnerId();
        } else {
            newId = getNextClientId();
        }
        
        String hashedPassword = hashPassword(password);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        User newUser = new User(newId, firstName, lastName, email, username, phoneNumber, hashedPassword, accountType, timestamp, hasAgreedToTerms);
        this.users.add(newUser);
        saveUsersToFile();
    }


    public void updateUserConsent(User userToUpdate) {
        users.stream()
            .filter(user -> user.getId() == userToUpdate.getId())
            .findFirst()
            .ifPresent(user -> user.setHasAgreedToTerms(true));
        saveUsersToFile();
    }

    public User verifyUser(String usernameOrEmail, String password) {
        String hashedPassword = hashPassword(password);
        Optional<User> foundUser = users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(usernameOrEmail) || user.getEmail().equalsIgnoreCase(usernameOrEmail))
                .findFirst();
        if (foundUser.isPresent() && foundUser.get().getHashedPassword().equals(hashedPassword)) {
            return foundUser.get();
        }

        return null;
    }
}