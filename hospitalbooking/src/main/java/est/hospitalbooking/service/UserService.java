package est.hospitalbooking.service;

import est.hospitalbooking.domain.User;
import est.hospitalbooking.dto.user.UserRequest;
import est.hospitalbooking.dto.user.UserResponse;
import est.hospitalbooking.exception.NotFoundException;
import est.hospitalbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * 유저 생성
     * - username, password, role 정보를 받아 저장
     */
    public UserResponse createUser(UserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(request.getRole()) // ADMIN / DOCTOR / PATIENT
                .build();
        return UserResponse.from(userRepository.save(user));
    }

    /**
     * 단일 유저 조회
     * - ID로 유저를 조회
     */
    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        return UserResponse.from(
                userRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다: " + id))
        );
    }

    /**
     * 전체 유저 조회
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 내부 조회용: ID로 User 조회
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다: " + id));
    }
}
