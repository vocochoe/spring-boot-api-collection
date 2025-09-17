package est.hospitalbooking.controller;

import est.hospitalbooking.dto.appointment.AppointmentRequest;
import est.hospitalbooking.dto.appointment.AppointmentResponse;
import est.hospitalbooking.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * 예약 생성
     * POST /api/doctors/{doctorId}/appointments
     */
    @PostMapping("/doctors/{doctorId}/appointments")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @PathVariable Long doctorId,
            @RequestBody AppointmentRequest request
    ) {
        return ResponseEntity.ok(appointmentService.createAppointment(doctorId, request));
    }

    /**
     * 예약 취소
     * DELETE /api/appointments/{appointmentId}
     */
    @DeleteMapping("/appointments/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }
}
