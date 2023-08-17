import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
import que from '../../assets/404.png';
import { Card, CardHeader, CardBody } from '@material-tailwind/react';
import banner_1 from '../../assets/banner_1.png';
import banner_2 from '../../assets/banner_2.png';
import banner_3 from '../../assets/banner_3.png';
import banner_4 from '../../assets/banner_4.png';
import banner_5 from '../../assets/banner_5.png';
import { getMyRelated, getLatest, getNewAccessToken } from '../../api';

const Teacher = () => {
  const navigate = useNavigate();
  const [currentSlide, setCurrentSlide] = useState(0);
  const [latestQuestionsPage, setLatestQuestionsPage] = useState(0);
  const [relatedQuestionsPage, setRelatedQuestionsPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const banners = [banner_1, banner_2, banner_3, banner_4, banner_5];
  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);

  type Question = {
    questionId: number;
    path: string;
    title: string;
    masterCodeName: string;
    createTime: string;
    userNickname: string;
  };

  const initialQuestion: Question = {
    questionId: -1, // 임의의 값, 보통 -1이나 0을 사용
    path: '', // 빈 문자열
    title: 'Default Title',
    masterCodeName: 'Default Master Code Name',
    createTime: 'Default Create Time',
    userNickname: '', // 임의의 값, 0을 사용
  };

  const [latestQuestions, setLatestQuestions] = useState<Question[]>([
    initialQuestion,
  ]);
  const [relatedQuestions, setRelatedQuestions] = useState<Question[]>([
    initialQuestion,
  ]);

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prevSlide) => (prevSlide + 1) % banners.length);
    }, 3000);

    return () => clearInterval(timer);
  }, [banners.length]);

  useEffect(() => {
    const fetchRelatedQuestions = async () => {
      try {
        let result = await getMyRelated(user.accessToken);
        if (result.success) {
          setRelatedQuestions(result.data);
        } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
          const newAccessToken = await getNewAccessToken(user.refreshToken);
          if (newAccessToken) {
            setUser({ ...user, accessToken: newAccessToken });
            result = await getMyRelated(newAccessToken);
            if (result.success) {
              setRelatedQuestions(result.data);
            } else {
              alert('토큰이 만료되었습니다. 다시 로그인 해주세요');
            }
          } else {
            alert('토큰이 만료되었습니다. 다시 로그인 해주세요');
          }
        } else {
          console.error(result.error);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchRelatedQuestions();
  }, [user, setUser]);

  useEffect(() => {
    const fetchLatestQuestions = async () => {
      try {
        let result = await getLatest(user.accessToken);
        if (result.success) {
          setLatestQuestions(result.data);
        } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
          const newAccessToken = await getNewAccessToken(user.refreshToken);
          if (newAccessToken) {
            setUser({ ...user, accessToken: newAccessToken });
            result = await getLatest(newAccessToken);
            if (result.success) {
              setLatestQuestions(result.data);
            } else {
              alert('토큰이 만료되었습니다. 다시 로그인 해주세요');
            }
          } else {
            alert('토큰이 만료되었습니다. 다시 로그인 해주세요');
          }
        } else {
          console.error(result.error);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchLatestQuestions();
  }, [user, setUser]);

  useEffect(() => {
    if (latestQuestions && relatedQuestions) {
      setIsLoading(false);
    }
  }, [latestQuestions, relatedQuestions]);

  const renderQuestion = (question: Question) => (
    <Card key={question.questionId} className="flex my-5">
      <CardHeader shadow={false} floated={false} className="h-30">
        <img
          src={question.path}
          alt={question.title}
          style={{ width: '100%', aspectRatio: '1/1' }}
        />
      </CardHeader>
      <CardBody>
        <div className="flex items-center">
          <div className="ml-4">
            <div className="text-center">{question.title}</div>
            <br />
            <div className="text-sm leading-tight text-center text-gray-700">
              {new Date(question.createTime).toLocaleString()}
            </div>
          </div>
        </div>
      </CardBody>
    </Card>
  );

  return (
    <div className="flex flex-col items-center">
      {isLoading}
      <div className="relative flex items-center justify-center w-full h-48 mb-8 overflow-hidden text-2xl font-bold bg-solive-200 bg-opacity-30">
        {banners.map((banner, idx) => (
          <div
            key={idx}
            className={`absolute w-full h-full flex items-center justify-center ${
              idx === currentSlide
                ? 'opacity-100 transition-opacity duration-1000'
                : 'opacity-0'
            }`}
          >
            <img
              src={banner}
              alt="{idx}"
              style={{
                width: '100%',
                height: '100%',
              }}
            />
          </div>
        ))}
      </div>
      <div className="flex flex-row w-full">
        <div
          className="flex-col justify-between w-1/2 p-4 text-center border-r-2 border-gray-200"
          style={{ minHeight: '300px' }}
        >
          <div>
            <h2 className="mb-4 text-xl font-bold">최신 문제</h2>
            {latestQuestions?.length > 0 ? (
              <div className="grid grid-cols-3 gap-4">
                {latestQuestions
                  ?.slice(
                    latestQuestionsPage * 3,
                    (latestQuestionsPage + 1) * 3,
                  )
                  .map(renderQuestion)}
              </div>
            ) : (
              <div className="grid grid-cols-3 gap-4">
                <Card className="flex my-5">
                  <CardHeader shadow={false} floated={false} className="h-30">
                    <img
                      src={que}
                      alt="표시할 문제가 없습니다."
                      style={{ width: '100%', aspectRatio: '1/1' }}
                    />
                  </CardHeader>
                  <CardBody>
                    <div>No</div>
                    <div>Question</div>
                  </CardBody>
                </Card>
              </div>
            )}
          </div>
          <div className="text-center">
            {/* Previous button for latestQuestions */}
            <button
              style={{ marginRight: '10px', padding: '4px 8px', width: '70px' }}
              disabled={latestQuestionsPage === 0}
              onClick={() => {
                if (latestQuestionsPage > 0) {
                  setLatestQuestionsPage((prev) => prev - 1);
                }
              }}
            >
              이전
            </button>
            <button
              style={{ marginTop: '10px', padding: '4px 8px', width: '70px' }}
              onClick={() => {
                if ((latestQuestionsPage + 1) * 3 < latestQuestions.length) {
                  setLatestQuestionsPage((prev) => prev + 1);
                }
              }}
            >
              다음
            </button>
          </div>
        </div>
        <div
          className="flex-col justify-between w-1/2 p-4 text-center border-r-2 border-gray-200"
          style={{ minHeight: '300px' }}
        >
          <div>
            <h2 className="mb-4 text-xl font-bold">관련 문제</h2>
            {relatedQuestions?.length > 0 ? (
              <div className="grid grid-cols-3 gap-4">
                {relatedQuestions
                  ?.slice(
                    relatedQuestionsPage * 3,
                    (relatedQuestionsPage + 1) * 3,
                  )
                  .map(renderQuestion)}
              </div>
            ) : (
              <div className="grid grid-cols-3 gap-4">
                <Card className="flex my-5">
                  <CardHeader shadow={false} floated={false} className="h-30">
                    <img
                      src={que}
                      alt="표시할 문제가 없습니다."
                      style={{ width: '100%', aspectRatio: '1/1' }}
                    />
                  </CardHeader>
                  <CardBody>
                    <div>No</div>
                    <div>Question</div>
                  </CardBody>
                </Card>
              </div>
            )}
          </div>
          <div style={{ textAlign: 'center' }}>
            <button
              style={{ marginRight: '10px', padding: '4px 8px', width: '70px' }}
              disabled={relatedQuestionsPage === 0}
              onClick={() => {
                if (relatedQuestionsPage > 0) {
                  setRelatedQuestionsPage((prev) => prev - 1);
                }
              }}
            >
              이전
            </button>
            <button
              style={{ marginTop: '10px', padding: '4px 8px', width: '70px' }}
              onClick={() => {
                if ((relatedQuestionsPage + 1) * 3 < relatedQuestions.length) {
                  setRelatedQuestionsPage((prev) => prev + 1);
                }
              }}
            >
              다음
            </button>
          </div>
        </div>
      </div>
      <button
        className="px-4 py-2 mt-5 mb-5 text-lg font-bold text-white rounded btn-primary"
        onClick={() => navigate('/teacher/question')}
      >
        모든문제 보러가기
      </button>
    </div>
  );
};

export default Teacher;
