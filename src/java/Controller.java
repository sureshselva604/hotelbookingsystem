package com.hotel.booking.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hotel.booking.Model.Bill;
import com.hotel.booking.Model.Booking;
import com.hotel.booking.Model.Cancellation;
import com.hotel.booking.Model.Discount;
import com.hotel.booking.Model.HotelFacilities;
import com.hotel.booking.Model.Hotels;
import com.hotel.booking.Model.Location;
import com.hotel.booking.Model.PasswordResetToken;
import com.hotel.booking.Model.Payment;
import com.hotel.booking.Model.Room;
import com.hotel.booking.Model.RoomBooking;
import com.hotel.booking.Model.User;
import com.hotel.booking.Model.roomFacilities;
import com.hotel.booking.Pojo.UserPojo;
import com.hotel.booking.Repo.UserRepo;
import com.hotel.booking.Service.CustomerInfoService;
import com.hotel.booking.Service.EmailService;
import com.hotel.booking.Service.PasswordResetTokenService;
import com.hotel.booking.Service.ServiceInterface;
import com.hotel.booking.Service.TokenBlacklist;
import com.hotel.booking.Service.UserService;
import com.hotel.booking.Service.jwtService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/hotelbooking")
@SecurityRequirement(name="Authorization")
public class Controller {

	@Autowired
	private ServiceInterface service;

    @Autowired
    private  CustomerInfoService service1;

	@Autowired
	private jwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenBlacklist tokenBlacklist;
	
	@Autowired
	private PasswordResetTokenService tokenService;

	@Autowired
	private EmailService emailService;
	 
	@Autowired
	private UserRepo userrepo;
	
	@Autowired
	private UserService userService;
	
    @GetMapping("/welcome") 
    public String welcome() { 
        return "Welcome this endpoint is not secure"; 
    } 
	
