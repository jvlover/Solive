import React, { useState, useEffect, ChangeEvent } from 'react';
import experience from '../../../assets/experience.png';
import { getNewAccessToken, getProfile, modifyProfile } from '../../../api';
import { useRecoilState } from 'recoil';
import { userState } from '../../../recoil/user/userState';
// import { userState } from '../../../recoil/user/userState';
// import { useRecoilValue } from 'recoil';

export interface UserProfile {
  path: string;
  nickname: string;
  experience: number;
  introduce: string;
}

const ProfilePage = () => {
  const [user, setUser] = useRecoilState(userState);
  const [userProfile, setUserProfile] = useState<UserProfile>({
    path: '',
    nickname: '',
    experience: 0,
    introduce: '',
  });
  const [profileImage, setProfileImage] = useState<File | null>(null); // 이미지 파일 상태
  const [isModified, setIsModified] = useState(false);

  useEffect(() => {
    const userProfile = async (accessToken: string): Promise<void> => {
      const result = await getProfile(accessToken);
      if (result.success) {
        setUserProfile(result.data);
      } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
        const newAccessToken = await getNewAccessToken(user.refreshToken);
        if (newAccessToken) {
          setUser({
            ...user,
            accessToken: newAccessToken,
          });
          getProfile(newAccessToken);
        }
      } else {
        console.error('Failed to load userProfile:', result.error);
      }
    };
    if (user !== null) {
      userProfile(user.accessToken);
    }
  }, [setUser, user]);

  const handleChange = (
    event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
    const { name, value } = event.target;
    setUserProfile({
      ...userProfile,
      [name]: value,
    });
    setIsModified(true);
  };

  const handleImageChange = (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return; // 파일이 없는 경우 함수를 종료

    const reader = new FileReader();
    reader.onloadend = () => {
      if (typeof reader.result === 'string') {
        setUserProfile({
          ...userProfile,
          path: reader.result,
        });
      }
      setProfileImage(file);
      setIsModified(true);
    };
    reader.readAsDataURL(file);
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!isModified) {
      return;
    }

    modifyProfile(
      userProfile.nickname,
      userProfile.experience,
      userProfile.introduce,
      profileImage,
    );

    setIsModified(false);
  };
  return (
    <div className="pt-20">
      <div>
        <h1 className="font-semibold ml-36">마이페이지</h1>
      </div>
      <hr className="h-1 mx-auto mt-4 bg-blue-200 border-none w-7/10" />
      <div
        className="mx-auto mt-8 h-[650px] w-[600px] p-6"
        style={{ border: '2px solid #646CFF' }}
      >
        <div className="flex flex-col items-start">
          <h3>프로필 이미지</h3>
          <div className="flex flex-row w-full mt-6">
            <div className="flex items-center w-1/2">
              <img
                src={userProfile.path || ''}
                alt="profile"
                style={{
                  width: '96px',
                  height: '128px',
                  border: userProfile.path
                    ? 'transparent'
                    : '2px solid #646CFF',
                }}
              />
            </div>
            <div className="w-1/2">
              <label className="text-blue-600 cursor-pointer">
                프로필 이미지 변경
                <input
                  type="file"
                  onChange={handleImageChange}
                  className="hidden"
                />
              </label>
            </div>
          </div>
          <label className="w-full mt-8">
            닉네임
            <input
              type="text"
              name="nickname"
              value={userProfile.nickname}
              onChange={handleChange}
              className="w-full p-2 mt-4 ml-4 border border-gray-300 rounded"
            />
          </label>
          <div className="flex items-center justify-between w-full mt-4">
            <div className="flex items-center">경험치</div>
            <div className="flex items-center">
              {userProfile.experience}
              <img
                src={experience}
                alt="experience"
                className="w-10 h-10 ml-2"
              />
            </div>
          </div>
          <label className="w-full mt-4">
            자기소개
            <textarea
              name="introduce"
              value={userProfile.introduce ? userProfile.introduce : ''}
              onChange={handleChange}
              className="w-full h-48 p-2 mt-4 border border-gray-300 rounded"
            />
          </label>
        </div>
      </div>
      <form onSubmit={handleSubmit} className="flex justify-center">
        <button
          className={`mt-8 px-4 py-2 rounded w-64 h-12 ${
            isModified ? 'bg-blue-600' : 'bg-gray-400'
          }`}
          type="submit"
          disabled={!isModified}
        >
          저장하기
        </button>
      </form>
    </div>
  );
};

export default ProfilePage;
