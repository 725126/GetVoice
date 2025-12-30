package com.voice.voice.model;

//CREATE TABLE `massage_file` (
//        `m_f_id` int NOT NULL AUTO_INCREMENT,
//        `m_f_name` varchar(255) NULL,
//        `m_f_path` varchar(255) NULL,
//        `m_f_type` varchar(255) NULL,
//        `m_f_size` varchar(255) NULL,
//        `log_id` int NOT NULL,
//        PRIMARY KEY (`m_f_id`)
//        );

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
@NoArgsConstructor
@ToString

@Table(name = "MassageFile")
@Entity
public class MassageFileVO { // 메세지 첨부 파일
    @Id
    @NotNull
    @Column(name = "m_f_id")
    private int mFId; // 메세지 파일 ID (회원 ID가 아님)
    @Column(name = "m_f_name")
    private String mFName; // 파일 이름
    @Column(name = "m_f_path")
    private String mFPath; // 파일 위치
    @Column(name = "m_f_type")
    private String mFType; // 파일 타입
    @Column(name = "m_f_size")
    private String mFSize; // 파일 크기
    @NotNull
    @Column(name = "log_id")
    private int logId; // 로그 ID
}
