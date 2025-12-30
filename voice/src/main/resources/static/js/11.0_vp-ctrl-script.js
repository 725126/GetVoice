window.addEventListener('beforeunload', function(event) {
   // 창을 닫기 전 사용자에게 경고 메세지를 표시.
    var confirmationMessage = '변경사항이 저장되지 않을 수 있습니다.';
    (event || window.event).returnValue = confirmationMessage;
    return confirmationMessage;
});

var params = new URLSearchParams(window.location.search);
var username = params.get('username');
var nickname = params.get('nickname');

$("#nickname").text(nickname);

var fileInput = document.getElementById('file-input');
var uploadProfileImage = document.querySelector('.profile-image');
var formData = new FormData();

// 프로필 이미지 미리보기 로드 및 파일 업로드
fileInput.addEventListener('change', function(event) {
    var profileImgFile = event.target.files[0];

    if (profileImgFile && profileImgFile.type.startsWith('image/')) {
        var reader = new FileReader();

        reader.onload = function(e) {
             if (profileImgFile) {
                    var fileSizeInMB = profileImgFile.size / (1024 * 1024); // 파일 크기를 MB로 계산
                    console.log("업로드 파일 용량(프로필 이미지): ", fileSizeInMB);
                    if (fileSizeInMB > 3) {
                            alert('파일 크기가 3Mb를 초과했습니다.\n' + '3Mb 이하의 파일만 업로드 할 수 있습니다.');
                            return;
                    }
             }

            // 이미지 파일을 미리보기로 표시
            uploadProfileImage.src = e.target.result;
            formData.append('profileImgFile', profileImgFile);

             // 파일 업로드를 자동으로 실행
             uploadImage();
        };
        // 파일을 읽어옴
        reader.readAsDataURL(profileImgFile);
    } else {
        // 이미지 파일이 아닌 경우에 대한 처리
        alert('이미지 파일을 업로드해주세요.');
        return;
    }
});

// 이미지 파일을 서버로 전송하는 함수
function uploadImage() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/popup/voiceport/upload-profile-image", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log("(프로필 이미지) 요청이 성공적으로 처리됨");
            } else {
                // 요청이 실패한 경우 에러를 출력하고 확인
                console.error("(프로필 이미지) 요청 실패: " + xhr.status);
            }
        }
    };

    xhr.send(formData);
    formData = new FormData();
}

// 키워드 카테고리 설정 여부 판별
// 부모페이지에서 자식페이지의 메시지 수

