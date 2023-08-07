import axios from 'axios';
import { useState, useEffect } from 'react';

const QuestionManagement = () => {
  const [problems, setProblems] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  // const problems = [
  //   {
  //     id: 1,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 2,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 3,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 4,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 5,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 6,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 7,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 8,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 9,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 10,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 11,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  // ];

  useEffect(() => {
    const accessToken = 'YOUR_ACCESS_TOKEN'; // 사용자의 액세스 토큰
    axios
      .get('api_url', { headers: { Authorization: `Bearer ${accessToken}` } })
      .then((response) => {
        setProblems(response.data);
      })
      .catch((error) => {
        console.error('Failed to load problems:', error);
      });
  }, []);

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
        <h1 className="ml-36 font-semibold">마이페이지</h1>
      </div>
      <hr className="mt-4 mx-auto w-7/10 border-none h-1 bg-blue-200" />
      <div
        className="mx-auto mt-8 mb-8 h-[650px] w-[800px] p-6"
        style={{ border: '2px solid #646CFF' }}
      >
        <p className="text-[18px] font-bold">내가 등록한 문제</p>
        <p className="text-[12px] mt-4">
          저장된 강의는 7일동안 다시 볼 수 있습니다.
        </p>
        <div className="grid grid-cols-3 gap-4">
          {problems.slice(currentIndex, currentIndex + 6).map((problem) => (
            <div key={problem.id} className="border-2 border-blue-200 p-2">
              <img
                src={problem.path_name}
                alt="problem"
                className="w-12 h-12"
              />
              <p>{problem.title}</p>
              <p>{problem.subject}</p>
              <p>{problem.path_name}</p>
              <p>{problem.time}</p>
              <p>{matchingStateToString(problem.matching_state)}</p>
            </div>
          ))}
        </div>
        {problems.length > 6 && (
          <button onClick={() => setCurrentIndex(currentIndex + 6)}>
            다음 문제 보기
          </button>
        )}
      </div>
    </div>
  );
};

export default QuestionManagement;
