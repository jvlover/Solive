import { useState, useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { useNavigate } from 'react-router-dom';
import {
  Question,
  // 문제 어떻게 나오는지 보려면 밑에 latest 주석해주세요
  latestQuestionsSelector,
  relatedQuestionsSelector,
} from '../../recoil/question/question';
// 문제 어떻게 나오는지 보려면 밑에 한줄 주석 해제해주세요.
// import que from '../../assets/404.png';

function Teacher() {
  const navigate = useNavigate();
  // 문제 어떻게 나오는지 보려면 밑에 latest 주석해주세요
  const latestQuestions = useRecoilValue(latestQuestionsSelector);
  const relatedQuestions = useRecoilValue(relatedQuestionsSelector);
  const [currentSlide, setCurrentSlide] = useState(0);
  const [latestQuestionsPage, setLatestQuestionsPage] = useState(0);
  const [relatedQuestionsPage, setRelatedQuestionsPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);

  const banners = [1, 2, 3, 4, 5];

  // 문제 어떻게 나오는지 보려면 밑에  const latest 주석 해제해주세요.
  // eslint-disable-next-line react-hooks/exhaustive-deps
  // const latestQuestions = [
  //   {
  //     id: 1,
  //     path_name: que,
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

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prevSlide) => (prevSlide + 1) % banners.length);
    }, 3000);

    return () => clearInterval(timer);
  }, [banners.length]);

  useEffect(() => {
    if (latestQuestions && relatedQuestions) {
      setIsLoading(false);
    }
  }, [latestQuestions, relatedQuestions]);

  const renderQuestion = (question: Question) => (
    <div className="border-2 border-blue-200 p-2 flex flex-col items-center m-2">
      <img
        src={question.path_name}
        alt={question.title}
        style={{ width: '100%', aspectRatio: '1/1' }}
      />
      <div>{question.title}</div>
      <div>{new Date(question.time).toLocaleString()}</div>
    </div>
  );

  return (
    <div className="flex flex-col items-center">
      {isLoading}
      <div className="w-full overflow-hidden h-48 flex items-center justify-center bg-gray-200 text-2xl font-bold mb-8 relative">
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
        <div
          className="w-1/2 p-4 text-center border-r-2 border-gray-200 flex flex-col justify-between"
          style={{ minHeight: '550px' }}
        >
          <div>
            <h2 className="text-xl font-bold mb-4">최신 문제</h2>
            <div
              style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)' }}
            >
              {latestQuestions
                ?.slice(latestQuestionsPage * 3, (latestQuestionsPage + 1) * 3)
                .map(renderQuestion)}
            </div>
          </div>
          <div style={{ textAlign: 'center' }}>
            {/* Previous button for latestQuestions */}
            <button
              style={{ marginRight: '10px', padding: '4px 8px', width: '70px' }}
              disabled={latestQuestionsPage === 0}
              onClick={() => setLatestQuestionsPage((prev) => prev - 1)}
            >
              이전
            </button>
            <button
              style={{ marginTop: '10px', padding: '4px 8px', width: '70px' }}
              onClick={() => setLatestQuestionsPage((prev) => prev + 1)}
            >
              다음
            </button>
          </div>
        </div>
        <div
          className="w-1/2 p-4 text-center flex flex-col justify-between"
          style={{ minHeight: '550px' }}
        >
          <div>
            <h2 className="text-xl font-bold mb-4">관련 문제</h2>
            <div
              style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)' }}
            >
              {relatedQuestions
                ?.slice(
                  relatedQuestionsPage * 3,
                  (relatedQuestionsPage + 1) * 3,
                )
                .map(renderQuestion)}
            </div>
          </div>
          <div style={{ textAlign: 'center' }}>
            <button
              style={{ marginRight: '10px', padding: '4px 8px', width: '70px' }}
              disabled={relatedQuestionsPage === 0}
              onClick={() => setRelatedQuestionsPage((prev) => prev - 1)}
            >
              이전
            </button>
            <button
              style={{ marginTop: '10px', padding: '4px 8px', width: '70px' }}
              onClick={() => setRelatedQuestionsPage((prev) => prev + 1)}
            >
              다음
            </button>
          </div>
        </div>
      </div>
      <button
        className="mb-100 py-2 px-4 mt-20 bg-blue-500 text-white text-lg font-bold rounded"
        onClick={() => navigate('/teacher/question')}
      >
        모든문제 보러가기
      </button>
    </div>
  );
}

export default Teacher;
