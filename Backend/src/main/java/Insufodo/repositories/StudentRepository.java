package Insufodo.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import Insufodo.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}