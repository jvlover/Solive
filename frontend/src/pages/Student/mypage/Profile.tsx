import React, { useState, useEffect, ChangeEvent, useRef } from 'react';
import experience from '../../../assets/experience.png';
import { getNewAccessToken, getProfile, modifyProfile } from '../../../api';
import { useRecoilState } from 'recoil';
import { userState } from '../../../recoil/user/userState';
import DefaultProfile from '../../../assets/default_profile_image.svg';
import { Avatar, Card, CardBody, Radio } from '@material-tailwind/react';
// import { userState } from '../../../recoil/user/userState';
// import { useRecoilValue } from 'recoil';

export interface UserProfile {
  path: string;
  nickname: string;
  experience: number;
  introduce: string;
  gender: number;
  questionCount: number | null;
  solvedCount: number | null;
  ratingSum: number | null;
  ratingCount: number | null;
}

const ProfilePage = () => {
  const [user, setUser] = useRecoilState(userState);
  const [userProfile, setUserProfile] = useState<UserProfile>({
    path: '',
    nickname: '',
    experience: 0,
    introduce: '',
    gender: 0,
    questionCount: 0,
    solvedCount: 0,
    ratingSum: 0,
    ratingCount: 0,
  });
  const [profileImage, setProfileImage] = useState<File | null>(null); // 이미지 파일 상태
  const [profileImageName, setProfileImageName] = useState<string>('');
  const [isModified, setIsModified] = useState(false);

  const imageInput = useRef<HTMLInputElement>();

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
    setProfileImageName(file.name);
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
      userProfile.gender,
      profileImage,
      user.accessToken,
    );

    setIsModified(false);
  };
  return (
    <div className="flex justify-center min-h-full min-w-fit">
      <Card className="flex my-5 w-[80vh] min-w-[600px] min-h-[80vh]">
        <CardBody>
          <div className="p-6 mx-auto">
            <div className="flex flex-col items-start">
              <p className="font-bold">프로필 이미지</p>
              <div className="flex flex-row w-full mt-3">
                <div className="flex items-center mr-10">
                  {/* <img
                    src={userProfile.path || ''}
                    alt="profile"
                    className={`w-[96px] h-[128px] ${
                      userProfile.path
                        ? 'border-transparent'
                        : 'border-2 border-solid border-solive-200'
                    }`}
                  /> */}
                  <Avatar
                    src={userProfile.path || DefaultProfile}
                    className="w-[100px] h-[100px] bg-solive-200 bg-opacity-30"
                  ></Avatar>
                </div>
                <div>
                  <p className="ml-3">프로필 이미지 변경</p>
                  <div className="flex items-center justify-start mt-3">
                    <input
                      className="w-full p-2 border border-gray-300 rounded bg-opacity-30 bg-solive-200 min-w-[200px] min-h-[42px]"
                      value={
                        profileImageName
                          ? profileImageName
                          : '현재 등록된 사진이 없습니다.'
                      }
                      disabled
                    ></input>
                    <input
                      type="file"
                      onChange={handleImageChange}
                      ref={imageInput}
                      className="hidden"
                    />
                    <button
                      className="ml-5 btn-primary px-0 w-[120px] h-[42px] flex items-center justify-center"
                      onClick={() => imageInput.current.click()}
                    >
                      파일 찾기
                    </button>
                  </div>
                </div>
              </div>
              <label className="w-full mt-8 font-bold">
                닉네임
                <input
                  type="text"
                  name="nickname"
                  value={userProfile.nickname}
                  onChange={handleChange}
                  className="w-full p-2 mt-4 border border-gray-300 rounded bg-opacity-30 bg-solive-200"
                />
              </label>
              <div className="flex items-center justify-between w-full mt-8">
                <div className="font-bold">성별</div>
                <div className="flex gap-5">
                  <Radio
                    name="gender"
                    label="남성"
                    value={1}
                    checked={userProfile.gender == 1}
                    onChange={handleChange}
                    className="before:w-5 before:h-5 hover:before:opacity-0 checked:border-solive-200"
                    containerProps={{
                      className: 'p-0 mx-2 my-1',
                    }}
                    iconProps={{
                      className: 'text-solive-200',
                    }}
                  ></Radio>
                  <Radio
                    name="gender"
                    label="여성"
                    value={2}
                    checked={userProfile.gender == 2}
                    onChange={handleChange}
                    className="before:w-5 before:h-5 hover:before:opacity-0 checked:border-solive-200"
                    containerProps={{
                      className: 'p-0 mx-2 my-1',
                    }}
                    iconProps={{
                      className: 'text-solive-200',
                    }}
                  ></Radio>
                  <Radio
                    name="gender"
                    label="비공개"
                    value={0}
                    checked={userProfile.gender == 0}
                    onChange={handleChange}
                    className="before:w-5 before:h-5 hover:before:opacity-0 checked:border-solive-200"
                    containerProps={{
                      className: 'p-0 mx-2 my-1',
                    }}
                    iconProps={{
                      className: 'text-solive-200',
                    }}
                  ></Radio>
                </div>
              </div>
              <div className="flex items-center justify-between w-full mt-8">
                <div className="flex items-center font-bold">경험치</div>
                <div className="flex items-center">
                  {userProfile.experience}
                  <img
                    src={experience}
                    alt="experience"
                    className="w-5 h-5 ml-2"
                  />
                </div>
              </div>
              {user.masterCodeId === 1 ? (
                <div className="flex items-center justify-between w-full mt-8">
                  <div className="font-bold">등록한 문제 수</div>
                  <div>{`${userProfile.questionCount} 개`}</div>
                </div>
              ) : (
                <div>
                  <div className="flex items-center justify-between w-full mt-8">
                    <div className="font-bold">푼 문제 수</div>
                    <div>{`${userProfile.solvedCount} 개`}</div>
                  </div>
                  <div className="flex items-center justify-between w-full mt-8">
                    <div className="font-bold">평점</div>
                    <div>{`${
                      userProfile.ratingSum / userProfile.ratingCount
                    }`}</div>
                  </div>
                </div>
              )}
              <label className="w-full mt-8 font-bold">
                자기소개
                <textarea
                  name="introduce"
                  value={userProfile.introduce ? userProfile.introduce : ''}
                  onChange={handleChange}
                  className="w-full h-48 p-2 mt-4 border border-gray-300 rounded resize-none bg-solive-200 bg-opacity-30"
                />
              </label>
            </div>
          </div>
          <form onSubmit={handleSubmit} className="flex justify-center">
            <button
              className={`mt-5 px-2 py-2 rounded w-2/3 h-12 text-blue-gray-800 ${
                isModified ? 'bg-solive-200' : 'bg-gray-400'
              }`}
              type="submit"
              disabled={!isModified}
            >
              저장하기
            </button>
          </form>
        </CardBody>
      </Card>
    </div>
  );
};

export default ProfilePage;
