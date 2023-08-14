import { useState, useEffect } from 'react';
import { useRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
import { getFavorites, getNewAccessToken } from '../../api';
import StarRating from '../star';

export interface Teacher {
  path: string;
  userNickName: string;
  teacherSubjectName: string;
  ratingSum: number;
  ratingCount: number;
}

const FavoritePage = () => {
  const [user, setUser] = useRecoilState(userState);
  const [favorites, setFavorites] = useState<Teacher[]>([]);
  const [pageIndex, setPageIndex] = useState<number>(0);

  const displayedFavorites = favorites.slice(
    pageIndex * 3,
    (pageIndex + 1) * 3,
  );

  useEffect(() => {
    const fetchFavorites = async () => {
      const result = await getFavorites(user.accessToken);
      if (result.success) {
        setFavorites(result.data);
      } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
        const newAccessToken = await getNewAccessToken(user.refreshToken);
        if (newAccessToken) {
          setUser({
            ...user,
            accessToken: newAccessToken,
          });
          getFavorites(newAccessToken);
        }
      } else {
        console.error('Failed to load favorites:', result.error);
      }
    };
    fetchFavorites();
  }, [setUser, user]);

  return (
    <div className="container mx-auto px-4 mt-12 h-full min-h-[70vh]">
      <div className="grid grid-cols-4 gap-4 relative">
        {displayedFavorites.map((teacher, index) => (
          <div key={index} className="border p-4 flex flex-col items-center">
            <div className="w-full h-52 flex items-center justify-center">
              <img src={teacher.path} alt="teacher" className="object-cover" />
            </div>
            <h2 className="text-lg font-semibold mt-2">
              {teacher.userNickName}
            </h2>
            <p>{teacher.teacherSubjectName}</p>
            <StarRating rating={teacher.ratingSum / teacher.ratingCount} />
          </div>
        ))}
      </div>
      <div className="flex justify-center mt-12">
        <button
          onClick={() => setPageIndex((prev) => prev - 1)}
          className="absolute left-0 top-1/3 transform -translate-x-6"
          disabled={pageIndex === 0}
        >
          ←
        </button>
        <button
          onClick={() => setPageIndex((prev) => prev + 1)}
          className="absolute right-0 top-1/3 transform translate-x-6"
          disabled={(pageIndex + 1) * 3 >= favorites.length}
        >
          →
        </button>
      </div>
    </div>
  );
};

export default FavoritePage;
