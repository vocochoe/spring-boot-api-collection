package est.hospitalbooking.dto.appointment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequest {
    private String patientName;
    private LocalDateTime appointmentTime;
    private Long patientId; // 예약자(User)
}
