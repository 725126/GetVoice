package com.voice.voice.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Table(name = "prefer")
@Entity
public class PreferVO { // 선호키워드
    @Id
    @NotNull
    @Column(name = "pr_id")
    private int prId; // 선호키워드 ID (회원 ID가 아님)
    @Column(name = "pr_type")
    private String prType; // 음성유형
    @Column(name = "pr_envir")
    private String prEnvir; // 녹음환경
    @Column(name = "pr_genre")
    private String prGenre; // 장르
    @NotNull
    @Column(name = "m_id")
    private String mId; // 회원 ID
}
