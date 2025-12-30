package com.voice.voice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginDTO {
    private String username; // 회원 아이디
    private String password; // 회원 비밀번호
}
