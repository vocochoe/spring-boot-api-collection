package est.hospitalbooking.repository;

import est.hospitalbooking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
