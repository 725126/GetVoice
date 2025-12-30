window.onload = function() {

    // 선택된 체크박스 데이터를 저장할 객체를 선언
    var selectedCheckboxes = {};

    var resultMine = document.querySelector('.chk-result');
    var resultMineText;
    // 만약 .chk-result 클래스를 가진 엘리먼트가 존재한다면
    if( resultMine != null ){
        // 해당 엘리먼트의 텍스트 내용을 resultMineText 변수에 저장
        resultMineText = resultMine.textContent;
        // 만약 resultMineText가 "not-null"이라는 문자열과 같다면
        if(resultMineText == "not-null"){
            // 서비스 유형, 음성 유형, 녹음 환경 정보를 가져와서 각각 배열로 변환
            var serviceTypes = document.querySelector('[data-serviceTypes]').getAttribute('data-serviceTypes').split(',');
            var voiceTypes = document.querySelector('[data-voiceTypes]').getAttribute('data-voiceTypes').split(',');
            var recordingEnvironments = document.querySelector('[data-recordingEnvironments]').getAttribute('data-recordingEnvironments').split(',');

            console.log("서비스 유형: ", serviceTypes);
            console.log("음성 유형: ", voiceTypes);
            console.log("녹음 유형: ", recordingEnvironments);

            // 서비스 유형을 selectedCheckboxes 객체에 속성으로 추가하고 값을 true로 설정
            for (var i = 0; i < serviceTypes.length; i++) {
               selectedCheckboxes[serviceTypes[i]] = true;
            }
            // 음성 유형을 selectedCheckboxes 객체에 속성으로 추가하고 값을 true로 설정
            for (var i = 0; i < voiceTypes.length; i++) {
               selectedCheckboxes[voiceTypes[i]] = true;
            }
            // 녹음 환경을 selectedCheckboxes 객체에 속성으로 추가하고 값을 true로 설정
            for (var i = 0; i < recordingEnvironments.length; i++) {
               selectedCheckboxes[recordingEnvironments[i]] = true;
            }
        }
    }

    // 서비스 유형 체크박스 복원
    for (var i = 1; i <= 20; i++) {
        var checkbox = document.getElementById(1000 + i);
        // 만약 해당 ID를 가진 체크박스 엘리먼트가 존재하고, selectedCheckboxes 객체에 속성이 true로 설정되어 있다면
        if (checkbox && selectedCheckboxes[1000 + i]) {
            // 해당 체크박스를 체크 상태로 설정
            checkbox.checked = true;
        }
    }

    // 음성 유형 체크박스 복원
    for (var i = 21; i <= 66; i++) {
        var checkbox = document.getElementById(i - 20);
        // 만약 해당 ID를 가진 체크박스 엘리먼트가 존재하고, selectedCheckboxes 객체에 속성이 true로 설정되어 있다면
        if (checkbox && selectedCheckboxes[i - 20]) {
            // 해당 체크박스를 체크 상태로 설정
            checkbox.checked = true;
        }
    }

    // 녹음 환경 체크박스 복원
    for (var i = 67; i <= 70; i++) {
        var checkbox = document.getElementById(9934 + i);
        // 만약 해당 ID를 가진 체크박스 엘리먼트가 존재하고, selectedCheckboxes 객체에 속성이 true로 설정되어 있다면
        if (checkbox && selectedCheckboxes[9934 + i]) {
            // 해당 체크박스를 체크 상태로 설정
            checkbox.checked = true;
        }
    }

    // 저장 버튼 클릭 시 이벤트 핸들러
    var saveButton = document.querySelector('.btn');
    saveButton.addEventListener('click', function(event) {
        event.preventDefault();

        // 체크된 항목을 저장하는 배열
        var serviceTypes = [];
        var voiceTypes = [];
        var recordingEnvironments = [];

        // 체크된 항목을 각 항목마다 최소 1개 이상 선택했는지 확인
        var serviceTypeSelected = false;
        var voiceTypeSelected = false;
        var recordingEnvironmentSelected = false;

        // 서비스 유형 체크박스 확인
        for (var i = 1; i <= 20; i++) {
            var checkbox = document.getElementById(1000 + i);
            if (checkbox && checkbox.checked) {
                serviceTypes.push(1000 + i); // 체크된 항목 저장
                serviceTypeSelected = true;
            }
        }

        // 음성 유형 체크박스 확인
        for (var i = 21; i <= 66; i++) {
            var checkbox = document.getElementById(i - 20);
            if (checkbox && checkbox.checked) {
                voiceTypes.push(i - 20); // 체크된 항목 저장
                voiceTypeSelected = true;
            }
        }

        // 녹음 환경 체크박스 확인
        for (var i = 67; i <= 70; i++) {
            var checkbox = document.getElementById(9934 + i);
            if (checkbox && checkbox.checked) {
                recordingEnvironments.push(9934 + i); // 체크된 항목 저장
                recordingEnvironmentSelected = true;
            }
        }

        // 최소 하나 이상의 체크박스를 선택하지 않은 경우 경고창 표시
        if (!serviceTypeSelected || !voiceTypeSelected || !recordingEnvironmentSelected) {
            alert('각 유형마다 최소 하나 이상의 항목을 선택해야 합니다.');
            return;
        }

        console.log("서비스 유형: ", serviceTypes);
        console.log("음성 유형: ", voiceTypes);
        console.log("녹음 유형: ", recordingEnvironments);

        var data = {

            serviceKeyList: serviceTypes,
            voiceKeyList: voiceTypes,
            recordingKeyList: recordingEnvironments

        };

        // ajax 전송
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/popup/voiceport/vp-ctrl-save-chk", true);
        xhr.setRequestHeader("Content-Type", "application/json");

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                // 서버 응답을 받은 후의 로직
                console.log("서버 응답:", xhr.responseText);

            } else if (xhr.readyState === 4 && xhr.status !== 200) {
                // 오류 처리
                console.error("오류 발생:", xhr.status);
            }
        };

        xhr.send(JSON.stringify(data));

        // 키워드가 설정됨 > 부모페이지의 버튼 텍스트 변경
        opener.window.document.querySelector('.setting-button').textContent = "키워드/카테고리 변경 ▶";


        // 저장되었다는 알림을 표시
        alert('설정이 저장되었습니다.');
        window.close();
    });
}


