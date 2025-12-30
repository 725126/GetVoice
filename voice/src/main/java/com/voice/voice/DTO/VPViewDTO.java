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
public class VPViewDTO {
    private VPCtrlDTO vpCtrlDTO; // 기본 입력 데이터
    private FileDTO profileDTO; // 프로필 이미지
    private FileDTO pdfDTO; // 이력서 PDF
    private List<AudioDTO> audioDTOList; // 보이스포트의 음성샘플 데이터를 저장하는 리스트
    private List<UrlDTO> urlDTOList; // 보이스포트의 영상샘플 데이터를 저장하는 리스트
}
