package project.automatedTesting;

import project.article.EncryptionUtil;

public class adminEncryptionAutoTesting {
    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nEncryption Utility Automated Testing");

        try {
            // Test Case 1: Basic Encryption and Decryption
            performEncryptionDecryptionTest();


            // Test Case 2: Encryption
            performEcryptionTest();
            
            // Test Case 3: Decryption
            performDecryptionTest();
            
            // Test Case 4: Empty String Handling
            performEmptyStringTest();

            // Test Case 5: Long String Encryption
            performLongStringEncryptionTest();

        } catch (Exception e) {
            System.err.println("Critical test failure: " + e.getMessage());
            numFailed++;
        }

        // Final Test Summary
        System.out.println("____________________________________________________________________________");
        System.out.println("\nTest Summary:");
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    private static void performEncryptionDecryptionTest() {
        System.out.println("\nTest Case 1: Basic Encryption and Decryption");
        try {
            String originalText = "Hello, World!";
            String encryptedText = EncryptionUtil.encrypt(originalText);
            String decryptedText = EncryptionUtil.decrypt(encryptedText);

            if (originalText.equals(decryptedText)) {
                System.out.println("***Success*** Basic encryption and decryption test passed.");
                numPassed++;
            } else {
                System.out.println("***Failure*** Basic encryption and decryption test failed.");
                numFailed++;
            }
        } catch (Exception e) {
            System.err.println("Basic encryption test error: " + e.getMessage());
            numFailed++;
        }
    }
    
    private static void performEcryptionTest() {
        System.out.println("\nTest Case 2: Basic Encryption");
        try {
            String originalText = "Testing";
            String answer = "i8w1xKIDpidYJ1iYcskk7Q==";
            String encryptedText = EncryptionUtil.encrypt(originalText);
            String decryptedText = EncryptionUtil.decrypt(answer);

            if (encryptedText.equals(answer)) {
                System.out.println("***Success*** Basic encryption test passed.");
                numPassed++;
            } else {
                System.out.println("***Failure*** Basic encryption test failed.");
                numFailed++;
            }
        } catch (Exception e) {
            System.err.println("Basic encryption test error: " + e.getMessage());
            numFailed++;
        }
    }
    
    //2VNvzL7xnDaDYxpGiXG0gw== for string "Workaholishitt"
    private static void performDecryptionTest() {
        System.out.println("\nTest Case 3: Basic Encryption");
        try {
            String originalText = "Workaholishitt";
            String answer = "2VNvzL7xnDaDYxpGiXG0gw==";
            String decryptedText = EncryptionUtil.decrypt(answer);

            if (decryptedText.equals(originalText)) {
                System.out.println("***Success*** Basic encryption test passed.");
                numPassed++;
            } else {
                System.out.println("***Failure*** Basic encryption test failed.");
                numFailed++;
            }
        } catch (Exception e) {
            System.err.println("Basic decryption test error: " + e.getMessage());
            numFailed++;
        }
    }
    
    private static void performEmptyStringTest() {
        System.out.println("\nTest Case 4: Empty String Encryption");
        try {
            String originalText = "";
            String encryptedText = EncryptionUtil.encrypt(originalText);
            String decryptedText = EncryptionUtil.decrypt(encryptedText);

            if (originalText.equals(decryptedText)) {
                System.out.println("***Success*** Empty string encryption test passed.");
                numPassed++;
            } else {
                System.out.println("***Failure*** Empty string encryption test failed.");
                numFailed++;
            }
        } catch (Exception e) {
            System.err.println("Empty string encryption test error: " + e.getMessage());
            numFailed++;
        }
    }

    private static void performLongStringEncryptionTest() {
        System.out.println("\nTest Case 5: Long String Encryption");
        try {
            StringBuilder longText = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longText.append("Lambda Calculus is hard");
            }

            String originalText = longText.toString();
            String encryptedText = EncryptionUtil.encrypt(originalText);
            String decryptedText = EncryptionUtil.decrypt(encryptedText);

            if (originalText.equals(decryptedText)) {
                System.out.println("***Success*** Long string encryption test passed.");
                numPassed++;
            } else {
                System.out.println("***Failure*** Long string encryption test failed.");
                numFailed++;
            }
        } catch (Exception e) {
            System.err.println("Long string encryption test error: " + e.getMessage());
            numFailed++;
        }
    }
}