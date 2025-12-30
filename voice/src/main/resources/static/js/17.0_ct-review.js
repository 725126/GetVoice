// 리뷰작성 팝업창
function showPopupPrkw13() {
    var dataReviewId = event.target.id;
    window.open("17.1_GV-F-ReviewW-17.1?id=" + dataReviewId, "리뷰하기", "width=540, height=420, left=100, top=50");
}

// 리뷰하기 > 리뷰수정
document.addEventListener('DOMContentLoaded', function() {

        var ctReviewButtons = document.querySelectorAll('.ctReviewId');
        var reviewButtons = document.querySelectorAll('.review-button');

        ctReviewButtons.forEach(function(ctReviewButton) {
            var ctReviewId = ctReviewButton.getAttribute('review-id');

            reviewButtons.forEach(function(reviewButton) {
                var reviewId = reviewButton.id;

                if (ctReviewId === reviewId) {
                    reviewButton.textContent = '리뷰수정';
                    reviewButton.onclick = function() { sendModifyRequest(ctReviewId); };
                }
            });
        });

});

// 수정 요청 함수
function sendModifyRequest(id) {
    // AJAX 요청을 보낼 URL 설정

    var url = "/board/ct/ct-review-modify-endpoint?id=" + id;

    // AJAX 요청 보내기
    $.ajax({
        type: "GET",
        url: url,
        success: function (response) {
            // 성공적으로 데이터를 받았을 때의 동작
            console.log("게시물 데이터를 성공적으로 받았습니다:", response);
             if (response) {
                // 수정 데이터를 전시할 팝업창에 수정 데이터 전송
                var popupUrl = "/board/ct/17.1_GV-F-ReviewW-17.1" +
                               "?stars=" + encodeURIComponent(response.stars) +
                               "&id=" + encodeURIComponent(response.id) +
                               "&content=" + encodeURIComponent(response.content);
                window.open(popupUrl, "_blank", "width=540, height=420, left=100, top=50");

             } else {
                console.error("서버에서 받은 데이터가 유효하지 않습니다.");
             }
        },
        error: function (error) {
            // 오류 처리 로직
            console.error("데이터를 불러오는 중 오류가 발생했습니다:", error);
        }
    });
}



