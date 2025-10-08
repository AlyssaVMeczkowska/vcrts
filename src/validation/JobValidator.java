package validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JobValidator {
    
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

    public boolean isDeadlineInFuture(String deadline) {
        if (deadline == null || deadline.trim().isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            Date deadlineDate = sdf.parse(deadline);
            Date today = new Date();
            SimpleDateFormat dateOnlySdf = new SimpleDateFormat("yyyy-MM-dd");
            Date todayDateOnly = dateOnlySdf.parse(dateOnlySdf.format(today));
            
            return !deadlineDate.before(todayDateOnly);
        } catch (ParseException e) {
            return false; 
        }
    }

}