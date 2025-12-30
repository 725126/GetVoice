package com.voice.voice.controller;
import com.voice.voice.DTO.ChatLogDTO;
import com.voice.voice.DTO.EstCalDTO;
import com.voice.voice.DTO.FileDTO;
import com.voice.voice.etc.ChatLogReader;
import com.voice.voice.model.LogFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/popup/chat")
public class ChatController {

    private EstCalDTO paymentDTO; // 결제하기 - 견적 계산 데이터 저장 클래스
    @PostConstruct
    public void paymentInit() { paymentDTO = new EstCalDTO();
    }

    @Value("${record-script.dir}")
    private String scriptDir; // 대본 파일 저장 경로
    private FileDTO scriptDTO;
    private static Long lastScriptId = 0L;
    @PostConstruct
    public void scriptInit() {
        scriptDTO = new FileDTO();
    }

    @PostMapping("/chat-payment-endpoint") // 결제하기 - 견적 계산 데이터를 전송받는 메소드
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("profit") String profit) {
        int characterCount = 0;

        FileDTO fileDTO = new FileDTO();

        try {
            // 파일을 읽어들일 BufferedReader 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;

            // 파일의 각 라인을 읽어들여 공백을 제외한 글자 수를 계산
            while ((line = reader.readLine()) != null) {
                // 문자열 내의 공백을 제외한 글자 수 계산
                characterCount += line.replaceAll("\\s+", "").length();
            }

            paymentDTO.setCharacterCount(characterCount);
            paymentDTO.setProfit(profit);

            // 실제로는 DB에서 가져와야함
            paymentDTO.setForProfitPrice(100);
            paymentDTO.setNonProfitPrice(40);

            if (profit.equals("for-profit")){
                paymentDTO.setAmount(paymentDTO.getForProfitPrice()*characterCount);
            }
            else if (profit.equals("non-profit")){
                paymentDTO.setAmount(paymentDTO.getNonProfitPrice()*characterCount);
            }

            System.out.println("(14.1) 글자 수 출력: " + characterCount);
            // BufferedReader 닫기
            reader.close();

            // 파일 업로드 수행
            String originalFileName = file.getOriginalFilename();

            // 랜덤(저장용) 파일명 생성
            String randomFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            // 파일 경로 생성
            Path filePath = Paths.get(scriptDir).resolve(randomFileName);

            // 원본 파일명 저장
            fileDTO.setOriginalFileName(originalFileName);

            // 랜덤(저장용) 파일명 저장
            fileDTO.setSaveFileName(randomFileName);

            // 파일 MIME 타입 저장
            fileDTO.setFileType(file.getContentType());

            // 파일 경로 저장
            fileDTO.setFilePath(filePath.toString());

            // 파일
            fileDTO.setId(++lastScriptId);

            // 파일 업로드
            file.transferTo(new File(filePath.toString()));

            // 파일 다운로드 URL 생성 - DownloadController 에서 이어짐
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(randomFileName)
                    .toUriString();

            fileDTO.setFileDownloadUri(fileDownloadUri);

            System.out.println("(결제하기 - 대본) 다운로드 링크: " + fileDownloadUri);

            scriptDTO = fileDTO;

        } catch (IOException e) {
            System.out.println("(14.1) 견적 계산 파일 에러: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("(결제하기 - 대본) 파일 업로드 성공!");

        return "redirect:/popup/chat/14.1_GV-F-Payment-14.1";
    }

    @PostMapping("/chat-payment-submit-endpoint") // 결제하기 버튼 클릭
    public String uploadFile(@RequestParam("timestamp") String timestamp) {

        paymentDTO.setTimestamp(timestamp);
        System.out.println("결제시각: " + timestamp);


        return "redirect:/popup/chat/14.0_GV-F-Chat-14";
    }


    @RequestMapping("/14.1_GV-F-Payment-14.1")
    public String payment(Model model) { // 결제화면 (구인자 전용)

        model.addAttribute("paymentDTO", paymentDTO);

        return "/popup/chat/14.1_GV-F-Payment-14.1";
    }

    @Autowired
    private com.voice.voice.repository.LogFileRepository logFileRepository;
    public static String fileName = null;
    private Long tempRoomId;
    @RequestMapping("/14.0_GV-F-Chat-14")
    public String chatting(Model model) { // 채팅
//        // ▽ 채팅내역을 화면에 불러오는 코드
//
        if(fileName !=null){
            // 로그 파일에서 불러온 로그 내역을 저장하는 리스트
            List<ChatLogDTO> logDTOList = new ArrayList<>();

            // log 파일명을 DB에서 불러옴
            // ▽ log 파일명으로 로그 파일 주소 뽑아냄
            String filePath = "C:/Users/82104/Desktop/voice/src/main/resources/static/logFile/" + fileName;

            ChatLogReader chatLogReader = new ChatLogReader();
            logDTOList = chatLogReader.printLog(filePath.toString());

            for (ChatLogDTO logDTO : logDTOList) {
                System.out.println("Mid: " + logDTO.getMid());
                System.out.println("Message: " + logDTO.getMessage());
                System.out.println("Timestamp: " + logDTO.getTimestamp());
                System.out.println("----------------------------");
            }

            model.addAttribute("logDTOList", logDTOList);
        }


//        model.addAttribute("roomId", tempRoomId);
        model.addAttribute("scriptDTO", scriptDTO);
        model.addAttribute("paymentDTO", paymentDTO);

        return "/popup/chat/14.0_GV-F-Chat-14";
    }

    @PostMapping("/saveChatLog")
    public ResponseEntity<String> saveChatLog(@RequestBody ChatLogDTO chatLogDTO) {

        fileName = "33_2023-11-04.txt";

        // 클라이언트에서 보낸 데이터를 ChatLogDTO 객체로 받음

        // DB 연결 시 로그 파일 명 : 방번호_0000-00-00(대화 시작 날짜).txt

//        // 로그 파일 마다 고유 파일명을 가지도록 함
//        if(temp==0) {
//            fileName += "_" + timestamp.substring(0, 10);
//        }
//
//        String filePath = "C:/Users/82104/Desktop/voice/src/main/resources/static/logFile/" + fileName + ".txt";
//
//        LogFileVO logFileVO = new LogFileVO();
//        logFileVO.setLogName(fileName);
//        logFileVO.setLogPath(filePath);
//
//        System.out.println(logFileVO);
//
//        if(temp==0) { // 방번호가 일치하지 않으면(새로운 로그 파일이면)
//            temp = 1;
//            logFileRepository.save(logFileVO); // (DB 저장)
//        }


        // 저장 결과에 따른 응답 전송
        return ResponseEntity.ok("Chat log saved successfully!");
    }

    @RequestMapping("/22.0_GV-F-ChatDtl-22.0")
    public String chatDtl(Model model) { // 채팅내역 (채팅방 목록)

        return "/popup/chat/22.0_GV-F-ChatDtl-22.0";
    }

}
