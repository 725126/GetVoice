package com.voice.voice.repository;

import com.voice.voice.model.MemberVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberVO, String> {
    List<MemberVO> findBymNn(String mNn);
}
