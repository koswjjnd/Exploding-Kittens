package explodingkittens.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class I18nUtilTest {

    @BeforeEach
    void setUp() {
        // Reset to default state before each test
        I18nUtil.initialize();
    }

    @Test
    void testInitialize() {
        I18nUtil.initialize();
        assertEquals(Locale.ENGLISH, I18nUtil.getCurrentLocale());
    }

    @Test
    void testSetLocale() {
        // Test setting English locale
        I18nUtil.setLocale(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, I18nUtil.getCurrentLocale());

        // Test setting Chinese locale (use zh_CN)
        I18nUtil.setLocale(Locale.CHINESE);
        assertEquals(Locale.CHINESE, I18nUtil.getCurrentLocale());

        // Test setting other locale
        Locale french = new Locale("fr");
        I18nUtil.setLocale(french);
        assertEquals(french, I18nUtil.getCurrentLocale());
    }

    @Test
    void testGetCurrentLocale() {
        assertEquals(Locale.ENGLISH, I18nUtil.getCurrentLocale());

        I18nUtil.setLocale(Locale.CHINESE);
        assertEquals(Locale.CHINESE, I18nUtil.getCurrentLocale());
    }

    @Test
    void testGetMessage() {
        // Test getting message with existing key
        String message = I18nUtil.getMessage("test.key");
        assertNotNull(message);

        // Test getting message with non-existing key
        String nonExistingKey = "non.existing.key";
        assertEquals(nonExistingKey, I18nUtil.getMessage(nonExistingKey));
    }

    @Test
    void testGetMessageWithParams() {
        // Test getting message with parameters
        String message = I18nUtil.getMessage("test.key.with.params", "param1", "param2");
        assertNotNull(message);

        // Test getting message with non-existing key and parameters
        String nonExistingKey = "non.existing.key";
        assertEquals(nonExistingKey, I18nUtil.getMessage(nonExistingKey, "param1", "param2"));
    }

    @Test
    void testGetMessageCatchBlock() {
        I18nUtil.initialize(); // 确保 messages 已初始化
        String nonExistingKey = "this.key.does.not.exist";
        assertEquals(nonExistingKey, I18nUtil.getMessage(nonExistingKey));
    }

    @Test
    void testGetMessageWithParamsCatchBlock() {
        I18nUtil.initialize(); // 确保 messages 已初始化
        String nonExistingKey = "this.key.does.not.exist";
        assertEquals(nonExistingKey, I18nUtil.getMessage(nonExistingKey, "foo", "bar"));
    }

    @Test
    void testToggleLanguage() {
        // Initial state should be English
        assertEquals(Locale.ENGLISH, I18nUtil.getCurrentLocale());

        // Toggle to Chinese
        I18nUtil.toggleLanguage();
        assertEquals(Locale.CHINESE, I18nUtil.getCurrentLocale());

        // Toggle back to English
        I18nUtil.toggleLanguage();
        assertEquals(Locale.ENGLISH, I18nUtil.getCurrentLocale());
    }

    @Test
    void testMessageFormatting() {
        // Test message formatting with multiple parameters
        String message = I18nUtil.getMessage("test.format", "John", 25);
        assertNotNull(message);

        // Test message formatting with null parameters
        String messageWithNull = I18nUtil.getMessage("test.format", null, null);
        assertNotNull(messageWithNull);
    }

	
	
}