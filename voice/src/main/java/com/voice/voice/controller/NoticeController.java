package com.voice.voice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board/notice")
public class NoticeController {

    @RequestMapping("/04_GV-F-Notice-04")
    public String notice(Model model) { // 공지사항 목록

        return "/board/notice/04_GV-F-Notice-04";
    }

    @RequestMapping("/05_GV-F-NoticeView-05")
    public String noticeView(Model model) { // 공지사항 글 보기

        return "/board/notice/05_GV-F-NoticeView-05";
    }

    @RequestMapping("/14_GV-B-Notice-14")
    public String noticeB(Model model) { // 공지사항 목록 (관리자)

        return "/board/notice/14_GV-B-Notice-14";
    }

    @RequestMapping("/15_GV-B-NoticeView-15")
    public String noticeViewB(Model model) { // 공지사항 글 보기 (관리자)

        return "/board/notice/15_GV-B-NoticeView-15";
    }

    @RequestMapping("/16_GV-B-NoticeW-16")
    public String noticeWrite(Model model) { // 공지사항 글 작성 (관리자)

        return "/board/notice/16_GV-B-NoticeW-16";
    }
}
