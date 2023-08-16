import registerServiceWorker from '../../registerServiceWorker';
import axios from 'axios';
import { useRecoilValue } from 'recoil';
import { userState } from '../../recoil/user/userState';
import { useLocation } from 'react-router-dom';
import VideoRoomComponent from '../../components/videoroom/VideoRoom.jsx';

const MatchPage = () =>{
    const user = useRecoilValue(userState);
    const picture = user.path;
    const Navigate = useLocation();
    const sessionName = axios.post(
      "https://i9a107.p.ssafy.io/api/matched", {
          applyId : Navigate.applyId,
      },
      {
          headers:{
              "accessToken" : "accesstoken 받아서 넣으세요",
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