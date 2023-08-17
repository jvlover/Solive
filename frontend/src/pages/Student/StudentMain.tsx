import { useState, useEffect } from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { useNavigate } from 'react-router-dom';
import { getMyProblems, getNewAccessToken, getTeachers } from '../../api';
import { userState } from '../../recoil/user/userState';
import banner_1 from '../../assets/banner_1.png';
import banner_2 from '../../assets/banner_2.png';
import banner_3 from '../../assets/banner_3.png';
import banner_4 from '../../assets/banner_4.png';
import banner_5 from '../../assets/banner_5.png';

const Student = () => {
  const navigate = useNavigate();
  const [currentSlide, setCurrentSlide] = useState(0);
  const [isLoading] = useState(true);
  const banners = [banner_1, banner_2, banner_3, banner_4, banner_5];
  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const [teachers, setTeachers] = useState([]);
  const [problems, setProblems] = useState([]);

  useEffect(() => {
    if (!user || user.masterCodeId !== 1) {
      navigate('/login');
    }
  }, [user, navigate]);

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prevSlide) => (prevSlide + 1) % banners.length);
    }, 3000);

    return () => clearInterval(timer);
  }, [banners.length]);

  useEffect(() => {
    const fetchProblems = async () => {
      try {
        const result = await getMyProblems(
          1,
          0,
          0,
          0,
          '',
          'Time_ASC',
          0,
          user.accessToken,
        );
        if (result.success) {
          setProblems(result.data);
        } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
          const newAccessToken = await getNewAccessToken(user.refreshToken);
          if (newAccessToken) {
            setUser({ ...user, accessToken: newAccessToken });
            const newResult = await getMyProblems(
              1,
              1100,
              0,
              1,
              '',
              'TIME_ASC',
              0,
              newAccessToken,
            );
            if (newResult.success) {
              setProblems(newResult.data);
            } else {
              alert('다시 로그인 해주세요');
              navigate('/login');
            }
          } else {
            navigate('/error');
          }
        }
      } catch (error) {
        navigate('/error');
      }
    };

    if (user) {
      fetchProblems();
    }
  }, [user, setUser, navigate, problems]);

  useEffect(() => {
    const fetchTeachers = async () => {
      try {
        const result = await getTeachers(user.accessToken);
        if (result.success) {
          setTeachers(result.data);
        } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
          const newAccessToken = await getNewAccessToken(user.refreshToken);
          if (newAccessToken) {
            setUser({ ...user, accessToken: newAccessToken });
            const newResult = await getTeachers(newAccessToken);
            if (newResult.success) {
              setTeachers(newResult.data);
            } else {
              navigate('/error');
            }
          } else {
            navigate('/error');
          }
        }
      } catch (error) {
        navigate('/error');
      }
    };

    if (user) {
      fetchTeachers();
    }
  }, [user, setUser, navigate]);

  const handleDetailPage = (id) => {
    navigate(`/student/question/${id}`);
  };

  return (
    <div className="flex flex-col items-center">
      {isLoading}
      <div className="relative flex items-center justify-center w-full h-48 mb-8 overflow-hidden text-2xl font-bold bg-gray-200">
        {banners.map((banner, idx) => (
          <div
            key={idx}
            className={`absolute w-full h-full flex items-center justify-center ${
              idx === currentSlide
                ? 'opacity-100 transition-opacity duration-1000'
                : 'opacity-0'
            }`}
          >
            <img src={banner} alt={String(idx)} className="w-full h-full" />
          </div>
        ))}
      </div>
      <div className="flex flex-row w-full">
        <div className="flex flex-col justify-between w-1/2 p-4 text-center border-r-2 border-gray-200">
          <div>
            <h2 className="mt-4 mb-4 text-xl font-bold">
              현재 접속중인 선생님 목록
            </h2>
            {teachers.length > 0 ? (
              <div className="grid grid-cols-3 gap-4">
                {teachers.map((teacher, index) => (
                  <div
                    key={index}
                    className="border-2 border-solive-200 p-2 flex flex-col items-center  h-[300px] w-[220px] rounded-lg border-opacity-50"
                  >
                    <img
                      className="h-[200px] w-[250px]"
                      src={teacher.path}
                      alt="teacher"
                    />
                    <p>이름: {teacher.nickname}</p>
                    <p>관심과목: {teacher.masterCodeName}</p>
                    <p>평점: {teacher.rating}</p>
                  </div>
                ))}
              </div>
            ) : (
              <div className="mt-48 font-semibold text-blue-gray-600">
                접속중인 선생님이 없습니다.
              </div>
            )}
          </div>
        </div>
        <div className="flex flex-col justify-between w-1/2 p-4 text-center border-r-2 border-gray-200">
          <h2 className="mt-4 mb-4 text-xl font-bold">내가 등록한 문제</h2>
          <div className="flex flex-col items-center justify-center h-full my-8">
            <div className="grid grid-cols-3 gap-4">
              {problems.slice(0, 3).map((problem) => (
                <div
                  key={problem.questionId}
                  className="border-2 border-solive-200 p-2 flex flex-col items-center h-[300px] w-[220px] rounded-lg border-opacity-50"
                >
                  <img
                    onClick={() => handleDetailPage(problem.questionId)}
                    className="h-[200px] w-[250px]"
                    src={problem.path}
                    alt="problem"
                  />
                  <p className="mt-2 font-bold">{problem.title}</p>
                  <p className="mt-1 font-extralight">
                    {problem.createTime.replace('T', ' ').slice(0, -7)}
                  </p>
                </div>
              ))}
            </div>
            <div className="flex justify-center mt-10 space-x-4 ">
              <button
                className="px-4 py-2 btn-primary"
                onClick={() => navigate('questionregistration')}
              >
                문제 등록하기
              </button>
              <button
                className="px-4 py-2 btn-primary"
                onClick={() => navigate('/mypage/questionmanagement')}
              >
                전체 보기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Student;
