import { Link } from 'react-router-dom';
import logo from '../assets/logo.png';
import { useState, useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { userState } from '../recoil/user/userState';
import { ReactComponent as SolvePoint } from '../assets/solve_point.svg';
import {
  Navbar,
  Typography,
  IconButton,
  Collapse,
  MenuHandler,
  Menu,
  MenuList,
  MenuItem,
} from '@material-tailwind/react';
import {
  DocumentTextIcon,
  PencilSquareIcon,
  PowerIcon,
  UserCircleIcon,
  UserIcon,
} from '@heroicons/react/24/outline';
import MatchingNotification from './MatchingNotification';

// 로그인 하면 로그인 했다고 표시
const HeaderNav = () => {
  const [openNav, setOpenNav] = useState(false);
  const user = useRecoilValue(userState);

  useEffect(() => {
    window.addEventListener(
      'resize',
      () => window.innerWidth >= 960 && setOpenNav(false),
    );
  }, []);

  const labelProps = {
    as: 'li',
    variant: 'small',
    className: 'p-1 font-normal font-[Pretendard]',
  };

  const navList = user ? (
    <ul className="flex flex-col gap-2 mt-2 mb-4 lg:mb-0 lg:mt-0 lg:flex-row lg:items-center lg:gap-6">
      <Typography {...labelProps}>
        안녕하세요{' '}
        <span className="font-semibold font-[Pretendard]">{user.nickname}</span>
        {user.masterCodeId === 2 ? ' 선생' : ' '}님
      </Typography>
      <MatchingNotification />
      <Menu>
        <MenuHandler>
          <UserIcon className="w-auto cursor-pointer h-7" />
        </MenuHandler>
        <MenuList>
          <MenuItem className="flex items-center gap-2 border-none bg-none hover:outline-none hover:border-none">
            <SolvePoint className="w-5 h-5" />
            <Typography variant="small" className="font-normal">
              {user.solvePoint} SP
            </Typography>
          </MenuItem>
          <MenuItem className="flex items-center gap-2 border-none bg-none hover:outline-none hover:border-none">
            <UserCircleIcon className="w-5 h-5" />
            <Typography variant="small" className="font-normal">
              프로필
            </Typography>
          </MenuItem>
          {user.masterCodeId === 1 ? (
            <MenuItem className="flex items-center gap-2 border-none bg-none hover:outline-none hover:border-none">
              <PencilSquareIcon className="w-5 h-5" />
              <Typography variant="small" className="font-normal">
                문제 등록하기
              </Typography>
            </MenuItem>
          ) : (
            <MenuItem className="flex items-center gap-2 border-none bg-none hover:outline-none hover:border-none">
              <DocumentTextIcon className="w-5 h-5" />
              <Typography variant="small" className="font-normal">
                문제 보기
              </Typography>
            </MenuItem>
          )}
          <hr className="my-2 border-blue-gray-50" />
          <MenuItem className="flex items-center gap-2 border-none bg-none hover:outline-none hover:border-none">
            <PowerIcon className="w-5 h-5" />
            <Typography variant="small" className="font-normal">
              로그아웃
            </Typography>
          </MenuItem>
        </MenuList>
      </Menu>
    </ul>
  ) : (
    <ul className="flex flex-col gap-2 mt-2 mb-4 lg:mb-0 lg:mt-0 lg:flex-row lg:items-center lg:gap-6">
      <Typography {...labelProps}>
        <Link to="/login" className="flex items-center text-black">
          로그인
        </Link>
      </Typography>
      <Typography {...labelProps}>
        <Link to="/signup" className="flex items-center text-black">
          회원가입
        </Link>
      </Typography>
    </ul>
  );

  return (
    <Navbar className="sticky top-0 z-10 max-w-full px-4 py-2 rounded-none h-max lg:px-8 lg:py-4">
      <div className="flex items-center justify-between text-blue-gray-900">
        <Link to="/">
          <img src={logo} alt="Logo" className="w-auto h-10" />
        </Link>
        <div className="flex items-center gap-4">
          <div className="hidden lg:block">{navList}</div>
          <IconButton
            variant="text"
            className="w-6 h-6 ml-auto text-inherit hover:bg-transparent focus:bg-transparent active:bg-transparent lg:hidden"
            ripple={false}
            onClick={() => setOpenNav(!openNav)}
          >
            {openNav ? (
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                className="w-6 h-6"
                viewBox="0 0 24 24"
                stroke="currentColor"
                strokeWidth={2}
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            ) : (
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="w-6 h-6"
                fill="none"
                stroke="currentColor"
                strokeWidth={2}
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M4 6h16M4 12h16M4 18h16"
                />
              </svg>
            )}
          </IconButton>
        </div>
      </div>
      <Collapse open={openNav}>{navList}</Collapse>
    </Navbar>
  );
};

export default HeaderNav;
