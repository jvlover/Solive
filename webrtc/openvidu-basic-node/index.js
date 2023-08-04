// .env 파일을 읽어와 환경 변수를 설정합니다. CONFIG 환경 변수가 존재하면 해당 파일을 사용하고, 그렇지 않으면 기본적으로 빈 객체를 사용합니다.
require("dotenv").config(
    !!process.env.CONFIG ? {path: process.env.CONFIG} : {});

// Express 애플리케이션을 생성합니다.
const express = require("express");
const bodyParser = require("body-parser"); // 요청의 본문을 파싱하기 위한 미들웨어
const http = require("http"); // HTTP 서버를 생성하기 위한 모듈
const OpenVidu = require("openvidu-node-client").OpenVidu; // OpenVidu 서버와 통신하기 위한 모듈
const cors = require("cors"); // CORS 지원을 위한 미들웨어
const app = express();
// 이제 이 app 이라는 변수로 REST 엔드포인트를 생성합니다.

// 환경 변수 : Node 서버가 리스닝하는 포트 번호
const SERVER_PORT = process.env.SERVER_PORT || 5000;
// 환경 변수 : OpenVidu 서버의 URL 주소
const OPENVIDU_URL = process.env.OPENVIDU_URL || 'http://localhost:4443';
// 환경 변수 : OpenVidu 서버와 공유하는 비밀 키
const OPENVIDU_SECRET = process.env.OPENVIDU_SECRET || 'MY_SECRET';

// CORS 지원을 활성화합니다. 모든 도메인으로부터의 요청을 허용합니다.
app.use(
    cors({
        origin: "*",
    })
);

// HTTP 서버를 생성합니다.
const server = http.createServer(app);
// OpenVidu 와 통신하기 위한 인스턴스를 생성합니다.
const openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);

// application/x-www-form-urlencoded 를 허용합니다.
app.use(bodyParser.urlencoded({extended: true}));
// application/json 을 허용합니다.
app.use(bodyParser.json());

// 정적 리소스를 제공합니다. (public 폴더 안의 파일들을 제공합니다.)
app.use(express.static(__dirname + '/public'));

// 애플리케이션 서버를 시작하고 지정된 포트에서 리스닝합니다.
server.listen(SERVER_PORT, () => {
    console.log("애플리케이션이 이 서버에서 시작햇어요 ", SERVER_PORT);
    console.warn('그리고 오픈비두 여기랑 연결 시도 중 ' + OPENVIDU_URL);
});

// "/api/sessions" 경로로 POST 요청이 오면 OpenVidu 에서 새 세션을 생성하고 세션 ID를 반환합니다.
app.post("/api/sessions", async (req, res) => {
    const session = await openvidu.createSession(req.body);
    res.send(session.sessionId);
});

// "/api/sessions/:sessionId/connections" 경로로 POST 요청이 오면 해당 세션에서 새 연결을 생성하고 토큰을 반환합니다.
app.post("/api/sessions/:sessionId/connections", async (req, res) => {
    const session = openvidu.activeSessions.find(
        (s) => s.sessionId === req.params.sessionId
    );
    // 세션 있는지 확인하고 없으면 404 반환. 있으면 연결 시도하고 token 반환.
    if (!session) {
        res.status(404).send();
    } else {
        const connection = await session.createConnection(req.body);
        res.send(connection.token);
    }
});

// 예외가 처리되지 않은 경우, 해당 오류를 콘솔에 출력합니다.
process.on('uncaughtException', err => console.error(err));