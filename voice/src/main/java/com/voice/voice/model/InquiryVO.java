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
@NoArgsConstructor
@ToString

@Table(name = "inquiry")
@Entity
public class InquiryVO { // 문의
    @Id
    @NotNull
    @Column(name = "in_id")
    private int inId; // 문의 ID (회원 ID가 아님)
    @NotNull
    @Column(name = "in_title")
    private String inTitle; // 문의 제목
    @NotNull
    @Column(name = "in_content")
    private String inContent; // 문의 내용
    @NotNull
    @Column(name = "in_date")
    private LocalDateTime inDate; // 문의 날짜
    @NotNull
    @Column(name = "m_id")
    private String mId; // 회원 ID
}
