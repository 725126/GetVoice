package com.voice.voice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board/back")
public class BackController {

    @RequestMapping("/01_GV-B-Main-01")
    public String mainB(Model model) { // 관리자 메인

        return "/board/back/01_GV-B-Main-01";
    }

    @RequestMapping("/05_GV-B-MemInfo-05")
    public String memInfo(Model model) { // 회원목록

        return "/board/back/05_GV-B-MemInfo-05";
    }
    @RequestMapping("/06_GV-B-CTmemDtl-06")
    public String ctMemDtl(Model model) { // 구인자 회원 상세 정보

        return "/board/back/06_GV-B-CTmemDtl-06";
    }
    @RequestMapping("/07_GV-B-VAmemDtl-07")
    public String vaMemDtl(Model model) { // 구직자 회원 상세 정보

        return "/board/back/07_GV-B-VAmemDtl-07";
    }
    @RequestMapping("/08_GV-B-MemTH-08")
    public String memTH(Model model) { // 회원 상세정보 - 회원 거래내역

        return "/board/back/08_GV-B-MemTH-08";
    }
    @RequestMapping("/09_GV-B-MemPost-09")
    public String memPost(Model model) { // 회원 상세정보 - 작성 글 조회

        return "/board/back/09_GV-B-MemPost-09";
    }

    @RequestMapping("/10_GV-B-Inqu-10")
    public String inqu(Model model) { // 문의목록(관리자)

        return "/board/back/10_GV-B-Inqu-10";
    }

    @RequestMapping("/11_GV-B-MemInqu-11")
    public String MemInqu(Model model) { // 회원 상세정보 - 문의내역

        return "/board/back/11_GV-B-MemInqu-11";
    }

    @RequestMapping("/12_GV-B-InquPost-12")
    public String InquPost(Model model) { // 문의글 보기

        return "/board/back/12_GV-B-InquPost-12";
    }

    @RequestMapping("/13_GV-B-TH-13")
    public String THB(Model model) { // 전체 회원 거래내역

        return "/board/back/13_GV-B-TH-13";
    }

}
