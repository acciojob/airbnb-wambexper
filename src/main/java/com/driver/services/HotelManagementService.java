package com.driver.services;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.driver.repositories.HotelManagementRepository;
import io.swagger.models.auth.In;

import java.util.*;

public class HotelManagementService {

    HotelManagementRepository hotelManagementRepository = new HotelManagementRepository();

    public Optional<Boolean> addHotel(Hotel hotel) {

        if(hotel == null || hotel.getHotelName() == null) return Optional.empty();

        Optional<Hotel> optionalHotel = getHotel(hotel.getHotelName());

        if(optionalHotel.isPresent()) return Optional.empty();

        hotelManagementRepository.addHotel(hotel);
        return Optional.of(Boolean.TRUE);
    }

    public Optional<Hotel> getHotel(String hotelName) {

        return hotelManagementRepository.getHotel(hotelName);
    }

    public Optional<Integer> addUser(User user) {

        hotelManagementRepository.addUser(user);
        return Optional.of(user.getaadharCardNo());
    }

    public Optional<String> getHotelWithMostFacilities() {

        Map<String, List<Facility>> facilities = new HashMap<>();
        hotelManagementRepository.mapFacilities(facilities);

        String ans_hotel = "";
        int max_fac = 0;

        for(String hotelName : facilities.keySet()){

            if(facilities.get(hotelName).size()>max_fac){
                max_fac = facilities.get(hotelName).size();
                ans_hotel = hotelName;
            }

            if(facilities.get(hotelName).size() == max_fac){

                if (ans_hotel.compareTo(hotelName)>0)
                    ans_hotel = hotelName;
            }
        }

        if(max_fac == 0) return Optional.empty();
        return Optional.of(ans_hotel);
    }

    public Optional<Integer> getBookings(Integer aadharCard) {

        return hotelManagementRepository.getBookings(aadharCard);
    }

    public Optional<Integer> bookARoom(Booking booking) {

        String bookingId = UUID.randomUUID().toString();

        while(Boolean.TRUE){

            Optional<Booking> optionalBooking = getBooking(bookingId);
            if(optionalBooking.isEmpty()){
                break;
            }else{
                bookingId = UUID.randomUUID().toString();
            }
        }

        booking.setBookingId(bookingId);

        Optional<Boolean> optionalBooking = isBookingPossible(booking);

        if(optionalBooking.isEmpty()) return Optional.empty();

        // vrna booking is possible go book rooms

        Optional<Integer> pricePerRoom = getPricePerRoom(booking.getHotelName());
        if(pricePerRoom.isEmpty()) return Optional.empty();

        booking.setAmountToBePaid(pricePerRoom.get()*booking.getNoOfRooms());

        hotelManagementRepository.bookARoom(booking);
        return Optional.of(booking.getAmountToBePaid());
    }

    private Optional<Integer> getPricePerRoom(String hotelName) {

        return hotelManagementRepository.getPricePerRoom(hotelName);
    }

    private Optional<Boolean> isBookingPossible(Booking booking) {

        Optional<Integer> availableRooms = getAvailableRoomsInHotel(booking.getHotelName());
        if(availableRooms.isEmpty()) return Optional.empty();

        if(availableRooms.get()>=booking.getNoOfRooms()) return Optional.of(Boolean.TRUE);
        return Optional.empty();
    }

    private Optional<Integer> getAvailableRoomsInHotel(String hotelName) {

        return hotelManagementRepository.getAvailableRoomsInHotel(hotelName);
    }

    public Optional<Booking> getBooking(String bookingId) {

        return hotelManagementRepository.getBooking(bookingId);
    }

    public Optional<Hotel> updateFacilities(List<Facility> newFacilities, String hotelName) {

        return hotelManagementRepository.updateFacilities(newFacilities,hotelName);
    }
}