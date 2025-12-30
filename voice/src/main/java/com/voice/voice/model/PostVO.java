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
import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "post")
@Entity
public class PostVO { // 게시글
    @Id
    @NotNull
    @Column(name = "p_id")
    private int pId; // 게시글 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "p_code")
    private String pCode; // 게시글 구분 코드 (보포 / 구인글)
    @NotNull
    @Column(name = "p_title")
    private String pTitle; // 제목 (보포 / 구인글)
    @Column(name = "p_ava")
    private String pAva; // 현재 구인/구직 여부 (보포 / 구인글)
    @Column(name = "p_intro")
    private String pIntro; // 소개 (보포 / 구인글)
    @Column(name = "p_mod")
    private String pMod; // 수정·재진행 (보포)
    @Column(name = "p_refund")
    private String pRefund; // 취소·환불 (보포)
    @NotNull
    @Column(name = "p_count")
    private int pCount; // 업로드한 샘플(영상+음성)의 갯수 (보포)
    @NotNull
    @Column(name = "p_date")
    private LocalDateTime pDate; // 등록일 (구인글)
    @Column(name = "p_mdate")
    private LocalDateTime pMdate; // 수정일 (구인글)
    @Column(name = "p_dl")
    private LocalDateTime pDl; // 마감일 (구인글)
    @Column(name = "p_non_price")
    private Integer pNonPrice; // 비영리 가격 (보포 / 구인글)
    @Column(name = "p_for_price")
    private Integer pForPrice; // 영리 가격 (보포 / 구인글)
    @Column(name = "p_word")
    private Integer pWord; // 기준 글자 수 (구인글)
    @Column(name = "p_service")
    private String pService; // 서비스 유형 (카테고리/장르) (보포 / 구인글)
    @Column(name = "p_voice")
    private String pVoice; // 음성유형 (보포 / 구인글)
    @NotNull
    @Column(name = "p_envir")
    private String pEnvir; // 녹음 환경 (보포 / 구인글)
    @NotNull
    @Column(name = "m_id")
    private String mId; // 회원 ID

}
