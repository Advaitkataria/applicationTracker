package org.example.applicationtracker.service;

import org.example.applicationtracker.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.example.applicationtracker.model.Application;
import org.example.applicationtracker.model.User;
import org.example.applicationtracker.repository.ApplicationRepository;
import org.example.applicationtracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ApplicationService applicationService;

    private User testUser;
    private User otherUser;
    private Application testApplication;
    private Application otherTestApplication;

    @BeforeEach
    void setup(){
        testUser = new User();
        testUser.setEmail("advait@gmail.com");
        testUser.setRole("ROLE_USER");

        otherUser = new User();
        otherUser.setEmail("other@gmail.com");
        otherUser.setRole("ROLE_USER");

        testApplication = new Application();
        testApplication.setId(1);
        testApplication.setCompanyName("Boeing");
        testApplication.setJobTitle("Java Developer Intern");
        testApplication.setStatus("Applied");
        testApplication.setUser(testUser);

        otherTestApplication = new Application();
        otherTestApplication.setId(99);
        otherTestApplication.setCompanyName("IBM");
        otherTestApplication.setJobTitle("Software Intern");
        otherTestApplication.setStatus("Applied");
        otherTestApplication.setUser(otherUser);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("advait@gmail.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("advait@gmail.com")).thenReturn(Optional.of(testUser));


    }

    @Nested
    @DisplayName("getAllApplications tests")
    class GetAllApplicationsTests {

        @Test
        @DisplayName("Should return empty list when user has no applications")
        void shouldReturnEmptyListWhenUserHasNoApplications() {
            when(applicationRepository.findByUserEmail("advait@gmail.com")).thenReturn(List.of());

            List<Application> result = applicationService.getAllApplications();

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("should return only current user applications")
        void shouldReturnOnlyCurrentUserApplications(){
            when(applicationRepository.findByUserEmail("advait@gmail.com")).thenReturn(List.of(testApplication));

            List<Application> result = applicationService.getAllApplications();

            assertEquals(1,result.size());
            assertEquals("Boeing",result.get(0).getCompanyName());
            assertEquals("advait@gmail.com",result.get(0).getUser().getEmail());
        }
    }

    @Nested
    @DisplayName("deleteApplications tests")
    class DeleteApplicationsTests{

        @Test
        @DisplayName("Should throw when not owner")
        void shouldThrowWhenNotOwner(){
            when(applicationRepository.findById(99)).thenReturn(Optional.of(otherTestApplication));

            assertThrows(UnauthorizedAccessException.class,()->applicationService.deleteApplication(99));

        }

        @Test
        @DisplayName("Should not delete when not owner")
        void shouldNotDeleteWhenNotOwner (){
            when(applicationRepository.findById(99)).thenReturn(Optional.of(otherTestApplication));

            try{
                applicationService.deleteApplication(99);
            } catch(UnauthorizedAccessException e){

            }

            verify(applicationRepository,never()).deleteById(anyInt());
        }
    }

    @Nested
    @DisplayName("updateApplications tests")
    class UpdateApplicationsTests{

        @Test
        @DisplayName("should throw when not owner")
        void shouldThrowWhenNotOwner(){
            when(applicationRepository.findById(99)).thenReturn(Optional.of(otherTestApplication));

            Application updatedData = new Application();
            updatedData.setCompanyName("Hacked Company");

            assertThrows(UnauthorizedAccessException.class,()-> applicationService.updateApplication(99,updatedData));

        }
    }


}
