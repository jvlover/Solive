import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import VideoRoomComponent from './components/VideoRoomComponent';
import registerServiceWorker from './registerServiceWorker';

const now = new Date();
// 이거 가능하면 matched 시간 받아서 적용하자
// const date = now.getFullYear() + "" + (now.getMonth() + 1)
//     + "" + (now.getDay() - 1) + "" + now.getHours() + "" + now.getMinutes() + ""
//     + now.getSeconds();
const userName = "juho" + now.getSeconds();
const sessionName = "juho" + "20230810";

ReactDOM.render(
    // 여기서 videoRoomComponent로 props 넘겨준다
    <VideoRoomComponent
        userName={userName} sessionName={sessionName}/>,
    document.getElementById('root')
);
registerServiceWorker();
