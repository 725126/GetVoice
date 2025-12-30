package com.voice.voice.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@ToString

@Table(name = "transaction")
@Entity
public class TransactionVO { // 거래 (거래내역)
    @Id
    @NotNull
    @Column(name = "tr_id")
    private int trId; // 거래 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "tr_st")
    private int trSt; // 거래 상태
    @NotNull
    @Column(name = "tr_date")
    private LocalDateTime trDate; // 거래 날짜
    @Column(name = "ct_id")
    private String ctId; // 구인 회원 ID
    @Column(name = "va_id")
    private String vaId; // 구직 회원 ID
    @NotNull
    @Column(name = "p_id")
    private int pId; // 결제 ID
}
