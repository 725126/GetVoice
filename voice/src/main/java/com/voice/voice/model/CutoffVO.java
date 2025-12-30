package com.voice.voice.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@ToString

@Table(name = "cutoff")
@Entity
public class CutoffVO { // 차단

    @Id
    @NotNull
    @Column(name = "co_id")
    private int coId; // 차단 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "co_getid")
    private String coGetid;  // 차단 당한 회원 ID
    @NotNull
    @Column(name = "co_putid")
    private String coPutid; // 차단한 회원 ID
}