// 저장 버튼 클릭 시 이벤트 핸들러
function getClass(button) {
    // 제목
    var title = document.querySelector('.vp-title').value;
    // 영리 목적 가격
    var forProfitPrice = document.querySelector('.forProfit').value;
    // 비영리 목적 가격
    var nonProfitPrice = document.querySelector('.nonProfit').value;

    var forProfitPriceInt = parseInt(forProfitPrice, 10);
    var nonProfitPriceInt = parseInt(nonProfitPrice, 10);

    // 소개, 수정·재진행, 취소·환불 탭의 내용을 가져옴
    var introduceContent = document.querySelector('[name="introduce"]').value;
    var modifyContent = document.querySelector('[name="modify"]').value;
    var refundContent = document.querySelector('[name="refund"]').value;

    // 의뢰 가능 여부 선택창 결과 가져오기
    var possibleCheckbox = document.getElementById("possible");
    var notPossibleCheckbox = document.getElementById("not-possible");

    // pdf 업로드 여부를 판별하기 위한 버튼 텍스트 가져오기
    var pdfBtn = document.querySelector('.pdf.upload-button');
    var pdfBtnText = pdfBtn.textContent || pdfBtn.innerText;

    // 키워드/카테고리 설정 여부를 판별하기 위한 버튼 텍스트 가져오기
    var keywordBtn = document.querySelector('.setting-button');
    var keywordBtnText = keywordBtn.textContent || keywordBtn.innerText;

    // 음성샘플 업로드 여부 판별을 위한 텍스트 가져오기
    var audioSpan = document.querySelector('.check-audio');
    var audioSpanText = audioSpan.textContent || audioSpan.innerText;

    // 영상샘플 업로드 여부 판별을 위한 텍스트 가져오기
    var videoSpan = document.querySelector('.check-video');
    var videoSpanText = videoSpan.textContent || videoSpan.innerText;

    var classes = button.className.split(' ');
    var buttonClass = '';

    for (var i = 0; i < classes.length; i++) {
        if (classes[i] === 'save' || classes[i] === 'submit') {
            buttonClass = classes[i];
            break;
        }
    }

    if (isNaN(forProfitPrice) || isNaN(nonProfitPrice) || forProfitPrice < 0 || nonProfitPrice < 0 ) {
        alert('올바른 가격을 입력해주세요.');
        return;
    }

    if (buttonClass === 'save') {
        console.log("저장 버튼을 클릭했습니다.");
        console.log("이력서: ", pdfBtnText.trim());

    } else if (buttonClass === 'submit') {
        console.log("제출 버튼을 클릭했습니다.");

        if (title.trim() === '') {
            alert('제목을 작성해주세요.');
            return;
        }

        if (isNaN(forProfitPrice) || isNaN(nonProfitPrice) || forProfitPrice <= 0 || nonProfitPrice <= 0) {
            alert('올바른 가격을 입력해주세요.');
            return;
        }

        if (!possibleCheckbox.checked && !notPossibleCheckbox.checked) {
            alert('의뢰 가능 여부를 선택해주세요.');
            return;
        }

        if (introduceContent.trim() === '') {
            alert('소개를 작성해주세요.');
            return;
        }

        if (modifyContent.trim() === '') {
            alert('수정·재진행 규정을 작성해주세요.');
            return;
        }

        if (refundContent.trim() === '') {
            alert('취소·환불 규정을 작성해주세요.');
            return;
        }

        if (keywordBtnText === '키워드/카테고리 설정 ▶') {
            alert("키워드/카테고리 설정을 해주세요.");
            return;
        }

        if (pdfBtnText.trim() === '이력서 PDF 업로드') {
            alert('이력서 PDF 파일을 업로드해주세요.');
            return;
        }

        if (videoSpanText === 'video-empty') {
            alert('영상샘플을 하나 이상 업로드해주세요.');
            return;
        }

        if (audioSpanText === 'audio-empty') {
            alert('음성샘플을 하나 이상 업로드해주세요.');
            return;
        }

    }
    // 현재 의뢰 가능 여부 선택 결과 저장하는 변수
    var availability = null;

    // 현재 의뢰 여부 선택 값 가져옴
    var possibleValue = possibleCheckbox.checked ? "possible" : null;
    var notPossibleValue = notPossibleCheckbox.checked ? "not-possible" : null;

    // 현재 의뢰 가능 여부 선택 결과 저장
    if( possibleValue != null ){
        availability = possibleValue;
    } else if(notPossibleValue != null ){
        availability = notPossibleValue;
    }

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/popup/voiceport/vp-ctrl", true);
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // 서버 응답을 받은 후의 로직
             if (buttonClass === 'save') {
                alert("임시 저장되었습니다.\n" + "※ 제출하지 않고 페이지를 벗어날 경우,\n" + "입력 내용은 소실되므로, 꼭 보이스포트 제출을 마쳐주세요.");
             } else if (buttonClass === 'submit'){
                submit = true;
                alert("보이스포트가 업로드되었습니다.");
                window.location.href = "10.0_GV-F-VP-10" + "?username=" + encodeURIComponent(username) +
                                                           "&nickname=" + encodeURIComponent(nickname);
             }

            console.log("서버 응답:", xhr.responseText);
        } else if (xhr.readyState === 4 && xhr.status !== 200) {
            // 오류 처리
            console.error("오류 발생:", xhr.status);
        }
    };

    var data = { // 컨트롤러에 전송되는(JAVA) 변수명 : js 변수명
            username : username,
            nickname : nickname,
            process : buttonClass,

            title : title,
            availability: availability,
            introduceContent: introduceContent,
            modifyContent: modifyContent,
            refundContent: refundContent,
            forProfitPrice: forProfitPrice,
            nonProfitPrice: nonProfitPrice
    };

    xhr.send(JSON.stringify(data));

}
