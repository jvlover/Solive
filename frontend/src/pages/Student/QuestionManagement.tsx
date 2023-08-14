// API ACCESSTOKEN 만료된 경우 생각하여 구현
// ACCESSTOKEN 밖으로 빼내기
// 페이지

import { useState, useEffect } from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
// import que from '../../../assets/404.png';
import { getMyProblems, getNewAccessToken } from '../../api';

const QuestionManagement = () => {
  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const [problems, setProblems] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);

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
  //   {
  //     id: 4,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 5,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 6,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 7,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 8,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 9,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 10,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 11,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 12,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 13,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 14,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 15,
  //     path: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  // ];

  useEffect(() => {
    const getProblems = async (accessToken: string): Promise<void> => {
      const result = await getMyProblems(accessToken);
      if (result.success) {
        setProblems(result.data);
      } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
        const newAccessToken = await getNewAccessToken(user.refreshToken);
        if (newAccessToken) {
          setUser({
            ...user,
            accessToken: newAccessToken,
          });
          getProblems(newAccessToken);
        }
      } else {
        console.error('Failed to load problems:', result.error);
      }
    };

    if (user !== null) {
      getProblems(user.accessToken);
    }
  }, [setUser, user]);

  const matchingStateToString = (state) => {
    switch (state) {
      case 0:
        return '등록됨';
      case 1:
        return '요청됨';
      case 2:
        return '완료됨';
      default:
        return '상태 없음';
    }
  };

  return (
    <div className="pt-4">
      <div>
        <h1 className="font-semibold ml-36">마이페이지</h1>
      </div>
      <hr className="h-1 mx-auto mt-4 bg-blue-200 border-none w-7/10" />
      <div
        className="mx-auto mt-8 mb-8 h-[650px] w-[800px] p-6"
        style={{ border: '2px solid #646CFF' }}
      >
        <p className="text-[18px] font-bold">내가 등록한 문제</p>
        <p className="text-[12px] mt-4">
          저장된 강의는 7일동안 다시 볼 수 있습니다.
        </p>
        <div className="grid grid-cols-3 gap-4 pt-12">
          {problems.slice(currentIndex, currentIndex + 6).map((problem) => (
            <div
              key={problem.id}
              className="flex flex-col items-center p-2 border-2 border-blue-200"
            >
              <img
                className="h-[140px] w-[250px]"
                src={problem.path}
                alt="problem"
              />
              <span>
                {problem.title} {matchingStateToString(problem.matching_state)}
              </span>
            </div>
          ))}
        </div>
        <div className="flex justify-between pt-8">
          {currentIndex >= 6 && (
            <button onClick={() => setCurrentIndex(currentIndex - 6)}>
              이전 문제 보기
            </button>
          )}
          {problems.length > currentIndex + 6 && (
            <button onClick={() => setCurrentIndex(currentIndex + 6)}>
              다음 문제 보기
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default QuestionManagement;
