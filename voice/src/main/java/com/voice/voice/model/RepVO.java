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

@Table(name = "rep")
@Entity
public class RepVO { // 신고
    @Id
    @NotNull
    @Column(name = "rep_id")
    private int repId; // 신고 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "rep_getid")
    private String repGetid; // 신고 당한 회원 ID
    @NotNull
    @Column(name = "rep_putid")
    private String repPutid; // 신고한 회원 ID
}
