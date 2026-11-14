package com.cuda.gym.domain.service.booking;

import com.cuda.gym.domain.model.ClassBooking;

import java.util.List;
import java.util.Optional;


public interface BookingService {

        ClassBooking createBooking(Long memberId, Long classId);

       void cancelBooking(Long bookingId);

       List<ClassBooking> findUpcomingBookingsForMember(Long memberId);

       List<ClassBooking> findPastBookingsForMember(Long memberId);

        Optional<ClassBooking> findById(Long bookingId);
}