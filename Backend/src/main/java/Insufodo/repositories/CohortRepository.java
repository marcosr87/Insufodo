package Insufodo.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import Insufodo.models.Cohort;

@Repository
public interface CohortRepository extends JpaRepository<Cohort, Integer> {
}