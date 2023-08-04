import {
  IconButton,
  SpeedDial,
  SpeedDialHandler,
  SpeedDialContent,
  SpeedDialAction,
  Typography,
} from '@material-tailwind/react';
import {
  PlusIcon,
  HomeIcon,
  UserCircleIcon,
  PencilSquareIcon,
} from '@heroicons/react/24/outline';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { userState } from '../recoil/user/userState';
const Dial = () => {
  const user = useRecoilValue(userState);
  const navigate = useNavigate();

  const labelProps = {
    variant: 'small',
    color: 'blue-gray',
    className:
      'absolute top-2/4 -left-2/4 -translate-y-2/4 -translate-x-3/4 font-normal',
  };

  const handleProfileClick = () => {
    if (user?.masterCodeId === 1) {
      navigate('/student/mypage/profile');
    } else if (user?.masterCodeId === 2) {
      navigate('/teacher/mypage/profile');
    }
  };

  const handleQuestionClick = () => {
    if (user?.masterCodeId === 1) {
      navigate('/student/questionregistration');
    } else if (user?.masterCodeId === 2) {
      navigate('/teacher/question');
    }
  };

  return (
    <div className="fixed bottom-7 right-10">
      <SpeedDial>
        <SpeedDialHandler>
          <IconButton
            size="lg"
            className="rounded-full bg-solive-100 focus:outline-none"
          >
            <PlusIcon className="h-5 w-5 transition-transform group-hover:rotate-45 " />
          </IconButton>
        </SpeedDialHandler>
        <SpeedDialContent>
          {/* 홈으로 */}
          <SpeedDialAction className="relative focus:outline-none">
            <HomeIcon className="h-5 w-5" onClick={() => navigate('/')} />
            <Typography color="red" {...labelProps}>
              홈
            </Typography>
          </SpeedDialAction>
          {/* 학생이면 학생 마이페이지, 선생이면 선생 마이페이지 */}
          <SpeedDialAction className="relative focus:outline-none">
            <UserCircleIcon className="h-5 w-5" onClick={handleProfileClick} />
            <Typography {...labelProps}>프로필</Typography>
          </SpeedDialAction>
          {/* 학생이면 문제 등록, 선생이면 문제 전체 보기 */}
          <SpeedDialAction className="relative focus:outline-none">
            <PencilSquareIcon
              className="h-5 w-5"
              onClick={handleQuestionClick}
            />
            <Typography {...labelProps}>
              {user?.masterCodeId === 1 ? '문제 등록' : '문제 보기'}
            </Typography>
          </SpeedDialAction>
        </SpeedDialContent>
      </SpeedDial>
    </div>
  );
};
export default Dial;