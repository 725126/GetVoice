// 아이디 중복확인 페이지 > 중복확인  버튼 클릭 시 이벤트 핸들러
var checkButton = document.querySelector('.button-check');
checkButton.addEventListener('click', function(event) {

    var id = document.getElementById("username").value;
    var regex = /^[a-z0-9]{5,15}$/; // 5~15자의 영문 소문자와 숫자로만 이루어진 문자열을 허용하는 정규 표현식

    if (!regex.test(id)) {
        alert("아이디는 5~15자의 영문 소문자와 숫자만 입력해주세요.");
        return;
    }

    $.ajax({
        url: '/board/ct/check-id-endpoint',
        type: 'GET',
        data: { id: id },
        success: function(data) {
            alert(data.message);
            if(data.message==='사용 가능한 아이디입니다.'){
                // 아이디 설정됨 > 부모페이지의 아이디 입력창에 입력 아이디 전송
                opener.window.document.querySelector('.input-id').value = id;
                window.close();
            }
        },
        error: function(error) {
            console.error('아이디 중복확인 에러:', error);
        }
    });

});
