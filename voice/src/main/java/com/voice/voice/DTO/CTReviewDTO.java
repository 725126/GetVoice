package com.voice.voice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CTReviewDTO { // 구인자가 구직자(성우)에게 작성하는 리뷰
    private int stars; // 별점
    private String content; // 리뷰 내용
    private Long id; // 거래 ID
}
