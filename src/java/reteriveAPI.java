package com.hotel.booking.MockMVC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.booking.Controller.Controller;
import com.hotel.booking.Model.Hotels;
import com.hotel.booking.Model.Location;
import com.hotel.booking.Model.Room;
import com.hotel.booking.Model.User;
import com.hotel.booking.Pojo.UserPojo;
import com.hotel.booking.Repo.PasswordResetTokenRepository;
import com.hotel.booking.Repo.RoomRepo;
import com.hotel.booking.Repo.UserRepo;
import com.hotel.booking.Service.CustomerInfoService;
import com.hotel.booking.Service.ServiceInterface;
import com.hotel.booking.Service.UserService;
import com.hotel.booking.Service.jwtService;
import com.jayway.jsonpath.JsonPath;

@WebMvcTest(Controller.class)
@ExtendWith(MockitoExtension.class)
public class reteriveAPI {
	
   @Autowired
   private MockMvc mockMvc;
   @Autowired
   private WebApplicationContext context;
   @MockBean
   private UserRepo userRepo;
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
   private CustomerInfoService userService;
   @MockBean
   private ServiceInterface service;
   @MockBean
   private PasswordResetTokenRepository passwordresettokenrepo;
   
   @MockBean
   private RoomRepo roomRepository;
   
   String token=null;
   
	@BeforeEach
	   public void setup() {
	       this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
	       .apply(SecurityMockMvcConfigurers.springSecurity())
	       .build();
	       User user = new User();
	       user.setUserName("John Doe");
	       user.setEmailId("john.doe@example.com");
	       user.setAddress("chennai");
	       user.setPassword("Password@123");
	       user.setAadharNumber(111122223333L);
	       user.setContactNumber("9047262272");
	       user.setRoles("ROLE_USER");
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
           System.out.println(token);
	       
	       }
	
	     @Test
	     public void testAvailableRoomsForHotelId() throws JsonProcessingException, Exception
	     {
	    	 Hotels hotels=new Hotels();
	    	 hotels.setHotelId(12l);
	    	 Room roomdetails1=new Room();
	    	 roomdetails1.setRoomId(11l);
	    	 roomdetails1.setStatus("Available");
	    	 roomdetails1.setHotelId(hotels);
	    	 Room roomdetails2=new Room();
	    	 roomdetails2.setRoomId(11l);
	    	 roomdetails2.setStatus("Available");
	    	 roomdetails2.setHotelId(hotels);
	    	 List<Room> availableRooms =new ArrayList<Room>();
	    	 availableRooms.add(roomdetails2);
	    	 availableRooms.add(roomdetails1);
	    	 
	    	 when(service.AvailableRoomsForHotelId(any(Room.class))).thenReturn(availableRooms);
	    	 
	    	 MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/hotelbooking/user/AvailableRoomsForHotelId")
	    		        .contentType(MediaType.APPLICATION_JSON)
	    		        .content(new ObjectMapper().writeValueAsString(roomdetails1))
	    		        .header("Authorization", "Bearer " +token))
	    		        .andExpect(status().isOk())
	    		        .andReturn();
	    	 
	    	 String jsonResponse = mvcResult.getResponse().getContentAsString();
	    	 MockHttpServletResponse response=mvcResult.getResponse();
	    	 System.out.println("Response:"+response);
	    	 JsonPath jsonPath = JsonPath.compile("$");
	    	 int size = JsonPath.read(jsonResponse, "$.length()");
	    	 assertEquals(availableRooms.size(), size);
             
	    	 verify(service,times(1)).AvailableRoomsForHotelId(any(Room.class));

	    	 
	     }
	     @Test
	     public void AvailableRoomsForHotel_Id() throws JsonProcessingException, Exception
	     {
	    	 Hotels hotels=new Hotels();
	    	 hotels.setHotelId(12l);
	    	 Hotels hotel=null;
	    	 Room rooms=new Room();
	    	 rooms.setHotelId(hotel);
	    	 when(roomRepository.existsByHotelId(rooms.getHotelId())).thenReturn(null);
	    	 MvcResult mvcResult=  mockMvc.perform(post("/api/hotelbooking/user/AvailableRoomsForHotelId")
	                 .contentType(MediaType.APPLICATION_JSON)
	    		      .content(new ObjectMapper().writeValueAsString(rooms))
	    		       .header("Authorization", "Bearer " +token))
	        		 .andExpect(status().isOk())
	        		 .andReturn();
	    	 String jsonResponse = mvcResult.getResponse().getContentAsString();
	    	 JsonPath jsonPath = JsonPath.compile("$");
	    	 int size = JsonPath.read(jsonResponse, "$.length()");
	    	 assertEquals(0, size);
             	 
	     }

	     
	     @Test
	     public void testAvailableRoomsForHotelId_Booked() throws JsonProcessingException, Exception
	     {
	    	 Hotels hotels=new Hotels();
	    	 hotels.setHotelId(12l);
	    	 Room roomdetails1=new Room();
	    	 roomdetails1.setRoomId(11l);
	    	 roomdetails1.setStatus("Booked");
	    	 roomdetails1.setHotelId(hotels);
	    	 
	         List<Room> availableRooms = Collections.singletonList(roomdetails1);
	    	 when(service.AvailableRoomsForHotelId(any(Room.class))).thenReturn(availableRooms);
	    	 when(roomRepository.existsByHotelId(roomdetails1.getHotelId())).thenReturn(Optional.of(roomdetails1));
	         when(roomRepository.findByHotelId(roomdetails1.getHotelId())).thenReturn(availableRooms);

	         MvcResult mvcResult=  mockMvc.perform(post("/api/hotelbooking/user/AvailableRoomsForHotelId")
	                 .contentType(MediaType.APPLICATION_JSON)
	    		      .content(new ObjectMapper().writeValueAsString(roomdetails1))
	    		       .header("Authorization", "Bearer " +token))
	        		 .andExpect(status().isOk())
	        		 .andReturn();
	         
	    	 String jsonResponse = mvcResult.getResponse().getContentAsString();
	    	 JsonPath jsonPath = JsonPath.compile("$");
             String status=JsonPath.read(jsonResponse, "$[0].status");
	    	 assertEquals(status, roomdetails1.getStatus());     
	    	 verify(service,times(1)).AvailableRoomsForHotelId(any(Room.class));
	     }
	     
	     

		
}
