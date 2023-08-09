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
} from '@heroicons/react/24/outline';
import { useEffect, useState } from 'react';

const MyPage = () => {
  const [openNav, setOpenNav] = useState(true);

  const { pageName } = useParams();
  const navigate = useNavigate();

  let PageComponent = null;
  if (pageName === 'profile') {
    PageComponent = ProfilePage;
  } else if (pageName === 'privacy') {
    PageComponent = PersonalInfoPage;
  }

  useEffect(() => {
    window.addEventListener('resize', () => {
      if (window.innerWidth >= 960) {
        setOpenNav(true);
      } else {
        setOpenNav(false);
      }
    });
  });

  return (
    <div>
      <Typography variant="h2" className="ml-[30vh] mt-10">
        마이페이지
      </Typography>
      <hr className="my-2 mx-[25vh] border-2 border-solive-200" />
      <div className={`flex flex-row ${!openNav ? 'justify-center' : ''}`}>
        {openNav ? (
          <Card className=" max-w-[20rem] p-4 shadow-xl shadow-blue-gray-900/5 ml-[25vh] mr-[5vh] mt-5">
            <List>
              <ListItem
                onClick={() => {
                  navigate('/student/mypage/profile');
                }}
              >
                <ListItemPrefix>
                  <UserCircleIcon className="w-5 h-5" />
                </ListItemPrefix>
                프로필
              </ListItem>
              <ListItem
                onClick={() => {
                  navigate('/student/mypage/privacy');
                }}
              >
                <ListItemPrefix>
                  <KeyIcon className="w-5 h-5" />
                </ListItemPrefix>
                개인정보
              </ListItem>
              <ListItem
                onClick={() => {
                  navigate('/student/mypage/questionmanagement');
                }}
              >
                <ListItemPrefix>
                  <PencilSquareIcon className="w-5 h-5" />
                </ListItemPrefix>
                문제 관리
              </ListItem>
              <ListItem
                onClick={() => {
                  navigate('/student/mypage/solvepoint');
                }}
              >
                <ListItemPrefix>
                  <CreditCardIcon className="w-5 h-5" />
                </ListItemPrefix>
                솔브포인트 관리
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
