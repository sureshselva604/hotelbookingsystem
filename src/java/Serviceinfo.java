package com.hotel.booking.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hotel.booking.Model.Bill;
import com.hotel.booking.Model.Cancellation;
import com.hotel.booking.Model.Discount;
import com.hotel.booking.Model.HotelFacilities;
import com.hotel.booking.Model.Hotels;
import com.hotel.booking.Model.Location;
import com.hotel.booking.Model.Payment;
import com.hotel.booking.Model.Room;
import com.hotel.booking.Model.RoomBooking;
import com.hotel.booking.Model.User;
import com.hotel.booking.Model.roomFacilities;
import com.hotel.booking.Repo.BillRepo;
import com.hotel.booking.Repo.BookingRepo;
import com.hotel.booking.Repo.CancellationRepo;
import com.hotel.booking.Repo.DiscountRepo;
import com.hotel.booking.Repo.HotelFacRepo;
import com.hotel.booking.Repo.HotelRepo;
import com.hotel.booking.Repo.LocationRepo;
import com.hotel.booking.Repo.RoomBookingRepo;
import com.hotel.booking.Repo.RoomFacRepo;
import com.hotel.booking.Repo.RoomRepo;
import com.hotel.booking.Repo.UserRepo;
import com.hotel.booking.Repo.paymentRepo;
import com.hotel.booking.Model.Booking;

@Service
public class Serviceinfo implements ServiceInterface {
	
	
	int totalPeoples=0;
	double totalAmount=0.0;
	int totaltotalNoOfdays=0;
	double actualdiscount=0.0;
	double Cancellationcharges;
	String userName;

	@Autowired
	private UserRepo userrepo;

	@Autowired
	private LocationRepo locationrepo;

	@Autowired
	private HotelRepo hotelrepo;

	@Autowired
	private RoomRepo roomrepo;

	@Autowired
	private RoomFacRepo roomfacrepo;

	@Autowired
	private HotelFacRepo hotelfacrepo;

	@Autowired
	private BillRepo billrepo;

	@Autowired
	private RoomBookingRepo roombookingrepo;

	@Autowired
	private DiscountRepo discountrepo;

	@Autowired
	private  CancellationRepo cancellationRepo;

	@Autowired
	private BookingRepo bookingrepo;
	
	@Autowired
	private paymentRepo paymentrepo;
	
	@Autowired
	private jwtService jwtService;
	
	//Saving an Details
	public String createlocationdetails(Location location) {
		String result=null;
		try
		{
		if(location!=null)
		{	
			locationrepo.save(location);
		    result="Locations Added successfully";
		}
		else if(locationrepo.existsById(location.getLocationId()) && locationrepo.existsByPinCode(location.getPinCode()))
		{
			result="Duplicate Exsits of Records";
		}
		else
		{
			result="Location Details cannot be Null";
		}
		}
		catch (DataAccessException e) {
	        e.printStackTrace();
	        result = "An error occurred while adding the location";
	    }
		return result;
	}

	public String createhoteldetails(Hotels hotel) {
		String result=null;
		try
		{
		if(hotel!=null)
		{
			hotelrepo.save(hotel);
	        result="Hotel Added successfully";
		}
		else
		{
			result="Hotel Details cannot be Null";
		}
		}
		catch (DataAccessException e) {
	        e.printStackTrace();
	        result = "An error occurred while adding the hotel";
	    }
		return result;
		
	  }

	public String roomdetails(Room room) {
		String result=null;
		try
		{
		if(room!=null)
		{
			roomrepo.save(room);
			result="Rooms Added successfully";
		}
		else
		{
			result="Rooms Details cannot be Null";
		}
		}
		catch (DataAccessException e) {
	        e.printStackTrace();
	        result = "An error occurred while adding the room";
	    }
		return result;
	}

	public String createroomfacdetails(roomFacilities roomfac) {
		String result=null;
		try
		{
		if(roomfac!=null)
		{
			roomfacrepo.save(roomfac);			
			result="RoomsFacilities Added successfully";
		}
		else
		{
			result="RoomsFacilities Details cannot be Null";
		}
		}
		catch (DataAccessException e) {
	        e.printStackTrace();
	        result = "An error occurred while adding the rooms facilities";
	    }
		return result;
	}

