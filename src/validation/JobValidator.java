package validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class JobValidator {

    public boolean isFieldValid(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public boolean isDurationValid(String durationStr) {
        if (!isFieldValid(durationStr)) return false;
        try {
            int duration = Integer.parseInt(durationStr.trim());
            return duration > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isDeadlineValid(String deadlineStr) {
        if (!isFieldValid(deadlineStr)) return false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(deadlineStr.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}