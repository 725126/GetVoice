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
@NoArgsConstructor
@ToString

@Table(name = "evaluation")
@Entity
public class EvaluationVO { // 평가도
    @Id
    @NotNull
    @Column(name = "ev_id")
    private int evId; // 평가도 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "ev_putid")
    private String evPutid; // 평가 받은 회원 ID
    @NotNull
    @Column(name = "ev_getid")
    private String evGetid; //평가한 회원 ID
    @NotNull
    @Column(name = "ev_star")
    private int evStar; // 평점
    @Column(name = "ev_content")
    private String evContent; //평가 내용 (구인자 -> 구직자만 해당)
    @NotNull
    @Column(name = "tr_id")
    private int trId; // 거래 ID
}