	public String createhotelfacilities(HotelFacilities hotelfac) {
		String result=null;
		try
		{
		if(hotelfac!=null)
		{
			hotelfacrepo.save(hotelfac);
			result="HotelFacilities Added successfully";
		}
		else
		{
			result="Hotel Facilities cannot be Null";
		}
		}
		catch (DataAccessException e) {
	        e.printStackTrace();
	        result = "An error occurred while adding the hotel Facilities";
	    }
		return result;
		
	}

	public String createBooking(Booking booking) {
		String result=null;
		try
		{
		if(booking!=null)
		{
			if(booking.getCheckInDate().isBefore(booking.getCheckOutDate()))
			{
				     booking.setBookingDate(LocalDateTime.now());
			         bookingrepo.save(booking);
			         result="Booking Details Added successfully";
			}
			else
			{
				     result="provide the correct checkIndate and CheckOutDate";
			}
		}
		else
		{
			result="Booking Details cannot be Null";
		}
		}
		catch (DataAccessException e) {
	        e.printStackTrace();
	        result = "An error occurred while adding the Booking Details";
	    }
		return result;
	}

	@Override
	public String creatediscountdetails(Discount discount) {
		String result=null;
		try
		{
		if(discount!=null)
		{
			discountrepo.save(discount);
	        result="Discount Added successfully";

		}
		else
		{
			result="Discount Details cannot be Null";
		}
		}
		catch (DataAccessException e) {
	        e.printStackTrace();
	        result = "An error occurred while adding the discount";
	    }
		return result;	
	}
	
	@Override
	public String createroombookingdetails(RoomBooking roombooking) {
		
		System.out.println(roombooking.getBookingId());
		System.out.println(roombooking.getRoomId());
		System.out.println(roombooking.isActiveStatus());
		
		String result=null;
		try
		{
		if(roombooking!=null)
		{
			if(bookingrepo.existsByBookingId(roombooking.getBookingId().getBookingId()) && roomrepo.existsByRoomId(roombooking.getRoomId().getRoomId()))
			{
				    Long room1=roombooking.getRoomId().getRoomId();
				    Optional<Room>room=roomrepo.findById(room1);
				
			     if(room.get().getStatus().equalsIgnoreCase("Available") && roombooking.isActiveStatus())
			      {

			           roombookingrepo.save(roombooking);        
			           result="RoomBooking Details Added successfully";
			      }
			    else
			     {
				        result="Already the room has been allocated to somebody";
			      }
			
			}
			else
			{
				result="Provided Details not founded in the database";
			}
		}
		else
		{
			result="RoomBooking Details cannot be Null";
		}
		}
		catch (DataAccessException e) {
	        e.printStackTrace();
	        result = "An error occurred while adding the RoomBooking Details";
	    }
		
		return result;
	}
//Getting the Details
	public List<User> getalluserdetails() 
	{
		try
		{
		return userrepo.findAll();
		}
		catch (Exception e) 
		{
            e.printStackTrace(); 
            System.err.println("An error occurred while fetching user details: " + e.getMessage());
            return new ArrayList<>(); 
        }
	}

	public List<Hotels> getallhoteldetails() 
	{
		try
		{
		return hotelrepo.findAll();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("An error occurred while fetching hotel details: " + e.getMessage());
            return new ArrayList<>();
		}
	}

	public List<Location> getalllocationdetails() 
	{
		try
		{
		return locationrepo.findAll();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("An error occurred while fetching location details: " + e.getMessage());
            return new ArrayList<>();
		}
	}

	public List<Room> getallRoomdetails() {
		try
		{
		return roomrepo.findAll();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("An error occurred while fetching room details: " + e.getMessage());
            return new ArrayList<>();
		}
	}

	public List<roomFacilities> getallroomfacilities() {
		try
		{
		return roomfacrepo.findAll();
		}
		catch(Exception e)
		{
			e.printStackTrace();
            System.err.println("An error occurred while fetching room facilities details: " + e.getMessage());
            return new ArrayList<>();
		}
		
	}

