package carsharing.backend;

import java.util.regex.Pattern;

/**
 * @author Alex Giazitzis
 */
public class Validator {
    private static final Pattern pattern = Pattern.compile("[0-9]+");

    public static boolean isNumber(String input) {
        return pattern.matcher(input).matches();
    }
}
