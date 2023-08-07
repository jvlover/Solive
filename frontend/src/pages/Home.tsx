import { useNavigate } from 'react-router-dom';
import BackgroundImg from '../assets/background.png';
import homeTeacher from '../assets/home-teacher.png';

const Home = () => {
  const navigate = useNavigate();

  return (
    <div
      className="flex items-center justify-center h-[90vh] w-[100%] "
      style={{
        backgroundImage: `url(${BackgroundImg})`,
        backgroundSize: 'cover',
        backgroundPosition: 'top',
      }}
    >
      <div className=" flex flex-col justify-evenly min-h-screen text-center">
        <div>
          <p className="text-white text-5xl mt-5 font-bold">
            원하는 선생님 너가 고른다
          </p>
          <p className="text-white text-5xl mt-5 font-bold">
            우리가 <span className="text-solive-100">빠르게</span> 끝내준다
          </p>
        </div>
        <div>
          <img
            src={homeTeacher}
            alt="teacher"
            className="-mt-10 ml-20 h-[55vh] w-[60vh]"
          ></img>
          <button
            className="w-[80vh] mb-3 py-2 px-4 bg-solive-100 text-white text-lg font-bold rounded shadow-2xl"
            onClick={() => navigate('/signup')}
          >
            지금 회원가입 하고 문제 등록하러 가기
          </button>
        </div>
      </div>
    </div>
  );
};

export default Home;
