package com.hotel.booking.MockMVC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.booking.Controller.Controller;
import com.hotel.booking.Model.Discount;
import com.hotel.booking.Model.Location;
import com.hotel.booking.Model.User;
import com.hotel.booking.Pojo.UserPojo;
import com.hotel.booking.Repo.HotelRepo;
import com.hotel.booking.Repo.LocationRepo;
import com.hotel.booking.Repo.RoomRepo;
import com.hotel.booking.Repo.UserRepo;
import com.hotel.booking.Service.CustomerInfoService;
import com.hotel.booking.Service.PasswordResetTokenServiceImpl;
import com.hotel.booking.Service.ServiceInterface;
import com.hotel.booking.Service.UserService;
import com.hotel.booking.Service.jwtService;


@WebMvcTest(Controller.class)
@ExtendWith(MockitoExtension.class)
public class AddLocationAndDiscount {
	String token,token1;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
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
	
	   @BeforeEach
	   public void setup() {
	       this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
	       .apply(SecurityMockMvcConfigurers.springSecurity())
	       .build();
	       
	       }
	   
	   @Test
	   public void testAddNewLocation_SucessFull()throws Exception
	    {
		   User user = new User();
	       user.setUserName("John Doe");
	       user.setEmailId("john.doe@example.com");
	       user.setAddress("chennai");
	       user.setPassword("Password@123");
	       user.setAadharNumber(111122223333L);
	       user.setContactNumber("9047262272");
	       user.setRoles("ROLE_ADMIN");
	       userRepo.save(user);
	       
		   UserPojo authRequest = new UserPojo();
	       authRequest.setUserName("John Doe");
	       authRequest.setPassword("john.doe@example.com");
	       when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(),authRequest.getPassword())))
	       .thenReturn(authentication);
	       when(authentication.isAuthenticated()).thenReturn(true);
	       when(userRepo.findByUserName(authRequest.getUserName())).thenReturn(user);
	       Optional<User> optionalUser = Optional.of(user);
	       when(userRepo.findByUserId(user.getUserId())).thenReturn(optionalUser);
	       UserDetails userDetails = userService.loadUserByUsername(authRequest.getUserName());
	       token = jwtservice.generateToken(userDetails.getUsername(),userDetails.getAuthorities());

		   
	    	Location location=new Location();
	    	location.setLocationId(11l);
	    	location.setLocationName("Avadi");
	    	location.setLocationDistrict("Chennai");
	    	location.setLocationState("Tamil Nadu");
	    	location.setCountry("India");
	    	location.setPinCode(777888l);
	    	String expectedResponse="Locations Added successfully";
		    when(service.createlocationdetails(any(Location.class))).thenReturn(expectedResponse);
	    	

	         System.out.println("The token is"+token);       
	                RequestBuilder requestBuilder =
	                		 MockMvcRequestBuilders
	                        .post("/api/hotelbooking/admin/createlocationdetails")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(new ObjectMapper().writeValueAsString(location))
	                        .header("Authorization", "Bearer " + token);
	                        
	               MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
	               MockHttpServletResponse response = mvcResult.getResponse();
	               System.out.println("ResponseStatus:"+response.getStatus());
	               assertEquals(HttpStatus.OK.value(), response.getStatus());
	               System.out.println("ResponseContentString:"+response.getContentAsString());
	               assertEquals(expectedResponse, response.getContentAsString());
	     
	   	    verify(service,times(1)).createlocationdetails(any(Location.class));
	   }
	   
	     
	   
	   @Test
	   public void testAddNewLocation_UnAuthorized()throws Exception
	    {
		   User user1 = new User();
	       user1.setUserName("Thangam");
	       user1.setEmailId("Thangam@example.com");
	       user1.setAddress("chennai");
	       user1.setPassword("Thangam@123");
	       user1.setAadharNumber(888877776666L);
	       user1.setContactNumber("9034765432");
	       user1.setRoles("ROLE_USER");
	       userRepo.save(user1);
	       
	       UserPojo authRequest1=new UserPojo();
           authRequest1.setUserName("Thangam");
	       authRequest1.setPassword("Thangam@123");
           when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest1.getUserName(),authRequest1.getPassword())))
	       .thenReturn(authentication);
	       when(authentication.isAuthenticated()).thenReturn(true);
	       when(userRepo.findByUserName(authRequest1.getUserName())).thenReturn(user1);
		       Optional<User> optionalUser1 = Optional.of(user1);
	       when(userRepo.findByUserId(user1.getUserId())).thenReturn(optionalUser1);
	       UserDetails userDetails1 = userService.loadUserByUsername(authRequest1.getUserName());
		       token1 = jwtservice.generateToken(userDetails1.getUsername(),userDetails1.getAuthorities());

	       
	    	Location location1=new Location();
	    	location1.setLocationId(12l);
	    	location1.setLocationName("AnnaNagar");
	    	location1.setLocationDistrict("Chennai");
	    	location1.setLocationState("Tamil Nadu");
	    	location1.setCountry("India");
	    	location1.setPinCode(777833l);
	    	String expectedResponse="Locations Added successfully";
		    when(service.createlocationdetails(any(Location.class))).thenReturn(expectedResponse);
	    	

	         System.out.println("The token is"+token1);       
	         int expectedStatus=403;      
	         MvcResult result = mockMvc.perform(
	        	        post("/api/hotelbooking/admin/createlocationdetails")
	        	            .contentType(MediaType.APPLICATION_JSON)
	        	            .content(new ObjectMapper().writeValueAsString(location1))
	        	            .header("Authorization", "Bearer " + token1))
	        	            .andExpect(status().isForbidden())
	        	            .andReturn();

	         int actualStatus = result.getResponse().getStatus();
	         assertEquals(expectedStatus,actualStatus);
	               
	     
	   	    verify(service,times(0)).createlocationdetails(any(Location.class));
	   }
	   
	   @Test
	   public void testDuplication()throws Exception
	   {
		   User user = new User();
	       user.setUserName("John Doe");
	       user.setEmailId("john.doe@example.com");
	       user.setAddress("chennai");
	       user.setPassword("Password@123");
	       user.setAadharNumber(111122223333L);
	       user.setContactNumber("9047262272");
	       user.setRoles("ROLE_ADMIN");
	       userRepo.save(user);
	       
		   UserPojo authRequest = new UserPojo();
	       authRequest.setUserName("John Doe");
	       authRequest.setPassword("john.doe@example.com");
	       when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(),authRequest.getPassword())))
	       .thenReturn(authentication);
	       when(authentication.isAuthenticated()).thenReturn(true);
	       when(userRepo.findByUserName(authRequest.getUserName())).thenReturn(user);
	       Optional<User> optionalUser = Optional.of(user);
	       when(userRepo.findByUserId(user.getUserId())).thenReturn(optionalUser);
	       UserDetails userDetails = userService.loadUserByUsername(authRequest.getUserName());
	       token = jwtservice.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
		   
		    Location location=new Location();
	    	location.setLocationId(11l);
	    	location.setLocationName("Avadi");
	    	location.setLocationDistrict("Chennai");
	    	location.setLocationState("Tamil Nadu");
	    	location.setCountry("India");
	    	location.setPinCode(777888l);

	    	String expectedResponse="Duplicate Exsits of Records";
	    	
		    when(service.createlocationdetails(any(Location.class))).thenReturn(expectedResponse);

		   RequestBuilder requestBuilder =
          		 MockMvcRequestBuilders
                  .post("/api/hotelbooking/admin/createlocationdetails")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(new ObjectMapper().writeValueAsString(location))
                  .header("Authorization", "Bearer " + token);
                  
         MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
         MockHttpServletResponse response = mvcResult.getResponse();
         System.out.println("ResponseStatus:"+response.getStatus());
         assertEquals(HttpStatus.OK.value(), response.getStatus());
         System.out.println("ResponseContentString:"+response.getContentAsString());
         assertEquals(expectedResponse, response.getContentAsString());
         
	   	 verify(service,times(1)).createlocationdetails(any(Location.class));


		   
	   }
	   
	   @Test
	   public void Discount_SuccessFull()
	   {
		   
		   User user = new User();
	       user.setUserName("John Doe");
	       user.setEmailId("john.doe@example.com");
	       user.setAddress("chennai");
	       user.setPassword("Password@123");
	       user.setAadharNumber(111122223333L);
	       user.setContactNumber("9047262272");
	       user.setRoles("ROLE_ADMIN");
	       userRepo.save(user);
	       
		   UserPojo authRequest = new UserPojo();
	       authRequest.setUserName("John Doe");
	       authRequest.setPassword("john.doe@example.com");
	       when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(),authRequest.getPassword())))
	       .thenReturn(authentication);
	       when(authentication.isAuthenticated()).thenReturn(true);
	       when(userRepo.findByUserName(authRequest.getUserName())).thenReturn(user);
	       Optional<User> optionalUser = Optional.of(user);
	       when(userRepo.findByUserId(user.getUserId())).thenReturn(optionalUser);
	       UserDetails userDetails = userService.loadUserByUsername(authRequest.getUserName());
	       token = jwtservice.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
	       
	       Discount discount1=new Discount();
	       discount1.setDiscountDetails("Long Stay");
	       discount1.setDiscountPercentage(15.0);
	       discount1.setDiscountId(1l);
	       Discount discount2=new Discount();
	       discount2.setDiscountDetails("Group Members");
	       discount2.setDiscountPercentage(10.0);
	       discount2.setDiscountId(2l);
	       Discount discount3=new Discount();
	       discount3.setDiscountDetails("No Discount");
	       discount3.setDiscountPercentage(15.0);
	       discount3.setDiscountId(3l);
		   
	   }
	   
	  // @Test
	   

}
