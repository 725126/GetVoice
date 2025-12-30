package com.voice.voice.repository;

import com.voice.voice.model.RepVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepRepository extends JpaRepository<RepVO, Integer> {
}
