import React, { useState, useEffect, ChangeEvent } from 'react';
import axios from 'axios';
import experience from '../../../assets/experience.png';
// import { userState } from '../../../recoil/user/userState';
// import { useRecoilValue } from 'recoil';

const BASE_URL = 'http://localhost:8080'

interface UserProfile {
  pathName: string;
  nickname: string;
  experience: number;
  introduce: string;
}

function ProfilePage() {
  const [userProfile, setUserProfile] = useState<UserProfile>({
    pathName: '',
    nickname: '',
    experience: 0,
    introduce: '',
  });
  const [profileImage, setProfileImage] = useState<File | null>(null); // 이미지 파일 상태
  const [isModified, setIsModified] = useState(false);

  //   const user = useRecoilValue(userState);
  // 나중에 access-token 보내기

  interface ProfileResponse {
    success: boolean;
    data: UserProfile;
  }

  interface UpdateResponse {
    success: boolean;
  }
  useEffect(() => {
    axios
      .get<ProfileResponse>(BASE_URL + '/profile')
      .then((response) => {
        if (response.data.success) {
          setUserProfile(response.data.data);
        } else {
          console.error('Failed to load profile: success is false');
        }
      })
      .catch((error) => {
        console.error('Failed to load profile:', error);
      });
  }, []);

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
          pathName: reader.result,
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

    const profileData = {
      nickname: userProfile.nickname,
      experience: userProfile.experience,
      introduce: userProfile.introduce,
    };

    const profileJSON = JSON.stringify(profileData);

    const formData = new FormData();
    if (profileImage) {
      formData.append('image', profileImage);
    }
    formData.append('profile', profileJSON);

    for (const pair of formData.entries()) {
      console.log(pair[0] + ': ' + pair[1]);
    }

    axios
      .put<UpdateResponse>(BASE_URL + '/profile', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      .then((response) => {
        console.log(formData);
        if (response.data.success) {
          console.log('Profile updated:', response);
          setIsModified(false);
        } else {
          console.error('Failed to update profile: success is false');
        }
      })
      .catch((error) => {
        console.error('Failed to update profile:', error);
      });
  };
  return (
    <div className="pt-20">
      <div>
        <h1 className="ml-36 font-semibold">마이페이지</h1>
      </div>
      <hr className="mt-4 mx-auto w-7/10 border-none h-1 bg-blue-200" />
      <div
        className="mx-auto mt-8 h-[650px] w-[600px] p-6"
        style={{ border: '2px solid #646CFF' }}
      >
        <div className="flex flex-col items-start">
          <h3>프로필 이미지</h3>
          <div className="flex flex-row w-full mt-6">
            <div className="w-1/2 flex items-center">
              <img
                src={userProfile.pathName || ''}
                alt="profile"
                style={{
                  width: '96px',
                  height: '128px',
                  border: userProfile.pathName
                    ? 'transparent'
                    : '2px solid #646CFF',
                }}
              />
            </div>
            <div className="w-1/2">
              <label className="cursor-pointer text-blue-600">
                프로필 이미지 변경
                <input
                  type="file"
                  onChange={handleImageChange}
                  className="hidden"
                />
              </label>
            </div>
          </div>
          <label className="mt-8 w-full">
            닉네임
            <input
              type="text"
              name="nickname"
              value={userProfile.nickname}
              onChange={handleChange}
              className="ml-4 mt-4 border border-gray-300 p-2 rounded w-full"
            />
          </label>
          <div className="mt-4 w-full flex justify-between items-center">
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
          <label className="mt-4 w-full">
            자기소개
            <textarea
              name="introduce"
              value={userProfile.introduce}
              onChange={handleChange}
              className="mt-4 w-full h-48 border border-gray-300 p-2 rounded"
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
        >
          저장하기
        </button>
      </form>
    </div>
  );
}

export default ProfilePage;
