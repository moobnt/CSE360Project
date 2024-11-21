package project.JUnitTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.time.*;
import project.account.DatabaseModel;

//registerUser for the Roles
class DatabaseModelJUnitTest {
    private static DatabaseModel databaseModel;

    @BeforeAll
    static void setUp() {
        // Initialize the DatabaseModel before running tests
        databaseModel = new DatabaseModel();
        try {
            databaseModel.connect();
            // Reset tables to ensure a clean state for testing
        } catch (SQLException e) {
            fail("Failed to set up database connection: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDown() {
        // Disconnect after all tests
        databaseModel.disconnect();
    }

//    @Test
//    void testRegisterUserUnique() {
//        // Test registering a new unique user
//        String username = "test1";
//        String password = "password123";
//        String email = "test1@example.com";
//        Object[] roles = {"Student"};
//        
//        assertFalse(databaseModel.doesUserExist(username), "User should not exist before registration");
//        
//        databaseModel.registerUser(
//            username, 
//            password, 
//            email, 
//            roles, 
//            false, 
//            OffsetDateTime.now(), 
//            new String[]{"Test1"}
//        );
//        
//        assertTrue(databaseModel.doesUserExist(username), "User should exist after registration");
//    }

    @Test
    void testUserExists() {
        // Test attempting to register a user with an existing username
        String username = "thaituan";
        
        
        // Capture system output to check for duplicate user message
        // Note: In a real-world scenario, you might want to use a more robust logging mechanism
        assertTrue(databaseModel.doesUserExist(username), "The Test User should exists");
    }
    
    void testNonExistentUser() {
        // Test attempting to register a user with an existing username
        String username = "definitelyNOT";
        
        
        // Capture system output to check for duplicate user message
        // Note: In a real-world scenario, you might want to use a more robust logging mechanism
        assertFalse(databaseModel.doesUserExist(username), "The Test User shouldn't exists");
    }

    @Test
    void testUserRoles() {
        // Test adding and retrieving user roles
        String username = "thaituan";
        String[] initialRoles = {"Admin", "Instructor"};
        
        
        // Verify initial roles
        String[] retrievedRoles = databaseModel.getUserRoles(username);
        assertArrayEquals(initialRoles, retrievedRoles, "Retrieved roles should match initial roles");
        
    }

    @Test
    void testAdminRoleCheck() {
        // Test admin role verification
        String adminUsername = "thaituan";
        String regularUsername = "10";
        
        // Verify admin role
        assertTrue(databaseModel.isUserAdmin(adminUsername), "Admin user should have Admin role");
        assertFalse(databaseModel.isUserAdmin(regularUsername), "Regular user should not have Admin role");
    }
    
    @Test
    public void testOneTimeCodeValidation() {
        assertFalse(databaseModel.validateOneTimeCode("testUser", "abcdabcde"), "One-time code validation failed for 'testUser'.");
    }


//    @Test
//    void testUserRemoval() {
//        // Test user removal
//        String username = "test1";
//        
//        // Register user
//        databaseModel.registerUser(
//            username, 
//            "password", 
//            "remove@test.com", 
//            new Object[]{"User"}, 
//            false, 
//            OffsetDateTime.now(), 
//            new String[]{"Test5"}
//        );
//        
//        // Verify user exists
//        assertTrue(databaseModel.doesUserExist(username), "User should exist before removal");
//        
//        // Remove user
//        databaseModel.removeUser(username);
//        
//        // Verify user is removed
//        assertFalse(databaseModel.doesUserExist(username), "User should not exist after removal");
//    }
}