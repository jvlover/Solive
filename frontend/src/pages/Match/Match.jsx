import React, { useState, useEffect } from 'react';
// import registerServiceWorker from '../../registerServiceWorker';
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
  const [isLoading, setIsLoading] = useState(true);

  const applyId = Navigate.state.applyId;
  const [state, setState] = useState('teacher');

  const fetchData = async () => {
    try {
      const sessionName = Navigate.state.sessionName;
      if (!sessionName) {
        setState('student');
        const response = await axios.post(
          'https://i9a107.p.ssafy.io/api/matched',
          {
            applyId: Navigate.state.applyId,
          },
          {
            headers: {
              'access-token': user.accessToken,
            },
          },
        );
        setSessionName(response.data.data);
      } else {
        setSessionName(sessionName);
      }
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    // 여기서 videoRoomComponent로 props 넘겨준다
    <VideoRoomComponent
      userName={user.nickname}
      picture={user.path}
      sessionName={sessionName}
      accessToken={user.accessToken}
      applyId={applyId}
      state={state}
    />
  );
};
// registerServiceWorker();

export default MatchPage;
