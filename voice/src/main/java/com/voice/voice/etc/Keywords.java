package com.voice.voice.etc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Keywords {

    Map<Integer, String> voiceTypesMap = new HashMap<>(); // 음성유형
    Map<Integer, String> serviceTypesMap = new HashMap<>(); // 서비스 유형
    Map<Integer, String> recordingEnvirMap = new HashMap<>(); // 녹음환경


    public Keywords() {
        // 음성유형 단어 추가
        voiceTypesMap.put(1, "#유아"); voiceTypesMap.put(2, "#소년"); voiceTypesMap.put(3, "#소녀"); voiceTypesMap.put(4, "#중년"); voiceTypesMap.put(5, "#노년");
        voiceTypesMap.put(6, "#여성"); voiceTypesMap.put(7, "#남성"); voiceTypesMap.put(8, "#귀여운"); voiceTypesMap.put(9, "#밝은"); voiceTypesMap.put(10, "#차분");
        voiceTypesMap.put(11, "#발랄"); voiceTypesMap.put(12, "#소심"); voiceTypesMap.put(13, "#열혈"); voiceTypesMap.put(14, "#맑은"); voiceTypesMap.put(15, "#따뜻");
        voiceTypesMap.put(16, "#깔끔"); voiceTypesMap.put(17, "#편안"); voiceTypesMap.put(18, "#신뢰"); voiceTypesMap.put(19, "#청량"); voiceTypesMap.put(20, "#상쾌");
        voiceTypesMap.put(21, "#단호"); voiceTypesMap.put(22, "#피곤"); voiceTypesMap.put(23, "#고상"); voiceTypesMap.put(24, "#상큼"); voiceTypesMap.put(25, "#튀는");
        voiceTypesMap.put(26, "#음침"); voiceTypesMap.put(27, "#분노"); voiceTypesMap.put(28, "#시크"); voiceTypesMap.put(29, "#진중"); voiceTypesMap.put(30, "#가볍");
        voiceTypesMap.put(31, "#감성"); voiceTypesMap.put(32, "#쿨"); voiceTypesMap.put(33, "#예민"); voiceTypesMap.put(34, "#지적"); voiceTypesMap.put(35, "#딱딱");
        voiceTypesMap.put(36, "#섹시"); voiceTypesMap.put(37, "애교"); voiceTypesMap.put(38, "#고음"); voiceTypesMap.put(39, "#중음"); voiceTypesMap.put(40, "#저음");
        voiceTypesMap.put(41, "#외국어"); voiceTypesMap.put(42, "#차분"); voiceTypesMap.put(43, "#버튜버"); voiceTypesMap.put(44, "#사투리"); voiceTypesMap.put(45, "#중성적");

        // 서비스 유형 단어 추가
        serviceTypesMap.put(1001, "캐릭터"); serviceTypesMap.put(1002, "내레이션"); serviceTypesMap.put(1003, "다큐"); serviceTypesMap.put(1004, "광고");
        serviceTypesMap.put(1005, "게임"); serviceTypesMap.put(1006, "더빙"); serviceTypesMap.put(1007, "ARS"); serviceTypesMap.put(1008, "동화");
        serviceTypesMap.put(1009, "노래"); serviceTypesMap.put(1010, "교육"); serviceTypesMap.put(1011, "오디오북"); serviceTypesMap.put(1012, "라디오");
        serviceTypesMap.put(1013, "홍보"); serviceTypesMap.put(1014, "애니"); serviceTypesMap.put(1015, "연기"); serviceTypesMap.put(1016, "성대모사");
        serviceTypesMap.put(1017, "패러디"); serviceTypesMap.put(1018, "안내음성"); serviceTypesMap.put(1019, "TV"); serviceTypesMap.put(1020, "CM");

        // 녹음환경 단어 추가
        recordingEnvirMap.put(10001, "현장 녹음"); recordingEnvirMap.put(10002, "홈 레코딩"); recordingEnvirMap.put(10003, "바이노럴 레코딩");
    }


}