	public List<HotelFacilities> gethotelallfacilities() {
		try
		{
		return hotelfacrepo.findAll();
		}
		catch(Exception e)
		{
			e.printStackTrace();
            System.err.println("An error occurred while fetching hotel facilities details: " + e.getMessage());
            return new ArrayList<>();
		}
	}

	@Override
	public List<Booking> getBookingdetails() {
		try
		{
		return bookingrepo.findAll();
		}
		catch(Exception e)
		{
			e.printStackTrace();
            System.err.println("An error occurred while fetching Booking details: " + e.getMessage());
            return new ArrayList<>();
		}
	}
	
	@Override
	public List<RoomBooking> getAllBookingDetails() {
		try
		{
		   return roombookingrepo.findAll();
		}
		catch(Exception e)
		{
			e.printStackTrace();
            System.err.println("An error occurred while fetching RoomBooking details: " + e.getMessage());
            return new ArrayList<>();
		}
	}
	
	@Override
	public List<Discount> getalldiscountdetails() {
		try
		{
		return discountrepo.findAll();
		}
		catch(Exception e)
		{
			e.printStackTrace();
            System.err.println("An error occurred while fetching Discount details: " + e.getMessage());
            return new ArrayList<>();
		}
	}

	
	
	
	//******************Calculating Bill details********************************
	
	public ResponseEntity<String> updatebilldetails(Bill bill) {
		try
		{
			
		    String message="";
			if(bookingrepo.existsById(bill.getBookingId().getBookingId()))
			{
			         billrepo.save(bill);
			         Long bookingId = bill.getBookingId().getBookingId();
	                 List<Bill> bills = billrepo.findByBookingId(bill.getBookingId());
	                 List<Booking> booking = bookingrepo.findBybookingId(bookingId);
	                 List<Cancellation> cancellations = cancellationRepo.findAll();
	        
	                 //Checking the BookingId present in the Cancellation Repository	        
	                 Cancellation cancel = cancellations.stream()
	                       .filter(c -> c.getBookingId().equals(bill.getBookingId()))
	                       .findFirst().orElse(null);
	        
	                //For Updating the Cancellation Chargers with Fixed Charges
	                if (cancel != null && cancel.getCancellationCharges() != 0.0)
	                  {
	                      bills.forEach(bi -> {
	                            double cancellationCharges = cancel.getCancellationCharges();
	                            bi.setAmountToBePaid(cancellationCharges);
	                            Cancellationcharges=cancellationCharges;
	                            billrepo.save(bi);
	                                   });
	                  } 
	               else {
	                  bills.forEach(bi -> {     	  
	                  booking.forEach(book->{ 
	        	   
	                              //Total No of Days
	                               LocalDateTime startDate = book.getCheckInDate();
	                               LocalDateTime endDate = book.getCheckOutDate();
	                               double days = Duration.between(startDate, endDate).toMinutes() / (60.0 * 24.0);
	                               int day = (int) Math.ceil(days);
	                               totaltotalNoOfdays=day;
	                               bi.setTotalNoOfDays(day);
	           
	                               //Total No of Peoples
	                                int totalNoOfPeople = book.getNoOfAdults() + book.getNoOfChildrens();
	                                totalPeoples=totalNoOfPeople;
	                                bi.setTotalNoOfPeoples(totalNoOfPeople);
	            
                                    //Total Amount Calculation
	                                 double amount = roombookingrepo.findAllBybookingId(bi.getBookingId()).stream()
	                                     .mapToDouble(rb -> bi.getTotalNoOfDays() * rb.getRoomId().getPricePerDay()).sum();
                                          totalAmount=amount;
	                                 bi.setTotalAmount(amount);
	             
	                                //Discount Name Calculation
	                                String offerName = getOfferName(bi.getTotalNoOfDays(), bi.getTotalNoOfPeoples());
	             
	                                 //Discount  Calculation
	                                 discountrepo.findAll().stream()
                                          .filter(di -> di.getDiscountDetails().equalsIgnoreCase(offerName))
                                           .findFirst()
                                          .ifPresent(di -> bi.setAmountToBePaid(calculateDiscountAmount(bi.getTotalAmount(), di.getDiscountPercentage()))
                                     );
	             
	                                //Saving the details in the billRepo
	                                 billrepo.save(bi); 
	             });
              });
	        
	    }
	           message="Bill Details Updated Successfully";
			}
			else
			{
				message="There is no such Booking Details";
			}
		return ResponseEntity.ok(message);
		}
		catch (Exception e) {
	                e.printStackTrace();
	               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating bill details.");
	    }
	}
	
