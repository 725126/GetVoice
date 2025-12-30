package com.voice.voice.repository;

import com.voice.voice.model.PostFileVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostFileRepository extends JpaRepository<PostFileVO, Integer> {
    List<PostFileVO> findBypId(int pId);
}
