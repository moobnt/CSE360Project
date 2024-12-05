package Tuan;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class Test2_2 {
	
	
	//List to stored codes
	private static List<String> generatedCodes = new ArrayList<>();

	

	
	
	 @ParameterizedTest
	 @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
             26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
             51, 52, 53, 54, 55, 56, 57, 58, 59})
    void testGenerateCode(int testNumber) throws Exception {


        // Invoke the method
        String code =  generateCode();

        // Test the output
        assertNotNull(code, "Generated code should not be null.");
        assertEquals(8, code.length(), "Generated code should have exactly 8 characters.");
        assertTrue(code.matches("[A-Za-z0-9]+"), "Generated code should be alphanumeric.");
        
        // Check if the code is already in the list (to avoid duplicates)
        assertFalse(generatedCodes.contains(code), "Generated code should not be a duplicate.");
        
        // Add the code to the list if it not there
        if (!generatedCodes.contains(code))
        	generatedCodes.add(code);
    }
	 
		@Test
	    void testGenerateCodeOnce() throws Exception {

	        String code = generateCode();

	        assertNotNull(code, "Generated code should not be null.");
	        assertEquals(8, code.length(), "Generated code should have exactly 8 characters.");
	        assertTrue(code.matches("[A-Za-z0-9]+"), "Generated code should be alphanumeric.");
	        
	        // Check if the code is already in the list (to avoid duplicates)
	        assertFalse(generatedCodes.contains(code), "Generated code should not be a duplicate.");
	        
	        // Add the code to the list if it not there
	        if (!generatedCodes.contains(code))
	        	generatedCodes.add(code);
	    }
    
    
    
    //use the exact same method as generate code in Admin
    private String generateCode() {
        // Generate a random 8-character alphanumeric code
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int randomIndex = (int) (Math.random() * chars.length());
            code.append(chars.charAt(randomIndex));
        }
        return code.toString();
    }
}
