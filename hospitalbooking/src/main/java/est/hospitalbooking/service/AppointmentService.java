package est.hospitalbooking.service;

import est.hospitalbooking.domain.Appointment;
import est.hospitalbooking.domain.Doctor;
import est.hospitalbooking.domain.User;
import est.hospitalbooking.dto.appointment.AppointmentRequest;
import est.hospitalbooking.dto.appointment.AppointmentResponse;
import est.hospitalbooking.exception.InvalidAppointmentException;
import est.hospitalbooking.exception.NotFoundException;
import est.hospitalbooking.repository.AppointmentRepository;
import est.hospitalbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final UserRepository userRepository;

    /**
     * 예약 생성
     * - 조건:
     *   1) 예약 시간은 현재 시각보다 미래여야 함
     *   2) 해당 의사의 동일한 시간대에 예약이 없어야 함
     */
    public AppointmentResponse createAppointment(Long doctorId, AppointmentRequest request) {
        Doctor doctor = doctorService.findById(doctorId);
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new NotFoundException("해당 환자를 찾을 수 없습니다: " + request.getPatientId()));

        if (request.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new InvalidAppointmentException("예약 시간은 현재 시각 이후여야 합니다: " + request.getAppointmentTime());
        }


        boolean exists = appointmentRepository.existsByDoctorAndAppointmentTime(doctor, request.getAppointmentTime());

        if (exists) {
            throw new InvalidAppointmentException("이미 예약된 시간대입니다.");
        }

        Appointment appointment = Appointment.builder()
                .patientName(request.getPatientName())
                .appointmentTime(request.getAppointmentTime())
                .doctor(doctor)
                .patient(patient)
                .build();

        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    /**
     * 예약 취소
     * - 예약 ID로 예약을 조회하고 삭제
     */
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("해당 예약을 찾을 수 없습니다: " + appointmentId));

        appointmentRepository.delete(appointment);
    }

    /**
     * 의사 예약 목록 조회
     * - 특정 의사에게 들어온 모든 예약을 조회
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        Doctor doctor = doctorService.findById(doctorId);

        return appointmentRepository.findByDoctor(doctor).stream()
                .map(AppointmentResponse::from)
                .collect(Collectors.toList());
    }
}
