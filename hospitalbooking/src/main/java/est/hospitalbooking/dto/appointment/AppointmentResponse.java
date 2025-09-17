package est.hospitalbooking.dto.appointment;

import est.hospitalbooking.domain.Appointment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AppointmentResponse {
    private Long id;
    private String patientName;
    private LocalDateTime appointmentTime;
    private Long doctorId;
    private Long patientId;

    public static AppointmentResponse from(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientName(appointment.getPatientName())
                .appointmentTime(appointment.getAppointmentTime())
                .doctorId(appointment.getDoctor() != null ? appointment.getDoctor().getId() : null)
                .patientId(appointment.getPatient() != null ? appointment.getPatient().getId() : null)
                .build();
    }
}
