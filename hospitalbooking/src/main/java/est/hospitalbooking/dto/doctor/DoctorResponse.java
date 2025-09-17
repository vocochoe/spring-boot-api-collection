package est.hospitalbooking.dto.doctor;

import est.hospitalbooking.domain.Doctor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoctorResponse {
    private Long id;
    private String name;
    private String specialty;
    private Long userId;

    public static DoctorResponse from(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialty(doctor.getSpecialty())
                .userId(doctor.getUser() != null ? doctor.getUser().getId() : null)
                .build();
    }
}
