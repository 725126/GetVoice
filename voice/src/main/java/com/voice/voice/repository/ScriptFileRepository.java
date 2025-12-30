package com.voice.voice.repository;

import com.voice.voice.model.ScriptFileVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptFileRepository extends JpaRepository<ScriptFileVO, Integer> {
}
