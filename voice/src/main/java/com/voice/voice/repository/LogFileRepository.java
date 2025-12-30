package com.voice.voice.repository;

import com.voice.voice.model.LogFileVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogFileRepository extends JpaRepository<LogFileVO, Integer> {
}
