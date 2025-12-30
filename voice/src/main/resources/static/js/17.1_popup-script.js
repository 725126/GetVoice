function handleStarRating(starIndex) {
  const stars = document.querySelectorAll('.rating input[type="radio"]');

  stars.forEach((star, index) => {
    const label = document.querySelector(`label[for="star${index + 1}"]`);
    if (index <= starIndex) {
      label.style.color = 'gold';
    } else {
      label.style.color = '#ccc';
    }
  });
}

document.addEventListener('DOMContentLoaded', function() {
  const stars = document.querySelectorAll('.rating input[type="radio"]');
  stars.forEach((star, index) => {
    star.addEventListener('click', function() {
      handleStarRating(index);
    });
  });
});

function fillStars(count) {
  const stars = document.querySelectorAll('.rating input[type="radio"]');

  stars.forEach((star, index) => {
    const label = document.querySelector(`label[for="star${index + 1}"]`);
    if (index < count) {
      star.checked = true; // 해당 별을 체크 상태로 만듭니다.
      label.style.color = 'gold'; // 노란색으로 변경합니다.
    } else {
      star.checked = false; // 해당 별을 언체크 상태로 만듭니다.
      label.style.color = '#ccc'; // 회색으로 변경합니다.
    }
  });
}

// 구인자 거래내역 - 리뷰수정 버튼 클릭 시
// URL에서 youtubeUrl,title, id, selectedKeywords 매개변수 읽기
var params = new URLSearchParams(window.location.search);
var stars = params.get('stars');
var content = params.get('content');
var id = params.get('id');

// 불러온 요소들 복원
$("#content").val(content);
if( id!=null ){
    fillStars(stars);
}

// 리뷰등록 버튼 클릭 시
document.getElementById("submitBtn").addEventListener("click", function(event) {
    event.preventDefault();
    // 리뷰 작성 게시물 데이터 가져오기
    // 선택한 별점과 리뷰 내용을 가져옴.
    const selectedStars = document.querySelector('input[name="stars"]:checked');
    if (!selectedStars) {
        alert("별점을 등록해주세요.");
        return;
    }
    // 선택한 별점 값을 정수로 변환
    // 별점이 선택되지 않았을 때 0으로 설정하도록 함.
    const stars = selectedStars ? parseInt(selectedStars.value) : 0;
    const content = document.getElementById('content').value;
    const dataReviewId = new URLSearchParams(window.location.search).get('id');

    if (!content) {
        alert("내용을 입력해주세요.");
        return;
    }

    const data = {
        "id": dataReviewId,
        "stars": stars,
        "content": content
    };

    // JSON 형식으로 변환
    const jsonData = JSON.stringify(data);

     // Ajax 요청 전송.
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/board/ct/review-write-endpoint', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                // 서버로부터의 응답을 처리
                console.log('리뷰가 성공적으로 등록되었습니다');
                // 요청이 성공적으로 처리됨
                // 팝업 창 닫기
                window.close();
                // 부모 페이지 새로고침 (부모 페이지의 URL을 정확하게 지정해야 함)
                window.opener.location.reload();
            } else {
                // 오류 처리
                console.error('리뷰 등록 중 오류 발생:', xhr.status);
            }
        }
    };
    // 별점과 내용을 서버로 전송
    xhr.send(jsonData);

    event.stopPropagation();
});

