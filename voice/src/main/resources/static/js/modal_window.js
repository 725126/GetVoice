//회원가입 창
const modalButton1 = document.getElementById("join-button");
const modal1 = document.getElementById("modal1");
const closeBtn1 = document.getElementsByClassName("close")[0];

modalButton1.addEventListener("click", function() {
  modal1.style.display = "block";
});

closeBtn1.addEventListener("click", function() {
  modal1.style.display = "none";
});

window.addEventListener("click", function(event) {
  if (event.target === modal1) {
    modal1.style.display = "none";
  }
});

//로그인 창
const modalButton2 = document.getElementById("login-button");
const modal2 = document.getElementById("modal2");
const closeBtn2 = document.getElementsByClassName("close")[1];

modalButton2.addEventListener("click", function() {
  modal2.style.display = "block";
});

closeBtn2.addEventListener("click", function() {
  modal2.style.display = "none";
});

window.addEventListener("click", function(event) {
  if (event.target === modal2) {
    modal2.style.display = "none";
  }
});


//팝업창
function showPopupNickname() { window.open("2.3_GV-F-JoinNickName-03", "닉네임 중복확인", "width=450, height=216, left=200, top=50"); }
function showPopupID() { window.open("2.2_GV-F-JoinID-03", "아이디 증복확인", "width=450, height=216, left=200, top=80"); }
function showPopupPrkw() { window.open("/popup/etc/2.1_GV-F-ChKw-2.1", "선호키워드", "width=520, height=565, left=100, top=50"); }

function showPopupPrkw1() { window.open("/popup/etc/8.1_GV-F-ChKw-8.1", "키워드 선택", "width=540, height=490, left=100, top=50"); }
function showPopupPrkw2() { window.open("8.2_GV-F-ChCAT-8.2", "카테고리 선택", "width=540, height=420, left=100, top=50"); }
function showPopupPrkw3() { window.open("8.3_GV-F-ChAt-8.3", "녹음환경 선택", "width=540, height=320, left=100, top=50"); }

function showPopupPrkw4() { window.open("12.3_GV-F-ReviewDtl-12.3", "내가 작성한 리뷰", "width=860, height=510, left=100, top=50"); }
function showPopupPrkw5() { window.open("/popup/etc/23.0_GV-F-CurrGV-23.0", "현재 겟보이스는", "width=1240, height=670, left=100, top=50"); }
function showPopupPrkw7() { window.open("/popup/etc/16.0_GV-F-MyInqu-16.0", "문의하기", "width=660, height=420, left=100, top=50"); }

function showPopupPrkw8() { window.open("/popup/etc/21.0_GV-F-MyMod-21.0", "회원정보 변경/확인", "width=535, height=535, left=100, top=50"); }
function showPopupPrkw9() { window.open("/popup/chat/22.0_GV-F-ChatDtl-22.0", "채팅 목록", "width=420, height=680, left=100, top=50"); }
function showPopupPrkw10() { window.open("12.2_GV-F-Fav-12.2", "즐겨찾기 목록", "width=420, height=510, left=100, top=50"); }
function showPopupPrkw11() { window.open("12.1_GV-F-VmChar-12.1", "V 머니충전", "width=620, height=480, left=100, top=50"); }

function showPopupPrkw12() { window.open("/popup/voiceport/10.0_GV-F-VP-10", "보이스포트", "width=1030, height=940, left=100, top=50"); }