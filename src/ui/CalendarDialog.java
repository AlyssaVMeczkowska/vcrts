package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.*;

public class CalendarDialog extends JDialog {
    private static final Color COLOR_PRIMARY = new Color(44, 116, 132);
    private static final Color COLOR_PRIMARY_LIGHT = new Color(230, 240, 242);
    private static final Color COLOR_SECONDARY = new Color(100, 100, 100);
    private static final Color COLOR_BACKGROUND = Color.WHITE;
    private static final Color COLOR_HEADER = new Color(245, 245, 245);

    private Date selectedDate;
    private Calendar currentMonthCalendar;
    private JLabel monthYearLabel;
    private JPanel daysPanel;
    private boolean restrictToFuture;
    private Date minSelectableDate;

    public CalendarDialog(Frame owner, boolean restrictToFuture, Date minSelectableDate) {
        super(owner, "Select Date", true);
        this.restrictToFuture = restrictToFuture;
        this.minSelectableDate = (minSelectableDate != null) ? resetTime(minSelectableDate) : null;
        
        if (restrictToFuture) {
            Date today = resetTime(new Date());
            if (this.minSelectableDate == null || this.minSelectableDate.before(today)) {
                this.minSelectableDate = today;
            }
        }

        currentMonthCalendar = Calendar.getInstance();
        currentMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        initComponents();
        updateCalendar();
        pack();
        setResizable(false);
    }
    
    private Date resetTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private void initComponents() {
        getContentPane().setBackground(COLOR_BACKGROUND);
        setLayout(new BorderLayout(5, 5));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Month and Year Navigation Panel
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(COLOR_BACKGROUND);
        JButton prevMonthButton = createNavButton("<");
        prevMonthButton.addActionListener(e -> navigateMonth(-1));
        
        JButton nextMonthButton = createNavButton(">");
        nextMonthButton.addActionListener(e -> navigateMonth(1));

        
        monthYearLabel = new JLabel("", SwingConstants.CENTER);
        monthYearLabel.setFont(new Font("Arial", Font.BOLD, 16));
        monthYearLabel.setForeground(COLOR_PRIMARY);

        navPanel.add(prevMonthButton, BorderLayout.WEST);
        navPanel.add(monthYearLabel, BorderLayout.CENTER);
        navPanel.add(nextMonthButton, BorderLayout.EAST);
        add(navPanel, BorderLayout.NORTH);

        // Main content panel (headers + days)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 2));
        centerPanel.setBackground(COLOR_BACKGROUND);
        
        // Weekday Headers
        JPanel headerPanel = new JPanel(new GridLayout(1, 7));
        headerPanel.setBackground(COLOR_HEADER);
        String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : weekdays) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setForeground(COLOR_SECONDARY);
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            headerPanel.add(label);
        }
        centerPanel.add(headerPanel, BorderLayout.NORTH);

        // Days Panel
        daysPanel = new JPanel(new GridLayout(0, 7, 5, 5)); // Add gaps between buttons
        daysPanel.setBackground(COLOR_BACKGROUND);
        daysPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        centerPanel.add(daysPanel, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(COLOR_PRIMARY);
        return button;
    }

    private void navigateMonth(int amount) {
        currentMonthCalendar.add(Calendar.MONTH, amount);
        updateCalendar();
    }

    private void updateCalendar() {
        daysPanel.removeAll();

        monthYearLabel.setText(new SimpleDateFormat("MMMM yyyy", Locale.US).format(currentMonthCalendar.getTime()));

        Calendar monthCal = (Calendar) currentMonthCalendar.clone();
        int firstDayOfWeek = monthCal.get(Calendar.DAY_OF_WEEK);

        for (int i = 1; i < firstDayOfWeek; i++) {
            daysPanel.add(new JLabel(""));
        }

        int numDaysInMonth = monthCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date today = resetTime(new Date());

        for (int i = 1; i <= numDaysInMonth; i++) {
            monthCal.set(Calendar.DAY_OF_MONTH, i);
            Date date = resetTime(monthCal.getTime());
            
            JButton dayButton = new JButton(String.valueOf(i));
            dayButton.setFont(new Font("Arial", Font.PLAIN, 12));
            dayButton.setFocusPainted(false);
            dayButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            dayButton.setBackground(COLOR_BACKGROUND);
            dayButton.setForeground(Color.BLACK);
            dayButton.setOpaque(true);

            boolean isDisabled = (minSelectableDate != null && date.before(minSelectableDate));

            if (isDisabled) {
                dayButton.setEnabled(false);
                dayButton.setForeground(Color.LIGHT_GRAY);
                dayButton.setBackground(new Color(250, 250, 250));
            } else {
                dayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                dayButton.addActionListener(e -> {
                    selectedDate = date;
                    dispose();
                });
                // --- Add hover effect ---
                dayButton.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent evt) {
                       dayButton.setBackground(COLOR_PRIMARY_LIGHT);
                    }
                    public void mouseExited(MouseEvent evt) {
                       dayButton.setBackground(COLOR_BACKGROUND);
                    }
                });
            }
            
            // --- Style for "today's date" ---
            if (date.equals(today)) {
                dayButton.setBackground(COLOR_PRIMARY_LIGHT);
                dayButton.setForeground(COLOR_PRIMARY);
                dayButton.setFont(new Font("Arial", Font.BOLD, 12));
            }

            daysPanel.add(dayButton);
        }

        revalidate();
        repaint();
    }

    public Date getSelectedDate() {
        return selectedDate;
    }
}