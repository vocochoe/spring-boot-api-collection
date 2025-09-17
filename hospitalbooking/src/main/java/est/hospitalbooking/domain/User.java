package est.hospitalbooking.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    // ADMIN / DOCTOR / PATIENT
    @Column(nullable = false, length = 20)
    private String role;

    // 의사 계정인 경우만 Doctor와 연결
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Doctor doctor;
}
