import React from 'react';
import { useNavigate } from 'react-router-dom';
import BackgroundImg from '../assets/background.png';

function Home(): JSX.Element {
  const navigate = useNavigate();

  return (
    <div
      className="flex items-center justify-center h-screen"
      style={{
        backgroundImage: `url(${BackgroundImg})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      <div className="pt-20 flex flex-col justify-between min-h-screen text-center">
        <div>
          <h1 className="text-2xl">Welcome to Home!</h1>
          <p className="text-white text-5xl mt-10 font-bold">
            원하는 선생님 너가 고른다.
          </p>
          <p className="text-white text-5xl mt-5 font-bold">
            우리가 <span className="text-blue-500">빠르게</span> 끝내준다
          </p>
        </div>
        <button
          className="mb-10 py-2 px-4 bg-blue-500 text-white text-lg font-bold rounded"
          onClick={() => navigate('/signup')}
        >
          회원가입 하고 문제 등록하러 가기
        </button>
      </div>
    </div>
  );
}

export default Home;
