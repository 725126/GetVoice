window.onload = function() {

    var dataService = window.opener.document.querySelector('[data-serviceTypes]');
    var dataVoice = window.opener.document.querySelector('[data-voiceTypes]');
    var dataRecord = window.opener.document.querySelector('[data-recordingEnvironments]');

    var serviceTypes, voiceTypes, recordingEnvironments;

    if(dataService != null && dataVoice != null && dataRecord != null){
        // 서비스 유형, 음성 유형, 녹음 환경 정보를 가져옴
       serviceTypes = window.opener.document.querySelector('[data-serviceTypes]').getAttribute('data-serviceTypes').split(',');
       voiceTypes = window.opener.document.querySelector('[data-voiceTypes]').getAttribute('data-voiceTypes').split(',');
       recordingEnvironments = window.opener.document.querySelector('[data-recordingEnvironments]').getAttribute('data-recordingEnvironments').split(',');

       loadCheckboxKeywords(serviceTypes, voiceTypes, recordingEnvironments);
    }

    var params = new URLSearchParams(window.location.search);
    var pid = params.get('pid');

    var url = "/popup/voiceport/open-vp-call-keyword?pid=" + pid;

    if(pid != null) {
        // AJAX 요청 보내기
        $.ajax({
            type: "GET",
            url: url,
            success: function (response) {
                // 성공적으로 데이터를 받았을 때의 동작
                console.log("게시물 데이터를 성공적으로 받았습니다");
                 if (response) {
                     var vpCtrlDTO = response;
                     console.log("10.1 (viewVP) 키워드 - vpCtrlDTO:", vpCtrlDTO);

                     serviceTypes = vpCtrlDTO.serviceKeyList;
                     voiceTypes = vpCtrlDTO.voiceKeyList;
                     recordingEnvironments = vpCtrlDTO.recordingKeyList;

                     loadCheckboxKeywords(serviceTypes, voiceTypes, recordingEnvironments);

                 } else {
                    console.error("10.1 (viewVP) 키워드 - 서버에서 받은 데이터가 유효하지 않습니다.");
                 }
            },
            error: function (error) {
                // 오류 처리 로직
                console.error("10.1 (viewVP) 키워드 - 데이터를 불러오는 중 오류가 발생했습니다:", error);
            }
        });
    }


}

function loadCheckboxKeywords(serviceTypes, voiceTypes, recordingEnvironments) {
    // 선택된 체크박스 데이터를 저장할 객체를 선언
   var selectedCheckboxes = {};

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
}

