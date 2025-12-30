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
@NoArgsConstructor
@ToString

@Table(name = "notice")
@Entity
public class NoticeVO { // 공지
    @Id
    @NotNull
    @Column(name = "n_id")
    private int nId; // 공지 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "n_title")
    private String nTitle; // 공지 제목
    @NotNull
    @Column(name = "n_content")
    private String nContent; //공지 내용
    @NotNull
    @Column(name = "n_date")
    private LocalDateTime nDate; // 등록일
}
