package validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JobValidator {
    
    // Check if the duration is a valid positive number
    public boolean isDurationValid(String duration) {
        if (duration == null || duration.trim().isEmpty()) {
            return false;
        }
        try {
            int dur = Integer.parseInt(duration.trim());
            return dur > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check if the deadline date is not in the past
    public boolean isDeadlineInFuture(String deadline) {
        if (deadline == null || deadline.trim().isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // Ensure strict parsing
        try {
            Date deadlineDate = sdf.parse(deadline);
            Date today = new Date();
            // Compare only date part, ignore time
            SimpleDateFormat dateOnlySdf = new SimpleDateFormat("yyyy-MM-dd");
            Date todayDateOnly = dateOnlySdf.parse(dateOnlySdf.format(today));
            
            // Deadline must be today or in the future
            return !deadlineDate.before(todayDateOnly);
        } catch (ParseException e) {
            return false; // Should ideally not happen with calendar picker
        }
    }

    // Old Description Validator
    /*
    public boolean isFieldValid(String field) {
        return field != null && !field.trim().isEmpty();
    }
    */
}