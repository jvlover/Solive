import { useParams, useNavigate } from 'react-router-dom';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
import { useState } from 'react';
import { applyToRate, getNewAccessToken, favoriteTeacher } from '../../api';

const TeacherRating = () => {
  const [rating, setRating] = useState<number | null>(null);
  const [showPopup, setShowPopup] = useState(false);
  const navigate = useNavigate();
  const defaultApplyId = 1;
  const { applyId: stringId } = useParams<{ applyId: string }>();
  const applyId = parseInt(stringId || defaultApplyId.toString());
  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const [showFavoritePopup, setShowFavoritePopup] = useState(false);

  const handleStarClick = (rate: number) => {
    setRating(rate);
  };

  const handlePopupConfirm = () => {
    setShowFavoritePopup(true); // 추가됨
  };

  const TeacherRate = async (applyId: number, rating: number) => {
    try {
      const result = await applyToRate(applyId, rating, user.accessToken);
      if (result.success) {
        setShowPopup(true);
      } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
        const newAccessToken = await getNewAccessToken(user.refreshToken);
        if (newAccessToken) {
          setUser({ ...user, accessToken: newAccessToken });
          const newResponse = await applyToRate(
            applyId,
            rating,
            newAccessToken,
          );
          if (newResponse.success) {
            alert('성공적으로 신청 되었습니다.');
          }
        }
      }
    } catch (error) {
      navigate('./error');
    }
  };

  const addTeacherToFavorites = async (applyId: number) => {
    try {
      const result = await favoriteTeacher(applyId, user.accessToken);
      if (result.success) {
        navigate('/student');
      } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
        const newAccessToken = await getNewAccessToken(user.refreshToken);
        if (newAccessToken) {
          setUser({ ...user, accessToken: newAccessToken });
          const newResponse = await favoriteTeacher(applyId, newAccessToken);
          if (newResponse.success) {
            navigate('/student');
          }
        }
      }
    } catch (error) {
      navigate('./error');
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <h1 className="mb-6 text-2xl font-bold">풀이는 만족하셨나요?</h1>
      <h2 className="mb-8 text-lg">방금전 풀이에 대하여 별점을 남겨주세요.</h2>
      <div className="flex mb-8">
        {[1, 2, 3, 4, 5].map((star) => (
          <button
            key={star}
            onClick={() => handleStarClick(star)}
            className={`mr-2 text-4xl ${
              rating && star <= rating ? 'text-yellow-400' : 'text-gray-400'
            }`}
          >
            ★
          </button>
        ))}
      </div>
      <button
        onClick={() => TeacherRate(applyId, rating)}
        className="px-4 py-2 text-white bg-solive-200"
        disabled={rating === null}
      >
        확인
      </button>

      {showPopup && (
        <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center">
          <div className="bg-white p-8 rounded shadow-lg">
            <p className="mb-4">
              별점을 남겨주셔서 감사합니다! 소중한 의견은 더 나은 경험을 위해
              사용됩니다.
            </p>
            <button
              onClick={handlePopupConfirm}
              className="px-4 py-2 text-white bg-solive-200"
            >
              확인
            </button>
          </div>
        </div>
      )}
      {showFavoritePopup && (
        <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center">
          <div className="bg-white p-8 rounded shadow-lg">
            <p className="mb-4">이 강사를 즐겨찾기에 추가하시겠습니까?</p>
            <button
              onClick={() => addTeacherToFavorites(applyId)}
              className="px-4 py-2 mr-4 text-white bg-solive-200"
            >
              네
            </button>
            <button
              onClick={() => navigate('/student')}
              className="px-4 py-2 text-white bg-solive-200"
            >
              아니오
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default TeacherRating;
