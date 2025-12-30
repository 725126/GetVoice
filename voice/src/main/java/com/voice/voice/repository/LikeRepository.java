package com.voice.voice.repository;

import com.voice.voice.model.LikeVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeVO, Integer> {
}
