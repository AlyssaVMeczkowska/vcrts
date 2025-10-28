package model;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phoneNumber;
    private String hashedPassword;
    private String accountType;
    private String creationTimestamp;
    private boolean hasAgreedToTerms;

    public User(int id, String firstName, String lastName, String email, String username, String phoneNumber, String hashedPassword, String accountType, String creationTimestamp, boolean hasAgreedToTerms) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.hashedPassword = hashedPassword;
        this.accountType = accountType;
        this.creationTimestamp = creationTimestamp;
        this.hasAgreedToTerms = hasAgreedToTerms;
    }

    public int getId() { 
        return id; 
    }

    public String getFirstName() { 
        return firstName; 
    }

    public String getLastName() { 
        return lastName; 
    }

    public String getEmail() { 
        return email; 
    }

    public String getUsername() { 
        return username; 
    }

    public String getPhoneNumber() { 
        return phoneNumber; 
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

    public boolean hasAgreedToTerms() { 
        return hasAgreedToTerms; 
    }

    public void setHasAgreedToTerms(boolean hasAgreedToTerms) {
        this.hasAgreedToTerms = hasAgreedToTerms;
    }
}