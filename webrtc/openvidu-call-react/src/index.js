import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import VideoRoomComponent from './components/VideoRoomComponent';
import registerServiceWorker from './registerServiceWorker';

const userName = "juho";
const now = new Date();
// 이거 가능하면 matched 시간 받아서 적용하자
// const date = now.getFullYear() + "" + (now.getMonth() + 1)
//     + "" + (now.getDay() - 1) + "" + now.getHours() + "" + now.getMinutes() + ""
//     + now.getSeconds();
const sessionName = userName;

ReactDOM.render(
    // 여기서 videoRoomComponent로 props 넘겨준다
    <VideoRoomComponent
        userName={userName} sessionName={sessionName}/>,
    document.getElementById('root')
);
registerServiceWorker();
