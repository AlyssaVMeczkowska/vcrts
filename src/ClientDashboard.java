import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;



public class ClientDashboard extends JFrame
{
    private JTextField durationField;
    private JTextField deadlineField;
    private JTable jobsTable;
    private DefaultTableModel tableModel;
    private int jobCounter = 1;

    public ClientDashboard()
    {
        //"Client Dashboard" Label Format
        setTitle("Client Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        //"Submit A Job" Label Formatting
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        TitledBorder border = BorderFactory.createTitledBorder("Submit A Job");
        border.setTitleFont(new Font("Georgia", Font.BOLD, 18));
        formPanel.setBorder(border);

        //"Duration" Label Formatting
        JLabel durationLabel = new JLabel("Duration (hrs):");
        durationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(durationLabel);

        durationField = new JTextField();
        durationField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(durationField);

        //"Deadline" Label Formatting
        JLabel deadlineLabel = new JLabel("Deadline (yyyy-mm-dd):");
        deadlineLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(deadlineLabel);

        deadlineField = new JTextField();
        deadlineField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(deadlineField);

        //"Submit" Label Formatting
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(submitButton);

        //Job Table Formatting
        jobsTable = new JTable(tableModel);
        jobsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        jobsTable.setRowHeight(22);
        jobsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        getContentPane().add(formPanel, BorderLayout.NORTH);

        String[] cols = {"Job ID", "Duration", "Deadline", "Timestamp"};
        tableModel = new DefaultTableModel(cols, 0);
        jobsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(jobsTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        submitButton.addActionListener(e -> submitJob());
    }

    private void submitJob()
    {
        String duration = durationField.getText().trim();
        String deadline = deadlineField.getText().trim();

        //Field format enforcement
        if (duration.isEmpty() || deadline.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "All fields required!");
            return;
        }

        int durationInt;
        try
        {
            durationInt = Integer.parseInt(duration);
            if (durationInt <= 0)
            {
                JOptionPane.showMessageDialog(this, "Duration must be an hour or more!");
                return;
            }
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Duration must be a valid number!");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try
        {
            sdf.parse(deadline);
        }
        catch (ParseException ex)
        {
            JOptionPane.showMessageDialog(this, "Deadline must be in yyyy-mm-dd format!");
            return;
        }

        //Write client data to txt file
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Object[] row = {jobCounter++, durationInt, deadline, timestamp};
        tableModel.addRow(row);

        try (FileWriter writer = new FileWriter("clients_data.txt", true))
        {
            writer.write(String.join(" | ", row[0].toString(), String.valueOf(durationInt), deadline, timestamp) + "\n");
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(this, "Error writing to file!");
        }

        durationField.setText("");
        deadlineField.setText("");
    }
}