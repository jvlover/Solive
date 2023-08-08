import { useNavigate } from 'react-router-dom';
import StudentImg from '../../assets/student.png';
import TeacherImg from '../../assets/teacher.png';
import BackgroundImg from '../../assets/background.png';

const SignupSelect = () => {
  const navigate = useNavigate();

  return (
    <div
      className="flex items-center justify-center h-[90vh]"
      style={{
        backgroundImage: `url(${BackgroundImg})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      <div className="flex flex-col w-[80%] mt-10">
        <div className="flex justify-evenly">
          <div
            className="flex flex-col items-center justify-start w-[26rem] h-[23rem] min-w-[26rem] min-h-[23rem] bg-white text-center rounded-lg shadow-lg cursor-pointer"
            onClick={() => navigate('/signup/student')}
          >
            <img src={StudentImg} className="w-1/2 mt-10" alt="student" />
            <p className="mt-auto mb-8 text-3xl font-semibold">
              과외를 받고 싶은 학생!
            </p>
          </div>
          <div
            className="flex flex-col items-center justify-start w-[26rem] h-[23rem] min-w-[26rem] min-h-[23rem] bg-solive-100 text-center rounded-lg shadow-lg cursor-pointer"
            onClick={() => navigate('/signup/teacher')}
          >
            <img src={TeacherImg} className="w-1/2 mt-10" alt="teacher" />
            <p className="mt-auto mb-8 text-3xl font-semibold">
              과외를 하고 싶은 선생님!
            </p>
          </div>
        </div>
        <div className="flex justify-center mt-10">
          <button
            onClick={() => navigate(-1)}
            className="w-[120px] text-lg text-blue-gray-600 underline underline-offset-4 bg-transparent hover:bg-transparent hover:border-none active:bg-transparent active:outline-none active:border-none focus:outline-none focus:border-none"
          >
            뒤로가기
          </button>
        </div>
      </div>
    </div>
  );
};

export default SignupSelect;
