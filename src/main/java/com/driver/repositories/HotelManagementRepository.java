package com.driver.repositories;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HotelManagementRepository {

    private Map<String, Hotel> hotelMap = new HashMap<>();
    private Map<Integer, User> userMap = new HashMap<>();
    private Map<String, Booking> bookingMap = new HashMap<>();
//    private Map<Integer,String> bookingMap = new HashMap<>();


    public Optional<Hotel> getHotel(String hotelName) {

        if(hotelMap.containsKey(hotelName)){
            return Optional.of(hotelMap.get(hotelName));
        }
        return Optional.empty();
    }

    public void addHotel(Hotel hotel) {

        hotelMap.put(hotel.getHotelName(),hotel);
        return;
    }

    public void addUser(User user) {

        userMap.put(user.getaadharCardNo(),user);
        return;
    }

    public void mapFacilities(Map<String, List<Facility>> facilities) {

        for(String hotelName : hotelMap.keySet()){
            facilities.put(hotelName,hotelMap.get(hotelName).getFacilities());
        }
        return;
    }

    public Optional<Integer> getBookings(Integer aadharCard) {

        Integer ct = 0;


        for(String bookingId : bookingMap.keySet()){

            if(bookingMap.get(bookingId).getBookingAadharCard() == aadharCard) ct++;
        }

        return Optional.of(ct);
    }

    public Optional<Booking> getBooking(String bookingId) {

        if(bookingMap.containsKey(bookingId))
            return Optional.of(bookingMap.get(bookingId));

        return Optional.empty();
    }

    public Optional<Integer> getAvailableRoomsInHotel(String hotelName) {

        if(hotelMap.containsKey(hotelName)){
            return Optional.of(hotelMap.get(hotelName).getAvailableRooms());
        }
        return Optional.empty();
    }

    public Optional<Integer> getPricePerRoom(String hotelName) {

        if(hotelMap.containsKey(hotelName)){
            return Optional.of(hotelMap.get(hotelName).getPricePerNight());
        }
        return Optional.empty();
    }

    public void bookARoom(Booking booking) {

        bookingMap.put(booking.getBookingId(),booking);

        Hotel hotel = hotelMap.get(booking.getHotelName());
        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());
        hotelMap.put(hotel.getHotelName(),hotel);
        return;
    }

    public Optional<Hotel> updateFacilities(List<Facility> newFacilities, String hotelName) {

        if(!hotelMap.containsKey(hotelName))
            return Optional.empty();


        Hotel hotel = hotelMap.get(hotelName);
        List<Facility> facilities = hotel.getFacilities();

        for(Facility facility : newFacilities){

            if(!facilities.contains(facility)){
                facilities.add(facility);
            }
        }

        hotel.setFacilities(facilities);
        hotelMap.put(hotelName,hotel);
        return Optional.of(hotel);
    }
}