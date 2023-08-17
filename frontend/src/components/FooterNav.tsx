import { Typography } from '@material-tailwind/react';
import whiteLogo from '../assets/logo_white.png';
import { Link } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { userState } from '../recoil/user/userState';

const FooterNav = () => {
  const user = useRecoilValue(userState);

  const labelProps = {
    as: 'a',
    color: 'white',
    className:
      'font-[Pretendard] font-normal transition-colors hover:text-solive-100 focus:text-solive-100',
  };

  return (
    <footer className="w-full p-3 bg-gray-700 border-t-2 border-blue-gray-200">
      <div className="flex flex-row flex-wrap items-center justify-center text-center bg-gray-700 gap-y-6 gap-x-12 md:justify-between">
        <img src={whiteLogo} alt="solive" className="h-10 w-auto ml-[10%]" />
        <ul className="flex flex-wrap items-center gap-y-2 gap-x-8 mr-[10%]">
          <li>
            <Link
              to={
                user ? (user.masterCodeId == 1 ? '/student' : '/teacher') : '/'
              }
            >
              <div className="font-[Pretendard] font-normal transition-colors text-white hover:text-solive-100 focus:text-solive-100">
                홈
              </div>
            </Link>
          </li>
          <li>
            <Typography href="https://about.gitlab.com/" {...labelProps}>
              깃랩
            </Typography>
          </li>
          <li>
            <Typography href="https://www.notion.so/" {...labelProps}>
              블로그
            </Typography>
          </li>
          <li>
            <Typography
              href="https://map.naver.com/v5/search/%EB%A9%80%ED%8B%B0%EC%BA%A0%ED%8D%BC%EC%8A%A4%20%EC%97%AD%EC%82%BC/place/11572240?placePath=%3Fentry=pll%26from=nx%26fromNxList=true"
              {...labelProps}
            >
              오시는 길
            </Typography>
          </li>
        </ul>
      </div>
    </footer>
  );
};
export default FooterNav;
