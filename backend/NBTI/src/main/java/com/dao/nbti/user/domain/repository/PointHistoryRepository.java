package com.dao.nbti.user.domain.repository;

import com.dao.nbti.user.domain.aggregate.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer> {
}
