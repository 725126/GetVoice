package com.voice.voice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EstCalDTO { // 견적 계산 데이터를 저장하는 클래스
    private int forProfitPrice; // 영리 가격
    private int nonProfitPrice; // 비영리 가격
    private int characterCount; // 글자 수
    private String profit; // 사용범위(영리, 비영리)

    private int amount; // 총 금액
    private String timestamp; // 결제시각

}
