package com.voice.voice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voice.voice.DTO.*;
import com.voice.voice.etc.Keywords;

import com.voice.voice.model.PostFileVO;
import com.voice.voice.model.PostVO;
import com.voice.voice.model.VpSampleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/popup/voiceport")
public class VPController {

    @Autowired
    private com.voice.voice.repository.PostRepository postRepository;
    @Autowired
    private com.voice.voice.repository.PostFileRepository postFileRepository;
    @Autowired
    private com.voice.voice.repository.VpSampleRepository vpSampleRepository;
    @Autowired
    private com.voice.voice.repository.MemberRepository memberRepository;

    private VPCtrlDTO vpCtrlDTO; // 기본 입력 데이터
    private FileDTO profileDTO; // 프로필 이미지
    private FileDTO thumbnailDTO; // 썸네일 이미지
    private FileDTO pdfDTO; // 이력서 PDF
    private List<AudioDTO> audioDTOList; // 보이스포트의 음성샘플 데이터를 저장하는 리스트
    private List<UrlDTO> urlDTOList; // 보이스포트의 영상샘플 데이터를 저장하는 리스트
    private EstCalDTO estCalDTO; // 견적 계산
    private Integer tabNum = 1; // 1번은 영상샘플, 2번은 음성샘플

    private static Long lastVideoId = 0L; // 영상샘플
    private static Long lastAudioId = 0L; // 음성샘플
    private static Long lastProfileId = 0L; // 프로필 이미지
    private static Long lastThumbnailId = 0L; // 썸네일 이미지
    private static Long lastPdfId = 0L; // 이력서 PDF


    @PostMapping("logout-init-vp")
    public String logoutDeleteData(){ // 로그아웃 시- 초기화 메소드
        vpCtrlInit();

        profileInit();
        thumbnailInit();
        pdfInit();

        initUrlDTOList();
        initAudioDTOList();

        estCalInit();

        lastVideoId = lastAudioId = 0L;
        lastProfileId = lastThumbnailId = lastPdfId = 0L;

        System.out.println("로그아웃 - voiceport 초기화");

        return "/board/ct/1.0_GV-F-Main-1.0";
    }

