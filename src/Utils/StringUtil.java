package Utils;

public class StringUtil {
    public static boolean isNullOrEmpty(String string) {
        return (string == null || string.trim().equals("")) ? true : false;
    }
}
