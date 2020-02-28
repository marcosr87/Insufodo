package Insufodo.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import Insufodo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}