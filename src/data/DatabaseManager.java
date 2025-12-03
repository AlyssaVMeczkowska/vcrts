package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

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
        connectionProps.put("allowPublicKeyRetrieval", "true"); // Often needed for local dev

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
        }
        
        // Get configuration values (with defaults)
        String host = env.getOrDefault("DB_HOST", "localhost");
        String port = env.getOrDefault("DB_PORT", "3306");
        String database = env.getOrDefault("DB_NAME", "vcrts_db");
        
        // Construct JDBC URL
        this.dbUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.dbUser = env.getOrDefault("DB_USER", "root");
        this.dbPassword = env.getOrDefault("DB_PASSWORD", "");
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
     * Reads the SQL script and executes it to create DB and Tables.
     * Use this when the application starts to ensure DB exists.
     */
    public void initializeDatabase(String scriptPath) {
        System.out.println("Initializing database from script: " + scriptPath);
        
        // We need to connect without the database name first to run DROP/CREATE DATABASE
        String rootUrl = this.dbUrl;
        
        // Strip the database name from the URL to connect to the server root
        // Example: jdbc:mysql://localhost:3306/vcrts_db -> jdbc:mysql://localhost:3306/
        int lastSlashIndex = this.dbUrl.lastIndexOf("/");
        if (lastSlashIndex > 0) {
            rootUrl = this.dbUrl.substring(0, lastSlashIndex + 1);
        }
        
        // Add allowMultiQueries for script execution support
        String scriptUrl = rootUrl + "?allowMultiQueries=true";

        try (Connection conn = DriverManager.getConnection(scriptUrl, connectionProps);
             Statement stmt = conn.createStatement()) {

            File scriptFile = new File(scriptPath);
            if (!scriptFile.exists()) {
                System.err.println("SQL Script not found at: " + scriptFile.getAbsolutePath());
                return;
            }

            // Read the script
            Scanner scanner = new Scanner(scriptFile);
            // Use ";" as delimiter to execute statement by statement
            scanner.useDelimiter(";");

            while (scanner.hasNext()) {
                String sql = scanner.next().trim();
                if (!sql.isEmpty()) {
                    try {
                        stmt.execute(sql);
                    } catch (SQLException e) {
                        System.err.println("Error executing SQL chunk: " + e.getMessage());
                    }
                }
            }
            scanner.close();
            System.out.println("✓ Database initialized successfully from script.");

        } catch (FileNotFoundException e) {
            System.err.println("Error: Script file not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error connecting to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get a database connection
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, connectionProps);
    }
    
    /**
     * Test database connection
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
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
        }
    }
    
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing result set: " + e.getMessage());
            }
        }
    }

    public String getDatabaseUrl() { return dbUrl; }
    public String getDatabaseUser() { return dbUser; }
}