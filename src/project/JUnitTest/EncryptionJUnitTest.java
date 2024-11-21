package project.JUnitTest;

import project.article.EncryptionUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EncryptionJUnitTest {

    @Test
    void testEncryptAndDecrypt_Normal() throws Exception {
        String originalText = "TestingCase1";
        
        // Encrypt the text
        String encryptedText = EncryptionUtil.encrypt(originalText);
        assertNotNull(encryptedText, "Encrypted text should not be null");
        assertNotEquals(originalText, encryptedText, "Encrypted text should be different from original");
        
        // Decrypt the text
        String decryptedText = EncryptionUtil.decrypt(encryptedText);
        assertEquals(originalText, decryptedText, "Decrypted text should match original text");
    }
    
    @Test
    void testEncrypt() throws Exception {
        String originalText = "Testing";
        String answer = "i8w1xKIDpidYJ1iYcskk7Q==";
        
        // Encrypt the text
        String encryptedText = EncryptionUtil.encrypt(originalText);
        assertNotNull(encryptedText, "Encrypted text should not be null");
        assertNotEquals(originalText, encryptedText, "Encrypted text should be different from original");
        
        // Check the Encrypt
        assertEquals(encryptedText, answer, "Encrypted text should match expected text");
    }
    
    @Test
    void testDencrypt() throws Exception {
        String originalText = "Workaholishitt";
        String answer = "2VNvzL7xnDaDYxpGiXG0gw==";
        
        // Decrypt the text
        String decryptedText = EncryptionUtil.decrypt(answer);
        assertEquals(originalText, decryptedText, "Decrypted text should match original text");
    }

    @Test
    void testEncrypt_EmptyString() throws Exception {
        String originalText = "";
        
        // Encrypt empty string
        String encryptedText = EncryptionUtil.encrypt(originalText);
        assertNotNull(encryptedText, "Encrypted text should not be null");
        
        // Decrypt empty string
        String decryptedText = EncryptionUtil.decrypt(encryptedText);
        assertEquals(originalText, decryptedText, "Decrypted empty string should match original");
    }

    @Test
    void testEncryptAndDecrypt_LongString() throws Exception {
        String originalText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                "nisi ut aliquip ex ea commodo consequat.";
        
        // Encrypt long text
        String encryptedText = EncryptionUtil.encrypt(originalText);
        assertNotNull(encryptedText, "Encrypted text should not be null");
        assertNotEquals(originalText, encryptedText, "Encrypted text should be different from original");
        
        // Decrypt long text
        String decryptedText = EncryptionUtil.decrypt(encryptedText);
        assertEquals(originalText, decryptedText, "Decrypted text should match original text");
    }

    @Test
    void testConsistentEncryption() throws Exception {
        String originalText = "Consistent Encryption Test";
        
        // Encrypt the same text twice
        String firstEncryption = EncryptionUtil.encrypt(originalText);
        String secondEncryption = EncryptionUtil.encrypt(originalText);
        
        // Verify both encryptions are consistent
        assertNotNull(firstEncryption, "First encryption should not be null");
        assertNotNull(secondEncryption, "Second encryption should not be null");
        
        // Note: Due to the static key, encryptions of the same text will be identical
        assertEquals(firstEncryption, secondEncryption, "Multiple encryptions of the same text should be identical");
        
        // Verify decryption works for both
        String decryptedFirst = EncryptionUtil.decrypt(firstEncryption);
        String decryptedSecond = EncryptionUtil.decrypt(secondEncryption);
        assertEquals(originalText, decryptedFirst, "First decryption should match original text");
        assertEquals(originalText, decryptedSecond, "Second decryption should match original text");
    }
}