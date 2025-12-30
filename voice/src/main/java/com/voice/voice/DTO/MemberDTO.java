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
public class MemberDTO {
    private String id; // 회원 ID
    private String purpose; // 사용목적 (구인/구직) > 회원 구분 코드
    private String password; // 비밀번호
    private String nickname; // 닉네임
    private String phone; // 연락처
    private List<Integer> keyList; // 선호키워드 (key)
}
