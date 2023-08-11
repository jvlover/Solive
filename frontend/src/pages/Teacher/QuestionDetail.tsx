import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getQuestionById, getNewAccessToken } from '../../api';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
// import que from '../../assets/404.png';
// import ques from '../../assets/home-teacher.png';
// import axios from 'axios';
import { applyQuestion } from '../../api';

const TeacherQuestionDetail = () => {
  const { id } = useParams<{ id: string }>();
  const [question, setQuestion] = useState(null);
  const [currentImage, setCurrentImage] = useState(0);

  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [solvePoint, setSolvePoint] = useState('');
  const [expectedTime, setExpectedTime] = useState('');

  //   const question = {
  //     path: [que, ques],
  //     title: 'title1',
  //     userNickname: '더도리',
  //     description: '어려운 문제',
  //     subject: '수학',
  //     subSubject: '수학1',
  //     state: '대기중',
  //   };

  useEffect(() => {
    const fetchQuestion = async () => {
      try {
        const result = await getQuestionById(id, user.accessToken);
        if (result.success) {
          setQuestion(result.data);
        } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
          const newAccessToken = await getNewAccessToken(user.refreshToken);
          if (newAccessToken) {
            setUser({ ...user, accessToken: newAccessToken });
            const newResult = await getQuestionById(id, newAccessToken);
            if (newResult.success) {
              setQuestion(newResult.data);
            } else {
              console.error(
                'Failed to load question after token refresh:',
                newResult.error,
              );
            }
          } else {
            console.error('Failed to refresh token');
          }
        }
      } catch (error) {
        console.error('Error fetching question:', error);
      }
    };

    fetchQuestion();
  }, [id, user, setUser]);

  return (
    <div className="flex w-screen min-h-screen text-black">
      <div className="w-1/2 p-5">
        <h2 className="text-2xl mb-4" style={{ fontWeight: '900' }}>
          {question.title}
        </h2>
        <div className="relative">
          {question.path.map((image, idx) => (
            <img
              key={idx}
              src={image}
              alt={`Question Image ${idx}`}
              className={`w-full object-contain h-[350px] ${
                idx === currentImage ? 'block' : 'hidden'
              }`}
            />
          ))}
          <button
            className="absolute top-1/2 left-0 px-4 py-2 text-white rounded border-light-blue-500 bg-light-blue-500"
            onClick={() =>
              setCurrentImage(
                (prev) =>
                  (prev - 1 + question.path.length) % question.path.length,
              )
            }
          >
            ←
          </button>
          <button
            className="absolute top-1/2 right-0 px-4 py-2 text-white rounded border-light-blue-500 bg-light-blue-500"
            onClick={() =>
              setCurrentImage((prev) => (prev + 1) % question.path.length)
            }
          >
            →
          </button>
        </div>
        <div className="flex justify-center mt-24">
          <button
            className="bg-light-blue-500 text-white px-4 py-2 rounded"
            onClick={() => {
              if (question.state === '완료됨') {
                alert('이미 매칭이 완료된 문제입니다.');
              } else {
                setIsModalOpen(true);
              }
            }}
          >
            신청하기
          </button>
        </div>
      </div>

      <div className="w-1/2 p-5">
        <div className="space-y-3">
          <p className="text-gray-700 font-extrabold mt-4">닉네임:</p>
          <div className="block w-full h-16 mt-1 border-2 rounded-md shadow-sm border-light-blue-500">
            {question.userNickname}
          </div>
          <div>
            <p className="text-gray-700 font-extrabold mt-4">과목</p>
            <div className="block w-full h-16 mt-1 border-2 rounded-md shadow-sm border-light-blue-500">
              {question.subject}
            </div>

            <p className="text-gray-700 font-extrabold mt-4">세부과목</p>
            <div className="block w-full h-16 mt-1 border-2 rounded-md shadow-sm border-light-blue-500">
              {question.subSubject}
            </div>
          </div>

          <p className="text-gray-700 font-extrabold mt-4">Description:</p>
          <textarea
            className="block w-full h-48 mt-1 border-2 rounded-md shadow-sm border-light-blue-500"
            readOnly
            value={question.description}
          />
        </div>
      </div>
      {isModalOpen && (
        <div className="fixed top-0 left-0 w-screen h-screen flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-6 rounded-md">
            <h3 className="mb-4 font-bold">문제 해결 신청</h3>
            <label className="block mb-2">
              Solve Point:
              <input
                type="number"
                value={solvePoint}
                onChange={(e) => setSolvePoint(e.target.value)}
                className="border p-2 mt-1 w-full"
              />
            </label>
            <label className="block mb-4">
              예상 시간:
              <input
                type="number"
                value={expectedTime}
                onChange={(e) => setExpectedTime(e.target.value)}
                className="border p-2 mt-1 w-full"
              />
            </label>
            <button
              className="bg-light-blue-500 text-white px-4 py-2 rounded"
              onClick={async () => {
                try {
                  const response = await applyQuestion(
                    {
                      solvePoint: solvePoint,
                      estimatedTime: expectedTime,
                      questionId: id,
                    },
                    user.accessToken,
                  );

                  if (response.success) {
                    alert('신청이 완료되었습니다.');
                    setIsModalOpen(false);
                  } else {
                    const errorMessage =
                      response.message ||
                      'Error applying for the question. Please try again.';
                    throw new Error(errorMessage);
                  }
                } catch (error) {
                  alert(error.message);
                }
              }}
            >
              신청하기
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default TeacherQuestionDetail;
