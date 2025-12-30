package com.voice.voice.etc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voice.voice.DTO.ChatLogDTO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatLogReader {

    List<ChatLogDTO> logDTOList = new ArrayList<>();

    public List<ChatLogDTO> printLog(String filePath){ // 매개변수의 주소에 위치한 채팅 로그 파일을 읽어들여 List<ChatLogDTO>로 반환하는 메소드

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            ObjectMapper objectMapper = new ObjectMapper();

            while ((line = br.readLine()) != null) {
                ChatLogDTO message = objectMapper.readValue(line, ChatLogDTO.class);

                logDTOList.add(message);

            }

            return logDTOList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
