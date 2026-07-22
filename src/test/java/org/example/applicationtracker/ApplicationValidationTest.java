package org.example.applicationtracker;

import org.example.applicationtracker.model.Application;
import org.example.applicationtracker.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationValidationTest {

    private Application application;
    private User user;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setRole("Role-User");
        user.setEmail("advait@gmail.com");

        application = new Application();
        application.setCompanyName("Boeing");
        application.setJobTitle("Java Developer Intern");
        application.setStatus("Applied");
        application.setUser(user);
    }

    @Test
    @DisplayName("Application should belong to correct user")
    void applicationShouldBelongToUser() {
        User user = new User();
        user.setEmail("advait@gmail.com");

        Application application = new Application();
        application.setUser(user);

        assertEquals(
                "advait@gmail.com",
                application.getUser().getEmail()
        );
    }


    @Test
    @DisplayName("Two applications with different users should not match")
    void twoApplicationWithDifferentUsersShouldNotMatch(){
        User otherUser = new User();
        otherUser.setEmail("other@gmail.com");

        Application otherApp = new Application();
        otherApp.setUser(otherUser);

        assertNotEquals(application.getUser().getEmail(),otherApp.getUser().getEmail());
    }

    @Test
    @DisplayName("Application with no user should have null user")
    void applicationWithNoUserShouldHaveNullUser(){
        Application newApp = new Application();

        assertNull(newApp.getUser());
    }
}
