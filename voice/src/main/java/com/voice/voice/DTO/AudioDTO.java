package com.voice.voice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AudioDTO {
    private String title; // 제목
    private  List<Integer> selectedKeyList; // 선택한 키워드의 key 값이 저장된 리스트
    private List<String> selectedKeywords; // 선택한 키워드(value)가 저장된 리스트
    private Long id; // 음성 샘플 게시물 아이디
    private String saveFileName; // 음성 샘플 저장 파일명
    private String originalFileName; // 원본 파일명
    private String filePath; // 음성 샘플 파일 저장 경로
    private String fileType; // 음성 샘플 파일 타입
    private String fileDownloadUri; // 파일 다운로드 URL

}
