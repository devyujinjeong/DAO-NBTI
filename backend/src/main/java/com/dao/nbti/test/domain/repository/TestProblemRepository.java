package com.dao.nbti.test.domain.repository;

import com.dao.nbti.test.domain.aggregate.TestProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestProblemRepository extends JpaRepository<TestProblem, Integer> {

}
