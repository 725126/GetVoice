package com.voice.voice.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@ToString
@Table(name = "member")
@Entity
public class MemberVO { // 회원
    @Id
    @NotNull
    @Column(name = "m_id")
    private String mId; // 회원 ID
    @NotNull
    @Column(name = "m_code")
    private String mCode; // 회원 구분 코드
    @NotNull
    @Column(name = "m_pw")
    private String mPw; // 비밀번호
    @NotNull
    @Column(name = "m_nn")
    private String mNn; // 닉네임
    @NotNull
    @Column(name = "m_num")
    private String mNum; // 연락처
    @NotNull
    @Column(name = "m_fav")
    private String mFav; // 선호키워드
    @Column(name = "m_money")
    private int mMoney; // v머니
    @NotNull
    @Column(name = "m_joindate")
    private LocalDateTime mJoinDate; // 가입일
    @Column(name = "m_ing_cnt")
    private Integer mIngCnt; // 진행 중인 거래 수
    @Column(name = "m_end")
    private Integer mEnd; // 작업 완료된 거래 수
    @Column(name = "m_cfm_cnt")
    private Integer mCfmCnt; // 확정된 거래 수
    @Column(name = "m_rep")
    private Integer mRep; // 신고 당한 횟수

    public MemberVO() {
        this.mMoney = 0;
        this.mIngCnt = 0; this.mEnd = 0; this.mCfmCnt = 0;
        this.mRep = 0;
        this.mJoinDate = LocalDateTime.now();
    }

    public void setmNn(String mNn) {
        this.mNn = mNn;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public void setmPw(String mPw) {
        this.mPw = mPw;
    }

    public void setmNum(String mNum) {
        this.mNum = mNum;
    }

    public void setmFav(String mFav) {
        this.mFav = mFav;
    }

    public void setmMoney(int mMoney) { this.mMoney = mMoney; }
}
