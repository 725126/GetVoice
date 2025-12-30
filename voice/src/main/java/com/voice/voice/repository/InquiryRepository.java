package com.voice.voice.repository;

import com.voice.voice.model.InquiryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryVO, Integer> {
}
