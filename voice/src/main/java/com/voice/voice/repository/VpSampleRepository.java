package com.voice.voice.repository;

import com.voice.voice.model.VpSampleVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VpSampleRepository extends JpaRepository<VpSampleVO, Integer> {
    void deleteBypId(int pId);
    List<VpSampleVO> findBypId(int pId);
}
