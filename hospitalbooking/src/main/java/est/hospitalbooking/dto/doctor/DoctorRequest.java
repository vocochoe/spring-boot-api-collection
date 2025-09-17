package est.hospitalbooking.dto.doctor;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequest {
    private String name;
    private String specialty;
    private Long userId;
}
