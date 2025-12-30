package com.voice.voice.repository;

import com.voice.voice.model.ChatVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatVO, Integer> {

}
