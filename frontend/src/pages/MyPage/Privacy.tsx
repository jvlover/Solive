import { useState, useEffect, ChangeEvent, FormEvent } from 'react';
import {
  getNewAccessToken,
  getPrivacy,
  modifyPassword,
  withdrawalUser,
} from '../../api';
import { useRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
import { Card, CardBody } from '@material-tailwind/react';
import { useNavigate } from 'react-router-dom';

const PersonalInfoPage = () => {
  const [user, setUser] = useRecoilState(userState);
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmNewPassword, setConfirmNewPassword] = useState('');
  const [isModified, setIsModified] = useState(false);
  const [email, setEmail] = useState('amco11@naver.com');
  const [signinTime, setSigninTime] = useState('2022.01.25');
  const [showPopup, setShowPopup] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const userPrivacy = async (accessToken: string) => {
      const result = await getPrivacy(accessToken);
      if (result.success) {
        setEmail(result.email);
        setSigninTime(result.signinTime);
      } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
        const newAccessToken = await getNewAccessToken(user.refreshToken);
        if (newAccessToken) {
          setUser({
            ...user,
            accessToken: newAccessToken,
          });
          getPrivacy(newAccessToken);
        }
      } else {
        navigate('/error');
      }
    };
    if (user !== null) {
      userPrivacy(user.accessToken);
    }
  }, [navigate, setUser, user]);

  const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    switch (name) {
      case 'oldPassword':
        setOldPassword(value);
        break;
      case 'newPassword':
        setNewPassword(value);
        break;
      case 'confirmNewPassword':
        setConfirmNewPassword(value);
        break;
      default:
        break;
    }
    setIsModified(true);
  };

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();

    if (newPassword !== confirmNewPassword) {
      alert('새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.');
      return;
    } else if (oldPassword === newPassword) {
      alert('현재 비밀번호와 새 비밀번호가 동일합니다.');
      return;
    }

    const result = await modifyPassword(
      oldPassword,
      newPassword,
      user.accessToken,
    );
    if (result.success) {
      alert('비밀번호가 변경되었습니다.');
      window.location.href = '/mypage/privacy';
    }
  };

  const handleWithdrawal = async () => {
    const result = await withdrawalUser(user.accessToken);
    if (result.success) {
      alert('회원탈퇴가 완료되었습니다.');
      localStorage.removeItem('user');
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      window.location.href = '/';
    }
  };

  return (
    <div>
      <Card className="my-5 min-h-[80vh] w-[80vh] p-6">
        <CardBody>
          <div className="flex items-center justify-between w-full font-bold">
            <div className="flex items-center">이메일</div>
          </div>
          <div className="w-full p-2 mt-4 border border-gray-300 rounded bg-solive-200 bg-opacity-30">
            <div className="flex items-center">{email}</div>
          </div>
          <h2 className="mt-8 font-bold">비밀번호 변경</h2>
          <label className="block w-full mt-4">
            <input
              type="password"
              name="oldPassword"
              placeholder="현재 비밀번호"
              onChange={handleInputChange}
              className="w-full p-2 border border-gray-300 rounded bg-solive-200 bg-opacity-30 "
            />
          </label>
          <label className="block w-full mt-4">
            <input
              type="password"
              name="newPassword"
              placeholder="새 비밀번호"
              onChange={handleInputChange}
              className="w-full p-2 border border-gray-300 rounded bg-solive-200 bg-opacity-30"
            />
          </label>
          <label className="block w-full mt-4">
            <input
              type="password"
              name="confirmNewPassword"
              placeholder="새 비밀번호 확인"
              onChange={handleInputChange}
              className="w-full p-2 border border-gray-300 rounded bg-solive-200 bg-opacity-30 "
            />
          </label>
          <div className="flex items-center justify-between w-full mt-8 font-bold">
            <div className="flex items-center">회원가입일자</div>
          </div>
          <div className="flex items-center justify-between w-full ">
            <div className="w-full p-2 mt-4 border border-gray-300 rounded bg-solive-200 bg-opacity-30">
              {signinTime.replace('T', ' ')}
            </div>
          </div>

          <form onSubmit={handleSubmit} className="flex justify-center">
            <button
              className={`mt-10 px-4 py-2 rounded w-64 h-12 ${
                isModified ? 'bg-solive-200' : 'bg-gray-400'
              }`}
              type="submit"
              disabled={!isModified}
            >
              비밀번호 변경
            </button>
          </form>
        </CardBody>
      </Card>
      <Card className="mx-auto mb-8 h-[100px] w-[80vh] p-6">
        <div className="flex items-center justify-between h-full">
          <p className="font-bold">회원 탈퇴</p>
          <button onClick={() => setShowPopup(true)}>➔</button>
        </div>
      </Card>
      {showPopup && (
        <div className="fixed top-0 left-0 flex items-center justify-center w-full h-full bg-black bg-opacity-40">
          <div className="flex flex-col items-center justify-center mx-auto mt-8 h-[300px] w-[600px] p-6 bg-white rounded-lg border-[1px] border-solive-200">
            <h2 className="font-bold text-center" style={{ fontSize: '24px' }}>
              정말 회원 탈퇴를 하시겠습니까?
            </h2>
            <div className="flex justify-center mt-16 space-x-28">
              <button
                onClick={handleWithdrawal}
                className="w-32 px-4 py-3 text-white rounded bg-solive-200"
              >
                네
              </button>
              <button
                onClick={() => setShowPopup(false)}
                className="w-32 px-4 py-3 bg-gray-400 rounded "
              >
                아니오
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default PersonalInfoPage;
