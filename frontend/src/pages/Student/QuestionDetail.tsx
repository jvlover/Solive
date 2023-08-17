import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  getQuestionById,
  getNewAccessToken,
  getTeachersList,
  applyToTeacher,
} from '../../api';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
import StarRating from '../star';

const StudentQuestionDetail = () => {
  console.log('StudentQuestionDetail is rendered');
  const defaultId = 1; // 원하는 default 값으로 설정
  const { id: stringId } = useParams<{ id: string }>();
  const id = parseInt(stringId || defaultId.toString());
  const [currentImage, setCurrentImage] = useState(0);

  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const [teachers, setTeachers] = useState([]);
  const [sort, setSort] = useState('Time');
  const [isFavorite, setIsFavorite] = useState(false);
  const initialQuestion: QuestionType = {
    userNickname: 'Default Nickname',
    title: 'Default Title',
    description: 'Default Description',
    path: [],
    masterCodeName: 'Default Master Code Name',
    masterCodeCategory: 'Default',
    createTime: 'Default Create Time',
    state: 'Default State',
  };

  const [question, setQuestion] = useState<QuestionType | null>(
    initialQuestion,
  );

  const navigate = useNavigate();

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
    console.log('useEffect is called');
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
              alert('토큰이 만료되었습니다. 다시 로그인 해주세요');
            }
          } else {
            alert('토큰이 만료되었습니다. 다시 로그인 해주세요');
          }
        }
      } catch (error) {
        // navigate('./error');
      }
    };

    fetchQuestion();
  }, [id, user, setUser, navigate]);

  useEffect(() => {
    const fetchTeachersData = async () => {
      const result = await getTeachersList(
        {
          questionId: id,
          sort,
          isFavorite,
        },
        user.accessToken,
      );
      if (result.success) {
        setTeachers(result.data);
      } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
        const newAccessToken = await getNewAccessToken(user.refreshToken);
        if (newAccessToken) {
          setUser({
            ...user,
            accessToken: newAccessToken,
          });
          getTeachersList(
            {
              questionId: id,
              sort,
              isFavorite,
            },
            newAccessToken,
          );
        }
      } else {
        // navigate('./error');
      }
    };

    if (user !== null) {
      fetchTeachersData();
    }
  }, [id, user, setUser, sort, isFavorite, navigate]);

  const handleApply = async (applyId: number) => {
    try {
      const response = await applyToTeacher(applyId, user.accessToken);

      if (response.success) {
        alert('성공적으로 신청 되었습니다.');
        navigate('/matchpage', {
          state: { applyId: applyId },
        });
      } else if (response.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
        const newAccessToken = await getNewAccessToken(user.refreshToken);
        if (newAccessToken) {
          setUser({ ...user, accessToken: newAccessToken });
          const newResponse = await applyToTeacher(applyId, newAccessToken);
          if (newResponse.success) {
            alert('성공적으로 신청 되었습니다.');
          }
        }
      }
    } catch (error) {
      // navigate('./error');
    }
  };

  return (
    <div className="flex justify-center">
      <div className="flex w-[80vw] min-h-[81.4vh] text-black">
        <div className="w-1/2 p-5 mt-4">
          <div className="flex h-full gap-x-10">
            <div className="w-1/2">
              <h2 className="mb-4 text-2xl" style={{ fontWeight: '900' }}>
                {question ? question.title : 'Loading...'}
              </h2>

              <div className="relative flex items-center justify-center mb-4">
                {question &&
                  question.path.map((image, idx) => (
                    <img
                      key={idx}
                      src={image}
                      alt={`Question Image ${idx}`}
                      className={`w-4/5 object-contain h-[395px] ${
                        idx === currentImage ? 'block' : 'hidden'
                      }`}
                    />
                  ))}

                <button
                  className="absolute left-0 px-3 py-1 text-white rounded top-1/2 border-solive-200 bg-solive-200"
                  onClick={() =>
                    setCurrentImage(
                      (prev) =>
                        (prev - 1 + question.path.length) %
                        question.path.length,
                    )
                  }
                >
                  ←
                </button>
                <button
                  className="absolute right-0 px-3 py-1 text-white rounded top-1/2 border-solive-200 bg-solive-200"
                  onClick={() =>
                    setCurrentImage((prev) => (prev + 1) % question.path.length)
                  }
                >
                  →
                </button>
              </div>
              <div className="flex items-center justify-center w-[70px] h-12 mt-4 border-2 rounded-md shadow-sm border-solive-200 border-opacity-50 mx-auto text-lg">
                {question.state}
              </div>
            </div>

            <div className="w-1/2 space-y-2">
              <p className="font-bold text-gray-700">닉네임</p>
              <div className="block w-full h-12 border-2 border-opacity-50 rounded-md shadow-sm border-solive-200">
                {question.userNickname}
              </div>

              <div className="flex space-x-4">
                <div className="w-full">
                  <p className="mt-8 font-bold text-gray-700">과목</p>
                  <div className="block w-full h-12 border-2 border-opacity-50 rounded-md shadow-sm border-solive-200">
                    {question.masterCodeCategory}
                  </div>
                </div>
                <div className="w-full">
                  <p className="mt-8 font-bold text-gray-700">세부과목</p>
                  <div className="block w-full h-12 border-2 border-opacity-50 rounded-md shadow-sm border-solive-200">
                    {question.masterCodeName}
                  </div>
                </div>
              </div>
              <div>
                <p className="mt-8 font-bold text-gray-700 border-opacity-50 border-solive-200">
                  문제 설명
                </p>
                <textarea
                  className="block w-full h-64 border-2 border-opacity-50 rounded-md shadow-sm cursor-default resize-none border-solive-200 focus:outline-none"
                  readOnly
                  value={question.description}
                />
              </div>
            </div>
          </div>
        </div>
        <div className="w-1/2 p-5 mt-4">
          <div>
            <h2 className="text-2xl text-center text-bold">
              이 문제에 지원한 선생님 목록입니다.
            </h2>
            <h2 className="text-center">원하시는 선생님을 선택해주세요.</h2>
            <div className="flex justify-between mt-8 space-x-4">
              <button
                className={`w-1/4 h-12 text-sm ${
                  sort === 'Time' ? 'bg-solive-200' : ''
                }`}
                onClick={() => setSort('Time')}
              >
                예상 대기순
              </button>
              <button
                className={`w-1/4 h-12 text-sm ${
                  sort === 'Price' ? 'bg-solive-200' : ''
                }`}
                onClick={() => setSort('Price')}
              >
                가격순
              </button>
              <button
                className={`w-1/4 h-12 text-sm ${
                  sort === 'Rate' ? 'bg-solive-200' : ''
                }`}
                onClick={() => setSort('Rate')}
              >
                평점순
              </button>
              <button
                className={`w-1/4 h-12 text-sm ${
                  isFavorite ? 'bg-solive-200' : 'bg-white'
                }`}
                onClick={() => setIsFavorite(!isFavorite)}
              >
                선호과목 일치
              </button>
            </div>
            <div className="grid grid-cols-3 gap-4 mt-6">
              {teachers.map((teacher) => (
                <div key={teacher.applyId}>
                  <img
                    src={teacher.path}
                    alt="Teacher profile"
                    className="mx-auto mt-16 rounded-full w-28 h-28"
                  />
                  <p className="mt-8 text-center">
                    닉네임: {teacher.teacherNickname}
                  </p>
                  <p className="text-center">
                    선호과목: {teacher.teacherSubjectName}
                  </p>
                  <p className="text-center">
                    {teacher.solvePoint} SP {teacher.estimatedTime}분
                  </p>
                  <StarRating
                    rating={(teacher.ratingSum / teacher.ratingCount).toFixed(
                      2,
                    )}
                  />

                  <button
                    className="w-full p-2 mt-8 text-white transition duration-300 rounded bg-solive-200 hover:bg-blue-600"
                    onClick={() => handleApply(teacher.applyId)}
                  >
                    신청하기
                  </button>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default StudentQuestionDetail;
