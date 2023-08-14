package com.ssafy.solive.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VideoCleanUpScheduler {

    @Scheduled(cron = "0 0 0 * * *")
    public void method() throws IOException {
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
        // 아무튼 여기 데이터 니가 알아서 제이슨으로 바꾼 다음
        // 그 데이터 중에서 items의 createdAt 이 일주일 넘은 친구들 다 삭제하시오
        // 삭제는 저 URL에 delete로 items의 id를 params로 보내면 됩니다.
    }

    @Getter
    static class recording {

        private final String id;
        private final String createdAt;

        public recording(String id, String createdAt) {
            this.id = id;
            this.createdAt = createdAt;
        }
    }
}
