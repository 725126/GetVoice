package com.voice.voice.model;

//CREATE TABLE `chat` (
//        `ch_id` int NOT NULL AUTO_INCREMENT,
//        `ch_crt` datetime NOT NULL,
//        `p_id` int NOT NULL,
//        `ch_getid` varchar(255) NOT NULL,
//        `ch_putid` varchar(255) NOT NULL,
//        `ch_last_co` varchar(255) NULL,
//        `ch_last_id` varchar(255) NULL,
//        `ch_last_time` timestamp NULL,
//        PRIMARY KEY (`ch_id`)
//        );

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString

@Table(name = "chat")
@Entity
public class ChatVO { // 채팅
    @Id
    @NotNull
    @Column(name = "ch_id")
    private int chId; // 채팅 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "ch_crt")
    private LocalDateTime chCrt; // 생성 시간
    @NotNull
    @Column(name = "p_id")
    private int pId; // 게시글 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "ch_getid")
    private String cGetid; // 채팅을 받는 회원 ID
    @NotNull
    @Column(name = "ch_putid")
    private String cPutid; // 채팅을 거는 회원
    @Column(name = "ch_last_co")
    private String chLastCo; // 마지막  전송  메세지 내용
    @Column(name = "ch_last_id")
    private String chLastId; // 마지막 메세지 발신인 ID
    @Column(name = "ch_last_time")
    private String chLastTime; // 마지막 메세지 전송 시각
}
