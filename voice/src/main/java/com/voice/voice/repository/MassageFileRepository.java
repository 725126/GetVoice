package com.voice.voice.repository;

import com.voice.voice.model.MassageFileVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MassageFileRepository extends JpaRepository<MassageFileVO, Integer> {
}