	//Method for getting the Offer
	private String getOfferName(int totalNoOfDays, int totalNoOfPeoples) {
	       return (totalNoOfDays >= 4 && totalNoOfPeoples < 4) ? "Long Stay" :
	             (totalNoOfPeoples >= 4 && totalNoOfDays < 4) ? "Group Members" :
	             (totalNoOfDays >= 4  && totalNoOfPeoples >= 4) ?"Long Stay":
	             "No Discount";
	}
	
	  //Method for getting the DiscountAmount
	  private double calculateDiscountAmount(double totalAmount, double discountPercentage) {
		  actualdiscount=(totalAmount - (totalAmount * discountPercentage / 100));
		  return (totalAmount - (totalAmount * discountPercentage / 100));
	  }
	  
	//******************Calculating Cancellation details********************************
	
	@Override
	public ResponseEntity<String> updatecancellationdetails(Cancellation cancell)
	{
	   try {
		      String message;
		      if(bookingrepo.existsById(cancell.getBookingId().getBookingId()))
		      {
		          if (cancellationRepo.existsByBookingId(cancell.getBookingId())) 
		             {
		               message = "Already Cancelled";
		              } 
		         else 
		            {
		                   cancellationRepo.save(cancell);
		          
		                // Retrieve the Cancellation object after saving it...
		                   Cancellation cancellations = cancellationRepo.findByBookingIdBookingId(cancell.getBookingId().getBookingId());

		                 cancellations.setCancellationDate(LocalDateTime.now());
		                 cancellationRepo.save(cancellations);
		                 LocalDateTime cancellationDate = cancellations.getCancellationDate();
		                 Booking bookings = bookingrepo.findByBookingId(cancell.getBookingId().getBookingId());
		                 LocalDateTime checkInDate = bookings.getCheckInDate();
		                 double days = Duration.between(cancellationDate, checkInDate).toMinutes() / (60.0 * 24.0);
		                 int day = (int) Math.ceil(days);
		                if (day > 2) 
		                {
		                   cancell.setCancellationCharges(200.0);
		                   message = "Successfully Cancelled";    
		                }
		               else 
		               {
		                  cancell.setCancellationCharges(0.0);
		                  message = "Cancellation time is over";      
		                }
		                  cancellationRepo.save(cancellations);
		            
		             }
		      }
		      else
		      {
		    	      message="There is no such booking Details are founded";
		      }
		              return ResponseEntity.ok(message);
		    } 
	   catch (Exception e) 
	   {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body("An error occurred while processing the cancellation request.");
	    }
	}
	
@Override
public ResponseEntity<String> updatePaymentDetails(Payment payment) {
	try {
	String message="";
	System.out.println(payment.getBillId());
	if (paymentrepo.existsByBillId(payment.getBillId())) 
    {
          message = "Already Exists Or Paid";
    } 
	else
	{
		Optional<Bill> bill=billrepo.findById(payment.getBillId().getBillId());
		System.out.println(bill);
		if(bill.isPresent())
		{
		   payment.setPaymentDate(LocalDateTime.now());
		   payment.setPaymentAmount(bill.get().getAmountToBePaid());
		   payment.setPaymentMethod(payment.getPaymentMethod());
		   payment.setPaymentStatus(payment.getPaymentStatus());
		   paymentrepo.save(payment);
		   if(payment.getPaymentStatus().equalsIgnoreCase("Paid"))
		   {
		   List<RoomBooking>roombookingDetails=roombookingrepo.findAllBybookingId(bill.get().getBookingId());
		   roombookingDetails.stream()
                 .forEach(roomBooking -> {
                  roomBooking.setActiveStatus(false);
                  Room room = roomrepo.findById(roomBooking.getRoomId().getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));
                  room.setStatus("Booked");
                  roomrepo.save(room);
            

        });
		   }
		message="Successfully Paid";
		}
		else
		{
			message="There is no such Bill details found";
		}

	}
	return ResponseEntity.ok(message);
	}
	catch (Exception e) 
	   {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body("An error occurred while processing the payment request.");
	    }
}

