package com.voice.voice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatLogDTO { // 채팅 정보를 저장하는 클래스

    public enum MessageType{
        ENTER, TALK
    }

    private MessageType messageType; // 메시지 타입
    private String mid; // 아이디
    private String message; // 메세지 내용
    private String timestamp; // 메세지 전송 시각
    private Long roomId; // 방 번호

}
