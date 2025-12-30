var params = new URLSearchParams(window.location.search);
var username = params.get('username');
var nickname = params.get('nickname');

if(nickname != null) {
    $("#nickname").text(nickname);
}

var vpModBtn = document.getElementById("vp-mod-btn");

if(vpModBtn != null) {
    vpModBtn.href = "11.0_GV-F-VPCtrl-11.0" + "?username=" + encodeURIComponent(username) +
                                              "&nickname=" + encodeURIComponent(nickname);
}

var dataPdf = document.querySelector('[data-pdf-save]');

// 데이터를 읽어옴
if(dataPdf != null ){
    var saveFileName = dataPdf.getAttribute('data-pdf-save');
    var originalFileName = dataPdf.getAttribute('data-pdf-ori');

    console.log("저장명:", saveFileName);
    console.log("원본명:", originalFileName);

    //// 서버로부터 다운로드 URL을 가져옴.
    $("#downloadButton").click(function() {
            // var fileName = "example.txt"; // 실제 파일 이름으로 대체
            var downloadUrl = "/downloadFile/" + saveFileName;

            var link = document.createElement('a');
            link.href = "http://localhost:8080/downloadFile/" + saveFileName;
            link.download = originalFileName;
            link.click();

            console.log('다운 링크: ', link);
    });
}


function showYoutubePopup(youtubeUrl) {
    // 유튜브 URL로 새 창 열기
    window.open(youtubeUrl, '_blank', 'width=600,height=400');
}

function toggleHeart(element) {
    element.classList.toggle('far'); // 비어있는 하트 클래스
    element.classList.toggle('fas'); // 속이 찬 하트 클래스
}

const dropdownToggle = document.querySelector('.dropdown-toggle');
const dropdownContent = document.querySelector('.dropdown-content');

if(dropdownToggle != null) {
    dropdownToggle.addEventListener('click', function() {
        if (dropdownContent.style.display === 'block') {
            dropdownContent.style.display = 'none';
        } else {
            dropdownContent.style.display = 'block';
        }
    });

    document.addEventListener('click', function(event) {
        if (!dropdownToggle.contains(event.target) && !dropdownContent.contains(event.target)) {
            dropdownContent.style.display = 'none';
        }
    });
}

function showPopupChat() { window.open("/popup/chat/14.0_GV-F-Chat-14", "채팅", "width=420, height=720, left=100, top=50"); }
function showPopupEstCal() { window.open("10.3_GV-F-EstCal-10.3", "예상견적계산", "width=470, height=620, left=100, top=50"); }
function showPopup() { window.open("GV-F-VPCrt-11.3", "결제하기", "width=470, height=546, left=100, top=50"); }
function showpopupchkw() { window.open("10.1_GV-F-KWDtl-10.1", "카테고리/키워드 더보기", "width=540, height=820, left=600, top=50"); }
function showPopuprv() { window.open("10.4_GV-F-Review-10.4", "리뷰보기", "width=900, height=550, left=400, top=150"); }