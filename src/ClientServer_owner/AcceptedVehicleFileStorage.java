package ClientServer_owner;
import java.io.FileWriter;
import java.io.IOException;


//Stores Accepted Vehicle information
public class AcceptedVehicleFileStorage
{

    public static synchronized void appendToFile(String filename, String data)
    {
        try (FileWriter writer = new FileWriter(filename, true))
        {
            writer.write(data + System.lineSeparator());
        }
        catch (IOException e)
        {
            System.err.println("File write error: " + e.getMessage());
        }
    }
}
