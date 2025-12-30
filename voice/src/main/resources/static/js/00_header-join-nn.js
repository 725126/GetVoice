// 닉네임 중복확인 페이지 > 중복확인  버튼 클릭 시 이벤트 핸들러
var checkButton = document.querySelector('.button-check');
checkButton.addEventListener('click', function(event) {

    var nickname = document.getElementById("nickname").value;
    var regex = /^([a-zA-Z0-9]{1,9}|[가-힣a-zA-Z0-9]{1,6})$/; // 영문 OR 영문+숫자 9자 이내, 한글 혹은 한글+영문+숫자 6자 이내 문자열을 허용하는 정규 표현식

    if (!regex.test(nickname)) {
        alert("닉네임은 영문 혹은 영문, 숫자 혼합 9자 이내,\n" + "한글 혹은 한글 포함 영문, 숫자 혼합 6자 이내로만 설정 가능합니다.");
        return;
    }

    $.ajax({
        url: '/board/ct/check-nickname-endpoint',
        type: 'GET',
        data: { nickname: nickname },
        success: function(data) {
            alert(data.message);
            if(data.message==='사용 가능한 닉네임입니다.'){
                // 아이디 설정됨 > 부모페이지의 아이디 입력창에 입력 아이디 전송
                opener.window.document.querySelector('.input-nn').value = nickname;
                window.close();
            }
        },
        error: function(error) {
            console.error('닉네임 중복확인 에러:', error);
        }
    });

});