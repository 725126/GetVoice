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

@Table(name = "log_File")
@Entity
public class LogFileVO { // 로그 파일
    @Id
    @NotNull
    @Column(name = "log_f_id")
    private int logFId; // 로그 파일 ID (회원 ID가 아님)
    @Column(name = "log_f_name")
    private String logName; // 파일 이름
    @Column(name = "log_f_path")
    private String logPath; // 파일 위치
    @Column(name = "log_f_size")
    private int logSize; // 파일 크기
//    @NotNull
    @Column(name = "ch_id")
    private int chId; // 채팅 ID

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public void setLogPath(String filePath) {
        this.logPath = filePath;
    }

    public void setLogSize(int size) {
        this.logSize = size;
    }
}
