package com.voice.voice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/popup/etc")
public class EtcController {

    @RequestMapping("/2.1_GV-F-ChKw-2.1")
    public String chPreferKeyword(Model model) { // 메인(회원가입)/ 마이페이지(회원정보 변경) - 선호 키워드

        return "/popup/etc/2.1_GV-F-ChKw-2.1";
    }

    @RequestMapping("/8.1_GV-F-ChKw-8.1")
    public String chChKw(Model model) { // 음성유형 선택

        return "/popup/etc/8.1_GV-F-ChKw-8.1";
    }

    @RequestMapping("/16.0_GV-F-MyInqu-16.0")
    public String myInqu(Model model) { // 마이페이지 - 문의하기

        return "/popup/etc/16.0_GV-F-MyInqu-16.0";
    }

    @RequestMapping("/21.0_GV-F-MyMod-21.0")
    public String myMod(Model model) { // 마이페이지 - 회원정보 변경

        return "/popup/etc/21.0_GV-F-MyMod-21.0";
    }

    @RequestMapping("/23.0_GV-F-CurrGV-23.0")
    public String currGV(Model model) { // 마이페이지 - 현재 겟보이스는

        return "/popup/etc/23.0_GV-F-CurrGV-23.0";
    }

}