//*****************Edit UserDetails************************

@Override
public String editUserDetails(User user,String userName) {
	String message;
	try
	{
	       User userdetails=userrepo.findByUserName(userName);
	if(userdetails!=null)
	{
		Optional.ofNullable(user.getEmailId()).ifPresent(userdetails::setEmailId);
		Optional.ofNullable(user.getAddress()).ifPresent(userdetails::setAddress);
		Optional.ofNullable(user.getAadharNumber()).ifPresent(userdetails::setAadharNumber);
		Optional.ofNullable(user.getContactNumber()).ifPresent(userdetails::setContactNumber);
		Optional.ofNullable(user.getRoles()).ifPresent(userdetails::setRoles);
	    userrepo.save(userdetails);
	    message="Sucessfully Edited the details"+" "+userName;
	}
	else
	{
		message="UnAuthorized:User Not Found for"+" "+userName;
	}
	}
	catch (Exception e) {
        message = "An error occurred while editing user details";
        e.printStackTrace();
    }
	
	return message;
}

@Override
public String editLocationDetails( Long locationId,  Location location) {
	String message;
	try
	{
	      Location locationdetails=locationrepo.findByLocationId(locationId);
	if(locationdetails!=null)
	{
	   Optional.ofNullable(location.getLocationName()).ifPresent(locationdetails::setLocationName);
   	   Optional.ofNullable(location.getLocationState()).ifPresent(locationdetails::setLocationState);
   	   Optional.ofNullable(location.getPinCode()).ifPresent(locationdetails::setPinCode);
   	   Optional.ofNullable(location.getLocationDistrict()).ifPresent(locationdetails::setLocationDistrict);
   	   Optional.ofNullable(location.getCountry()).ifPresent(locationdetails::setCountry);
   	   locationrepo.save(locationdetails);
   	   message="Location details edited Sucessfully";
	}
	else
	{
		message="location details not found";
	}
	}
	catch (Exception e) {
        message = "An error occurred while editing location details";
        e.printStackTrace();
    }
	return message;
	
}

@Override
public String editBookingDetails(Long bookingId, Booking booking,String userName) {
	String message="";
	try {
	     Booking editbooking=bookingrepo.findByBookingId(bookingId);
         if(editbooking!=null)
         {
		      User  userdetails=userrepo.findByUserName(userName);
	     if(userdetails!=null && userdetails.getUserId()==editbooking.getUserId().getUserId())
	     {
	    	 Optional.ofNullable(booking.getNoOfAdults()).ifPresent(editbooking::setNoOfAdults);
	    	 Optional.ofNullable(booking.getCheckInDate()).ifPresent(editbooking::setCheckInDate);
	    	 Optional.ofNullable(booking.getCheckOutDate()).ifPresent(editbooking::setCheckOutDate);
	    	 Optional.ofNullable(booking.getNoOfChildrens()).ifPresent(editbooking::setNoOfChildrens);
	    	 Optional.ofNullable(booking.getNoOfRooms()).ifPresent(editbooking::setNoOfRooms);
	    	 Optional.ofNullable(booking.getLocationId()).ifPresent(editbooking::setLocationId);
	    	 bookingrepo.save(editbooking);
	    	 message="Booking details edited Succesfully for"+" "+userName;
	     }
	     else
         {
           message = "Unauthorized: User details do not match for"+" "+userName;
         }
 }
    else {
          message = "Booking details not found for"+" "+userName;
       }
}
	catch (Exception e) {
        message = "An error occurred while editing booking details";
        e.printStackTrace();
    }
	
	     return message;
	
}

@Override
public String editDiscountDetails(Long discountId, Discount discount) {
		String message="";
		try
		{
			
		Discount editdiscount=discountrepo.findByDiscountId(discountId);
		if(editdiscount!=null)
		{
	    	 Optional.ofNullable(discount.getDiscountDetails()).ifPresent(editdiscount::setDiscountDetails);
	    	 Optional.ofNullable(discount.getDiscountPercentage()).ifPresent(editdiscount::setDiscountPercentage);
	    	 discountrepo.save(editdiscount);
	    	 message="Discount details edited Succesfully";
		}
		else
	     {
	    	 message="Discount details not found";
	     }
		}
		catch (Exception e) {
	        message = "An error occurred while editing location details";
	        e.printStackTrace();
	    }
	     return message;
	
}

