var profileSrc = window.opener.document.getElementById("profile-img").src;
document.getElementById("profile-img").src = profileSrc;

var nickname = window.opener.document.querySelector('.nickname').textContent;
$("#nickname").html(' ' + nickname + ' 님과 거래 시,');

var url = "/board/ct/vp-my-money";

// 현재 나의 V머니 : AJAX 요청 보내기
$.ajax({
    type: "GET",
    url: url,
    success: function (response) {
        // 성공적으로 데이터를 받았을 때의 동작
        console.log("게시물 데이터를 성공적으로 받았습니다: ", response);
         if (response) {
             $("#myMoney").text(formatCurrency(response) + '원');
         } else {
            console.error("서버에서 받은 데이터가 유효하지 않습니다.");
         }
    },
    error: function (error) {
        // 오류 처리 로직
        console.error("데이터를 불러오는 중 오류가 발생했습니다:", error);
    }
});

function formatCurrency(number) {
    // 숫자가 1000 미만인 경우에는 그대로 반환
    if (number < 1000) {
        return number.toString();
    }

    // 숫자를 문자열로 변환하고 소수점을 기준으로 나눔
    var parts = number.toString().split(".");

    // 정수 부분에 콤마를 추가
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");

    // 다시 합치고 반환
    return parts.join(".");
}

const checkboxes = document.querySelectorAll('input[type="checkbox"]');
checkboxes.forEach(checkbox => {
    checkbox.addEventListener('change', () => {
        checkboxes.forEach(cb => {
            if (cb !== checkbox) {
                cb.checked = false;
            }
        });
    });
});

// 계산하기 버튼 클릭 시
document.getElementById("submitBtn").addEventListener("click", function(event) {
    event.preventDefault();
    // 견적 계산 게시물 데이터 가져오기
    var fileInput = document.getElementById('fileInput');
    var file = fileInput.files[0];

    // 사용범위(영리, 비영리) 선택창 결과 가져오기
    var forProfitPriceCheckbox = document.getElementById("forProfitPrice");
    var nonProfitPriceCheckbox = document.getElementById("nonProfitPrice");

    // 파일이 비어있는지 확인
    if (!file) {
        alert('파일을 선택해주세요.');
        return;
    }

     if (file) {
            var fileSizeInMB = file.size / (1024 * 1024); // 파일 크기를 MB로 계산
            console.log("업로드 파일 용량(견적 계산): ", fileSizeInMB);
            if (fileSizeInMB > 10) {
                alert('파일 크기가 10Mb를 초과했습니다.\n' + '10Mb 이하의 파일만 업로드 할 수 있습니다.');
                return;
            }
     }

    if (!forProfitPriceCheckbox.checked && !nonProfitPriceCheckbox.checked) {
        alert('사용 범위(영리, 비영리)를 선택해주세요.');
        return;
    }

    // 현재 영리 비영리 선택 결과 저장하는 변수
    var profit = null;

    // 현재 영리 비영리 선택값 가져옴
    var forProfitValue = forProfitPriceCheckbox.checked ? "for-profit" : null;
    var nonProfitValue = nonProfitPriceCheckbox.checked ? "non-profit" : null;

    if( forProfitValue != null ){
            profit = forProfitValue;
        } else if(nonProfitValue != null ){
            profit = nonProfitValue;
    }

    if(file!=null){
        // 파일 확장자 확인
        var allowedExtensions = /(\.pdf|\.txt|\.hwp)$/i;
        var fileName = file.name;

        if (!allowedExtensions.exec(fileName)) {
            alert('올바른 파일 형식이 아닙니다. pdf, txt, hwp 파일을 업로드 해주세요.');
            return;
        }

        // 부모 페이지의 forProfitPrice 클래스를 가진 <td> 요소 선택
        var forProfitPriceElement = window.opener.document.querySelector('.forProfitPrice');
        var nonProfitPriceElement = window.opener.document.querySelector('.nonProfitPrice');

        // 선택된 요소의 텍스트 내용 가져오기
        var forProfitPrice = extractNumber(forProfitPriceElement.textContent);
        var nonProfitPrice = extractNumber(nonProfitPriceElement.textContent);

        console.log('For Profit Price: ' + forProfitPrice);
        console.log('Non Profit Price: ' + nonProfitPrice);


        // FormData 객체 생성
        var formData = new FormData();

        // 데이터 추가
        formData.append("file", file);
        formData.append("profit", profit);
        formData.append("forProfitPrice", forProfitPrice);
        formData.append("nonProfitPrice", nonProfitPrice);

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/popup/voiceport/vp-est-cal-endpoint", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    console.log("(견적계산 파일 첨부) 요청이 성공적으로 처리됨");
                    // 페이지 새로고침
                    window.location.reload();
                } else {
                    // 요청이 실패한 경우 에러를 출력하고 확인
                    console.error("(견적계산 파일 첨부) 요청 실패: " + xhr.status);
                }
            }
        };

        xhr.send(formData);
    }

    event.stopPropagation();
});

// 페이지가 새로고침되었는지 확인
function isPageRefreshing() {
    return performance.navigation.type === 1; // 1: 새로고침, 2: 뒤로가기/앞으로 가기, 0: 일반적인 방문
}

window.addEventListener('unload', function () {
    // 페이지가 새로고침되지 않은 경우에만 onPageUnload 함수 실행
    if (!isPageRefreshing()) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/popup/voiceport/vp-est-cal-init", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    console.log("(견적계산 초기화) 요청이 성공적으로 처리됨");
                } else {
                    // 요청이 실패한 경우 에러를 출력하고 확인
                    console.error("(견적계산 초기화) 요청 실패: " + xhr.status);
                }
            }
        };

        xhr.send();
    }
});

function extractNumber(inputString) {
    // 정규 표현식을 사용하여 숫자만 추출
    var extractedNumber = parseInt(inputString.replace(/\D/g, ''), 10);
    return extractedNumber;
}