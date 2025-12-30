package com.voice.voice.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

//CREATE TABLE `vp_sample` (
//        `p_sp_id`	int	NOT NULL AUTO_INCREMENT,
//        `sp_title`	varchar(255) NULL,
//        `sp_keyword`	varchar(255) NULL,
//        `sp_ytb_url`	varchar(255) NULL,
//        `sp_thum_url`	varchar(255) NULL,
//        `sp_f_save_name`	varchar(255) NULL,
//        `sp_f_ori_name`	varchar(255) NULL,
//        `sp_f_path`	varchar(255) NULL,
//        `sp_f_type`	varchar(255) NULL,
//        `sp_f_size`	varchar(255) NULL,
//        `sp_f_down_url` varchar(255) NULL,
//        `sp_code` varchar(255) NOT NULL,
//        `m_id` varchar(255) NOT NULL,
//        `p_id`	int	NOT NULL,
//        PRIMARY KEY (`p_sp_id`)
//        );

@Getter
@Setter
@NoArgsConstructor
@ToString

@Table(name = "vp_sample")
@Entity
public class VpSampleVO {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임
    @Column(name = "p_sp_id")
    private int pSpId; // 보이스포트 샘플 ID (회원 ID가 아님)
    @Column(name = "sp_title")
    private String spTitle; // 보이스포트 샘플 제목
    @Column(name = "sp_keyword")
    private String spKeyword; // 보이스포트 샘플 키워드
    @Column(name = "sp_ytb_url")
    private String spYtbUrl; // 보이스포트 영상 샘플 - 유튜브 URL
    @Column(name = "sp_thum_url")
    private String spThumUrl; // 보이스포트 영상 샘플 - 썸네일 URL
    @Column(name = "sp_f_save_name")
    private String spSaveName; // 보이스포트 음성 샘플 - 파일 저장명
    @Column(name = "sp_f_ori_name")
    private String spOriName; // 보이스포트 음성 샘플 - 파일 원본명
    @Column(name = "sp_f_path")
    private String spFPath; // 보이스포트 음성 샘플 - 파일 경로
    @Column(name = "sp_f_type")
    private String spFType; // 보이스포트 음성 샘플 - 파일 타입
    @Column(name = "sp_f_size")
    private String spFSize; // 보이스포트 음성 샘플 - 파일 크기
    @Column(name = "sp_f_down_url")
    private String spFDownUrl; // 보이스포트 음성 샘플 - 파일 다운로드 URL
    @NotNull
    @Column(name = "sp_code")
    private String spCode; // 보이스포트 샘플 구분 코드
    @NotNull
    @Column(name = "m_id")
    private String mId; // 회원 아이디
    @NotNull
    @Column(name = "p_id")
    private int pId; // 게시글 ID
}