@Override
public String edithoteldetails(Long hotelId, Hotels hotel,String userName) {
	String message="";
	try
	{
		Hotels hotels=hotelrepo.findByHotelId(hotelId);
        if(hotels!=null)
        {
	         User  userdetails=userrepo.findByUserName(userName);
	         Long hotelid=hotels.getUserId().getUserId();
	         if(userdetails!=null && userdetails.getUserId()==hotelid)
	             {
		
		             Optional.ofNullable(hotel.getHotelName()).ifPresent(hotels::setHotelName);
		             Optional.ofNullable(hotel.getHotelStreet()).ifPresent(hotels::setHotelStreet);
		             Optional.ofNullable(hotel.getHotelType()).ifPresent(hotels::setHoteltype);
		             Optional.ofNullable(hotel.getHotelCountry()).ifPresent(hotels::setHotelCountry);
		             Optional.ofNullable(hotel.getHotelState()).ifPresent(hotels::setHotelState);
		             Optional.ofNullable(hotel.getHotelCity()).ifPresent(hotels::setHotelCity);
		             Optional.ofNullable(hotel.getRating()).ifPresent(hotels::setRating);
		             hotelrepo.save(hotels);
		             message="Hotel details Edited Successfully for"+" "+userName;
	             }
	       else
                 {
                   message = "Unauthorized: User details do not match for"+" "+userName;
                 }
         }
         else {
           message = "Hotel details not found for"+" "+userName;
              }
     }
	catch (Exception e) {
        message = "An error occurred while editing hotel details";
        e.printStackTrace();
    }
	
	return message;
}

@Override
public String editroomdetails(Long roomId, Room rooms,String userName) {
	String message="";
	try
	{
	   Room room=roomrepo.findByRoomId(roomId);
	   if(room!=null)
	   {
	        User  userdetails=userrepo.findByUserName(userName);
	        Long roomid= room.getHotelId().getUserId().getUserId();
	        if(userdetails!=null && roomid==userdetails.getUserId())
	            {
		          Optional.ofNullable(rooms.getCapacity()).ifPresent(room::setCapacity);
		          Optional.ofNullable(rooms.getPricePerDay()).ifPresent(room::setPricePerDay);
		          Optional.ofNullable(rooms.getRoomNumber()).ifPresent(room::setRoomNumber);
		          Optional.ofNullable(rooms.getRoomType()).ifPresent(room::setRoomType);
		          Optional.ofNullable(rooms.getStatus()).ifPresent(room::setStatus);
                  roomrepo.save(room);
		          message="Room details Edited Successfully for"+" "+userName;
	            }
	       else
                {
               message = "Unauthorized: User details do not match for"+" "+userName;
                }
	    }
	   else {
           message = "Room Facilities details not found for"+" "+userName;
            }
	}
	catch (Exception e) {
        message = "An error occurred while editing room details";
        e.printStackTrace();
    }
	
	return message;
}

@Override
public String editHotelFacilities(Long hotelfacId, HotelFacilities hotelfacilities,String userName) {
	 String message="";
	
	 try {
	        HotelFacilities hotelfac = hotelfacrepo.findByhotelfacId(hotelfacId);
	        if (hotelfac != null) {
	            User userdetails = userrepo.findByUserName(userName);
	            if (userdetails != null && userdetails.getUserId().equals(hotelfac.getHotelId().getUserId().getUserId())) {
	                Optional.ofNullable(hotelfacilities)
	                        .ifPresent(facilities -> {
	                            hotelfac.setCctv(facilities.isCctv());
	                            hotelfac.setFitnessAllowed(facilities.isFitnessAllowed());
	                            hotelfac.setFreeCarParking(facilities.isFreeCarParking());
	                            hotelfac.setFreeWifi(facilities.isFreeWifi());
	                            hotelfac.setLaundaryService(facilities.isLaundaryService());
	                            hotelfac.setPetsAllowed(facilities.isPetsAllowed());
	                            hotelfac.setRestranaunt(facilities.isRestranaunt());
	                            hotelfac.setSwimingPool(facilities.isSwimingPool());
	                        });
	                hotelfacrepo.save(hotelfac);
	                message = "Hotel Facilities details Edited Successfully"+" "+userName;
	            } else {
	                message = "Unauthorized: User details do not match"+" "+userName;
	            }
	        } else {
	            message = "Hotel Facilities details not found"+" "+userName;
	        }
	    } catch (Exception e) {
	        message = "An error occurred while editing Hotel Facilities details";
	        e.printStackTrace();
	    }
	
	return message;
}

