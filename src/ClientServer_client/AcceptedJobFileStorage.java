package ClientServer_client;

import java.io.FileWriter;
import java.io.IOException;

public class AcceptedJobFileStorage {

    public static synchronized void appendToFile(String filename, String data) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(data + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("File write error: " + e.getMessage());
        }
    }
}
