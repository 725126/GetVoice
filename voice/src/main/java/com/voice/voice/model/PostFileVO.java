package com.voice.voice.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

//CREATE TABLE `post_file` (
//        `p_f_id` int NOT NULL AUTO_INCREMENT,
//        `p_f_save_name` varchar(255) NULL,
//        `p_f_ori_name` varchar(255) NULL,
//        `p_f_path` varchar(255) NULL,
//        `p_f_type` varchar(255) NULL,
//        `p_f_size` varchar(255) NULL,
//        `p_f_down_url` varchar(255) NULL,
//        `m_id` varchar(255) NULL,
//        `p_id` int NOT NULL,
//        PRIMARY KEY (`p_f_id`)
//        );

@Getter
@Setter
@NoArgsConstructor
@ToString

@Table(name = "post_file")
@Entity
public class PostFileVO { // 게시글 첨부 파일
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임
    @Column(name = "p_f_id")
    private Long pFId; // 게시글 파일 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "p_f_code")
    private String pFCode; // 게시글 파일 구분 코드
    @Column(name = "p_f_save_name")
    private String pFSaveName; // 파일 저장명
    @Column(name = "p_f_ori_name")
    private String pFOriName; // 파일 원본명
    @Column(name = "p_f_path")
    private String pFPath; // 파일 위치
    @Column(name = "p_f_type")
    private String pFType; // 파일 타입
    @Column(name = "p_f_size")
    private String pFSize; // 파일 크기
    @Column(name = "p_f_down_url")
    private String pFDownUrl; // 파일 다운로드 URL
    @NotNull
    @Column(name = "m_id")
    private String mId; // 회원 아이디
    @NotNull
    @Column(name = "p_id")
    private int pId; // 게시글 ID
}
