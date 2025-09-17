package est.hospitalbooking.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String username;
    private String password;
    private String role; // ADMIN / DOCTOR / PATIENT
}
