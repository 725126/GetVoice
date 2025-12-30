package com.voice.voice.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voice.voice.model.PostVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VPCtrlDTO { // 보이스포트 작성
    private String username; // 회원 아이디
    private String nickname; // 회원 닉네임
    private  String process; // 게시물 데이터 처리 - submit / save
    private int sampleCount; // 업로드한 영상/음성샘플의 갯수
    private String title; // 제목
    private List<String> serviceTypes; // 서비스 유형 value
    private List<String> voiceTypes; // 음성 유형 value
    private List<String> recordingEnvironments; // 녹음 환경 value
    private List<Integer> serviceKeyList; // 서비스 유형 Key
    private List<Integer> voiceKeyList; // 음성 유형 Key
    private List<Integer> recordingKeyList; // 녹음 환경 Key
    private String availability; // 현재 구직 여부
    private String introduceContent; // 소개
    private String modifyContent; // 수정·재진행
    private String refundContent; // 취소·환불
    private int forProfitPrice; // 영리 가격
    private int nonProfitPrice; // 비영리 가격
    private int pid; // 게시물 ID
    private String thumnbDownUrl; // 썸네일 URL

    public VPCtrlDTO(PostVO postVO) {
        this.username = postVO.getMId();

        this.sampleCount = postVO.getPCount();
        this.title = postVO.getPTitle();

        this.serviceKeyList = jsonStringToList(postVO.getPService());
        this.voiceKeyList = jsonStringToList(postVO.getPVoice());
        this.recordingKeyList = jsonStringToList(postVO.getPEnvir());

        this.availability = postVO.getPAva();

        this.introduceContent = postVO.getPIntro();
        this.modifyContent = postVO.getPMod();
        this.refundContent = postVO.getPRefund();

        this.forProfitPrice = postVO.getPForPrice();
        this.nonProfitPrice = postVO.getPNonPrice();

        this.pid = postVO.getPId();
    }
    private static List<Integer> jsonStringToList(String jsonString){
        // 클라이언트에서 전송한 JSON 형식의 문자열을 JSON 배열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> keywordsList = null;
        try {
            keywordsList = objectMapper.readValue(jsonString, new TypeReference<List<Integer>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println( "Invalid JSON format: " + e.getMessage() );
        }
        return keywordsList;
    }
}