public void setCancellationRepo(CancellationRepo cancellationRepo2) {
	this.cancellationRepo=cancellationRepo2;
	
}

public void setBookingRepo(BookingRepo bookingRepo2) {
	this.bookingrepo=bookingRepo2;
	
}

public void setBillRepo(BillRepo billRepo2) {
    this.billrepo=billRepo2;

}

@Override
public String editroomFacilities(Long roomFacId, roomFacilities roomfacilities) {
	String message="";
	try
	{
		roomFacilities roomfac=roomfacrepo.findByRoomfacId(roomFacId);
		if(roomfac!=null)
		{
            User userdetails = userrepo.findByUserName(userName);
		if(userdetails!=null && userdetails.getUserId()==roomfac.getRoomId().getHotelId().getUserId().getUserId())
		{
			Optional.ofNullable(roomfacilities).ifPresent(facilities -> {
			    roomfac.setAirConditioning(facilities.isAirConditioning());
			    roomfac.setBalcony(facilities.isBalcony());
			    roomfac.setExtraBeds(facilities.isExtraBeds());
			    roomfac.setKitchen(facilities.isKitchen());
			    roomfac.setRefrigerator(facilities.isRefrigerator());
			    roomfac.setwashingMachine(facilities.iswashingMachine());
			});
			roomfacrepo.save(roomfac);
			message="Room Facilities details Edited Successfully for"+" "+userName;
		
			}
		   else {
                 message = "Unauthorized: User details do not match"+" "+userName;
                }
      } 
		else {
               message = "Room Facilities details not found for"+" "+userName;
             }
}
		
	catch(Exception e)
	{
		  message = "An error occurred while editing roomfacilites details";
          e.printStackTrace();
	}

	return message;
}

public String extractUserName(String token)
{
	String tokens;

	if(token!=null && token.startsWith("Bearer "))
	{
		System.out.println("Extract UserName:"+token);
        tokens = token.substring(7);
		userName=jwtService.extractUsername(tokens);
		return userName;
    }
	return null;
}

@Override
public List<Room> AvailableRoomsForHotelId(Room rooms) {
	List<Room>countRoomsForHotelId;
	Optional<Room>room=roomrepo.existsByHotelId(rooms.getHotelId());
	if(room.isPresent())
	{
		countRoomsForHotelId=roomrepo.findByHotelId(rooms.getHotelId());
		countRoomsForHotelId= countRoomsForHotelId.stream()
				.filter(roomss->roomss.getStatus().equalsIgnoreCase("Available"))		
				.collect(Collectors.toList());
	}
	else
	{
		countRoomsForHotelId=null;
	}
	return countRoomsForHotelId;	
}

@Override
public List<Room> totalRoomsForHotelId(Room rooms) {
	List<Room>totalRoom=null;
	Optional<Room>totalroom=roomrepo.existsByHotelId(rooms.getHotelId());
	if(totalroom.isPresent())
	{
	 totalRoom=roomrepo.findByHotelId(rooms.getHotelId());
	}
	else
	{
		totalRoom=null;
	}
	return totalRoom;
}

@Override
public List<roomFacilities> roomFacForRoomId(roomFacilities rooms) {
	   List<roomFacilities>roomfacilities=null;
	   Optional<roomFacilities> roomfac=roomfacrepo.existsByRoomId(rooms.getRoomId());
	if(roomfac.isPresent())
	{
	     roomfacilities=roomfacrepo.findByRoomId(rooms.getRoomId());
	}
	else
	{
		roomfacilities=null;
	}
	return roomfacilities;
}

