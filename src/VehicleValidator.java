import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;

public class VehicleValidator {

    public boolean isFieldValid(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public boolean isYearValid(String yearStr) {
        if (!isFieldValid(yearStr)) {
            return false;
        }
        try {
            int year = Integer.parseInt(yearStr.trim());
            int maxYear = Year.now().getValue() + 5; // Get current year and add 5
            return year >= 1900 && year <= maxYear;
        } catch (NumberFormatException e) {
            return false; // Not a valid number
        }
    }

    public boolean isDateFormatValid(String dateStr) {
        if (!isFieldValid(dateStr)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    public boolean isDateRangeValid(String startDateStr, String endDateStr) {
         try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startDateStr.trim());
            Date endDate = sdf.parse(endDateStr.trim());
            return !endDate.before(startDate);
        } catch (ParseException e) {
            return false;
        }
    }
}