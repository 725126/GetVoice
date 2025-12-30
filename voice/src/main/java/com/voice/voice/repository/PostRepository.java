package com.voice.voice.repository;

import com.voice.voice.model.PostVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostVO, Integer> {
    List<PostVO> findBymId(String mId);
}
