import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  getQuestionById,
  getNewAccessToken,
  getTeachersList,
  applyToTeacher,
} from '../../api';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
// import que from '../../assets/404.png';
// import ques from '../../assets/home-teacher.png';
// import a from '../../assets/logo.png';
// import b from '../../assets/logo_white.png';
// import c from '../../assets/home-teacher.png';

const TeacherQuestionDetail = () => {
  const { id } = useParams<{ id: string }>();
  const [currentImage, setCurrentImage] = useState(0);

  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const [teachers, setTeachers] = useState([]);
  const [sort, setSort] = useState('Time');
  const [isFavorite, setIsFavorite] = useState(false);
  const [question, setQuestion] = useState(null);

  // const question = {
  //   path: [que, ques],
  //   title: 'title1',
  //   userNickname: '더도리',
  //   description: '어려운 문제',
  //   subject: '수학',
  //   subSubject: '수학1',
  //   state: '등록됨',
  // };

  // const teachers = [
  //   {
  //     applyId: 1,
  //     teacherSubjectName: '기하',
  //     path: a,
  //     teacherNickname: '더도리2',
  //     solvePoint: '10',
  //     estimatedTime: '10',
  //     ratingSum: 50,
  //     ratingCount: 4,
  //   },
  //   {
  //     applyId: 2,
  //     teacherSubjectName: '기하',
  //     path: b,
  //     teacherNickname: '더도리1',
  //     solvePoint: '10',
  //     estimatedTime: '10',
  //     ratingSum: 50,
  //     ratingCount: 4,
  //   },
  //   {
  //     applyId: 3,
  //     teacherSubjectName: '기하',
  //     path: c,
  //     teacherNickname: '더도리3',
  //     solvePoint: '10',
  //     estimatedTime: '10',
  //     ratingSum: 50,
  //     ratingCount: 4,
  //   },
  // ];

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
        console.error('Failed to load problems:', result.error);
      }
    };

    if (user !== null) {
      fetchTeachersData();
    }
  }, [id, user, setUser, sort, isFavorite]);

  const handleApply = async (applyId: number) => {
    try {
      const response = await applyToTeacher(applyId, user.accessToken);

      if (response.success) {
        alert('성공적으로 신청 되었습니다.');
      } else if (response.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
        const newAccessToken = await getNewAccessToken(user.refreshToken);
        if (newAccessToken) {
          setUser({ ...user, accessToken: newAccessToken });
          const newResponse = await applyToTeacher(applyId, newAccessToken);
          if (newResponse.success) {
            alert('성공적으로 신청 되었습니다.');
          } else {
            console.error('Failed to apply:', newResponse.error);
          }
        } else {
          console.error('Failed to refresh token');
        }
      } else {
        console.error('Failed to apply:', response.error);
      }
    } catch (error) {
      console.error(error);
    }
  };

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
              className={`w-full object-contain h-[200px] ${
                idx === currentImage ? 'block' : 'hidden'
              }`}
            />
          ))}
          <button
            className="absolute top-1/2 left-0 px-3 py-1 text-white rounded border-light-blue-500 bg-light-blue-500"
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
            className="absolute top-1/2 right-0 px-3 py-1 text-white rounded border-light-blue-500 bg-light-blue-500"
            onClick={() =>
              setCurrentImage((prev) => (prev + 1) % question.path.length)
            }
          >
            →
          </button>
        </div>
        <div className="flex items-center justify-center w-[70px] h-12 mt-10 border-2 rounded-md shadow-sm border-light-blue-500 mx-auto text-lg">
          {question.state}
        </div>
        <div className="space-y-2 mt-4">
          <p className="text-gray-700 font-bold">닉네임:</p>
          <div className="block w-full h-12 border-2 rounded-md shadow-sm border-light-blue-500">
            {question.userNickname}
          </div>

          <div className="flex space-x-4">
            <div className="w-1/2">
              <p className="text-gray-700 font-bold">과목</p>
              <div className="block w-full h-12 border-2 rounded-md shadow-sm border-light-blue-500">
                {question.subject}
              </div>
            </div>

            <div className="w-1/2">
              <p className="text-gray-700 font-bold">세부과목</p>
              <div className="block w-full h-12 border-2 rounded-md shadow-sm border-light-blue-500">
                {question.subSubject}
              </div>
            </div>
          </div>

          <p className="text-gray-700 font-bold mt-2">Description:</p>
          <textarea
            className="block w-full h-32 border-2 rounded-md shadow-sm border-light-blue-500"
            readOnly
            value={question.description}
          />
        </div>
      </div>
      <div className="w-1/2 p-5">
        <div>
          <h2 className="text-2xl text-center text-bold">
            이 문제에 지원한 선생님 목록입니다.
          </h2>
          <h2 className="text-center">원하시는 선생님을 선택해주세요.</h2>

          <div className="mt-4 flex space-x-4 justify-between">
            <button
              className="w-1/6 h-16 text-sm"
              onClick={() => setSort('Time')}
            >
              예상 대기순
            </button>
            <button
              className="w-1/6 h-16 text-sm"
              onClick={() => setSort('Price')}
            >
              가격순
            </button>
            <button
              className="w-1/6 h-16 text-sm"
              onClick={() => setSort('Rate')}
            >
              평점순
            </button>
            <button
              className="w-1/6 h-16 text-sm"
              onClick={() => setIsFavorite(!isFavorite)}
              style={{
                backgroundColor: isFavorite ? 'grey' : 'blue',
              }}
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
                  className="w-48 h-48 mx-auto rounded-full mt-16"
                />
                <p className="text-center mt-2">{teacher.teacherNickname}</p>
                <p className="text-center">{teacher.teacherSubjectName}</p>
                <p className="text-center">{teacher.solvePoint} SP</p>
                <p className="text-center">{teacher.estimatedTime}분</p>
                <p className="text-center">
                  {(teacher.ratingSum / teacher.ratingCount).toFixed(2)}점
                </p>
                <button
                  className="mt-2 w-full p-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition duration-300"
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
  );
};

export default TeacherQuestionDetail;
