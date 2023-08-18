package com.ssafy.solive.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VideoCleanUpScheduler {

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUp() throws IOException {
        URL url = new URL("https://i9a107.p.ssafy.io:8447/openvidu/api/recordings/");
        String line;
        StringBuilder sb = new StringBuilder();
        BufferedReader br;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Basic T1BFTlZJRFVBUFA6c29saXZlMTA3");

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        conn.disconnect();
        String text = sb.toString();

        // text 로 온 Json String 을 객체로 바꾸기
        ObjectMapper objectMapper = new ObjectMapper();
        RecordingData recordingData = objectMapper.readValue(text, RecordingData.class);

        // 데이터 중에서 items 의 createdAt 이 일주일 넘은 데이터들 삭제
        long currentTimeMillis = System.currentTimeMillis();
        long sevenDaysInMillis = 7L * 24L * 60L * 60L * 1000L;
        for (int i = 0; i < recordingData.getCount(); i++) {
            if (currentTimeMillis - recordingData.getItems().get(i).getCreatedAt()
                > sevenDaysInMillis) {

                // 해당 영상 삭제
                url = new URL("https://i9a107.p.ssafy.io:8447/openvidu/api/recordings/"
                    + recordingData.getItems().get(i).getId());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Authorization", "Basic T1BFTlZJRFVBUFA6c29saXZlMTA3");
                conn.disconnect();
            }
        }
    }

    @Getter
    static class RecordingData {

        private final int count;
        private final List<RecordingItem> items;

        public RecordingData(int count, List<RecordingItem> items) {
            this.count = count;
            this.items = items;
        }
    }

    @Getter
    static class RecordingItem {

        private final String id;
        private final String object;
        private final String name;
        private final String outputMode;
        private final String resolution;
        private final int frameRate;
        private final String recordingLayout;
        private final String sessionId;
        private final long createdAt;
        private final long size;
        private final double duration;
        private final String url;
        private final boolean hasAudio;
        private final boolean hasVideo;
        private final String status;

        public RecordingItem(String id, String object, String name, String outputMode,
            String resolution,
            int frameRate, String recordingLayout, String sessionId, long createdAt, long size,
            double duration, String url, boolean hasAudio, boolean hasVideo, String status) {
            this.id = id;
            this.object = object;
            this.name = name;
            this.outputMode = outputMode;
            this.resolution = resolution;
            this.frameRate = frameRate;
            this.recordingLayout = recordingLayout;
            this.sessionId = sessionId;
            this.createdAt = createdAt;
            this.size = size;
            this.duration = duration;
            this.url = url;
            this.hasAudio = hasAudio;
            this.hasVideo = hasVideo;
            this.status = status;
        }
    }
}
