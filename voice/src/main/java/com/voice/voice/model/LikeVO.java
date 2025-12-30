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

@Table(name = "like")
@Entity
public class LikeVO {
    @Id
    @NotNull
    @Column(name = "l_id")
    private int lId; // 좋아요 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "l_getid")
    private String lGetid; // 좋아요를 받은 회원 ID
    @NotNull
    @Column(name = "l_putid")
    private String lPutid; // 좋아요를 한 회원 ID
}
