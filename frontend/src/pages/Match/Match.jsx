import registerServiceWorker from '../registerServiceWorker';
import axios from 'axios';
import { useRecoilValue } from 'recoil';
import { userState } from '../recoil/user/userState.js';
import { useLocation } from 'react-router-dom';
import VideoRoomComponent from '../components/videoroom/VideoRoom.jsx';

const MatchPage = () =>{
    const user = useRecoilValue(userState);
    const picture = user.path;
    const Navigate = useLocation();
    const sessionName = axios.post(
      "보낼 주소", {
          applyId : Navigate.applyId,
      },
      {
          headers:{
              "헤더에 넣을 값 이름" : "넣을 정보",
          },
      }
    );

    return(
      // 여기서 videoRoomComponent로 props 넘겨준다
      <VideoRoomComponent
        userName={user.nickname} picture={picture} sessionName={sessionName}/>
    )
}
registerServiceWorker();

export default MatchPage;