    @GetMapping("open-vp-call-keyword") // viewVP : 키워드 더보기 팝업창을 열 때 해당 데이터를 서버에 전송하는 메서드
    public ResponseEntity<VPCtrlDTO> vpCallKeywordRequest(@RequestParam(name = "pid") int pid){
        // pid에 해당하는 게시물을 찾기

        System.out.println("pid: " + pid);

        PostVO postVO = postRepository.findById(pid).get();

        if (postVO != null) {
            VPCtrlDTO vpDto = new VPCtrlDTO(postVO);

            return ResponseEntity.ok(vpDto);
        } else {
            // 게시물이 존재하지 않을 경우, 404 Not Found를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/open-vp-call-data") // 구직게시물에서 보이스포트 팝업창 열 때 해당 데이터를 서버에 전송하는 메서드
    public ResponseEntity<VPViewDTO> vpCallDataRequest(@RequestParam(name = "pid") int pid) {
        VPViewDTO vpViewDTO = new VPViewDTO();

        // pid에 해당하는 게시물을 찾기
        PostVO postVO = postRepository.findById(pid).get();

        if (postVO != null) {
            VPCtrlDTO vpCtrlDTO1 = new VPCtrlDTO(postVO);

            vpCtrlDTO1.setVoiceTypes(returnValueList(vpCtrlDTO1.getVoiceKeyList(), "voice"));
            vpCtrlDTO1.setServiceTypes(returnValueList(vpCtrlDTO1.getServiceKeyList(), "service"));
            vpCtrlDTO1.setRecordingEnvironments(returnValueList(vpCtrlDTO1.getRecordingKeyList(), "record"));

            List<VpSampleVO> vpSampleVOList = vpSampleRepository.findBypId(pid);
            List<UrlDTO> urlDTOList = new ArrayList<>();
            List<AudioDTO> audioDTOList = new ArrayList<>();

            vpCtrlDTO1.setNickname(memberRepository.findById(vpCtrlDTO1.getUsername()).get().getMNn());

            List<PostFileVO> postFileVOList = postFileRepository.findBypId(pid);
            FileDTO profileDTO = new FileDTO();
            FileDTO pdfDTO = new FileDTO();

            // 영상샘플 & 음성샘플
            for (VpSampleVO vo : vpSampleVOList) {
                List<Integer> keyList;
                if (vo.getSpCode().equals("video")) {
                    UrlDTO urlDTO = new UrlDTO();

                    urlDTO.setId(++lastVideoId);
                    urlDTO.setTitle(vo.getSpTitle());

                    keyList = jsonStringToList(vo.getSpKeyword());
                    urlDTO.setSelectedKeyList(keyList);
                    urlDTO.setSelectedKeywords(returnValueList(keyList, "voice"));

                    urlDTO.setYoutubeUrl(vo.getSpYtbUrl());
                    urlDTO.setThumbnailUrl(vo.getSpThumUrl());

                    urlDTOList.add(urlDTO);
                } else if (vo.getSpCode().equals("audio")) {
                    AudioDTO audioDTO = new AudioDTO();

                    audioDTO.setId(++lastAudioId);
                    audioDTO.setTitle(vo.getSpTitle());

                    keyList = jsonStringToList(vo.getSpKeyword());
                    audioDTO.setSelectedKeyList(keyList);
                    audioDTO.setSelectedKeywords(returnValueList(keyList, "voice"));

                    audioDTO.setSaveFileName(vo.getSpSaveName());
                    audioDTO.setOriginalFileName(vo.getSpOriName());
                    audioDTO.setFilePath(vo.getSpFPath());
                    audioDTO.setFileType(vo.getSpFType());
                    audioDTO.setFileDownloadUri(vo.getSpFDownUrl());

                    audioDTOList.add(audioDTO);
                }
            }

            // 프로필 이미지 & 썸네일 이미지 & 이력서 PDF
            for (PostFileVO vo : postFileVOList) {
                if (vo.getPFCode().equals("vp-profile")) {
                    profileDTO.setId(++lastProfileId);

                    profileDTO.setSaveFileName(vo.getPFSaveName());
                    profileDTO.setOriginalFileName(vo.getPFOriName());
                    profileDTO.setFilePath(vo.getPFPath());
                    profileDTO.setFileType(vo.getPFType());
                    profileDTO.setFileDownloadUri(vo.getPFDownUrl());

                } else if (vo.getPFCode().equals("vp-pdf")) {
                    pdfDTO.setId(++lastPdfId);

                    pdfDTO.setSaveFileName(vo.getPFSaveName());
                    pdfDTO.setOriginalFileName(vo.getPFOriName());
                    pdfDTO.setFilePath(vo.getPFPath());
                    pdfDTO.setFileType(vo.getPFType());
                    pdfDTO.setFileDownloadUri(vo.getPFDownUrl());
                }
            }

            vpViewDTO.setVpCtrlDTO(vpCtrlDTO1);
            vpViewDTO.setUrlDTOList(urlDTOList);
            vpViewDTO.setAudioDTOList(audioDTOList);
            vpViewDTO.setProfileDTO(profileDTO);
            vpViewDTO.setPdfDTO(pdfDTO);

        // 선택한 게시물을 JSON 형태로 응답으로 보냄
        return ResponseEntity.ok(vpViewDTO);
    } else {
            // 게시물이 존재하지 않을 경우, 404 Not Found를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping("/10.0_GV-F-VP-10")
    public String voicePort(Model model) { // 보이스포트

        // 더 많이 업로드된 샘플의 탭을 노출
        if (urlDTOList.size()>audioDTOList.size()){
            tabNum = 1; // 영상샘플 탭을 노출
        }else {
            tabNum = 2; // 음성샘플 탭을 노출
        }

        // 탭 번호 전송
        model.addAttribute("tabNum", tabNum);
        // 영상 샘플 데이터 전송
        model.addAttribute("urlDTOList", urlDTOList);
        // 음성 샘플 데이터 전송
        model.addAttribute("audioDTOList", audioDTOList);
        // 이력서 데이터 전송
        model.addAttribute("pdfDTO", pdfDTO);
        // 프로필 이미지 데이터 전송
        model.addAttribute("profileDTO", profileDTO);
        // 기본 입력 데이터 전송
        model.addAttribute("vpCtrlDTO", vpCtrlDTO);

        return "/popup/voiceport/10.0_GV-F-VP-10";
    }

    @RequestMapping("/10.1_GV-F-KWDtl-10.1")
    public String kwDtl(Model model) { // 키워드 더보기 (보이스포트)

        return "/popup/voiceport/10.1_GV-F-KWDtl-10.1";
    }

    @RequestMapping("/10.4_GV-F-Review-10.4")
    public String review(Model model) { // 리뷰 더보기 (보이스포트)

        return "/popup/voiceport/10.4_GV-F-Review-10.4";
    }

    static List<String> returnValueList(List<Integer> keyList, String code){
        List<String> valueList = new ArrayList<>();
        Keywords keywords = new Keywords();

        if(code.equals("service")){
            for (int key : keyList){
                valueList.add(keywords.getServiceTypesMap().get(key));
            }
        } else if (code.equals("voice")) {
            for (int key : keyList){
                valueList.add(keywords.getVoiceTypesMap().get(key));
            }
        } else if (code.equals("record")) {
            for (int key : keyList){
                valueList.add(keywords.getRecordingEnvirMap().get(key));
            }
        }

        return valueList;
    }

    @PostMapping("/vp-data-call") // 보이스포트 데이터를 불러오는 메소드
    public String vpDatacall(@RequestBody VPCtrlDTO vpCtrlDTO) {

        // 기본 입력 데이터
        this.vpCtrlDTO = vpCtrlDTO;

        this.vpCtrlDTO.setServiceTypes(returnValueList(vpCtrlDTO.getServiceKeyList(), "service"));
        this.vpCtrlDTO.setVoiceTypes(returnValueList(vpCtrlDTO.getVoiceKeyList(), "voice"));
        this.vpCtrlDTO.setRecordingEnvironments(returnValueList(vpCtrlDTO.getRecordingKeyList(), "record"));

        System.out.println("*** 보이스포트 데이터 불러오기 ***");
        System.out.println("vpCtrlDTO: " + vpCtrlDTO);
        System.out.println("pid: " + vpCtrlDTO.getPid());

        List<VpSampleVO> vpSampleVOList = vpSampleRepository.findBypId(vpCtrlDTO.getPid());
        List<UrlDTO> urlDTOList = new ArrayList<>();
        List<AudioDTO> audioDTOList = new ArrayList<>();

        List<PostFileVO> postFileVOList = postFileRepository.findBypId(vpCtrlDTO.getPid());
        FileDTO profileDTO = new FileDTO();
        FileDTO thumbnailDTO = new FileDTO();
        FileDTO pdfDTO = new FileDTO();

        // 영상샘플 & 음성샘플
        for (VpSampleVO vo : vpSampleVOList){
            List<Integer> keyList;
            if (vo.getSpCode().equals("video")){
                UrlDTO urlDTO = new UrlDTO();

                urlDTO.setId(++lastVideoId);
                urlDTO.setTitle(vo.getSpTitle());

                keyList = jsonStringToList(vo.getSpKeyword());
                urlDTO.setSelectedKeyList(keyList);
                urlDTO.setSelectedKeywords(returnValueList(keyList, "voice"));

                urlDTO.setYoutubeUrl(vo.getSpYtbUrl());
                urlDTO.setThumbnailUrl(vo.getSpThumUrl());

                urlDTOList.add(urlDTO);
            } else if (vo.getSpCode().equals("audio")) {
                AudioDTO audioDTO = new AudioDTO();

                audioDTO.setId(++lastAudioId);
                audioDTO.setTitle(vo.getSpTitle());

                keyList = jsonStringToList(vo.getSpKeyword());
                audioDTO.setSelectedKeyList(keyList);
                audioDTO.setSelectedKeywords(returnValueList(keyList, "voice"));

                audioDTO.setSaveFileName(vo.getSpSaveName());
                audioDTO.setOriginalFileName(vo.getSpOriName());
                audioDTO.setFilePath(vo.getSpFPath());
                audioDTO.setFileType(vo.getSpFType());
                audioDTO.setFileDownloadUri(vo.getSpFDownUrl());

                audioDTOList.add(audioDTO);
            }

        }
        this.urlDTOList = urlDTOList;
        this.audioDTOList = audioDTOList;

        System.out.println("urlDTOList: " + urlDTOList);
        System.out.println("audioList: " + audioDTOList);

        // 프로필 이미지 & 썸네일 이미지 & 이력서 PDF
        for (PostFileVO vo : postFileVOList){
            if (vo.getPFCode().equals("vp-profile")){
                profileDTO.setId(++lastProfileId);

                profileDTO.setSaveFileName(vo.getPFSaveName());
                profileDTO.setOriginalFileName(vo.getPFOriName());
                profileDTO.setFilePath(vo.getPFPath());
                profileDTO.setFileType(vo.getPFType());
                profileDTO.setFileDownloadUri(vo.getPFDownUrl());

            } else if (vo.getPFCode().equals("vp-thumbnail")) {
                thumbnailDTO.setId(++lastThumbnailId);

                thumbnailDTO.setSaveFileName(vo.getPFSaveName());
                thumbnailDTO.setOriginalFileName(vo.getPFOriName());
                thumbnailDTO.setFilePath(vo.getPFPath());
                thumbnailDTO.setFileType(vo.getPFType());
                thumbnailDTO.setFileDownloadUri(vo.getPFDownUrl());

            } else if (vo.getPFCode().equals("vp-pdf")) {
                pdfDTO.setId(++lastPdfId);

                pdfDTO.setSaveFileName(vo.getPFSaveName());
                pdfDTO.setOriginalFileName(vo.getPFOriName());
                pdfDTO.setFilePath(vo.getPFPath());
                pdfDTO.setFileType(vo.getPFType());
                pdfDTO.setFileDownloadUri(vo.getPFDownUrl());
            }
        }
        this.profileDTO = profileDTO;
        this.thumbnailDTO = thumbnailDTO;
        this.pdfDTO = pdfDTO;

        System.out.println("profileDTO: " + profileDTO);
        System.out.println("thumbnailDTO: " + thumbnailDTO);
        System.out.println("pdfDTO: " + pdfDTO);

        return "redirect:/popup/voiceport/10.0_GV-F-VP-10";
    }

//    private static Long lastVpCtrlId = 0L;
    @PostConstruct
    public void vpCtrlInit() {vpCtrlDTO = new VPCtrlDTO();}

    @PostMapping("/vp-ctrl") // 보이스포트 작성 - 기본 입력 데이터들 받는 메소드 (부모페이지)
    @Transactional
    public String saveVpCtrl(@RequestBody VPCtrlDTO vpCtrlDTO) {

        // 기본 입력데이터 저장
        this.vpCtrlDTO.setUsername(vpCtrlDTO.getUsername());
        this.vpCtrlDTO.setNickname(vpCtrlDTO.getNickname());
        this.vpCtrlDTO.setProcess(vpCtrlDTO.getProcess());

        this.vpCtrlDTO.setTitle(vpCtrlDTO.getTitle());
        this.vpCtrlDTO.setAvailability(vpCtrlDTO.getAvailability());
        this.vpCtrlDTO.setForProfitPrice(vpCtrlDTO.getForProfitPrice());
        this.vpCtrlDTO.setNonProfitPrice(vpCtrlDTO.getNonProfitPrice());
        this.vpCtrlDTO.setIntroduceContent(vpCtrlDTO.getIntroduceContent());
        this.vpCtrlDTO.setModifyContent(vpCtrlDTO.getModifyContent());
        this.vpCtrlDTO.setRefundContent(vpCtrlDTO.getRefundContent());

        if( vpCtrlDTO.getProcess().equals("submit") ){
            List<PostVO> postVOList = postRepository.findBymId(vpCtrlDTO.getUsername());
            PostFileVO profileVO = new PostFileVO();
            PostFileVO thumbnailVO = new PostFileVO();
            PostFileVO pdfVO = new PostFileVO();
            List<VpSampleVO> vpSampleVOList = new ArrayList<>();

            PostVO postVO = new PostVO();
            postVO.setPCode("vp");

            postVO.setMId(this.vpCtrlDTO.getUsername()); // 회원 아이디
            postVO.setPCount(this.vpCtrlDTO.getSampleCount()); // 업로드한 샘플 갯수
            postVO.setPAva(this.vpCtrlDTO.getAvailability()); // 현재 구직 여부

            postVO.setPForPrice(this.vpCtrlDTO.getForProfitPrice()); // 영리 가격
            postVO.setPNonPrice(this.vpCtrlDTO.getNonProfitPrice()); // 비영리 가격

            postVO.setPTitle(this.vpCtrlDTO.getTitle()); // 제목
            postVO.setPIntro(this.vpCtrlDTO.getIntroduceContent()); // 소개
            postVO.setPMod(this.vpCtrlDTO.getModifyContent()); // 수정·재진행
            postVO.setPRefund(this.vpCtrlDTO.getRefundContent()); // 취소·환불

            postVO.setPService(this.vpCtrlDTO.getServiceKeyList().toString()); // 음성 유형
            postVO.setPVoice(this.vpCtrlDTO.getVoiceKeyList().toString()); // 서비스 유형
            postVO.setPEnvir(this.vpCtrlDTO.getRecordingKeyList().toString()); // 녹음 환경

            if(postVOList.isEmpty()){ // 게시물 첫 등록 데이터의 경우
                postVO.setPDate(LocalDateTime.now());

                profileVO.setPFCode("vp-profile");
                thumbnailVO.setPFCode("vp-thumbnail");
                pdfVO.setPFCode("vp-pdf");

                System.out.println("보이스포트 등록");

            } else { // 게시물 수정 데이터의 경우
                postVO.setPId(postVOList.get(0).getPId());

                postVO.setPMdate(LocalDateTime.now());
                postVO.setPDate(postVOList.get(0).getPDate());

                boolean profileMod = false;
                boolean thumMod = false;

                // 파일 덮어쓰기
                for (PostFileVO postFileVO : postFileRepository.findBypId(postVO.getPId())){
                    if (postFileVO.getPFCode().equals("vp-profile")){
                        profileVO = postFileVO;
                        profileMod = true;
                        System.out.println("불러온 데이터 - profileVO: " +  profileVO);
                    } else if (postFileVO.getPFCode().equals("vp-thumbnail")) {
                        thumbnailVO = postFileVO;
                        thumMod = true;
                        System.out.println("불러온 데이터 - thumbnailVO: " +  profileVO);

                    } else if (postFileVO.getPFCode().equals("vp-pdf")) {
                        pdfVO = postFileVO;
                        System.out.println("불러온 데이터 - pdfVO: " +  profileVO);
                    }
                }

                if (!profileMod) profileVO.setPFCode("vp-profile");
                if (!thumMod) thumbnailVO.setPFCode("vp-thumbnail");

               vpSampleRepository.deleteBypId(postVO.getPId());

                System.out.println("보이스포트 수정");
            }

            postRepository.save(postVO);

            List<PostVO> postVOs = postRepository.findBymId(vpCtrlDTO.getUsername());
            System.out.println("postVOs: " + postVOs);

            int pId = postVOs.get(0).getPId();

            System.out.println("postVOs.get(0).getPId(): " + pId);

            // 프로필 이미지
            if (!profileDTO.isEmpty()){
                profileVO.setPFSaveName(profileDTO.getSaveFileName());
                profileVO.setPFOriName(profileDTO.getOriginalFileName());
                profileVO.setPFPath(profileDTO.getFilePath());
                profileVO.setPFType(profileDTO.getFileType());
                profileVO.setPFDownUrl(profileDTO.getFileDownloadUri());

                profileVO.setMId(vpCtrlDTO.getUsername());
                profileVO.setPId(pId);

                System.out.println("프로필 이미지 DB 저장: " + profileVO);

                postFileRepository.saveAndFlush(profileVO);
            }

            // 썸네일 이미지
            if (!thumbnailDTO.isEmpty()){
                thumbnailVO.setPFSaveName(thumbnailDTO.getSaveFileName());
                thumbnailVO.setPFOriName(thumbnailDTO.getOriginalFileName());
                thumbnailVO.setPFPath(thumbnailDTO.getFilePath());
                thumbnailVO.setPFType(thumbnailDTO.getFileType());
                thumbnailVO.setPFDownUrl(thumbnailDTO.getFileDownloadUri());

                thumbnailVO.setMId(vpCtrlDTO.getUsername());
                thumbnailVO.setPId(pId);

                System.out.println("썸네일 이미지 DB 저장: " + thumbnailVO);

                postFileRepository.saveAndFlush(thumbnailVO);
            }

            // 이력서 PDF
            if (!pdfDTO.isEmpty()){

                pdfVO.setPFSaveName(pdfDTO.getSaveFileName());
                pdfVO.setPFOriName(pdfDTO.getOriginalFileName());
                pdfVO.setPFPath(pdfDTO.getFilePath());
                pdfVO.setPFType(pdfDTO.getFileType());
                pdfVO.setPFDownUrl(pdfDTO.getFileDownloadUri());

                pdfVO.setMId(vpCtrlDTO.getUsername());
                pdfVO.setPId(pId);

                System.out.println("이력서 PDF DB 저장: " + pdfVO);

                postFileRepository.saveAndFlush(pdfVO);
            }

            // 영상샘플
            for (UrlDTO urlDTO : urlDTOList){
                VpSampleVO vpSampleVO = new VpSampleVO();

                vpSampleVO.setSpCode("video");

                vpSampleVO.setSpTitle(urlDTO.getTitle());
                vpSampleVO.setSpKeyword(urlDTO.getSelectedKeyList().toString());
                vpSampleVO.setSpYtbUrl(urlDTO.getYoutubeUrl());
                vpSampleVO.setSpThumUrl(urlDTO.getThumbnailUrl());

                vpSampleVO.setMId(vpCtrlDTO.getUsername());
                vpSampleVO.setPId(pId);

                vpSampleRepository.saveAndFlush(vpSampleVO);
            }

            // 음성샘플
            for (AudioDTO auoDTO : audioDTOList){
                VpSampleVO vpSampleVO = new VpSampleVO();

                vpSampleVO.setSpCode("audio");

                vpSampleVO.setSpTitle(auoDTO.getTitle());
                vpSampleVO.setSpKeyword(auoDTO.getSelectedKeyList().toString());
                vpSampleVO.setSpSaveName(auoDTO.getSaveFileName());
                vpSampleVO.setSpOriName(auoDTO.getOriginalFileName());
                vpSampleVO.setSpFPath(auoDTO.getFilePath());
                vpSampleVO.setSpFType(auoDTO.getFileType());
                vpSampleVO.setSpFDownUrl(auoDTO.getFileDownloadUri());

                vpSampleVO.setMId(vpCtrlDTO.getUsername());
                vpSampleVO.setPId(pId);

                vpSampleRepository.saveAndFlush(vpSampleVO);
            }

            System.out.println("11.0 - DB 업로드 완료: " + this.vpCtrlDTO);
        }

        System.out.println("11.0 - 기본데이터 저장완료: " + this.vpCtrlDTO);

        // 클라이언트에 응답
        return "redirect:/popup/voiceport/10.0_GV-F-VP-10";
    }

    @PostMapping("/vp-ctrl-save-chk") // 보이스포트 작성 - 11.1 키워드/카테고리 데이터들 받는 메소드 (자식페이지)
    public String saveCheckboxes(@RequestBody VPCtrlDTO vpCtrlDTO) {
        // 키워드/카테고리 저장

        // 선택한 음성유형의 Key 값이 담긴 리스트

        // 선택한 음성유형의 value(단어)를 담을 리스트
        List<String> voiceList = new ArrayList<>();
        // 선택한 서비스 유형의 value(단어)를 담을 리스트
        List<String> serviceList = new ArrayList<>();
        // 선택한 녹음환경의 value(단어)를 담을 리스트
        List<String> recordingKeyList = new ArrayList<>();

        // 음성 및 서비스 유형 map이 정의된 클래스 정의
        Keywords keywords = new Keywords();

        for (int key : vpCtrlDTO.getServiceKeyList()){
            // serviceList에 담긴 key 값과 일치하는 서비스 유형을 keywordsList에 추가
            serviceList.add(keywords.getServiceTypesMap().get(key));
        }

        for (int key : vpCtrlDTO.getVoiceKeyList()) {
            // voiceList에 담긴 key 값과 일치하는 음성유형을 keywordsList에 추가
            voiceList.add(keywords.getVoiceTypesMap().get(key));
        }

        for (int key : vpCtrlDTO.getRecordingKeyList()){
            // recordingKeyList에 담긴 key 값과 일치하는 녹음 유형을 recordingKeyList에 추가
            recordingKeyList.add(keywords.getRecordingEnvirMap().get(key));
        }

        //  선택된 key 값 저장
        this.vpCtrlDTO.setServiceKeyList(vpCtrlDTO.getServiceKeyList());
        this.vpCtrlDTO.setVoiceKeyList(vpCtrlDTO.getVoiceKeyList());
        this.vpCtrlDTO.setRecordingKeyList(vpCtrlDTO.getRecordingKeyList());

        // 선택된 value 값 저장
        this.vpCtrlDTO.setServiceTypes(serviceList);
        this.vpCtrlDTO.setVoiceTypes(voiceList);
        this.vpCtrlDTO.setRecordingEnvironments(recordingKeyList);

        System.out.println("11.1 - 키워드/카테고리 설정 저장완료: " + vpCtrlDTO);

        // 클라이언트에 응답
        return "redirect:/popup/voiceport/11.1_GV-F-VP-ChKw-11.1";
    }

    @RequestMapping("/11.1_GV-F-VP-ChKw-11.1")
    public String vpChKw(Model model) { // 보이스포트 작성/수정 - 키워드/카테고리 설정 (자식페이지)
        // 키워드/카테고리 데이터 전송
        model.addAttribute("vpCtrlDTO", vpCtrlDTO);

        return "/popup/voiceport/11.1_GV-F-VP-ChKw-11.1";
    }
    @PostConstruct
    public void profileInit() {profileDTO = new FileDTO();}

    @PostMapping("/upload-profile-image") // 보이스포트 작성 - 프로필 이미지 업로드 메소드
    public String handleProfileFileUpload(@RequestParam("profileImgFile") MultipartFile file) {

        FileDTO fileDTO = new FileDTO();

        // 외부 디렉터리에 파일 저장
        try {
            String originalFileName = file.getOriginalFilename();

            System.out.println("프로필 이미지: " + originalFileName);

            // 랜덤(저장용) 파일명 생성
            String randomFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            // 파일 경로 생성
            Path filePath = Paths.get(imgDir).resolve(randomFileName);

            // 원본 파일명 저장
            fileDTO.setOriginalFileName(originalFileName);

            // 랜덤(저장용) 파일명 저장
            fileDTO.setSaveFileName(randomFileName);

            // 파일 MIME 타입 저장
            fileDTO.setFileType(file.getContentType());

            // 파일 경로 저장
            fileDTO.setFilePath(filePath.toString());

            fileDTO.setId(++lastProfileId);

            if (profileDTO.getSaveFileName()!=null){// 수정된 파일일 경우,
                // 이전 파일 삭제
                Path path = Paths.get(profileDTO.getFilePath());
                Files.delete(path);
                System.out.println("(프로필 이미지 파일 수정) : 이전 파일이 삭제되었습니다.");
            }

            // 파일 업로드
            file.transferTo(new File(filePath.toString()));

            // 파일 다운로드 URL 생성 - DownloadController 에서 이어짐
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(randomFileName)
                    .toUriString();

            fileDTO.setFileDownloadUri(fileDownloadUri);

            System.out.println("(프로필) 다운로드 링크: " + fileDownloadUri);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println( "(프로필) 파일 업로드 OR 삭제 오류: " + e.getMessage() );
        }

        System.out.println("(프로필) 파일 업로드 성공!");

        profileDTO = fileDTO;

        return "redirect:/popup/voiceport/11.0_GV-F-VPCtrl-11.0";
    }

    @RequestMapping("/11.2_GV-F-VP-AudioUpload-11.2")
    public String vpAudioUpload(Model model) { // 보이스포트 작성/수정 - 음성샘플 업로드/수정

        return "/popup/voiceport/11.2_GV-F-VP-AudioUpload-11.2";
    }

    // audioDTO의 ID를 관리하기 위한 클래스 정적 변수 선언
    // 원래는 id값은 데이터베이스의 id를 사용하나,
    // 이 경우, 보이스포트 최종 저장 시에 DB가 업데이트 되는 형식이므로 정적 변수를 사용
    @PostConstruct
    public void initAudioDTOList() {
        audioDTOList = new ArrayList<>();
    }

    @Value("${upload.dir}")
    private String uploadDir; // 파일 저장 경로

    @PostMapping("/vp-audio-post-endpoint") // 팝업창(음성샘플 업로드/수정) - 파일을 포함한 입력 데이터를 전송받는 메소드
    public  String audioAjaxRequest(@RequestParam("file") MultipartFile file,
                                    @RequestParam("title") String title,
                                    @RequestParam("selectedKeywords") String selectedKeywords,
                                    @RequestParam("id") String id) {

        AudioDTO audioDTO = new AudioDTO();

        // 받은 데이터를 사용한 작업 수행
        boolean mod = false; // 수정 판별 여부
        int modNum = -1; // 수정 게시물의 아이디를 저장

        Long longId = -1L; // 문자열로 받은 아이디 타입 변환하여 저장

        // 선택한 음성유형의 Key 값이 담긴 리스트
        List<Integer> keyList = jsonStringToList(selectedKeywords);
        audioDTO.setSelectedKeyList(keyList);
        // 선택한 음성유형의 value(단어)를 담을 리스트
        List<String> keywordsList = new ArrayList<>();
        // 음성 및 서비스 유형 map이 정의된 클래스 정의
        Keywords keywords = new Keywords();

        for (int key : keyList) {
            // keyList에 담긴 key 값과 일치하는 음성유형을 keywordsList에 추가
            keywordsList.add(keywords.getVoiceTypesMap().get(key));
        }

        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            System.out.println("유효한 숫자 형식이 아닙니다.");
        }

        for (int i=0; i<audioDTOList.size(); i++){ // 받은 데이터가 수정 데이터인지 확인
            if( longId>-1L && audioDTOList.get(i).getId().equals(longId)){ // 수정 데이터일 경우,
                mod = true; // 수정 데이터임
                modNum = i; // 수정 데이터의 위치 저장
                break;
            }
        }

        // 외부 디렉터리에 파일 저장
        try {
            // 랜덤(저장용) 파일명 생성
            String randomFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            // 파일 경로 생성
            Path filePath = Paths.get(uploadDir).resolve(randomFileName);

            if (!mod){ // 업로드 파일일 경우,
                // 원본 파일명 저장
                audioDTO.setOriginalFileName(file.getOriginalFilename());

                // 랜덤(저장용) 파일명 저장
                audioDTO.setSaveFileName(randomFileName);

                // 파일 MIME 타입 저장
                audioDTO.setFileType(file.getContentType());

                // 파일 경로 저장
                audioDTO.setFilePath(filePath.toString());
            } else {// 수정된 파일일 경우,
                // 이전 파일 삭제
                Path path = Paths.get(audioDTOList.get(modNum).getFilePath());
                Files.delete(path);

                audioDTOList.get(modNum).setOriginalFileName(file.getOriginalFilename());
                audioDTOList.get(modNum).setSaveFileName(randomFileName);
                audioDTOList.get(modNum).setFileType(file.getContentType());
                audioDTOList.get(modNum).setFilePath(filePath.toString());
            }

            // 파일 업로드
            file.transferTo(new File(filePath.toString()));

            // 파일 다운로드 URL 생성 - DownloadController 에서 이어짐
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(randomFileName)
                    .toUriString();

            if (!mod){ // 업로드 파일일 경우,
                audioDTO.setFileDownloadUri(fileDownloadUri);
            } else {  // 수정된 파일일 경우,
                audioDTOList.get(modNum).setFileDownloadUri(fileDownloadUri);
            }

            System.out.println("다운로드 링크: " + fileDownloadUri);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println( "파일 업로드 OR 삭제 오류: " + e.getMessage() );
        }

        System.out.println("파일 업로드 성공!");


        if (mod){ // 수정 데이터의 경우.
            audioDTOList.get(modNum).setTitle(title);
            audioDTOList.get(modNum).setSelectedKeywords(keywordsList);

            System.out.println("*** (Yes 파일) 음성샘플 수정 데이터 업데이트 완료 ***");
        } else {// 업로드 데이터의 경우,
            audioDTO.setTitle(title);
            audioDTO.setSelectedKeywords(keywordsList);

            audioDTO.setId(++lastAudioId); // 아이디 부여

            System.out.println("음성샘플 제목: " + audioDTO.getTitle());
            System.out.println("음성샘플 키워드: " +audioDTO.getSelectedKeywords().toString());
            System.out.println("음성샘플 파일 URL:" + audioDTO.getFilePath());


            audioDTOList.add(audioDTO);
        }

        tabNum = 2; // 탭 위치는 음성샘플

        return "redirect:/popup/voiceport/11.0_GV-F-VPCtrl-11.0";

    }
    private static List<Integer> jsonStringToList(String jsonString){
        // 클라이언트에서 전송한 JSON 형식의 문자열을 JSON 배열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> keywordsList = null;
        try {
            keywordsList = objectMapper.readValue(jsonString, new TypeReference<List<Integer>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println( "Invalid JSON format: " + e.getMessage() );
        }
        return keywordsList;
    }
    @PostMapping("/vp-audio-no-file-modify") // 팝업창(음성샘플 수정) - 파일 수정이 없는 경우 (파일을 제외한 입력 데이터를 전송받는 메소드 )
    public  String audioNoFileModifyRequest(@RequestParam("title") String title,
                                            @RequestParam("selectedKeywords") String selectedKeywords,
                                            @RequestParam("id") String id) {

        System.out.println("파일 비첨부 수정");
        System.out.println("제목: " + title);
        System.out.println("키워드: " + selectedKeywords);
        System.out.println("아이디: " + id);

        Long longId = -1L;

        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            System.out.println("유효한 숫자 형식이 아닙니다.");
        }

        // 받은 데이터를 사용한 작업 수행
        int modNum = -1; // 수정 게시물의 아이디를 저장

        for (int i=0; i<audioDTOList.size(); i++){ // 수정 데이터의 위치 검색
            if(longId>-1L && audioDTOList.get(i).getId().equals(longId)){
                modNum = i; // 수정 데이터의 위치 저장
                System.out.println("수정 데이터 위치 발견: " + modNum);
                break;
            }
        }

        // 선택한 음성유형의 Key 값이 담긴 리스트
        List<Integer> keyList = jsonStringToList(selectedKeywords);
        audioDTOList.get(modNum).setSelectedKeyList(keyList);
        // 선택한 음성유형의 value(단어)를 담을 리스트
        List<String> keywordsList = new ArrayList<>();
        // 음성 및 서비스 유형 map이 정의된 클래스 정의
        Keywords keywords = new Keywords();

        for (int key : keyList) {
            // keyList에 담긴 key 값과 일치하는 음성유형을 keywordsList에 추가
            keywordsList.add(keywords.getVoiceTypesMap().get(key));
        }

        audioDTOList.get(modNum).setSelectedKeywords(keywordsList);


        if (modNum>-1){ // 수정 데이터의 위치가 잘 저장되었는지 확인
            audioDTOList.get(modNum).setTitle(title);
            audioDTOList.get(modNum).setSelectedKeywords(keywordsList);

            System.out.println(audioDTOList.get(modNum).toString());

            System.out.println("*** (No 파일) 음성샘플 수정 데이터 업데이트 완료 ***");
        }

        tabNum = 2;

        return "redirect:/popup/voiceport/11.0_GV-F-VPCtrl-11.0";
    }

    @DeleteMapping("/vp-audio-delete-endpoint/{itemId}") // 음성 샘플 삭제
    public ResponseEntity<String> deleteAudioItem(@PathVariable Long itemId) {
        // itemId를 사용해 해당 아이템을 찾아서 삭제
        Optional<AudioDTO> optionalAudioDTO = audioDTOList.stream()
                .filter(audioDTO -> audioDTO.getId().equals(itemId))
                .findFirst();

        if (optionalAudioDTO.isPresent()) {
            AudioDTO deletedAudioDTO = optionalAudioDTO.get();
            audioDTOList.remove(deletedAudioDTO);

            tabNum = 2;

            return ResponseEntity.ok("(음성샘플) 아이템이 성공적으로 삭제되었습니다. Item ID: " + itemId);
        } else {
            // 해당 ID를 가진 아이템이 없는 경우 404 Not Found 응답을 보냄
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("(음성샘플) 해당 ID를 가진 아이템을 찾을 수 없습니다. Item ID: " + itemId);
        }
    }

    @GetMapping("/vp-audio-modify-endpoint") // 음성 샘플 수정
    public ResponseEntity<AudioDTO> handlePopupAudioRequest(@RequestParam(name = "id") Long id) {
        // id에 해당하는 게시물을 audioDTOList에서 찾기
        AudioDTO selectedAudioDTO = null;
        for (AudioDTO audioDTO : audioDTOList) {
            if (audioDTO.getId().equals(id)) {
                selectedAudioDTO = audioDTO;
                break;
            }
        }

        if (selectedAudioDTO != null) {
            // 선택한 게시물을 JSON 형태로 응답으로 보냄
            return ResponseEntity.ok(selectedAudioDTO);
        } else {
            // 게시물이 존재하지 않을 경우, 404 Not Found를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping("/11.3_GV-F-VP-VideoUpload-11.3")
    public String vpVideoUpload(Model model) { // 보이스포트 작성/수정 - 영상샘플 업로드/수정


        // 적절한 뷰로 리디렉션 (예: 결과를 보여주는 뷰로 이동)
        return "/popup/voiceport/11.3_GV-F-VP-VideoUpload-11.3"; // 결과 페이지 뷰 이름
    }


    // UrlDTO의 ID를 관리하기 위한 클래스 정적 변수 선언
    // 원래는 id값은 데이터베이스의 id를 사용하나,
    // 이 경우, 보이스포트 최종 저장 시에 DB가 업데이트 되는 형식이므로 정적 변수를 사용


    @PostConstruct
    public void initUrlDTOList() {
         urlDTOList = new ArrayList<>();
     }

    @PostMapping("/vp-video-post-endpoint") // 팝업창(영상샘플 업로드/수정) - 입력 데이터 전송받는 메소드
    public  String videoAjaxRequest(@RequestBody UrlDTO urlDTO) {
        // 받은 데이터(urlDTO)를 사용한 작업 수행

        boolean mod = false; // 수정 판별 여부

        // 선택한 음성유형의 value(단어)를 담을 리스트
        List<String> keywordsList = new ArrayList<>();
        // 음성 및 서비스 유형 map이 정의된 클래스 정의
        Keywords keywords = new Keywords();

        for (int key : urlDTO.getSelectedKeyList()) {
            // keyList에 담긴 key 값과 일치하는 음성유형을 keywordsList에 추가
            keywordsList.add(keywords.getVoiceTypesMap().get(key));
        }

        for (int i=0; i<urlDTOList.size(); i++){ // 받은 데이터가 수정 데이터인지 확인
            if(urlDTOList.get(i).getId().equals(urlDTO.getId())){ // 수정 데이터일 경우,
                urlDTO.setThumbnailUrl(urlDTO.generateThumbnailUrl(urlDTO.extractVideoId(urlDTO.getYoutubeUrl()))); // 유튜브 썸네일 코드 추출 밎 저장
                urlDTO.setSelectedKeywords(keywordsList); // 선택한 키워드(value) 저장
                urlDTOList.set(i, urlDTO);
                mod = true;
            }
        }

        if (!mod) { // 업로드 데이터일 경우,
            urlDTO.setThumbnailUrl(urlDTO.generateThumbnailUrl(urlDTO.extractVideoId(urlDTO.getYoutubeUrl()))); // 유튜브 썸네일 코드 추출 밎 저장
            urlDTO.setSelectedKeywords(keywordsList); // 선택한 키워드(value) 저장
            urlDTO.setId(++lastVideoId); // 아이디 부여

            System.out.println("영상샘플 유튜브 URL: " + urlDTO.getYoutubeUrl());
            System.out.println("영상샘플 제목: " + urlDTO.getTitle());
            System.out.println("영상샘플 키워드: " +urlDTO.getSelectedKeywords().toString());
            System.out.println("영상샘플 썸네일 URL:" + urlDTO.getThumbnailUrl());

            urlDTOList.add(urlDTO);
        }

        tabNum = 1;
        return "redirect:/popup/voiceport/11.0_GV-F-VPCtrl-11.0";
    }

    @DeleteMapping("/vp-video-delete-endpoint/{itemId}") // 영상 샘플 삭제
    public ResponseEntity<String> deleteVideoItem(@PathVariable Long itemId) {
        // itemId를 사용해 해당 아이템을 찾아서 삭제
        Optional<UrlDTO> optionalUrlDTO = urlDTOList.stream()
                .filter(urlDTO -> urlDTO.getId().equals(itemId))
                .findFirst();

        if (optionalUrlDTO.isPresent()) {
            UrlDTO deletedUrlDTO = optionalUrlDTO.get();
            urlDTOList.remove(deletedUrlDTO);

            tabNum = 1;
            return ResponseEntity.ok("(영상샘플) 아이템이 성공적으로 삭제되었습니다. Item ID: " + itemId);
        } else {
            // 해당 ID를 가진 아이템이 없는 경우, 404 Not Found 응답을 보냄.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("(영상샘플) 해당 ID를 가진 아이템을 찾을 수 없습니다. Item ID: " + itemId);
        }
    }

    @GetMapping("/vp-video-modify-endpoint") // 영상 샘플 수정
    public ResponseEntity<UrlDTO> handlePopupVideoRequest(@RequestParam(name = "id") Long id) {
        // id에 해당하는 게시물을 urlDTOList에서 찾기
        UrlDTO selectedUrlDTO = null;
        for (UrlDTO urlDTO : urlDTOList) {
            if (urlDTO.getId().equals(id)) {
                selectedUrlDTO = urlDTO;
                break;
            }
        }

        if (selectedUrlDTO != null) {
            // 선택한 게시물을 JSON 형태로 응답으로 보냄
            return ResponseEntity.ok(selectedUrlDTO);
        } else {
            // 게시물이 존재하지 않을 경우, 404 Not Found를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Value("${img.dir}")
    private String imgDir; // 이미지 파일 저장 경로
    @PostConstruct
    public void thumbnailInit() {
        thumbnailDTO = new FileDTO();
    }

    @PostMapping("/vp-thumbnail-endpoint") // 팝업창(썸네일 이미지 업로드/수정)
    public  String ThumbnailRequest(@RequestParam("file") MultipartFile file) {

        // 받은 데이터를 사용한 작업 수행

        FileDTO fileDTO = new FileDTO();

        // 외부 디렉터리에 파일 저장
        try {
            String originalFileName = file.getOriginalFilename();

            // 랜덤(저장용) 파일명 생성
            String randomFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            // 파일 경로 생성
            Path filePath = Paths.get(imgDir).resolve(randomFileName);

            // 원본 파일명 저장
            fileDTO.setOriginalFileName(originalFileName);

            // 랜덤(저장용) 파일명 저장
            fileDTO.setSaveFileName(randomFileName);

            // 파일 MIME 타입 저장
            fileDTO.setFileType(file.getContentType());

            // 파일 경로 저장
            fileDTO.setFilePath(filePath.toString());

            fileDTO.setId(++lastThumbnailId);

            if (thumbnailDTO.getSaveFileName() != null ){// 수정된 파일일 경우,
                // 이전 파일 삭제
                Path path = Paths.get(thumbnailDTO.getFilePath());
                Files.delete(path);
            }

            // 파일 업로드
            file.transferTo(new File(filePath.toString()));

            // 파일 다운로드 URL 생성 - DownloadController 에서 이어짐
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(randomFileName)
                    .toUriString();

            fileDTO.setFileDownloadUri(fileDownloadUri);

            System.out.println("(썸네일) 다운로드 링크: " + fileDownloadUri);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println( "파일 업로드 OR 삭제 오류: " + e.getMessage() );
        }

        System.out.println("파일 업로드 성공!");


        thumbnailDTO = fileDTO;

        return "redirect:/popup/voiceport/11.0_GV-F-VPCtrl-11.0";

    }

    @GetMapping("/vp-thumbnail-modify-endpoint") // 썸네일 수정
    public ResponseEntity<FileDTO> handlePopupThumbnailRequest(@RequestParam(name = "id") Long id) {

        if ( thumbnailDTO.getId().equals(id) && !thumbnailDTO.getId().equals(null)) {
            // 선택한 게시물을 JSON 형태로 응답으로 보냄
            return ResponseEntity.ok(thumbnailDTO);
        } else {
            // 게시물이 존재하지 않을 경우, 404 Not Found를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @RequestMapping("/11.4_thumbnail-upload-11.4")
    public String vpThumbnailUpload(Model model) { // 보이스포트 작성/수정 - 썸네일 업로드

        return "/popup/voiceport/11.4_thumbnail-upload-11.4";
    }

    @Value("${pdf.dir}")
    private String pdfDir; // pdf 파일 저장 경로
    @PostConstruct
    public void pdfInit() {
        pdfDTO = new FileDTO();
    }

    @PostMapping("/vp-pdf-endpoint") // 팝업창(썸네일 이미지 업로드/수정)
    public  String pdfRequest(@RequestParam("file") MultipartFile file) {

        // 받은 데이터를 사용한 작업 수행

        FileDTO fileDTO = new FileDTO();

        // 외부 디렉터리에 파일 저장
        try {
            String originalFileName = file.getOriginalFilename();

            // 랜덤(저장용) 파일명 생성
            String randomFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            // 파일 경로 생성
            Path filePath = Paths.get(pdfDir).resolve(randomFileName);

            // 원본 파일명 저장
            fileDTO.setOriginalFileName(originalFileName);

            // 랜덤(저장용) 파일명 저장
            fileDTO.setSaveFileName(randomFileName);

            // 파일 MIME 타입 저장
            fileDTO.setFileType(file.getContentType());

            // 파일 경로 저장
            fileDTO.setFilePath(filePath.toString());

            fileDTO.setId(++lastPdfId);

            if (pdfDTO.getSaveFileName() != null ){// 수정된 파일일 경우,
                // 이전 파일 삭제
                Path path = Paths.get(pdfDTO.getFilePath());
                Files.delete(path);
            }

            // 파일 업로드
            file.transferTo(new File(filePath.toString()));

            // 파일 다운로드 URL 생성 - DownloadController 에서 이어짐
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(randomFileName)
                    .toUriString();

            fileDTO.setFileDownloadUri(fileDownloadUri);

            System.out.println("(pdf) 다운로드 링크: " + fileDownloadUri);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println( "(pdf) 파일 업로드 OR 삭제 오류: " + e.getMessage() );
        }

        System.out.println("(pdf) 파일 업로드 성공!");


        pdfDTO = fileDTO;

        return "redirect:/popup/voiceport/11.0_GV-F-VPCtrl-11.0";

    }

    @GetMapping("/vp-pdf-modify-endpoint") // 이력서 수정
    public ResponseEntity<FileDTO> handlePopupPdfRequest(@RequestParam(name = "id") Long id) {

        if ( pdfDTO.getId().equals(id) && !pdfDTO.getId().equals(null)) {
            // 선택한 게시물을 JSON 형태로 응답으로 보냄
            return ResponseEntity.ok(pdfDTO);
        } else {
            // 게시물이 존재하지 않을 경우, 404 Not Found를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping("/11.5_pdf-upload-11.5")
    public String vpPdfUpload(Model model) { // 보이스포트 작성/수정 - 이력서 pdf 파일 업로드

        return "/popup/voiceport/11.5_pdf-upload-11.5";
    }
    @PostConstruct
    public void estCalInit() {
        estCalDTO = new EstCalDTO();
    }

    @PostMapping("/vp-est-cal-endpoint") // 예상견적 계산 (보이스포트) 데이터를 전송받는 메소드
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("profit") String profit,
                             @RequestParam("forProfitPrice") int forProfitPrice,
                             @RequestParam("nonProfitPrice") int nonProfitPrice) {
        int characterCount = 0;

        try {
            // 파일을 읽어들일 BufferedReader 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;

            // 파일의 각 라인을 읽어들여 공백을 제외한 글자 수를 계산
            while ((line = reader.readLine()) != null) {
                // 문자열 내의 공백을 제외한 글자 수 계산
                characterCount += line.replaceAll("\\s+", "").length();
            }

            estCalDTO.setCharacterCount(characterCount);
            estCalDTO.setProfit(profit);
            estCalDTO.setForProfitPrice(forProfitPrice);
            estCalDTO.setNonProfitPrice(nonProfitPrice);

            System.out.println("(10.3) 글자 수 출력: " + characterCount);
            // BufferedReader 닫기
            reader.close();
        } catch (IOException e) {
            System.out.println("(10.3) 견적 계산 파일 에러: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/popup/voiceport/10.3_GV-F-EstCal-10.3";
    }

    @PostMapping("/vp-est-cal-init") // 예상견적 계산 (보이스포트) 데이터를 초기화하는 메서드
    public String estCalDataInit() {
        estCalInit();
        System.out.println("견적계산 데이터 초기화 완료");

        return "redirect:/popup/voiceport/10.3_GV-F-EstCal-10.3";
    }

    @RequestMapping("/10.3_GV-F-EstCal-10.3")
    public String estCal(Model model) { // 예상견적 계산 (보이스포트)
        // 프로필 이미지 데이터 전송
        model.addAttribute("profileDTO", profileDTO);
        // 예상 견적 데이터 전송
        model.addAttribute("estCalDTO", estCalDTO);

        return "/popup/voiceport/10.3_GV-F-EstCal-10.3";
    }

    @RequestMapping("/11.0_GV-F-VPCtrl-11.0")
    public String vpCtrl(Model model) { // 보이스포트 작성/수정 (팝업창 부모페이지)
        // 업로드한 샘플 갯수 저장
        vpCtrlDTO.setSampleCount(audioDTOList.size()+urlDTOList.size());

        // 탭 번호 전송
        model.addAttribute("tabNum", tabNum);
        // 영상 샘플 데이터 전송
        model.addAttribute("urlDTOList", urlDTOList);
        // 음성 샘플 데이터 전송
        model.addAttribute("audioDTOList", audioDTOList);
        // 썸네일 데이터 전송
        model.addAttribute("thumbnailDTO", thumbnailDTO);
        // 이력서 데이터 전송
        model.addAttribute("pdfDTO", pdfDTO);
        // 프로필 이미지 데이터 전송
        model.addAttribute("profileDTO", profileDTO);
        // 기본 입력 데이터 전송
        model.addAttribute("vpCtrlDTO", vpCtrlDTO);

        System.out.println("11.0 음성유형: " + vpCtrlDTO.getVoiceTypes());

        return "/popup/voiceport/11.0_GV-F-VPCtrl-11.0";
    }

    @RequestMapping("/10.0_GV-View-VP-10")
    public String vpView(Model model) { // 보이스포트 > 게시물 보기

        // 탭 번호 전송
        model.addAttribute("tabNum", tabNum);
        // 영상 샘플 데이터 전송
        model.addAttribute("urlDTOList", urlDTOList);
        // 음성 샘플 데이터 전송
        model.addAttribute("audioDTOList", audioDTOList);
        // 이력서 데이터 전송
        model.addAttribute("pdfDTO", pdfDTO);
        // 프로필 이미지 데이터 전송
        model.addAttribute("profileDTO", profileDTO);
        // 기본 입력 데이터 전송
        model.addAttribute("vpCtrlDTO", vpCtrlDTO);

        return "/popup/voiceport/10.0_GV-View-VP-10";
    }


}
