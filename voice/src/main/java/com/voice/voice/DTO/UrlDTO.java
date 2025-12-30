package com.voice.voice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UrlDTO {
    private String youtubeUrl; // 유튜브 URL
    private String title; // 제목
    private  List<Integer> selectedKeyList; // 선택한 키워드의 key 값이 저장된 리스트
    private List<String> selectedKeywords; // 선택한 키워드(value)가 저장된 리스트
    private String thumbnailUrl; // 유튜브 썸네일 URL
    private Long id; // 영상 샘플 게시물 아이디

    // 생성자
    public UrlDTO(String youtubeUrl, String title, List<String> selectedKeywords) {
        this.youtubeUrl = youtubeUrl;
        this.title = title;
        this.selectedKeywords = selectedKeywords;
        this.thumbnailUrl = generateThumbnailUrl(extractVideoId(youtubeUrl));
    }

    public String extractVideoId(String youtubeUrl) { // 유튜브 동영상 ID를 추출하는 메소드
        String videoId = null;
        String[] patterns = {
                "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|youtu.be\\/|watch\\?v%3D|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%\u200C\u200B2F|%2Fv%2F)[^#\\&\\?\\n]*",
                "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|youtu.be\\/|watch\\?v%3D|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%\u200C\u200B2F|%2Fv%2F)[^#\\&\\?\\n]*",
                "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|youtu.be\\/|watch\\?v%3D|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%\u200C\u200B2F|%2Fv%2F)[^#\\&\\?\\n]*"
        };
        for (String pattern : patterns) {
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(youtubeUrl); // 여기에 정규표현식을 적용해 동영상 ID를 추출
            if (matcher.find()) {
                videoId = matcher.group();
                break;
            }
        }
        return videoId;
    }

    public String generateThumbnailUrl(String videoId) { // 매개변수 ID를 바탕으로 썸네일 이미지 URL을 추출하는 메소드
        // 썸네일 이미지 URL 생성
        return "https://img.youtube.com/vi/" + videoId + "/mqdefault.jpg";
    }
}

