import { Link } from 'react-router-dom';
import logo from '../assets/logo.png';
import { useState, useEffect } from 'react';
import {
  Navbar,
  MobileNav,
  Typography,
  IconButton,
} from '@material-tailwind/react';

// 로그인 하면 로그인 했다고 표시
const HeaderNav = () => {
  const [openNav, setOpenNav] = useState(false);

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

  const navList = (
    <ul className="mb-4 mt-2 flex flex-col gap-2 lg:mb-0 lg:mt-0 lg:flex-row lg:items-center lg:gap-6">
      <Typography {...labelProps}>
        <Link to="/board" className="flex items-center text-black">
          공지사항
        </Link>
      </Typography>
      <Typography {...labelProps}>
        <Link to="/signup" className="flex items-center text-black">
          회원가입
        </Link>
      </Typography>
      <Typography {...labelProps}>
        <Link to="/login" className="flex items-center text-black">
          로그인
        </Link>
      </Typography>
    </ul>
  );

  return (
    <Navbar className="sticky top-0 z-10 h-max max-w-full rounded-none py-2 px-4 lg:px-8 lg:py-4">
      <div className="flex items-center justify-between text-blue-gray-900">
        <Link to="/">
          <img src={logo} alt="Logo" className="h-10 w-auto" />
        </Link>
        <div className="flex items-center gap-4">
          <div className="hidden lg:block">{navList}</div>
          {/* <Button
              variant="gradient"
              size="sm"
              className="hidden lg:inline-block"
            >
              <span>Buy Now</span>
            </Button> */}
          <IconButton
            variant="text"
            className="ml-auto h-6 w-6 text-inherit hover:bg-transparent focus:bg-transparent active:bg-transparent lg:hidden"
            ripple={false}
            onClick={() => setOpenNav(!openNav)}
          >
            {openNav ? (
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                className="h-6 w-6"
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
                className="h-6 w-6"
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
      <MobileNav open={openNav}>
        {navList}
        {/* <Button variant="gradient" size="sm" fullWidth className="mb-2">
          <span>Buy Now</span>
        </Button> */}
      </MobileNav>
    </Navbar>
  );

  // return (
  //   <nav className="fixed w-full flex items-center justify-between flex-wrap bg-white p-4 z-10">
  //     <div className="flex items-center flex-shrink-0 mr-6">
  //       <img src={logo} alt="Logo" className="h-10 w-auto" />
  //     </div>
  //     <div className="w-full block flex-grow lg:flex lg:items-center lg:w-auto">
  //       <div className="text-sm lg:flex-grow"></div>
  //       <div>
  //         <Link
  //           to="/"
  //           className="block mt-4 lg:inline-block lg:mt-0 text-black hover:text-white mr-4"
  //         >
  //           Home
  //         </Link>
  //         <Link
  //           to="/board"
  //           className="block mt-4 lg:inline-block lg:mt-0 text-black hover:text-white mr-4"
  //         >
  //           Board
  //         </Link>
  //         <Link
  //           to="/signup"
  //           className="block mt-4 lg:inline-block lg:mt-0 text-black hover:text-white mr-4"
  //         >
  //           Signup
  //         </Link>
  //         {/* 수정 navbar에 로그인 추가 */}
  //         <Link
  //           to="/login"
  //           className="block mt-4 lg:inline-block lg:mt-0 text-black hover:text-white mr-4"
  //         >
  //           Login
  //         </Link>
  //       </div>
  //     </div>
  //   </nav>
  // );
};

export default HeaderNav;
