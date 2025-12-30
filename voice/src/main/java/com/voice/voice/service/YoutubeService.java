package com.voice.voice.service;

import org.springframework.stereotype.Service;

@Service
public class YoutubeService {
    public String extractVideoId(String youtubeUrl) {
        // YouTube URL에서 동영상 ID 추출
        String videoId = youtubeUrl.substring(youtubeUrl.lastIndexOf("/") + 1);
        return videoId;
    }

    public String generateThumbnailUrl(String videoId) {
        // 썸네일 이미지 URL 생성
        String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/mqdefault.jpg";
        return thumbnailUrl;
    }
}
