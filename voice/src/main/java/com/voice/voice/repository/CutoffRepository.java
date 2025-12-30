package com.voice.voice.repository;

import com.voice.voice.model.CutoffVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CutoffRepository extends JpaRepository<CutoffVO, Integer> {
}
