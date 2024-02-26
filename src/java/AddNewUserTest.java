package com.hotel.booking.MockMVC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.booking.Controller.Controller;
import com.hotel.booking.Model.Location;
import com.hotel.booking.Model.User;
import com.hotel.booking.Pojo.UserPojo;
import com.hotel.booking.Service.CustomerInfoService;
import com.hotel.booking.Service.PasswordResetTokenServiceImpl;
import com.hotel.booking.Service.ServiceInterface;
import com.hotel.booking.Service.UserService;
import com.hotel.booking.Service.jwtService;
import com.hotel.booking.Repo.HotelRepo;
import com.hotel.booking.Repo.LocationRepo;
import com.hotel.booking.Repo.RoomRepo;
import com.hotel.booking.Repo.UserRepo;

@WebMvcTest(Controller.class)
@ExtendWith(MockitoExtension.class)
//@WithMockUser(username = "testUser", roles = {"USER", "ADMIN"})
public class AddNewUserTest {
	
    String token;
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CustomerInfoService userService;
    
    @MockBean
    private ServiceInterface service;
    
    @MockBean
    private PasswordResetTokenServiceImpl passwordresetTokenservice;

    @MockBean
    private UserRepo userRepo;
    
    @MockBean
    private LocationRepo locationRepo;
    
    @MockBean
    private HotelRepo hotelrepo;
    
    @MockBean
    private RoomRepo roomRepo;

    @Autowired
    private WebApplicationContext context;
    
    @MockBean
    private AuthenticationManager authenticationManager;
    
    @InjectMocks
    private jwtService jwtservice;
    
    @MockBean
    private UserService userservice;
    
    @MockBean
    private Authentication authentication; 
    
    @MockBean
    private UserDetails userDetails;
    
    @Autowired
   	private PasswordEncoder encoder;
    
    

  

    @BeforeEach
   public void setup() {
       this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
       .apply(SecurityMockMvcConfigurers.springSecurity())
       .build();

   }
      
	  @Test 
	  public void testWelcome() throws Exception { 
		  mockMvc
		        .perform(MockMvcRequestBuilders.get("/api/hotelbooking/welcome")) 
				.andExpect(status().isOk())
	            .andExpect(content().string("Welcome this endpoint is not secure"));
	
	  }
	 
    @Test
    public void testAddNewUser() throws Exception {
        User user = new User();
        user.setUserName("John Doe");
        user.setEmailId("john.doe@example.com");
        user.setAddress("chennai");
        user.setPassword("Password@123");
        user.setAadharNumber(111122223333L);
        user.setContactNumber("9047262272");
        user.setRoles("ROLE_USER");
        user.setConfirmPassword("Password@123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setGender("Male");
        

        String expectedResponse = "User Added Successfully";

        lenient().when(userService.addUser(user)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/hotelbooking/addNewUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(userService, times(0)).addUser(user);
    }
    
    @Test
    public void testAddNewUser_InvalidDetails() throws Exception {
    	User user = new User();
        user.setUserName("JohnDoe123");
        user.setEmailId("john.doe@example.com");
        user.setAddress("chennai");
        user.setPassword("Password@123");
        user.setAadharNumber(111122223333l);
        user.setContactNumber("9047262272");
        user.setRoles("ROLE_USER");
        user.setConfirmPassword("Password@123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setGender("Male");
        
        String expectedResponse = "Invalid Credentials";

        lenient().when(userService.addUser(user)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/hotelbooking/addNewUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
        
        verify(userService, times(0)).addUser(user);


    }
    
}