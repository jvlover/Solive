const StarRating = ({ rating }) => {
  // 가장 가까운 정수로 반올림하여 전체 별의 개수를 결정
  const stars = Math.round(rating);
  // 별 배열을 생성
  let starsArr = [];
  for (let i = 0; i < 5; i++) {
    starsArr.push(i < stars ? '★' : '☆');
  }
  return (
    <div className="text-yellow-400">
      {starsArr.map((star, index) => (
        <span key={index}>{star}</span>
      ))}
    </div>
  );
};

export default StarRating;
