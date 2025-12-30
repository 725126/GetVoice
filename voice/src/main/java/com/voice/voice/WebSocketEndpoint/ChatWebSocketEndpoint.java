package com.voice.voice.WebSocketEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voice.voice.DTO.ChatLogDTO;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@ServerEndpoint(value = "/chat")
@Service
public class ChatWebSocketEndpoint {
    // ▽ 사용자를 관리할 수 있는 Set 전역변수를 선언(현재 연결된 세션들)
    private static Set<Session> CLIENTS = Collections.synchronizedSet(new HashSet<>());
    // ▽ chatRoomId: {session1, session2}
    private static Map<Long,Set<Session>> chatRoomSessionMap = Collections.synchronizedMap(new HashMap<>());
    private static String fileName;

    // @OnOpen > 사용자가 접속하면 session을 추가
    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.toString());

        if (CLIENTS.contains(session)) {
            System.out.println("이미 연결된 세션입니다. > " + session);
        } else {
            CLIENTS.add(session);
            System.out.println("새로운 세션입니다. > " + session);
        }
    }

    // @OnClose > 사용자가 종료되면 session을 제거
    @OnClose
    public void onClose(Session session) throws Exception {
        CLIENTS.remove(session);
        System.out.println("세션을 닫습니다. : " + session);
    }

    // @OnMessage > 사용자가 입력한 메세지를 받고 접속되어있는 사용자에게 메세지를 제거
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {

        // JSON 문자열을 Java 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ChatLogDTO chatLogDTO = objectMapper.readValue(message, ChatLogDTO.class);
        System.out.println("@OnMessage > ChatLogDTO 출력:" + chatLogDTO);


        Long chatRoomId = chatLogDTO.getRoomId();
        // 메모리 상에 채팅방에 대한 세션 없으면 만들어줌
        if(!chatRoomSessionMap.containsKey(chatRoomId)){
            chatRoomSessionMap.put(chatRoomId, new HashSet<>());
            System.out.println("채팅방 세션 생성, chatRoomId: " + chatRoomId);
        }

        Set<Session> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        // message 에 담긴 타입을 확인
        // 이때 message 에서 getType 으로 가져온 내용이
        // ChatDTO 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면
        if (chatLogDTO.getMessageType().equals(ChatLogDTO.MessageType.ENTER)) {
            // sessions 에 넘어온 session 을 담고,
            chatRoomSession.add(session);
            System.out.println("방에 추가된 세션: " + session);
        }
        if (chatRoomSession.size()>=3) {
            removeClosedSession(chatRoomSession);
        }

        System.out.println("입력된 메세지입니다. > " + message);

        if (chatLogDTO.getMessageType().equals(ChatLogDTO.MessageType.TALK)){
            for (Session client : chatRoomSession) {
                System.out.println("세션: " + client + ", 메세지를 전달합니다. > " + message);
                client.getBasicRemote().sendText(message);
            }

            // 한 채팅에서 여러 로그 파일이 생성되는 것을 방지 - 채팅방 마다 로그 파일 생성
            fileName = chatLogDTO.getRoomId() + "_" + formatDate(chatLogDTO.getTimestamp()) + ".txt";
            System.out.println("파일명: " + fileName);

            if (fileName != null) { // 클라이언트가 보낸 메시지를 해당 채팅방의 파일에 저장
                saveChatMessageToFile(fileName, message);
            } else {
                System.out.println("fileName is null");
            }
        }
    }

    private void removeClosedSession(Set<Session> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !CLIENTS.contains(sess));
    }


    private void saveChatMessageToFile(String fileName, String message) { // 채팅 로그를 파일로 저장하는 메소드
        try {
            String filePath = "C:/Users/82104/Desktop/voice/src/main/resources/static/logFile/" + fileName;
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true); // true 옵션을 주면 기존 파일에 이어서 작성합니다.
            writer.write(message + "\n");
            writer.close();

            System.out.println("filePath: " + filePath);
            System.out.println("fileName: " + fileName);
            System.out.println("Chat message saved to file successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("로그 파일 저장 오류: " + e.getMessage());
        }
    }

    public static String formatDate(String inputDate) {
        // 주어진 문자열의 형식에 맞는 SimpleDateFormat 정의
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy. MM. dd. a hh:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // 주어진 문자열을 Date 객체로 파싱
            Date date = inputFormat.parse(inputDate);

            // 원하는 포맷으로 변환
            String formattedDate = outputFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


}
