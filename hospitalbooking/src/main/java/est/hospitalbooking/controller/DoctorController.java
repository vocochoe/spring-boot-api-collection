package est.hospitalbooking.controller;

import est.hospitalbooking.dto.doctor.DoctorRequest;
import est.hospitalbooking.dto.doctor.DoctorResponse;
import est.hospitalbooking.service.DoctorService;
import est.hospitalbooking.service.AppointmentService;
import est.hospitalbooking.dto.appointment.AppointmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    /**
     * 의사 등록
     * POST /api/doctors
     */
    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.createDoctor(request));
    }

    /**
     * 전체 의사 목록 조회
     * GET /api/doctors
     */
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    /**
     * 특정 의사의 예약 가능 시간 조회
     * GET /api/doctors/{doctorId}/available-times
     */
    @GetMapping("/{doctorId}/available-times")
    public ResponseEntity<List<LocalDateTime>> getAvailableTimes(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.getAvailableTimes(doctorId));
    }

    /**
     * 특정 의사의 예약 목록 조회
     * GET /api/doctors/{doctorId}/appointments
     */
    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }
}
