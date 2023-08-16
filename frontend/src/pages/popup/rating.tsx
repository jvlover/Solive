import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const TeacherRating = () => {
  const [rating, setRating] = useState<number | null>(null);
  const [showPopup, setShowPopup] = useState(false);
  const navigate = useNavigate();

  const handleStarClick = (rate: number) => {
    setRating(rate);
  };

  const handleSubmit = () => {
    setShowPopup(true);
  };

  const handlePopupConfirm = () => {
    navigate('/student');
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
        onClick={handleSubmit}
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
    </div>
  );
};

export default TeacherRating;
