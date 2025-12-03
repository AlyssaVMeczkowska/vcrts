package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * DatabaseManager - Singleton class for managing MySQL database connections
 * Reads configuration from .env file for security
 */
public class DatabaseManager {
    // Singleton instance
    private static DatabaseManager instance;
    
    // Database configuration (loaded from .env)
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    
    // Connection properties
    private Properties connectionProps;
    
    /**
     * Private constructor for Singleton pattern
     * Loads configuration from .env file
     */
    private DatabaseManager() {
        loadEnvFile();
        
        connectionProps = new Properties();
        connectionProps.put("user", dbUser);
        connectionProps.put("password", dbPassword);
        connectionProps.put("useSSL", "false");
        connectionProps.put("serverTimezone", "UTC");
        
        // Load MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    /**
     * Load environment variables from .env file
     */
    private void loadEnvFile() {
        Map<String, String> env = new HashMap<>();
        
        // Try to read .env file
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines and comments
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Parse key=value pairs
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    env.put(key, value);
                }
            }
            
            System.out.println("✓ Loaded configuration from .env file");
            
        } catch (IOException e) {
            System.err.println("Warning: .env file not found, using default values");
            System.err.println("Create a .env file in your project root with database credentials");
        }
        
        // Get configuration values (with defaults)
        String host = env.getOrDefault("DB_HOST", "localhost");
        String port = env.getOrDefault("DB_PORT", "3306");
        String database = env.getOrDefault("DB_NAME", "vcrts_db");
        
        // Construct JDBC URL
        this.dbUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.dbUser = env.getOrDefault("DB_USER", "root");
        this.dbPassword = env.getOrDefault("DB_PASSWORD", "");
        
        // Warn if using default password
        if (this.dbPassword.isEmpty() || this.dbPassword.equals("your_password_here")) {
            System.err.println("⚠ WARNING: Using default/empty database password!");
            System.err.println("⚠ Please update DB_PASSWORD in your .env file");
        }
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, connectionProps);
    }
    
    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean isValid = conn != null && !conn.isClosed();
            if (isValid) {
                System.out.println("✓ Database connection successful!");
                System.out.println("  Connected to: " + dbUrl);
            }
            return isValid;
        } catch (SQLException e) {
            System.err.println("✗ Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Close connection safely
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close statement safely
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close result set safely
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing result set: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get current database URL (for debugging)
     */
    public String getDatabaseUrl() {
        return dbUrl;
    }
    
    /**
     * Get current database user (for debugging)
     */
    public String getDatabaseUser() {
        return dbUser;
    }
}