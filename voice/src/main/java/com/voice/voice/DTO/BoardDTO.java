package com.voice.voice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardDTO {
    private String title;       // 글의 제목
    private String amount;      // 가능한 금액.
    private String wordLimit;   // 기준 글자 수.
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline; // 마감일 (날짜 정보)
    private String usageRange;  // 사용 범위
    private String usagePurpose; // 사용 목적
    private String affiliation; // 글을 올리는 사용자의 소속 정보
    private String availability; // 현재 구인 여부 ("possible" 또는 "not-possible" 값 중 하나)
    private String content;     // 글의 내용

}
