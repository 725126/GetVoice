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

@Table(name = "payment")
@Entity
public class PaymentVO { // 결제
    @Id
    @NotNull
    @Column(name = "p_id")
    private int pId; // 결제 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "p_money")
    private int pMoney; // 결제 금액
    @Column(name = "ct_id")
    private String ctId; // 구인 회원 ID
    @Column(name = "va_id")
    private String vaId; // 구직 회원 ID
    @NotNull
    @Column(name = "sc_f_id")
    private int scFId; // 대본 파일 ID
}
