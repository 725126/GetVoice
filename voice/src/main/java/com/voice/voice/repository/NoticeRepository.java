package com.voice.voice.repository;

import com.voice.voice.model.NoticeVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeVO, Integer> {
}
