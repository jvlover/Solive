import React from 'react';
import VideoRoomComponent from './VideoRoom/VideoRoom.jsx';
import registerServiceWorker from '../registerServiceWorker';

const now = new Date();
const userName = "juho" + now.getSeconds();
const sessionName = "juho" + now.getDay();

const TestPage = () =>{
    return(
        // 여기서 videoRoomComponent로 props 넘겨준다
        <VideoRoomComponent
            userName={userName} sessionName={sessionName}/>
    );
}
registerServiceWorker();

export default TestPage;