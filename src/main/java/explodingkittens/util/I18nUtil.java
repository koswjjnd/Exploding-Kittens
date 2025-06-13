package explodingkittens.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility class for handling internationalization (i18n) in the application.
 * Provides methods to load and access translated messages.
 */
public class I18nUtil {
    private static ResourceBundle messages;
    private static Locale currentLocale;

    // ✅ Add this private constructor
    private I18nUtil() {
        // Prevent instantiation
    }

    /**
     * Initialize the I18nUtil with the default locale (English).
     */
    public static void initialize() {
        setLocale(Locale.ENGLISH);
    }

    /**
     * Set the locale for the application.
     * @param locale The locale to set
     */
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        if (locale.equals(Locale.CHINESE)) {
            messages = ResourceBundle.getBundle("messages", new Locale("zh", "CN"));
        } 
        else {
            messages = ResourceBundle.getBundle("messages", locale);
        }
    }

    /**
     * Get the current locale.
     * @return The current locale
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Get a translated message for the given key.
     * @param key The message key
     * @return The translated message
     */
    public static String getMessage(String key) {
        try {
            return messages.getString(key);
        }
        catch (Exception e) {
            System.out.println("Catch block hit: " + key + " - " + e.getClass().getName());
            return key; // Return the key if translation is not found
        }
    }

    /**
     * Get a translated message with parameters.
     * @param key The message key
     * @param params The parameters to insert into the message
     * @return The translated message with parameters
     */
    public static String getMessage(String key, Object... params) {
        try {
            String message = messages.getString(key);
            return MessageFormat.format(message, params);
        }
        catch (Exception e) {
            System.out.println("Catch block hit: " + key + " - " + e.getClass().getName());
            return key; // Return the key if translation is not found
        }
    }

    /**
     * Switch between English and Chinese locales.
     */
    public static void toggleLanguage() {
        if (currentLocale.equals(Locale.ENGLISH)) {
            setLocale(Locale.CHINESE);
        }
        else {
            setLocale(Locale.ENGLISH);
        }
    }
} 