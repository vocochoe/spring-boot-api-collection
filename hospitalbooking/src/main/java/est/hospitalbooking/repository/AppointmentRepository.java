package est.hospitalbooking.repository;

import est.hospitalbooking.domain.Appointment;
import est.hospitalbooking.domain.Doctor;
import est.hospitalbooking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 특정 의사의 예약 목록 조회
    List<Appointment> findByDoctor(Doctor doctor);

    // 예약 시간 중복 체크 (해당 시간에 이미 예약이 있는지)
    boolean existsByDoctorAndAppointmentTime(Doctor doctor, LocalDateTime appointmentTime);
}
