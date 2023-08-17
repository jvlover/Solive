import {
  Card,
  List,
  ListItem,
  ListItemPrefix,
  Typography,
} from '@material-tailwind/react';
import ProfilePage from './Profile';
import { useNavigate, useParams } from 'react-router-dom';
import PersonalInfoPage from './Privacy';
import {
  CreditCardIcon,
  KeyIcon,
  PencilSquareIcon,
  UserCircleIcon,
  StarIcon,
} from '@heroicons/react/24/outline';
import { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';
import { userState } from '../../recoil/user/userState';
import QuestionManagement from '../Student/QuestionManagement';
import PointChargePage from './SolvePoint';
import TeacherSolve from './TeacherSolve';
import FavoritePage from './Favorite';

const MyPage = () => {
  const user = useRecoilValue(userState);

  const [openNav, setOpenNav] = useState(true);

  const { pageName } = useParams();
  const navigate = useNavigate();

  let PageComponent = null;
  if (pageName === 'profile') {
    PageComponent = ProfilePage;
  } else if (pageName === 'privacy') {
    PageComponent = PersonalInfoPage;
  } else if (pageName === 'questionmanagement') {
    PageComponent = QuestionManagement;
  } else if (pageName === 'solvepoint') {
    PageComponent = PointChargePage;
  } else if (pageName === 'favorite') {
    PageComponent = FavoritePage;
  } else if (pageName === 'teachersolve') {
    PageComponent = TeacherSolve;
  }

  useEffect(() => {
    window.addEventListener('resize', () => {
      if (window.innerWidth >= 1200) {
        setOpenNav(true);
      } else {
        setOpenNav(false);
      }
    });
  });

  return (
    <div>
      <Typography variant="h2" className="ml-[30vh] mt-10 font-[Pretendard]">
        마이페이지
      </Typography>
      <hr className="my-2 mx-[25vh] border-2 border-solive-200" />
      <div className={`flex flex-row ${!openNav ? 'justify-center' : ''}`}>
        {openNav ? (
          <Card className=" max-w-[20rem] p-4 shadow-xl shadow-blue-gray-900/5 ml-[28vh] mr-[3vh] mt-5">
            <List>
              <ListItem
                onClick={() => {
                  navigate('/mypage/profile');
                }}
                className="font-[pretendard]"
              >
                <ListItemPrefix>
                  <UserCircleIcon className="w-5 h-5" />
                </ListItemPrefix>
                프로필
              </ListItem>
              <ListItem
                onClick={() => {
                  navigate('/mypage/privacy');
                }}
                className="font-[pretendard]"
              >
                <ListItemPrefix>
                  <KeyIcon className="w-5 h-5" />
                </ListItemPrefix>
                개인정보
              </ListItem>
              <ListItem
                onClick={() => {
                  navigate(
                    `${
                      user.masterCodeId == 1
                        ? '/mypage/questionmanagement'
                        : '/teacher/question'
                    }`,
                  );
                }}
                className="font-[pretendard]"
              >
                <ListItemPrefix>
                  <PencilSquareIcon className="w-5 h-5" />
                </ListItemPrefix>
                문제 관리
              </ListItem>
              {user.masterCodeId == 1 ? (
                <ListItem
                  onClick={() => {
                    navigate('/mypage/solvepoint');
                  }}
                  className="font-[pretendard]"
                >
                  <ListItemPrefix>
                    <CreditCardIcon className="w-5 h-5" />
                  </ListItemPrefix>
                  솔브포인트 관리
                </ListItem>
              ) : (
                <ListItem
                  onClick={() => {
                    navigate('/mypage/teachersolve');
                  }}
                  className="font-[pretendard]"
                >
                  <ListItemPrefix>
                    <CreditCardIcon className="w-5 h-5" />
                  </ListItemPrefix>
                  솔브포인트 출금
                </ListItem>
              )}
              <ListItem
                onClick={() => {
                  navigate('/mypage/favorite');
                }}
                className="font-[pretendard]"
              >
                <ListItemPrefix>
                  <StarIcon className="w-5 h-5" />
                </ListItemPrefix>
                즐겨찾기
              </ListItem>
            </List>
          </Card>
        ) : (
          <></>
        )}
        {PageComponent && <PageComponent />}
      </div>
    </div>
  );
};
export default MyPage;
