package project.group;

import java.sql.*;
import java.util.*;

import project.account.DatabaseHelper;

/**
 * <p> GroupDatabase Class </p>
 * 
 * <p> Description: Class to manage database operations for groups </p>
 * 
 * @version 1.00 2024-11-13 Initial baseline
 */
//Automated test required
public class SpecialGroupDatabase {
    protected Connection connection;
    private final String DEFAULT_GROUP_NAME = "unprotected";
    private final String ARRAY_SEPERATOR = ", "; // item seperator in the SQL tables

    /**
     * Default constructor; connects to the databse and creates tables
     * @throws SQLException 
     */
    public SpecialGroupDatabase() {
        try {
            connect();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        try {
            createTable();
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public void connect() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DatabaseHelper.connectToDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sets up the table initially and makes it up-to-date
     * 
     * @throws SQLException
     */
    public void createTable() throws SQLException {
        //Statement stmt = connection.createStatement();

        String groupTable = "CREATE TABLE IF NOT EXISTS special_group ("
            + "id INT AUTO_INCREMENT PRIMARY KEY, "
            + "title VARCHAR(255) UNIQUE NOT NULL, "
            + "admin VARCHAR(255), "
            + "instructors VARCHAR(255), "
            + "students VARCHAR(65535), "
            + "articles VARCHAR(255))";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(groupTable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // creates a default group that will not encrypt any articles
        // anyone can access this group, but because it is so simple
        // it does not have an admin
        //
        // Every instructor and student should be in this group

        if (!doesGroupExist(DEFAULT_GROUP_NAME)) {
            String defaultGroup = "INSERT INTO special_group (title, admin, instructors, students, articles) "
                + "VALUES (?, NULL, NULL, NULL, NULL)";

            try (PreparedStatement pstmt = connection.prepareStatement(defaultGroup)) {
                pstmt.setString(1, DEFAULT_GROUP_NAME);

                pstmt.executeUpdate();
            }

        } else {
            System.err.println("Default group exists");
        }
        
        // finds all instructors / students in the database
        // String findAll = "SELECT username FROM users WHERE roles LIKE ?";

        // String allInstructors = null;
        // String allStudents = null;

        // try (PreparedStatement pstmt = connection.prepareStatement(findAll)) {
        //     pstmt.setString(1, "Instructor");

        //     ResultSet instructorUsernames = pstmt.executeQuery();

        //     if (instructorUsernames != null) {
        //         do {
        //             allInstructors = addItemToString(allInstructors, 
        //                                 instructorUsernames.getString("username"));
        //         } while (instructorUsernames.next());
        //     } else {
        //         System.err.println("No instructors found");
        //     }
        // }

        // try (PreparedStatement pstmt = connection.prepareStatement(findAll)) {
        //     pstmt.setString(1, "Student");

        //     ResultSet studentUsernames = pstmt.executeQuery();

        //     if (studentUsernames != null) {
        //         do {
        //             allStudents = addItemToString(allStudents, 
        //                             studentUsernames.getString("username"));
        //         } while (studentUsernames.next());
        //     } else {
        //         System.err.println("No students found");
        //     }
        // }

        // adds every currently existing person to the group
        // if they aren't already in it
        // String addAll = "UPDATE special_group SET instructors = ?, students = ? WHERE title = ?";

        // try (PreparedStatement pstmt = connection.prepareStatement(addAll)) {
        //     pstmt.setString(1, allInstructors);
        //     pstmt.setString(2, allStudents);
        //     pstmt.setString(3, DEFAULT_GROUP_NAME);

        //     pstmt.executeUpdate();
        // }
    }

    /**
     * Method that creates a new group
     * Should only be called by admin
     * 
     * @param title Unique title of the group
     * @param admin Instructor(s) to make admin
     * @param instructors Instructors with access to the group
     * @param students Students with access to the group
     * @param articles Articles to add to the group by ID
     */
    public void createGroup(String title, String admin, String instructors, String students, String articles) {
        String createGroup = "INSERT INTO special_group (title, admin, instructors, students, articles) "
                + "VALUES (?, ?, ?, ?, ?) ";

        try (PreparedStatement pstmt = connection.prepareStatement(createGroup)) {
            pstmt.setString(1, title);
            pstmt.setString(2, admin);
            pstmt.setString(3, instructors);
            pstmt.setString(4, students);
            pstmt.setString(5, articles);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Table already exists");
        }
    }

    /**
     * Method that deletes a group
     * Should only be called by admin
     * 
     * @param title Unique title of the group
     */
    public void deleteGroup(String title) {
        String deleteGroup = "DELETE FROM special_group WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteGroup)) {
            pstmt.setString(1, title);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Adds a person to a special group
     * 
     * @param title The title of the group
     * @param role The role of the person to be added
     * @param person The username of the person to be added
     */
    public void addPerson(String title, String role, String person) {
        String roleListString = null;
        String getPeople = "SELECT ? FROM special_group WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(getPeople)) {
            pstmt.setString(1, role);
            pstmt.setString(2, title);

            ResultSet resultSet = pstmt.executeQuery();

            roleListString = addItemToString(resultSet.getString(role), person);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        String addPerson = "UPDATE special_group SET ? = ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(addPerson)) {
            pstmt.setString(1, roleListString);
            pstmt.setString(2, title);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Removes a person from a specific group
     * 
     * @param title Unique title of the group
     * @param role The role of the person to be removed
     * @param person Person to remove from the group
     */
    public void removePerson(String title, String role, String person) {
        String roleListString = null;
        String getPeople = "SELECT ? FROM special_group WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(getPeople)) {
            pstmt.setString(1, role);
            pstmt.setString(2, title);

            ResultSet studentResultSet = pstmt.executeQuery();

            roleListString = removeItemFromString(studentResultSet.getString("students"), person);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        String removeStudent = "UPDATE special_group SET students = ? WHERE title = ?"; 

        try (PreparedStatement pstmt = connection.prepareStatement(removeStudent)) {
            pstmt.setString(1, roleListString);
            pstmt.setString(2, title); 

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Checks to see what groups someone is in, if any
     * They should at least be in the general group
     * 
     * @param person The person to find in groups
     */
    public String[] findGroups(String person, Object[] roles) {
        String foundGroups = DEFAULT_GROUP_NAME;

        // loop through all groups
        // TODO: fix
        for (int ii = 0; ii < numberOfGroups(); ii++) {
            String findGroup = "SELECT ? FROM special_group WHERE title LIKE 1=1";
        
            // check for instructors if user who called function is an instructor
            if (Arrays.asList(roles).contains("Instructor")) {
                try (PreparedStatement pstmt = connection.prepareStatement(findGroup)) {
                    pstmt.setString(1, "instructors");
        
                    ResultSet foundInstructors = pstmt.executeQuery();

                    if (foundInstructors != null) {
                        addItemToString(foundGroups, String.valueOf(ii));
                    }

                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            } 

            // check for students if user who called function is a student
            if (Arrays.asList(roles).contains("Student")) {
                try (PreparedStatement pstmt = connection.prepareStatement(findGroup)) {
                    pstmt.setString(1, "students");
        
                    ResultSet foundStudents = pstmt.executeQuery();

                    if (foundStudents != null) {
                        addItemToString(foundGroups, String.valueOf(ii));
                    }

                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        List<String> listWithDuplicates = Arrays.asList(foundGroups.split(ARRAY_SEPERATOR));
        List<String> groupIDList = new ArrayList<>();
        List<String> groupList = new ArrayList<>();

        for (int ii = 0; ii < listWithDuplicates.size(); ii++) {
            groupIDList.add(listWithDuplicates.get(ii));
        }
        
        for (int ii = 0; ii < groupIDList.size(); ii++) {
            String findGroupName = "SELECT ? FROM special_group WHERE id = ?";

            try (PreparedStatement pstmt2 = connection.prepareStatement(findGroupName)) {
                pstmt2.setString(1, "title");
                pstmt2.setString(2, String.valueOf(ii));

                try(ResultSet foundGroupTitle = pstmt2.executeQuery()) {
                    while(foundGroupTitle.next()) {
                        groupList.add(foundGroupTitle.getString("title"));
                    }
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        
        
        return groupList.toArray(new String[0]);
    }

    /**
     * Converts a string into a list, adds an item, and changes it back to a string
     * 
     * @param initString The initial string that is passed in
     * @param item The item to be added
     * @return String with item added to the end
     */
    private String addItemToString(String initString, String item) {
        List<String> itemArray = new ArrayList<>();

        itemArray = Arrays.asList(initString.split(ARRAY_SEPERATOR));
        itemArray.add(item);

        return String.join(ARRAY_SEPERATOR, itemArray);
    }

    /**
     * Converts a string into a list, removes the item, and changes it back to a string
     * 
     * @param initString The initial string that is passed in
     * @param item The item to be removed
     * @return String with item removed
     */
    private String removeItemFromString(String initString, String item) {
        List<String> itemArray = new ArrayList<>();

        itemArray = Arrays.asList(initString.split(ARRAY_SEPERATOR));
        itemArray.remove(item);

        return String.join(ARRAY_SEPERATOR, itemArray);
    }

    private boolean doesGroupExist(String title) {
        String query = "SELECT COUNT(*) FROM special_group WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, title);
            try (ResultSet rs = pstmt.executeQuery()) {
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println(e.getSQLState());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns the number of groups (rows) that are in the table
     * @return
     */
    private int numberOfGroups() {
        return 0;
    }
}
