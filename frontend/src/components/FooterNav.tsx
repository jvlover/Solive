import { Typography } from '@material-tailwind/react';
import whiteLogo from '../assets/logo_white.png';

const FooterNav = () => {
  return (
    <footer className="w-full bg-gray-700 p-5 border-t-2 border-blue-gray-200">
      <div className="flex flex-row flex-wrap items-center justify-center gap-y-6 gap-x-12 bg-gray-700 text-center md:justify-between">
        <img src={whiteLogo} alt="solive" className="h-16 w-auto ml-[10%]" />
        <ul className="flex flex-wrap items-center gap-y-2 gap-x-8 mr-[10%]">
          <li>
            <Typography
              as="a"
              href="/"
              color="white"
              className="font-normal transition-colors hover:text-solive-100 focus:text-solive-100"
            >
              홈
            </Typography>
          </li>
          <li>
            <Typography
              as="a"
              href="https://about.gitlab.com/"
              color="white"
              className=" font-normal transition-colors hover:text-solive-100 focus:text-solive-100"
            >
              깃랩
            </Typography>
          </li>
          <li>
            <Typography
              as="a"
              href="https://www.notion.so/"
              color="white"
              className=" font-normal transition-colors hover:text-solive-100 focus:text-solive-100"
            >
              블로그
            </Typography>
          </li>
          <li>
            <Typography
              as="a"
              href="https://map.naver.com/v5/search/%EB%A9%80%ED%8B%B0%EC%BA%A0%ED%8D%BC%EC%8A%A4%20%EC%97%AD%EC%82%BC/place/11572240?placePath=%3Fentry=pll%26from=nx%26fromNxList=true"
              color="white"
              className="font-normal transition-colors hover:text-solive-100 focus:text-solive-100"
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