	@PostMapping("/addNewUser")
	public  ResponseEntity<Object> addNewUser(@RequestBody User user) {
		try
		{
		   String result; 
		   result= service1.addUser(user);
		   return ResponseEntity.ok(result);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping("admin/createlocationdetails")
	public ResponseEntity<Object> locationdetails(@RequestBody  Location location) {
		try
		{
			String result= service.createlocationdetails(location);
			return ResponseEntity.ok(result);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping("hotelowner/createhoteldetails")
	public ResponseEntity<Object> createhoteldetails(@RequestBody Hotels hotel) {
		try
		{
		   String result= service.createhoteldetails(hotel);
			return ResponseEntity.ok(result);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
		
	}
	
	@PostMapping("admin/creatediscountdetails")
	public ResponseEntity<Object> creatediscountdetails(@RequestBody Discount discount)
	{
		try
		{
			String result= service.creatediscountdetails(discount);
			return ResponseEntity.ok(result);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	
        }
	}
	
	@PostMapping("user/createbookingdetails")
	public ResponseEntity<Object> createBooking(@RequestBody Booking booking)
	{
		
		try
		{
	      String result= service.createBooking(booking);
			return ResponseEntity.ok(result);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
	}
	
	@PostMapping("hotelowner/createroomdetails")
	public ResponseEntity<Object> roomdetails(@RequestBody Room room) {
		try
		{
			String result= service.roomdetails(room);
			return ResponseEntity.ok(result);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
	}

	@PostMapping("hotelowner/createroomfacdetails")
	public  ResponseEntity<Object> roomfacdetails(@RequestBody roomFacilities roomfac) {
		try
		{
		    String result= service.createroomfacdetails(roomfac);
			return ResponseEntity.ok(result);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
	}

	@PostMapping("hotelowner/createhotelfacdetails")
	public ResponseEntity<Object> hotelfacilities(@RequestBody HotelFacilities hotelfac) {
		try
		{
		 String result= service.createhotelfacilities(hotelfac);
			return ResponseEntity.ok(result);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
		
	}
	
	@PostMapping("admin/createroombookingdetails")
    public ResponseEntity<Object> createroombookingdetails(@RequestBody RoomBooking roombooking)
    {
		try
		{
		String result= service.createroombookingdetails(roombooking);
		return ResponseEntity.ok(result);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
    }

	@GetMapping("/getalluserdetails")
	public ResponseEntity<List<User>> getalluserdetails() {
		try
		{
			List<User> user= service.getalluserdetails();
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		catch(Exception e)
		{
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/getbookingdetails")
	public ResponseEntity<List<Booking>> getBookingdetails()
	{
		try
		{
		List<Booking> booking=service.getBookingdetails();
		return new ResponseEntity<>(booking, HttpStatus.OK);
		}
		catch(Exception e)
		{
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}

	@GetMapping("/getallhoteldetails")
	public ResponseEntity<List<Hotels>> getallhoteldetails() {
		try
		{
		List<Hotels> hotels= service.getallhoteldetails();
		return new ResponseEntity<>(hotels, HttpStatus.OK);
		}
		catch(Exception e)
		{
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/getalllocationdetails")
	public ResponseEntity<List<Location>> getalllocationdetails() {
		try
		{
		List<Location> location= service.getalllocationdetails();
		return new ResponseEntity<>(location, HttpStatus.OK);
		}
		catch(Exception e)
		{
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
	
	@GetMapping("/getallroomdetails")
	public ResponseEntity<List<Room>> getallRoomdetails() {
		try
		{
		List<Room> room= service.getallRoomdetails();
		return new ResponseEntity<List<Room>>(room,HttpStatus.OK);
		}
		catch(Exception e)
		{
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getallroomfacilities")
	public ResponseEntity<List<roomFacilities>> getallroomfacilities() {
		try
		{
		List<roomFacilities> roomfacilities= service.getallroomfacilities();
		return new ResponseEntity<List<roomFacilities>>(roomfacilities,HttpStatus.OK);
		}
		catch(Exception e)
		{
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getallhotelfacilities")
	public ResponseEntity<List<HotelFacilities>> gethotelallfacilities() {
		try
		{
			List<HotelFacilities>hotelfac= service.gethotelallfacilities();
			return new ResponseEntity<List<HotelFacilities>>(hotelfac,HttpStatus.OK);
		}
		catch(Exception e)
		{
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/getallroombookingdetails")
    public ResponseEntity<List<RoomBooking>> getAllBookingDetails()
    {
		try
		{
		List<RoomBooking>roombooking= service.getAllBookingDetails();
		return new ResponseEntity<List<RoomBooking>>(roombooking,HttpStatus.OK);
		}
		catch(Exception e)
		{
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

    }
	
	@GetMapping("/getalldiscountdetails")
	public ResponseEntity<List<Discount>> getalldiscountdetails() 
	{
		try
		{
			List<Discount> discount= service.getalldiscountdetails();
            return new ResponseEntity<>(discount,HttpStatus.OK);
		}
		catch(Exception e)
		{
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("admin/updatebilldetails")
	public ResponseEntity<Object> updatebilldetails(@RequestBody Bill bills) {
		try
		{
		     return ResponseEntity.ok(service.updatebilldetails(bills));
		}
		catch(Exception e)
		{
			 return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PostMapping("user/updatecancellationdetails")
	public ResponseEntity<Object> updatecancellationdetails(@RequestBody Cancellation cancellation) {
		try
		{
		  return ResponseEntity.ok(service.updatecancellationdetails(cancellation));
		
		}
		catch(Exception e)
		{
			 return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("user/updatepaymentdetails")
	public ResponseEntity<Object> updatePaymentDetails(@RequestBody Payment payment)
	{
		try
		{
		     return ResponseEntity.ok(service.updatePaymentDetails(payment));
		}
		catch(Exception e)
		{
			 return ResponseEntity.badRequest().body(e.getMessage());

		}
	}
	
	@PostMapping("user/edituserdetails")
	public String editUserDetails(@RequestHeader("Authorization") String token,@RequestBody User user)
	{   
		String userName,editdetails = null;
		userName=service.extractUserName(token);
		if(userName!=null)
		{
			try
			{
				editdetails= service.editUserDetails(user,userName);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				editdetails = "Error occurred while editing user details";
			}
		}
		else
		{
			editdetails="Inavlid Request or Ivalid User Details";
		}
		return editdetails;
	}
	@PostMapping("user/editbookingdetails/{bookingId}")
	public String editBookingDetails(@PathVariable Long bookingId,@RequestBody Booking booking,@RequestHeader("Authorization") String token)
	{
		String userName = null,editdetails = null;
		userName=service.extractUserName(token);
		if(userName!=null)
		{
		  try
		    {
		
		       editdetails= service.editBookingDetails(bookingId,booking,userName);
		    }
		   catch(Exception e)
		    {
			  e.printStackTrace();
			  editdetails = "Error occurred while editing booking details";
		    }
		}
		else
		{
			editdetails= "Invalid Request";
		}
		
		return editdetails;
	}
	@PostMapping("admin/editlocationdetails/{locationId}")
	public String editLocationDetails(@PathVariable Long locationId, @RequestBody Location location,@RequestHeader("Authorization") String token)
	{
		String userName = null,editdetails = null;
		 userName=service.extractUserName(token);
		 if(userName!=null)
		 {
			 try
			 {
				
				 editdetails= service.editLocationDetails(locationId,location);
			 }
			 catch(Exception e)
			  {
					e.printStackTrace();
		            System.out.print(e.getMessage());
					editdetails = "Error occurred while editing hotel details";

			  }
		 }
		 else
			{
				editdetails= "Invalid Request or Invalid User Details";
			}
          return editdetails;
	}
	
	
	@PostMapping("admin/editdiscountdetails/{discountId}")
	public String editDiscountDetails(@PathVariable Long discountId,@RequestBody Discount discount,@RequestHeader("Authorization") String token)
	{
		 String userName = null,editdetails = null;
		 userName=service.extractUserName(token);

		 if(userName!=null)
		 {
			 try
			 {
				 editdetails= service.editDiscountDetails(discountId,discount);
			 }
			 catch(Exception e)
				{
					e.printStackTrace();
		            System.out.print(e.getMessage());
					editdetails = "Error occurred while editing hotel details";

				}
		 }
		 else
			{
				editdetails= "Invalid Request or Invalid User Details";
			}
          return editdetails;
	}
	
	
	@PostMapping("hotelowner/edithoteldetails/{hotelId}")
	public String edithoteldetails(@PathVariable Long hotelId,@RequestBody Hotels hotel,@RequestHeader("Authorization") String token)
	{
		String userName = null,editdetails = null;
		userName=service.extractUserName(token);
		if(userName!=null)
		{
		  try
		    {
			editdetails= service.edithoteldetails(hotelId,hotel,userName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
            System.out.print(e.getMessage());
			editdetails = "Error occurred while editing hotel details";
		}
		}
		else
		{
			editdetails= "Invalid Request or Invalid User Details";
		}
		return editdetails;
	}
	
	
	@PostMapping("hotelowner/editroomdetails/{roomId}")
	public String editroomdetails(@PathVariable Long roomId,@RequestBody Room rooms,@RequestHeader("Authorization") String token)
	{
		String userName = null,editdetails = null;
		userName=service.extractUserName(token);
		if(userName!=null)
		{
		  try
		    {
		      editdetails= service.editroomdetails(roomId,rooms,userName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
            System.out.print(e.getMessage());
			editdetails = "Error occurred while editing room details";
		}
		}
		else
		{
			editdetails= "Invalid Request or Invalid User Details";
		}
		return editdetails;
	}
	
	
	@PostMapping("hotelowner/edithotelfacilties/{hotelfacId}")
	public String editHotelFacilities(@PathVariable Long hotelfacId, @RequestBody HotelFacilities hotelfacilities,@RequestHeader("Authorization") String token)
	{
		String userName = null,editdetails = null;
		userName=service.extractUserName(token);
		if(userName!=null)
		{
		  try
		    {
		return service.editHotelFacilities(hotelfacId,hotelfacilities,userName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
            System.out.print(e.getMessage());
			editdetails = "Error occurred while editing hotel Facilities details";

		}
		}
		else
		{
			editdetails= "Invalid Request or Invalid User Details";
		}
		return editdetails;
	}
	
	
	@PostMapping("hotelowner/editroomfacilties/{roomFacId}")
	public String editRoomFacilities(@PathVariable Long roomFacId,@RequestBody roomFacilities roomfacilities,@RequestHeader("Authorization") String token)
	{
		String userName = null,editdetails = null;
		userName=service.extractUserName(token);
		if(userName!=null)
		{
		  try
		    {
			return service.editroomFacilities(roomFacId,roomfacilities);
		}
		catch(Exception e)
		{
			e.printStackTrace();
            System.out.print(e.getMessage());
			editdetails = "Error occurred while editing hotel Facilities details";

		}
		}
		
		else
		{
			editdetails= "Invalid Request or Invalid User Details";
		}
		return editdetails;
	}
	
	@PostMapping("user/AvailableRoomsForHotelId")
	public ResponseEntity<Object> AvailableRoomsForHotelId(@RequestBody Room rooms)
	{
		try
		{
		 List<Room>room= service.AvailableRoomsForHotelId(rooms);
		 return ResponseEntity.ok(room);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
		
	}
	
	@GetMapping("user/totalRoomsForHotelId")
	public ResponseEntity<Object> totalRoomsForHotelId(@RequestBody Room rooms)
	{
		try
		{
		
		  List<Room>totalRoom= service.totalRoomsForHotelId(rooms);
		  return ResponseEntity.ok(totalRoom);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	
	@GetMapping("user/getRoomFacilitiesForRoomId")
	public ResponseEntity<Object> roomFacForRoomId(@RequestBody roomFacilities roomfac)
	{
		try {
		
			List<roomFacilities>roomfacilities= service.roomFacForRoomId(roomfac);
			return ResponseEntity.ok(roomfacilities);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	
	}
	
	@GetMapping("user/getHotelFacilitiesForHotelId")
	public ResponseEntity<List<HotelFacilities>> hotelFacForHotelId(@RequestBody HotelFacilities hotelfac )
	{
		
		List<HotelFacilities>hotelfacilities= service.hotelFacForHotelId(hotelfac);
        return ResponseEntity.ok(hotelfacilities); 
		
	}
	
	@GetMapping("user/getHotelsForLocationId")
	public ResponseEntity<List<Hotels>> getHotelsforLocation(@RequestBody Hotels hotels) {
	    
	        List<Hotels> hotelsloc = service.getHotelsforLocation(hotels);
	        return ResponseEntity.ok(hotelsloc);
	    
	}
	
	@GetMapping("user/getFilterByStarRating/{starRating}")
	public ResponseEntity<Object> getFilterByStarRating(@PathVariable Double starRating ) throws IllegalAccessException
	{
		try {
		
			  List<Hotels>hotels=service.getFilterByStarRating(starRating);
		      return ResponseEntity.ok(hotels);
		   }
		catch(Exception e)
		{
			 return ResponseEntity.badRequest().body(e.getMessage());

		}
		
		
		
	}
	
	@GetMapping("user/getFilterByUserRating/{userRating}")
	public ResponseEntity<Object> getFilterByUserRating(@PathVariable Double userRating) throws IllegalAccessException
	{
		try
		{
			 List<Hotels>hotels=service.getFilterByUserRating(userRating);
		      return ResponseEntity.ok(hotels);
		}
		catch(Exception e)
		{
			 return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@GetMapping("user/getFilterByPrice/{minimumamount}/{maximumamount}")
	public ResponseEntity<Object> getFilterByPrice(@PathVariable Double minimumamount,@PathVariable Double maximumamount ) throws IllegalAccessException
	{
		try
		{
		
			List<Room>roomPrice=service.getFilterByPrice(minimumamount,maximumamount);
			return ResponseEntity.ok(roomPrice);
		}
		catch(Exception e)
		{
			 return ResponseEntity.badRequest().body(e.getMessage());

		}
		
	}
	
	@GetMapping("/user/userProfile")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String userProfile() {

		return "Welcome to User Profile";
	}

	/*
	 * @GetMapping("/admin/adminProfile")
	 * 
	 * @PreAuthorize("hasAuthority('ROLE_ADMIN')") public String adminProfile() {
	 * return "Welcome to Admin Profile"; }
	 * 
	 * @GetMapping("/hotelowner/hotelownerProfile")
	 * 
	 * @PreAuthorize("hasAuthority('ROLE_HOTELOWNER')") public String
	 * hotelownerProfile() { return "Welcome to hotelowner Profile"; }
	 */

	
	@PostMapping("/login")
	public ResponseEntity<String> authenticateAndGetToken(@RequestBody UserPojo authRequest) {
	
		String token = null;
	    Authentication authentication;
	    try {
	        authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
	        System.out.println("Authentication:"+authentication);
	    } catch (AuthenticationException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	    }

	    if (authentication!=null && authentication.isAuthenticated()) {
	    	        UserDetails userDetails = service1.loadUserByUsername(authRequest.getUserName());	         
	    	        token = jwtService.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
	        return ResponseEntity.ok(token);
	    } else {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
	    }
	}

	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		try
		{

		String token = extractTokenFromRequest(request);
		if (!tokenBlacklist.isBlacklisted(token)) {
			tokenBlacklist.addToBlacklist(token);
			return ResponseEntity.ok("Logged out successfully");
		}
		else {
            return ResponseEntity.badRequest().body("Invalid or blacklisted token.");
        }
		}
		catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during logout.");
	    }	

	}
	
	public String extractTokenFromRequest(HttpServletRequest request) {

		String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7);
		}
		return null;
	}
	
	@PostMapping("/forgotpassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> json) 
      {
	     try
	       {
               String userEmail = json.get("email");
              User user =userrepo.findByEmailId(userEmail);
              if (user != null) 
              {
                   PasswordResetToken token = tokenService.createToken(user);
                   emailService.sendPasswordResetEmail(user,token.getToken());
                   return ResponseEntity.ok("Email Sended Successfully ");
               } 
            else 
             {
                return ResponseEntity.badRequest().body("User not found.");
             }
	       }
	 catch (Exception e) 
	     {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during password reset.");
	    }
	 
 }
 
 @PostMapping("/resetpassword")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> json) {
	 try
	 {
        String resetToken = json.get("token");
        String newPassword=json.get("newPassword");
        String confirmPassword=json.get("confirmPassword");
        
        
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("New password and confirm password do not match.");
        }
        
        PasswordResetToken token = tokenService.findByToken(resetToken);

        if (token != null && !token.isExpired()) 
        {
            User user = token.getUser();
            userService.updatePassword(user, newPassword);
            tokenService.deleteToken(token);
            return ResponseEntity.ok("Password reset successful.");
        } 
        else 
        {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
	 }
	 catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during password reset.");
	    }
    }

}
