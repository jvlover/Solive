const StarRating = ({ rating }) => {
  const stars = Math.round(rating);
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
