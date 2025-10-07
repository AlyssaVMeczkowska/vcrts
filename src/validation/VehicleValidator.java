package validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class VehicleValidator {

    public boolean isFieldValid(String field) {
        return field != null && !field.trim().isEmpty();
    }

    public boolean isYearValid(String yearStr) {
        if (yearStr == null || yearStr.trim().isEmpty()) {
            return false;
        }
        try {
            int year = Integer.parseInt(yearStr.trim());
            int currentYear = Year.now().getValue();
            return year >= 1900 && year <= (currentYear + 5);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isDateInFuture(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateStr);
            
            Calendar todayCal = Calendar.getInstance();
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 0);
            todayCal.set(Calendar.MILLISECOND, 0);
            Date today = todayCal.getTime();

            return !date.before(today);
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean isDateFormatValid(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean isDateRangeValid(String startDateStr, String endDateStr) {
        if (startDateStr == null || startDateStr.trim().isEmpty() ||
            endDateStr == null || endDateStr.trim().isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            return !endDate.before(startDate);
        } catch (ParseException e) {
            return false;
        }
    }
}