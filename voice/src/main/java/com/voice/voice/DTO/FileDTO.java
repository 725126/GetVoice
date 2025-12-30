package com.voice.voice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FileDTO {
    private Long id; // 파일 아이디
    private String saveFileName; // 저장 파일명
    private String originalFileName; // 원본 파일명
    private String filePath; // 저장 경로
    private String fileType; // 파일 타입
    private String fileDownloadUri; // 파일 다운로드 URL

    public boolean isEmpty() {
        return this.id == null &&  this.saveFileName == null && fileType == null &&
               this.filePath == null && this.originalFileName == null && this.fileDownloadUri == null;
    }

}
