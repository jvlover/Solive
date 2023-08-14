import React from 'react';
import VideoRoomComponent from './VideoRoom/VideoRoom.jsx';
import registerServiceWorker from '../registerServiceWorker';
import { useRecoilValue } from 'recoil';
import {userState} from '../recoil/user/userState';

// 이 페이지 이식 시 필요한 것
// 1. api로 수락한 요청의 applyId, accessToken 보내는 버튼 만든다.
// 2. 그러면 sessionName 주니까 그거랑 userName으로 nickname이랑 user프로필사진 아마 path? 담아서 props에 넣고 이 페이지로 이동한다.
// 근데 이거 recoil 에서 받아올 수 있으면 props로 줄 필요 없긴 함
const MatchPage = () =>{
    const user = useRecoilValue(userState);
    const sessionName = "1234"
    const picture = user.path;
    return(
        // 여기서 videoRoomComponent로 props 넘겨준다
        <VideoRoomComponent
            userName={user.nickname} picture={picture} sessionName={sessionName}/>
    );
}
registerServiceWorker();

export default MatchPage;