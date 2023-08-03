import { useNavigate } from 'react-router-dom';
import StudentImg from '../../assets/student.png';
import TeacherImg from '../../assets/teacher.png';
import BackgroundImg from '../../assets/background.png';

const Signup = () => {
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
      <div className="flex gap-96">
        <div
          className="mx-auto w-96 h-96 flex flex-col items-center justify-start bg-white border-2 border-black text-center rounded-lg cursor-pointer"
          onClick={() => navigate('/signup/StudentSignup')}
        >
          <img src={StudentImg} className="w-3/5 mt-8" alt="Logo" />
          <p className="text-2xl mt-auto mb-8">학생으로 회원가입</p>
        </div>
        <div
          className="mx-auto w-96 h-96 flex flex-col items-center justify-start bg-white border-2 border-black text-center rounded-lg cursor-pointer"
          onClick={() => navigate('/signup/TeacherSignup')}
        >
          <img src={TeacherImg} className="w-3/5 mt-8" alt="Logo" />
          <p className="text-2xl mt-auto mb-8">선생님으로 회원가입</p>
        </div>
      </div>
    </div>
  );
};

export default Signup;
