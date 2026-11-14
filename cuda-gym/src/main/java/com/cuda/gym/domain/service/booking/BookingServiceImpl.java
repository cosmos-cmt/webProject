package com.cuda.gym.domain.service.booking;

import com.cuda.gym.domain.model.ClassBooking;
import com.cuda.gym.domain.model.GymClass;
import com.cuda.gym.domain.model.Membership;
import com.cuda.gym.domain.model.User;
import com.cuda.gym.domain.service.user.UserService;
import com.cuda.gym.domain.service.membership.MembershipService;
import com.cuda.gym.domain.service.classmgmt.GymClassService;
import com.cuda.gym.domain.repository.ClassBookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private static final long CANCELLATION_LIMIT_HOURS = 24L;

    private final ClassBookingRepository bookingRepository;
    private final GymClassService classScheduleService;
    private final MembershipService membershipService;
    private final UserService userService;
    private final Clock clock;

    public BookingServiceImpl(ClassBookingRepository bookingRepository,
                              GymClassService classScheduleService,
                              MembershipService membershipService,
                              UserService userService,
                              Clock clock) {
        this.bookingRepository = bookingRepository;
        this.classScheduleService = classScheduleService;
        this.membershipService = membershipService;
        this.userService = userService;
        this.clock = clock;
    }

       public BookingServiceImpl(BookingRepository bookingRepository,
                              ClassScheduleService classScheduleService,
                              MembershipService membershipService,
                              UserService userService) {
        this(bookingRepository, classScheduleService, membershipService, userService, Clock.systemDefaultZone());
    }

    @Override
    public Booking createBooking(Long memberId, Long classId) {
        User member = userService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        GymClass clazz = classScheduleService.findById(classId);

                Membership currentMembership = membershipService.findCurrentMembershipForMember(member.getId())
                .orElseThrow(() -> new IllegalStateException("Member does not have an active membership"));

        if (!currentMembership.isActiveOn(clazz.getStartDateTime())) {
            throw new IllegalStateException("Membership is not active at class time");
        }

                long existingBookings = bookingRepository.countByClazzIdAndStatus(clazz.getId(), Booking.Status.BOOKED);
        if (existingBookings >= clazz.getCapacity()) {
            throw new IllegalStateException("Class is fully booked");
        }

              boolean alreadyBooked = bookingRepository.existsByMemberIdAndClazzIdAndStatus(
                member.getId(),
                clazz.getId(),
                Booking.Status.BOOKED
        );
        if (alreadyBooked) {
            throw new IllegalStateException("You have already booked this class");
        }

        Booking booking = new Booking();
        booking.setMember(member);
        booking.setClazz(clazz);
        booking.setStatus(Booking.Status.BOOKED);
        booking.setCreatedAt(LocalDateTime.now(clock));

        return bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != Booking.Status.BOOKED) {
            throw new IllegalStateException("Only active bookings can be cancelled");
        }

        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime classStart = booking.getClazz().getStartDateTime();

        if (!isCancellationAllowed(now, classStart)) {
            throw new IllegalStateException("Cancellation not allowed within 24 hours of class start");
        }

        booking.setStatus(Booking.Status.CANCELLED);
        booking.setCancelledAt(now);
        bookingRepository.save(booking);
    }

    boolean isCancellationAllowed(LocalDateTime now, LocalDateTime classStart) {
        return now.isBefore(classStart.minusHours(CANCELLATION_LIMIT_HOURS));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> findUpcomingBookingsForMember(Long memberId) {
        LocalDateTime now = LocalDateTime.now(clock);
        return bookingRepository.findByMemberIdAndClazz_StartDateTimeAfterAndStatus(
                memberId,
                now,
                Booking.Status.BOOKED
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> findPastBookingsForMember(Long memberId) {
        LocalDateTime now = LocalDateTime.now(clock);
        return bookingRepository.findByMemberIdAndClazz_StartDateTimeBeforeAndStatusIn(
                memberId,
                now,
                List.of(Booking.Status.BOOKED, Booking.Status.ATTENDED)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> findById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }
}