@Override
public List<HotelFacilities> hotelFacForHotelId(HotelFacilities hotelfac) {
	   List<HotelFacilities> hotelfacilities=null;
	   Optional<HotelFacilities>hotelfacId=hotelfacrepo.existsByHotelId(hotelfac.getHotelId());
	   if(hotelfacId.isPresent())
	   {
	       hotelfacilities=hotelfacrepo.findByHotelId(hotelfac.getHotelId());
	   }
	   else
	   {
		  hotelfacilities=null;  
	   }
	   return hotelfacilities;
}

@Override
public List<Hotels> getHotelsforLocation(Hotels hotels) {
	  List<Hotels>hotelList;
	  Optional<Hotels>hotellocation= hotelrepo.existsByLocationId(hotels.getLocationId());
	  if(hotellocation.isPresent())
	  {
		  hotelList=hotelrepo.findByLocationId(hotels.getLocationId());
	  }
	  else
	  {
		  hotelList=null;
	  }
	  return hotelList;
	
}

@Override
public List<Hotels> getFilterByStarRating(Double starRating) throws IllegalAccessException {
	List<Hotels>hotelsList,result;
	System.out.println("Star rating:"+starRating);
	if(starRating<=5.0)
	{
		hotelsList=hotelrepo.findAll();
	    result= hotelsList.stream()
	      .filter(hotel->hotel.getStarRating()>=starRating)
	      .sorted(Comparator.comparingDouble(Hotels::getStarRating).reversed())
	      .collect(Collectors.toList());
	}
	else
	{
		throw new IllegalAccessException("provide the rating upto 5.0");

     }
	return result;
	

}

@Override
public List<Hotels> getFilterByUserRating(Double userRating) throws IllegalAccessException {
	List<Hotels>hotelsList,result;
	System.out.println("Star rating:"+userRating);
	if(userRating<=10.0)
	{
		hotelsList=hotelrepo.findAll();
	    result=hotelsList.stream()
	      .filter(hotels->hotels.getRating()>=userRating)
	      .sorted(Comparator.comparingDouble(Hotels::getRating).reversed())
	      .collect(Collectors.toList());
	}

		else {
			throw new IllegalAccessException("provide the rating upto 10.0");
        }
	return result;
	
}

@Override
public List<Room> getFilterByPrice(Double minimumamount, Double maximumamount) throws IllegalAccessException {
	List<Room>roomsList,result;
	if(minimumamount<=0.0)
	{
		throw new IllegalAccessException("provide the minimum amount greter than 0");

	}
	else
	{
		List<Room>roomList=roomrepo.findAll();
		result=roomList.stream().filter(roomprice->(roomprice.getPricePerDay()>=minimumamount && roomprice.getPricePerDay()<=maximumamount))
		.sorted(Comparator.comparingDouble(Room::getPricePerDay))
		.collect(Collectors.toList());
		
	}
	return result;
}

@Override
public void updateRoomStatuses() {
	LocalDateTime currentDate = LocalDateTime.now();
	List<Booking>bookingDetails=bookingrepo.findAll();
	for(Booking bookdetails:bookingDetails)
	{
		if(bookdetails.getCheckOutDate()!=null && currentDate.isAfter(bookdetails.getCheckOutDate()))
		{

			  getBookingRoomId(bookdetails);
			  bookdetails.setCheckOutDate(null);
			  bookingrepo.save(bookdetails);
		}
	}
	
	
}

private void getBookingRoomId(Booking bookdetails) {
        List<RoomBooking>roomBookingDetails=roombookingrepo.findAllBybookingId(bookdetails)	;
        for(RoomBooking roombookdetails:roomBookingDetails)
        {
        	roombookdetails.setActiveStatus(true);
        	getRoomstatus(roombookdetails.getRoomId().getRoomId());
        	roombookingrepo.save(roombookdetails);
        }
}

private void getRoomstatus(Long roomId) {
        Optional<Room>roomdetails=roomrepo.findById(roomId);
        roomdetails.get().setStatus("Available");
        roomrepo.save(roomdetails.get());
}
}
