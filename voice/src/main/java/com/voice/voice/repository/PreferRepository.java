package com.voice.voice.repository;

import com.voice.voice.model.PreferVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferRepository extends JpaRepository<PreferVO, Integer> {
}
