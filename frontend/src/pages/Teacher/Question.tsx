import { useState, ChangeEvent, useEffect } from 'react';
import { Select, Option as MOption } from '@material-tailwind/react';
import { questionSearch, getNewAccessToken } from '../../api';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
// import que from '../../assets/full_heart.svg';

// 페이지 보려면 아래 한줄 주석하세요.
// 페이지 보려면 아래 주석 해제하세요.

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

  // 페이지 보려면 아래 주석 해제하세요.
  // const questionList = [
  //   {
  //     id: 1,
  //     path_name: que,
  //     title: '기하와 벡터',
  //     subject: '수학',
  //     time: '2023-08-02',
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
  //   {
  //     id: 12,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 13,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 14,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  //   {
  //     id: 15,
  //     path_name: 'path1',
  //     title: 'title1',
  //     subject: 'subject1',
  //     time: 'time1',
  //     matching_state: 1,
  //   },
  // ];

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
    if (pageNum < Math.ceil(questionList.length / itemsPerPage) - 1)
      setPageNum((prev) => prev + 1);
  };

  const getPageNumQuestions = () => {
    const start = pageNum * itemsPerPage;
    const end = start + itemsPerPage;
    return questionList.slice(start, end);
  };

  return (
    <div className="flex flex-col items-center justify-center pt-28">
      <div className="w-full mb-4 text-center">
        <h1 className="text-black text-3xl">전체 문제 목록 보기</h1>
      </div>
      <div className="w-full flex space-x-2 mt-4 justify-center">
        <div className="flex-1 flex items-center space-x-2 justify-center ml-4 ">
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
        <div className="flex-1 flex items-center space-x-2 justify-center mx-2">
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
        <div className="flex-1 flex items-center space-x-2 justify-center mx-2">
          <span>검색어: </span>
          <input
            type="text"
            value={searchKeyword}
            onChange={questionSearchKeywordChange}
            placeholder="검색어 입력"
          />
        </div>
        <div className="flex-initial text-center mx-2">
          <button
            className="px-3 py-2 border rounded text-black border-solive-200"
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
        <div className="flex-initial ml-4 inline-block mx-2">
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
      <div className="flex flex-wrap justify-center w-full mt-4">
        {questionList.length === 0 ? (
          <div className="text-gray-500 text-xl">등록된 문제가 없습니다.</div>
        ) : (
          getPageNumQuestions().map((question) => (
            <div
              key={question.id}
              className="w-1/5 m-2 border-2 border-solive-200 rounded-md h-72"
            >
              <img
                src={question.path_name}
                alt={question.title}
                className="w-full h-52 object-contain"
              />

              <div className="flex p-2 h-20 overflow-hidden items-center space-x-2">
                <div className="border rounded px-2 py-1 border-solive-200">
                  <h2 className="font-bold truncate">{question.title}</h2>
                </div>
                <div className="border rounded px-2 py-1 border-solive-200">
                  <p className="truncate">{question.time.toString()}</p>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
      <div className="mt-4 ">
        <button
          className="bg-solive-200"
          onClick={handlePrevPage}
          disabled={pageNum === 0}
        >
          이전
        </button>
        <button
          className="bg-solive-200"
          onClick={handleNextPage}
          disabled={
            pageNum >= Math.ceil(questionList.length / itemsPerPage) - 1
          }
        >
          다음
        </button>
      </div>
    </div>
  );
};

export default TeacherQuestion;
