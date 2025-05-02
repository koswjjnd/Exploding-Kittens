package com.example;

/**
 * This class is used to test various checkstyle rules.
 * It contains examples of both compliant and non-compliant code patterns.
 */
public class CheckstyleTest {
    // Test constant naming - should use uppercase with underscores
    public static final int MAX_LENGTH = 100;
    public static final String DEFAULT_NAME = "test";

    /**
     * Test method naming rule - should use camelCase
     * @param input input parameter
     * @return processed result
     */
    public String testMethodNaming(String input) {
        // Test method length - should not exceed 30 lines
        if (input == null) {
            return "null input";
        }
        
        // Test nesting depth - should not exceed 2 levels
        if (input.length() > MAX_LENGTH) {
            if (input.startsWith("test")) {  // This is the second level of nesting
                return "too long test input";
            }
            return "too long input";
        }
        
        return input.toUpperCase();
    }

    /**
     * Test brace style rule - { should be at the end of line
     * @param number input number
     * @return processed number
     */
    public int testBraceStyle(int number) {  // { at the end of line
        if (number > 0) {
            return number * 2;
        } 
        else {
            return -number;
        }
    }
    // This class demonstrates:
    // 1. Class name uses PascalCase
    // 2. Method name uses camelCase
    // 3. Constants use uppercase with underscores
    // 4. Public methods have Javadoc
    // 5. Braces are at the end of line
    // 6. Uses 4-space indentation
    // 7. Method length does not exceed 30 lines
    // 8. Nesting depth does not exceed 2 levels
}
