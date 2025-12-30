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

@Table(name = "scriptFile")
@Entity
public class ScriptFileVO { // 의뢰 대본 파일
    @Id
    @NotNull
    @Column(name = "sc_f_id")
    private int scFId; // 대본 파일 ID (회원 ID가 아님)
    @Column(name = "sc_f_name")
    private String scFName; // 파일 이름
    @Column(name = "sc_f_path")
    private String scFPath; // 파일 위치
    @Column(name = "sc_f_type")
    private String scFType; // 파일 타입
    @Column(name = "sc_f_size")
    private Integer scFSize; // 파일 크기
    @Column(name = "ct_id")
    private String ctId; // 구인 회원 ID
    @Column(name = "va_id")
    private String vaId; // 구직 회원 ID
    @NotNull
    @Column(name = "ch_id")
    private int chId; // 채팅 ID
}
