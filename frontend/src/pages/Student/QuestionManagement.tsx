import { useState, useEffect, ChangeEvent } from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
import { getMyProblems, getNewAccessToken } from '../../api';
import { Select, Option as MOption } from '@material-tailwind/react';
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

const QuestionManagement = () => {
  const [subjectNum, setSubjectNum] = useState(0);
  const [subSubjectNum, setSubSubjectNum] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [order, setOrder] = useState('TIME_ASC');
  const [pageNum, setPageNum] = useState(0);
  const [matchingState, setMatchingState] = useState(0);

  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const [problems, setProblems] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const getProblems = async (): Promise<void> => {
      if (!user) {
        alert('로그인이 필요합니다!');
        navigate('./login');
        return;
      }

      const result = await getMyProblems(
        user.masterCodeId,
        subjectNum,
        subSubjectNum,
        matchingState,
        searchKeyword,
        order,
        pageNum,
        user.accessToken,
      );

      if (result.success) {
        setProblems(result.data);
      } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
        const newAccessToken = await getNewAccessToken(user.refreshToken);
        if (newAccessToken) {
          setUser({
            ...user,
            accessToken: newAccessToken,
          });
          getProblems();
        }
      } else {
        navigate('./error');
      }
    };

    getProblems();
  }, [
    matchingState,
    navigate,
    order,
    pageNum,
    searchKeyword,
    setUser,
    subSubjectNum,
    subjectNum,
    user,
  ]);

  const matchingStateToString = (state) => {
    if (typeof state === 'string') state = Number(state);
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

  const handleSubjectChange = (value: string) => {
    setSubjectNum(Number(value));
    setSubSubjectNum(0);
  };

  const handleSubSubjectChange = (value: string) => {
    setSubSubjectNum(Number(value));
  };

  const handleMatchingStateChange = (value: string) => {
    setMatchingState(Number(value));
  };

  const questionSearchKeywordChange = (
    event: ChangeEvent<HTMLInputElement>,
  ) => {
    setSearchKeyword(event.target.value);
  };

  const handlePrevPage = () => {
    if (pageNum > 0) setPageNum((prev) => prev - 1);
  };

  const handleNextPage = () => {
    if (pageNum < Math.ceil(problems.length / 8) - 1)
      setPageNum((prev) => prev + 1);
  };

  const handleDetailPage = (questionId: number) => {
    navigate(`/student/question/${questionId}`);
  };

  return (
    <div className="pt-4">
      <div className="mx-auto mt-8 mb-8 h-[650px] w-[800px] p-6 border-2 border-solive-200">
        <p className="text-[18px] font-bold">내가 등록한 문제</p>
        <p className="text-[12px] mt-4">
          저장된 강의는 7일동안 다시 볼 수 있습니다.
        </p>
        <div className="flex justify-center w-full mt-4 space-x-2">
          <div className="flex items-center justify-center flex-1 ml-4 space-x-2">
            <span>과목 선택: </span>
            <Select
              color="blue"
              size="md"
              onChange={handleSubjectChange}
              placeholder="과목 선택"
            >
              {subjects.map((subject) => (
                <MOption key={subject.value} value={subject.value.toString()}>
                  {subject.label}
                </MOption>
              ))}
            </Select>
          </div>
          <div className="flex items-center justify-center flex-1 mx-2 space-x-2">
            <span>세부 과목 선택: </span>
            <Select
              color="blue"
              size="md"
              onChange={handleSubSubjectChange}
              placeholder="세부 과목 선택"
            >
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
            <span>매칭 여부 선택: </span>
            <Select
              color="blue"
              size="md"
              onChange={handleMatchingStateChange}
              placeholder="매칭 여부 선택"
            >
              <MOption key={0} value={'0'}>
                {'등록됨'}
              </MOption>
              <MOption key={1} value={'1'}>
                {'요청됨'}
              </MOption>
              <MOption key={2} value={'2'}>
                {'완료됨'}
              </MOption>
            </Select>
          </div>
          <div className="flex items-center justify-center flex-1 mx-2 space-x-2">
            <span>검색어: </span>
            <input
              type="text"
              value={searchKeyword}
              onChange={questionSearchKeywordChange}
              placeholder="검색어 입력"
            />
          </div>
          <div className="flex-initial mx-2 text-center">
            <button
              className="px-3 py-2 text-black border border-black rounded"
              onClick={() =>
                getMyProblems(
                  user.masterCodeId,
                  subjectNum,
                  subSubjectNum,
                  matchingState,
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
          <div className="flex-initial inline-block mx-2 ml-4">
            <label>
              <input
                type="radio"
                value="TIME_ASC"
                checked={order === 'TIME_ASC'}
                onChange={(e) => setOrder(e.target.value)}
              />
              최신순
            </label>
            <label className="ml-2">
              <input
                type="radio"
                value="TIME_DESC"
                checked={order === 'TIME_DESC'}
                onChange={(e) => setOrder(e.target.value)}
              />
              오래된순
            </label>
          </div>
        </div>
        <div className="grid grid-cols-3 gap-4 pt-12">
          {problems.map((problem) => (
            <div
              key={problem.questionId}
              className="flex flex-col items-center p-2 border-2 border-solive-200"
            >
              <img
                onClick={() => handleDetailPage(problem.questionId)}
                className="h-[140px] w-[250px]"
                src={problem.path}
                alt="problem"
              />
              <span>
                {problem.title} {matchingStateToString(problem.matchingState)}
              </span>
            </div>
          ))}
        </div>
        <div className="flex justify-between pt-8">
          <button onClick={handlePrevPage} disabled={pageNum === 0}>
            이전
          </button>
          <button
            onClick={handleNextPage}
            disabled={pageNum >= Math.ceil(problems.length / 8) - 1}
          >
            다음
          </button>
        </div>
      </div>
    </div>
  );
};

export default QuestionManagement;
