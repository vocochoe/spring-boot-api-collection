package est.hospitalbooking.service;

import est.hospitalbooking.domain.Doctor;
import est.hospitalbooking.domain.User;
import est.hospitalbooking.dto.doctor.DoctorRequest;
import est.hospitalbooking.dto.doctor.DoctorResponse;
import est.hospitalbooking.exception.NotFoundException;
import est.hospitalbooking.repository.DoctorRepository;
import est.hospitalbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    /**
     * 의사 등록
     * - User 엔티티를 기반으로 Doctor 엔티티를 생성하여 저장
     */
    public DoctorResponse createDoctor(DoctorRequest request) {
        User doctorUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다: " + request.getUserId()));

        Doctor doctor = Doctor.builder()
                .name(request.getName())
                .specialty(request.getSpecialty())
                .user(doctorUser)
                .build();
        return DoctorResponse.from(doctorRepository.save(doctor));
    }

    /**
     * 전체 의사 목록 조회
     */
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(DoctorResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 예약 가능 시간 조회
     * - 현재 시각 이후 7일간 9시~18시까지의 시간대 중 이미 예약이 없는 시간만 반환
     */
    @Transactional(readOnly = true)
    public List<LocalDateTime> getAvailableTimes(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("해당 의사를 찾을 수 없습니다: " + doctorId));

        List<LocalDateTime> available = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0);

        for (int day = 0; day < 7; day++) {
            for (int hour = 9; hour <= 18; hour++) {
                LocalDateTime candidate = now.plusDays(day).withHour(hour).withMinute(0).withSecond(0).withNano(0);

                boolean reserved = doctor.getAppointments().stream()
                        .anyMatch(a -> a.getAppointmentTime().withSecond(0).withNano(0).equals(candidate));

                if (!reserved && candidate.isAfter(LocalDateTime.now())) {
                    available.add(candidate);
                }
            }
        }
        return available;
    }

    /**
     * 내부 조회용: ID로 Doctor 조회
     */
    public Doctor findById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("해당 의사를 찾을 수 없습니다: " + doctorId));
    }
}
