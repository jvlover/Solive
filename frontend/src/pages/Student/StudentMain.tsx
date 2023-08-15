import { useState, useEffect } from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { useNavigate } from 'react-router-dom';
// import que from '../../assets/404.png';
import { getMyProblems, getNewAccessToken, getTeachers } from '../../api';
import { userState } from '../../recoil/user/userState';

const Student = () => {
  const navigate = useNavigate();
  const [currentSlide, setCurrentSlide] = useState(0);
  const [isLoading] = useState(true);
  const banners = [1, 2, 3, 4, 5];
  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const [teachers, setTeachers] = useState([]);
  const [problems, setProblems] = useState([]);

  // const problems = [
  //   {
  //     id: 1,
  //     path: que,
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 2,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 3,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  // ];

  // const teachers = [
  //   {
  //     path: que,
  //     name: 12,
  //     subjectId: 'subject1',
  //     ratingRatio: 1,
  //   },
  //   {
  //     path: 'path1',
  //     name: 12,
  //     subjectId: 'subject1',
  //     ratingRatio: 1,
  //   },
  //   {
  //     path: 'path1',
  //     name: 12,
  //     subjectId: 'subject1',
  //     ratingRatio: 1,
  //   },
  // ];

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
          1100,
          0,
          1,
          '',
          'TIME_ASC',
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
              console.error(
                'Failed to load problems after token refresh:',
                newResult.error,
              );
            }
          } else {
            console.error('Failed to refresh token');
          }
        }
      } catch (error) {
        console.error('Error fetching problems:', error);
      }
    };

    if (user) {
      fetchProblems();
    }
  }, [user, setUser]);

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
              console.error(
                'Failed to load problems after token refresh:',
                newResult.error,
              );
            }
          } else {
            console.error('Failed to refresh token');
          }
        }
      } catch (error) {
        console.error('Error fetching problems:', error);
      }
    };

    if (user) {
      fetchTeachers();
    }
  }, [user, setUser]);

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
            {banner}
          </div>
        ))}
      </div>
      <div className="flex flex-row w-full">
        <div className="flex flex-col justify-between w-1/2 p-4 text-center border-r-2 border-gray-200">
          <div>
            <h2 className="mt-4 mb-4 text-xl font-bold">
              현재 접속중인 선생님 목록
            </h2>
            <div className="grid grid-cols-3 gap-4">
              {teachers.map((teacher, index) => (
                <div
                  key={index}
                  className="border-2 border-blue-200 p-2 flex flex-col items-center  h-[300px] w-[220px]"
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
          </div>
        </div>
        <div className="flex flex-col justify-between w-1/2 p-4 text-center border-r-2 border-gray-200">
          <h2 className="mt-4 mb-4 text-xl font-bold">내가 등록한 문제</h2>
          <div className="flex flex-col items-center justify-center h-full mb-16">
            <div className="grid grid-cols-3 gap-4">
              {problems.slice(0, 3).map((problem) => (
                <div
                  key={problem.id}
                  className="border-2 border-blue-200 p-2 flex flex-col items-center h-[300px] w-[220px]"
                >
                  <img
                    onClick={() => handleDetailPage(problem.id)}
                    className="h-[200px] w-[250px]"
                    src={problem.path}
                    alt="problem"
                  />
                  <p>제목 : {problem.title}</p>
                  <p>등록시간 : {problem.time}</p>
                </div>
              ))}
            </div>
            <div className="flex justify-center mt-20 space-x-4 ">
              <button
                className="px-4 py-2 text-white bg-blue-500 rounded"
                onClick={() => navigate('questionregistration')}
              >
                문제 등록하기
              </button>
              <button
                className="px-4 py-2 text-white bg-blue-500 rounded"
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
