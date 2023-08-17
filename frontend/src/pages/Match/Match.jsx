import React, { useState, useEffect } from 'react';
import registerServiceWorker from '../../registerServiceWorker';
import axios from 'axios';
import { useRecoilValue } from 'recoil';
import { userState } from '../../recoil/user/userState';
import { useLocation } from 'react-router-dom';
import VideoRoomComponent from '../../components/videoroom/VideoRoom.jsx';

const MatchPage = () => {
  const user = useRecoilValue(userState);
  //   const picture = user.path;
  const Navigate = useLocation();

  const [sessionName, setSessionName] = useState('');

  const fetchData = async () => {
    try {
      const response = await axios.post(
        //   'https://i9a107.p.ssafy.io/api/matched',
        'http://localhost:8200/matched',
        {
          applyId: Navigate.state.applyId,
        },
        {
          headers: {
            'access-token': user.accessToken,
          },
        },
      );

      console.log(response.data);
      setSessionName(response.data);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []); // applyId가 변경될 때마다 fetchData 실행

  return (
    // 여기서 videoRoomComponent로 props 넘겨준다
    <VideoRoomComponent
      userName={user.nickname}
      picture={user.path}
      sessionName={sessionName}
    />
  );
};
registerServiceWorker();

export default MatchPage;
