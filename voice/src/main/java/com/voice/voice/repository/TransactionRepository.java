package com.voice.voice.repository;

import com.voice.voice.model.TransactionVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionVO, Integer> {
}
