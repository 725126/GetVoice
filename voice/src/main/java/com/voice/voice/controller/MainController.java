package com.voice.voice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voice.voice.DTO.*;

import com.voice.voice.etc.Keywords;
import com.voice.voice.model.MemberVO;
import com.voice.voice.model.PostFileVO;
import com.voice.voice.model.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
@RequestMapping("/board/ct")
public class MainController {
    @Autowired
    private com.voice.voice.repository.MemberRepository memberRepository;
    @Autowired
    private com.voice.voice.repository.PostRepository postRepository;
    @Autowired
    private com.voice.voice.repository.PostFileRepository postFileRepository;
    Map<Integer, String> favVoiceTypesMap;
    MemberDTO memberDTO;

    @PostConstruct
    public void initFavVoiceTypesMap(){favVoiceTypesMap = new HashMap<>();}
    @PostConstruct
    public void initCurrLoginMember(){ memberDTO = new MemberDTO(); }

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

    @GetMapping("/vp-my-money") // 현재 나의 V머니 정보를 전송하는 메서드
    public ResponseEntity<Integer> callMyMoneyRequest(){
        // pid에 해당하는 게시물을 찾기
        if (!this.memberDTO.getId().isEmpty()){
            Integer vMoney = memberRepository.findById(memberDTO.getId()).get().getMMoney();

            return ResponseEntity.ok(vMoney);
        } else {
            // 게시물이 존재하지 않을 경우, 404 Not Found를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/vm-char-endpoint")
    public ResponseEntity<Integer> handleCharVMoney(@RequestParam("amount") int amount) {

        System.out.println("선택된 충전 금액: " + amount);

        MemberVO memberVO = memberRepository.findById(this.memberDTO.getId()).get();
        memberVO.setmMoney(memberVO.getMMoney()+amount);
        memberRepository.save(memberVO);

        int currAmount = memberRepository.findById(this.memberDTO.getId()).get().getMMoney();

        return ResponseEntity.ok(currAmount);
    }

    @RequestMapping("/1.0_GV-F-Main-1.0")
    public String main(Model model) { // 비 로그인 시 메인

        System.out.println("비 로그인: " + memberDTO);

        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/1.0_GV-F-Main-1.0";
    }

    @RequestMapping("/1.1_GV-F-CTMain-1.1")
    public String ctMain(Model model) { // 구인자 로그인 시 메인

        System.out.println("구인자 로그인: " + memberDTO);

        // 구직 게시물 데이터 추출
        List<PostVO> postVOList = postRepository.findAll();
        List<VPCtrlDTO> vpCtrlDTOList = new ArrayList<>();

        for (PostVO vo : postVOList){
            if (vo.getPCode().equals("vp")){
                MemberVO memberVO = new MemberVO();
                List<PostFileVO> postFileVOList = postFileRepository.findBypId(vo.getPId());
                Optional<MemberVO> memberVOList = memberRepository.findById(vo.getMId());
                if (memberVOList.isPresent()){
                    memberVO = memberVOList.get();
                }

                VPCtrlDTO vpCtrlDTO = new VPCtrlDTO(vo);
                vpCtrlDTO.setNickname(memberVO.getMNn());
                vpCtrlDTO.setVoiceTypes(returnValueList(vpCtrlDTO.getVoiceKeyList(), "voice"));

                for (PostFileVO postFileVO : postFileVOList){
                    if (postFileVO.getPFCode().equals("vp-thumbnail")){
                        vpCtrlDTO.setThumnbDownUrl(postFileVO.getPFDownUrl());
                    }
                }

                vpCtrlDTOList.add(vpCtrlDTO);
            }
        }

        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("favVoiceTypesMap", favVoiceTypesMap);
        model.addAttribute("vpCtrlDTOList", vpCtrlDTOList);

        return "/board/ct/1.1_GV-F-CTMain-1.1";
    }

    @RequestMapping("/1.2_GV-F-VAMAIN-1.2")
    public String vaMain(Model model) { // 구직자 로그인 시 메인

        System.out.println("구직자 로그인: " + memberDTO);

        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("favVoiceTypesMap", favVoiceTypesMap);

        return "/board/ct/1.2_GV-F-VAMAIN-1.2";
    }

    @GetMapping("/check-Login-endpoint") // 메인(회원가입) - 로그인 입력데이터를 검증하는 메소드
    public ResponseEntity<Map<String, String>> LoginIdPwCheck(@RequestParam("username") String username,
                                                              @RequestParam("password") String password) {
        Map<String, String> response = new HashMap<>();

        System.out.println("로그인 - 입력한 아이디: " + username);
        System.out.println("로그인: " + memberRepository.findById(username));

        if (!memberRepository.findById(username).isEmpty()){
            Optional<MemberVO> memberVOS = memberRepository.findById(username);

            if (memberVOS.isPresent()) {
                MemberVO memberVO = memberVOS.get();

                if (memberVO.getMPw().equals(password)) {

                    Keywords keywords = new Keywords();

                    List<Integer> keyList = jsonStringToList(memberVO.getMFav());

                    // 로그인 회원 데이터 DTO에 저장
                    this.memberDTO.setId(memberVO.getMId()); // 아이디
                    this.memberDTO.setPurpose(memberVO.getMCode()); // 회원 구분 코드
                    this.memberDTO.setNickname(memberVO.getMNn()); // 닉네임
                    this.memberDTO.setKeyList(keyList); // 선호키워드
                    this.memberDTO.setPhone(memberVO.getMNum()); // 연락처

                    for (int key : keyList) {
                        // keyList에 담긴 key 값과 일치하는 음성유형을 favVoiceTypesMap에 추가
                        favVoiceTypesMap.put(key, keywords.getVoiceTypesMap().get(key));
                    }

                    response.put("message", "login-true");
                    System.out.println("로그인 성공");

                    return ResponseEntity.ok(response);
                }

            }
        }

        response.put("message", "login-false");
        System.out.println("로그인 실패");

        return ResponseEntity.ok(response);
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

    @PostMapping("/0.0_login-check")
    public String loginUser(@RequestParam("lg_username") String username) { // 검증된 로그인 입력데이터로 이동할 메인페이지를 결정하는 메소드

        System.out.println("로그인 페이지 이동: " + username);

        if ( memberDTO.getId().equals(username)){
            if (memberDTO.getPurpose().equals("ct")) { // 구인자 로그인
                return "redirect:/board/ct/1.1_GV-F-CTMain-1.1";
            } else if (memberDTO.getPurpose().equals("va")) { // 구직자 로그인
                return "redirect:/board/ct/1.2_GV-F-VAMAIN-1.2";
            }
        }


        return "redirect:/board/ct/1.0_GV-F-Main-1.0";
    }

    @PostMapping("/0.0_join-register")
    public String registerUser(@ModelAttribute MemberDTO memberDTO) { // 회원가입 데이터를 DB에 저장하는 메소드
        // 회원가입 로직 수행
        memberDTO.setKeyList(favkeyList);
        System.out.println("회원가입 데이터:" + memberDTO);

        MemberVO memberVO = new MemberVO();

        memberVO.setmId(memberDTO.getId());
        memberVO.setmPw(memberDTO.getPassword());
        memberVO.setmCode(memberDTO.getPurpose());
        memberVO.setmNn(memberDTO.getNickname());
        memberVO.setmNum(memberDTO.getPhone());
        memberVO.setmFav(favkeyList.toString());

        memberRepository.save(memberVO);

        // 회원가입 성공 후, 메인 페이지로 리다이렉트
        return "redirect:/board/ct/1.0_GV-F-Main-1.0";
    }

    @GetMapping("/my-vp-endpoint") // 나의 보이스버튼 클릭 > 첫 작성 여부 판단 메소드
    public ResponseEntity<VPCtrlDTO> handlePopupMyVpRequest(@RequestParam(name = "username") String username) {

        List<PostVO> postVOList = postRepository.findBymId(username);
        VPCtrlDTO vpCtrlDTO = new VPCtrlDTO();

        if (postVOList.isEmpty()) { // 게시물(vp) 첫 작성의 경우
            // 선택한 게시물을 JSON 형태로 응답으로 보냄
            if (username.equals(memberDTO.getId())){
                vpCtrlDTO.setUsername(memberDTO.getId());
                vpCtrlDTO.setNickname(memberDTO.getNickname());
            }
            System.out.println("나의 보이스포트 : 첫 작성" + vpCtrlDTO);
            return ResponseEntity.ok(vpCtrlDTO);
        } else {
            vpCtrlDTO = new VPCtrlDTO(postVOList.get(0));
            vpCtrlDTO.setNickname(memberDTO.getNickname());

            System.out.println("나의 보이스포트 - 불러오기(vpCtrlDTO): " + vpCtrlDTO);
            System.out.println("나의 보이스포트 - 불러오기(postVO): " + postVOList.get(0));

            return ResponseEntity.ok(vpCtrlDTO);
        }
    }

    @PostMapping("/logout-init-member")
    public String logoutInitMember() { // 로그아웃 메소드
        initCurrLoginMember();
        initFavVoiceTypesMap();

        System.out.println("로그아웃 - member 초기화");

        return "/board/ct/1.0_GV-F-Main-1.0";
    }

    @GetMapping("/check-id-endpoint") // 메인(회원가입) - 입력한 아이디 데이터를 받아와서 중복확인을 수행하는 메소드
    public ResponseEntity<Map<String, String>> joinIDCheck(@RequestParam("id") String id) {
        Map<String, String> response = new HashMap<>();

        System.out.println("회원가입 - 입력한 ID: " + id);
        System.out.println(memberRepository.findById(id));

        if (memberRepository.findById(id).isEmpty()) {
            response.put("message", "사용 가능한 아이디입니다.");
        } else {
            response.put("message", "이미 사용 중인 아이디입니다.");
        }

        return ResponseEntity.ok(response);
    }

    @RequestMapping("/2.2_GV-F-JoinID-03")
    public String joinID(Model model) { // 메인(회원가입) - 아이디 중복확인

        return "/board/ct/2.2_GV-F-JoinID-03";
    }

    @GetMapping("/check-nickname-endpoint") // 메인(회원가입) - 입력한 닉네임 데이터를 받아와서 중복확인을 수행하는 메소드
    public ResponseEntity<Map<String, String>> joinNicknameCheck(@RequestParam("nickname") String nickname) {
        Map<String, String> response = new HashMap<>();

        System.out.println("회원가입 - 입력한 닉네임: " + nickname);
        System.out.println(memberRepository.findBymNn(nickname));

        if (memberRepository.findBymNn(nickname).isEmpty()) {
            response.put("message", "사용 가능한 닉네임입니다.");
        } else {
            response.put("message", "이미 사용 중인 닉네임입니다.");
        }

        return ResponseEntity.ok(response);
    }

    @RequestMapping("/2.3_GV-F-JoinNickName-03")
    public String joinNickname(Model model) { // 메인(회원가입) - 닉네임 중복확인

        return "/board/ct/2.3_GV-F-JoinNickName-03";
    }
    List<Integer> favkeyList;
    @PostConstruct
    public void initFavoriteKeyList() {
        favkeyList = new ArrayList<>();
    }

    @PostMapping("/join-fav-checkbox-endpoint") // 메인(회원가입) - 입력한 선호키워드 데이터를 받아오는 메소드
    public String joinFavKeywords(@RequestBody KeywordsDTO keywordsDTO) {

        favkeyList = keywordsDTO.getVoiceKeyList();

        System.out.println("회원가입 - 설정한 선호키워드: " + favkeyList);

        return "redirect:/popup/etc/2.1_GV-F-ChKw-2.1";
    }

    @GetMapping("/load-join-fav-checkbox") // 메인(회원가입) - 입력한 선호키워드 데이터를 불러오는 메소드
    public ResponseEntity<List<Integer>> handlePopupFavKeywordsRequest() {
        if (!favkeyList.isEmpty()) {
            // 선택한 게시물을 JSON 형태로 응답으로 보냄
            return ResponseEntity.ok(favkeyList);
        } else {
            // 게시물이 존재하지 않을 경우, 404 Not Found를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping("/7.0_GV-F-CTboard-7.0")
    public String ctBoard(Model model) { // 구인 게시판
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/7.0_GV-F-CTboard-7.0";
    }

    @RequestMapping("/8.0_GV-F-CrtCTPost-8.0")
    public String crtCTPost(Model model) { // 구인 글 작성/수정
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/8.0_GV-F-CrtCTPost-8.0";
    }

    @PostMapping("/create-post")
    public String createPost(@ModelAttribute CTPostDTO postDTO, Model model) {
        // 게시물을 저장하고 postId를 받아온다고 가정


        // 생성된 게시물의 상세 정보 페이지로 리디렉션
        return "redirect:/posts/"; // + postId;
    }


    @RequestMapping("/8.1_GV-F-ChKw-8.1")
    public String chKw(Model model) { // 구인 글 작성/수정 - 음성유형 선택


        return "/board/ct/8.1_GV-F-ChKw-8.1";
    }

    @RequestMapping("/8.2_GV-F-ChCAT-8.2")
    public String chCAT(Model model) { // 구인 글 작성/수정 - 카테고리 선택

        return "/board/ct/8.2_GV-F-ChCAT-8.2";
    }

    @RequestMapping("/8.3_GV-F-ChAt-8.3")
    public String chAt(Model model) { // 구인 글 작성/수정 - 녹음환경 선택

        return "/board/ct/8.3_GV-F-ChAt-8.3";
    }

    @RequestMapping("/9.0_GV-F-CTPost-9.0")
    public String ctPost(Model model) { // 구인 글 보기
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/9.0_GV-F-CTPost-9.0";
    }

    @RequestMapping("/12.0_GV-F-CTMy-12.0")
    public String ctMy(Model model) { // 구인자 마이
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/12.0_GV-F-CTMy-12.0";
    }

    @RequestMapping("/12.1_GV-F-VmChar-12.1")
    public String vmChar(Model model) { // 구인자 마이 - v머니 충전
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/12.1_GV-F-VmChar-12.1";
    }

    @RequestMapping("/12.2_GV-F-Fav-12.2")
    public String fav(Model model) { // 구인자 마이 - 즐겨찾기 목록
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/12.2_GV-F-Fav-12.2";
    }

    @RequestMapping("/12.3_GV-F-ReviewDtl-12.3")
    public String reviewDtl(Model model) { // 구인자 마이 - 작성한 리뷰 목록
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/12.3_GV-F-ReviewDtl-12.3";
    }

    private List<CTReviewDTO> reviewDTOList; // 등록한 리뷰 데이터를 저장하는 리스트 (임시)
    // 추후 DB 연결 시에는 필요없으므로 삭제 예정
    @PostConstruct
    public void initReviewDTOList() {
        reviewDTOList = new ArrayList<>();
    }

    CTReviewDTO ctReviewDTO = new CTReviewDTO();

    @PostMapping("/review-write-endpoint") // 리뷰 등록/수정 - 입력 데이터 전송받는 메소드
    public String submitReview(@RequestBody CTReviewDTO reviewDTO) {

        boolean mod = false; // 수정 판별 여부

        for (int i=1; i<reviewDTOList.size(); i++){ // 받은 데이터가 수정 데이터인지 확인
            if(reviewDTOList.get(i).getId().equals(reviewDTO.getId())){ // 수정 데이터일 경우,
                reviewDTOList.set(i, reviewDTO);
                mod = true;
            }
        }

        if (!mod) { // 업로드 데이터일 경우,
            System.out.println("*** 구인자 > 구직자 리뷰 ***");
            System.out.println("리뷰(거래) ID: " + reviewDTO.getId());
            System.out.println("리뷰 평점(별점): " + reviewDTO.getStars());
            System.out.println("리뷰 내용: " + reviewDTO.getContent());

            reviewDTOList.add(reviewDTO);
        }

        return "redirect:/board/ct/17.0_GV-F-CTTH-17.0";
    }

    @GetMapping("/ct-review-modify-endpoint") // 리뷰 수정
    public ResponseEntity<CTReviewDTO> handleCTReviewRequest(@RequestParam(name = "id") Long id) {
        // id에 해당하는 게시물을 reviewDTOList에서 찾기
        CTReviewDTO selectedReviewDTO = null;
        for (CTReviewDTO reviewDTO : reviewDTOList) {
            if (reviewDTO.getId().equals(id)) {
                selectedReviewDTO = reviewDTO;
                break;
            }
        }

        if (selectedReviewDTO != null) {
            // 선택한 게시물을 JSON 형태로 응답으로 보냄
            return ResponseEntity.ok(selectedReviewDTO);
        } else {
            // 게시물이 존재하지 않을 경우, 404 Not Found를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @RequestMapping("/17.0_GV-F-CTTH-17.0")
    public String ctTH(Model model) { // 구인자 마이 - 구인자 거래내역
        if (ctReviewDTO.getId()==null){
            ctReviewDTO.setId(0L);
            reviewDTOList.add(ctReviewDTO);
        }
        System.out.println("ID 출력: " + ctReviewDTO.getId());
        System.out.println(reviewDTOList.toString());
        model.addAttribute("reviewDTOList", reviewDTOList);
        return "/board/ct/17.0_GV-F-CTTH-17.0";
    }

    @RequestMapping("/17.1_GV-F-ReviewW-17.1")
    public String reviewW(Model model) { // 구인자 거래내역 - 구인자 > 구직자 리뷰 작성/수정
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/17.1_GV-F-ReviewW-17.1";
    }

    @RequestMapping("/24.0_GV-F-MyPost-24.0")
    public String myPost(Model model) { // 구인자 마이 - 내가 올린 글
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/24.0_GV-F-MyPost-24.0";
    }


    @RequestMapping("/06_GV-F-VAboard-06")
    public String vaBoard(Model model) { // 구직 게시판
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/06_GV-F-VAboard-06";
    }

    @RequestMapping("/13.0_GV-F-VAMy-13")
    public String vaMy(Model model) { // 구직자 마이
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/13.0_GV-F-VAMy-13";
    }

    @RequestMapping("/18.0_GV-F-VATH-18")
    public String vaTH(Model model) { // 구직자 거래내역
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/18.0_GV-F-VATH-18";
    }

    @RequestMapping("/18.1_GV-F-CTEval-18.1")
    public String ctEval(Model model) { // 구직자 > 구인자 평가
        model.addAttribute("memberDTO", memberDTO);

        return "/board/ct/18.1_GV-F-CTEval-18.1";
    }

    @RequestMapping("/ttt")
    public String mainttt(Model model) { // 비 로그인 시 메인

        return "/board/ct/ttt";
    }

    @PostMapping("/9.0_GV-F-CTPost-9.0")
    public String handleFormSubmission(@ModelAttribute BoardDTO boardDTO) {

        System.out.println("Title: " + boardDTO.getTitle());
        System.out.println("Amount: " + boardDTO.getAmount());
        System.out.println("Word Limit: " + boardDTO.getWordLimit());
        System.out.println("Deadline: " + boardDTO.getDeadline());
        System.out.println("Usage Range: " + boardDTO.getUsageRange());
        System.out.println("Usage Purpose: " + boardDTO.getUsagePurpose());
        System.out.println("Affiliation: " + boardDTO.getAffiliation());
        System.out.println("Availability: " + boardDTO.getAvailability());
        System.out.println("Content: " + boardDTO.getContent());

        return "/board/ct/9.0_GV-F-CTPost-9.0";
    }

}
