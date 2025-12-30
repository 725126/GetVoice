package com.voice.voice.repository;

import com.voice.voice.model.PaymentVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentVO, Integer> {
}
