import { useState, ChangeEvent, useEffect } from 'react';
import { Select, Option as MOption } from '@material-tailwind/react';
import { questionSearch, getNewAccessToken } from '../../api';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
import { useNavigate } from 'react-router-dom';

interface Subject {
  label: string;
  value: number;
  subSubjects: Array<{
    label: string;
    value: number;
  }>;
}

const subjects: Subject[] = [
  {
    label: '수학',
    value: 100,
    subSubjects: [
      { label: '수1', value: 10 },
      { label: '수2', value: 20 },
      { label: '확률과 통계', value: 30 },
      { label: '기하', value: 40 },
    ],
  },
  {
    label: '과학',
    value: 200,
    subSubjects: [
      { label: '물리', value: 10 },
      { label: '화학', value: 20 },
      { label: '생명', value: 30 },
      { label: '지구', value: 40 },
    ],
  },
];

const TeacherQuestion = () => {
  const [subjectNum, setSubjectNum] = useState(0);
  const [subSubjectNum, setSubSubjectNum] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [questionList, setQuestionList] = useState([]);
  const [order, setOrder] = useState('TIME_ASC');
  const [pageNum, setPageNum] = useState(0);
  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const itemsPerPage = 8;
  const navigate = useNavigate();

  const handleSubjectChange = (value: string) => {
    setSubjectNum(Number(value));
    setSubSubjectNum(0);
  };

  const handleSubSubjectChange = (value: string) => {
    setSubSubjectNum(Number(value));
  };

  const questionSearchKeywordChange = (
    event: ChangeEvent<HTMLInputElement>,
  ) => {
    setSearchKeyword(event.target.value);
  };

  useEffect(() => {
    const fetchData = async () => {
      if (user) {
        const result = await questionSearch(
          subjectNum,
          subSubjectNum,
          searchKeyword,
          order,
          pageNum,
          user.accessToken,
        );

        if (result.success) {
          setQuestionList(result.data);
        } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
          const newAccessToken = await getNewAccessToken(user.refreshToken);
          if (newAccessToken) {
            setUser({
              ...user,
              accessToken: newAccessToken,
            });
            fetchData();
          }
        } else {
          console.error('Failed to load questions:', result.error);
        }
      }
    };

    fetchData();
  }, [subjectNum, subSubjectNum, searchKeyword, order, pageNum, user, setUser]);

  const handlePrevPage = () => {
    if (pageNum > 0) setPageNum((prev) => prev - 1);
  };

  const handleNextPage = () => {
    setPageNum((prev) => prev + 1);
  };

  const getPageNumQuestions = () => {
    const start = 0;
    const end = 8;
    return questionList.slice(start, end);
  };

  const handleDetailPage = (id) => {
    navigate(`/teacher/question/${id}`);
  };

  return (
    <div className="flex justify-center pt-8">
      <div className="flex flex-col items-center justify-center w-[70vw]">
        <div className="w-full mb-4 text-center">
          <h1 className="text-3xl text-black">전체 문제 목록 보기</h1>
        </div>
        <div className="flex items-center justify-center w-full mt-4 space-x-2">
          <div className="flex-initial inline-block mx-2 ml-4">
            <label>
              <input
                type="radio"
                value="TIME_DESC"
                checked={order === 'TIME_DESC'}
                onChange={(e) => setOrder(e.target.value)}
              />
              최신순
            </label>
            <label className="ml-2">
              <input
                type="radio"
                value="TIME_ASC"
                checked={order === 'TIME_ASC'}
                onChange={(e) => setOrder(e.target.value)}
              />
              오래된순
            </label>
          </div>
          <div className="flex justify-center gap-x-2">
            <Select onChange={handleSubjectChange}>
              {subjects.map((subject) => (
                <MOption key={subject.value} value={subject.value.toString()}>
                  {subject.label}
                </MOption>
              ))}
            </Select>
            <Select onChange={handleSubSubjectChange}>
              {(
                subjects.find((subject) => subject.value === subjectNum)
                  ?.subSubjects || []
              ).map((subSubject) => (
                <MOption
                  key={subSubject.value}
                  value={subSubject.value.toString()}
                >
                  {subSubject.label}
                </MOption>
              ))}
            </Select>
          </div>
          <div className="flex items-center justify-center flex-1 mx-2 space-x-2">
            <input
              type="text"
              value={searchKeyword}
              onChange={questionSearchKeywordChange}
              placeholder="검색어 입력"
              className="w-full py-[0.5rem] px-4 border-2 rounded focus:outline-none"
            />
          </div>
          <div className="flex-initial mx-2 text-center">
            <button
              className="px-5 py-2 btn-primary"
              onClick={() =>
                questionSearch(
                  subjectNum,
                  subSubjectNum,
                  searchKeyword,
                  order,
                  pageNum,
                  user.accessToken,
                )
              }
            >
              검색
            </button>
          </div>
        </div>

        <div className="flex flex-wrap justify-center w-full mt-8">
          {questionList.length === 0 ? (
            <div className="text-xl text-gray-500">등록된 문제가 없습니다.</div>
          ) : (
            getPageNumQuestions().map((question) => (
              <div
                key={question.questionId}
                className="w-1/5 m-2 border-2 rounded-md border-solive-200 border-opacity-50 h-72"
              >
                <img
                  onClick={() => handleDetailPage(question.questionId)}
                  src={question.path}
                  alt={question.title}
                  className="object-contain w-full h-48"
                />

                <div className="flex flex-col items-center h-20 p-2 space-x-2 overflow-hidden">
                  <p className="mt-2 font-bold">{question.title}</p>
                  <p className="mt-1 font-extralight">
                    {question.createTime.replace('T', ' ').slice(0, -7)}
                  </p>
                </div>
              </div>
            ))
          )}
        </div>
        <div className="flex mt-10 mb-8 gap-x-6">
          <button
            className="px-5 btn-primary"
            onClick={handlePrevPage}
            disabled={pageNum === 0}
          >
            이전
          </button>
          <button
            className="px-5 btn-primary"
            onClick={handleNextPage}
            disabled={pageNum >= Math.ceil(questionList.length / itemsPerPage)}
          >
            다음
          </button>
        </div>
      </div>
    </div>
  );
};
export default TeacherQuestion;
