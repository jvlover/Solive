import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getQuestionById, getNewAccessToken } from '../../api';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
// import que from '../../assets/404.png';
// import ques from '../../assets/home-teacher.png';
// import axios from 'axios';
import { applyQuestion } from '../../api';

const TeacherQuestionDetail = () => {
  const defaultId = 1; // 원하는 default 값으로 설정
  const { id: stringId } = useParams<{ id: string }>();
  const id = parseInt(stringId || defaultId.toString());
  const initialQuestion: QuestionType = {
    userNickname: 'Default Nickname',
    title: 'Default Title',
    description: 'Default Description',
    path: [],
    masterCodeName: 'Default Master Code Name',
    masterCodeCategory: 'Default Master Code Category',
    createTime: 'Default Create Time',
    state: 'Default State',
  };

  const [question, setQuestion] = useState<QuestionType | null>(
    initialQuestion,
  );

  const [currentImage, setCurrentImage] = useState(0);
  const navigate = useNavigate();
  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [solvePoint, setSolvePoint] = useState('');
  const [expectedTime, setExpectedTime] = useState('');

  type QuestionType = {
    userNickname: string;
    title: string;
    description: string;
    path: string[];
    masterCodeName: string;
    masterCodeCategory: string;
    createTime: string;
    state: string;
  };

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
              alert('다시 로그인 해주세요');
              navigate('./error');
            }
          } else {
            navigate('/error');
          }
        }
      } catch (error) {
        navigate('./error');
      }
    };

    fetchQuestion();
  }, [id, user, setUser, navigate]);

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
              {question.masterCodeCategory}
            </div>

            <p className="text-gray-700 font-extrabold mt-4">세부과목</p>
            <div className="block w-full h-16 mt-1 border-2 rounded-md shadow-sm border-light-blue-500">
              {question.masterCodeName}
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
          <div className="relative bg-white p-6 rounded-md">
            <button
              className="absolute top-2 right-2 p-1 w-6 h-6 text-black z-10"
              onClick={() => setIsModalOpen(false)}
            >
              X
            </button>
            <h3 className="mb-4 font-bold">문제 해결 신청</h3>
            <label className="block mb-2">
              <p>SolvePoint (10분당 가격)</p>
              <input
                type="number"
                value={solvePoint}
                onChange={(e) => setSolvePoint(e.target.value)}
                step="100"
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
