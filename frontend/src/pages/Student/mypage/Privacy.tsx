import { useState, useEffect, ChangeEvent, FormEvent } from 'react';
import axios from 'axios';

const PersonalInfoPage = () => {
  //   const [email, setEmail] = useState('');
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmNewPassword, setConfirmNewPassword] = useState('');
  //   const [signinTime, setsigninTime] = useState('');
  const [isModified, setIsModified] = useState(false);
  const [email, setEmail] = useState('amco11@naver.com');
  const [signinTime, setsigninTime] = useState('2022.01.25');
  const [showPopup, setShowPopup] = useState(false);
  const [withdrawalPassword, setWithdrawalPassword] = useState('');

  useEffect(() => {
    axios
      .get('/user/privacy')
      .then((response) => {
        if (response.data.success) {
          setEmail(response.data.data.email);
          setsigninTime(response.data.data.signinTime);
        } else {
          console.error('Failed to load personal info: success is false');
        }
      })
      .catch((error) => {
        console.error('Failed to load personal info:', error);
      });
  }, []);

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

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault();

    if (newPassword !== confirmNewPassword) {
      alert('새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.');
      return;
    } else if (oldPassword === newPassword) {
      alert('현재 비밀번호와 새 비밀번호가 동일합니다.');
      return;
    }

    const dataObject = {
      oldPassword: oldPassword,
      newPassword: newPassword,
    };
    const blobData = new Blob([JSON.stringify(dataObject)], {
      type: 'application/json',
    });

    axios
      .post('/user/privacy', blobData, {
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .then((response) => {
        if (response.data.success) {
          console.log('비밀번호가 성공적으로 변경되었습니다.');
          setIsModified(false);
        } else {
          console.error('비밀번호 변경 실패: success is false');
        }
      })
      .catch((error) => {
        console.error('비밀번호 변경 실패:', error);
      });
  };

  const handleWithdrawalPasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    setWithdrawalPassword(e.target.value);
  };

  const handleWithdrawal = () => {
    axios
      .post('/user/privacy', { password: withdrawalPassword })
      .then((response) => {
        if (response.data.success) {
          alert('회원 탈퇴가 성공적으로 완료되었습니다.');
        } else {
          alert('비밀번호가 틀렸습니다. 다시 시도해주세요.');
        }
      })
      .catch((error) => {
        console.error('회원 탈퇴 실패:', error);
      });
  };

  return (
    <div className="pt-20">
      <div>
        <h1 className="ml-36 font-semibold">마이 페이지</h1>
      </div>
      <hr className="mt-4 mx-auto w-7/10 border-none h-1 bg-blue-200" />
      <div
        className="mx-auto mt-8 h-[550px] w-[600px] p-6"
        style={{ border: '2px solid #646CFF' }}
      >
        <div className="mt-4 w-full flex justify-between items-center font-bold">
          <div className="flex items-center">이메일</div>
        </div>
        <div className="mt-4 border border-gray-300 p-2 rounded w-full bg-blue-200">
          <div className="flex items-center">{email}</div>
        </div>
        <h2 className="mt-4 font-bold">비밀번호 변경</h2>
        <label className="block mt-4 w-full">
          <input
            type="password"
            name="oldPassword"
            placeholder="현재 비밀번호"
            onChange={handleInputChange}
            className=" border border-gray-300 p-2 rounded w-full bg-blue-200"
          />
        </label>
        <label className="block mt-4 w-full">
          <input
            type="password"
            name="newPassword"
            placeholder="새 비밀번호"
            onChange={handleInputChange}
            className="border border-gray-300 p-2 rounded w-full bg-blue-200"
          />
        </label>
        <label className="block mt-4 w-full">
          <input
            type="password"
            name="confirmNewPassword"
            placeholder="새 비밀번호 재입력"
            onChange={handleInputChange}
            className=" border border-gray-300 p-2 rounded w-full bg-blue-200"
          />
        </label>
        <div className="mt-4 w-full flex justify-between items-center font-bold">
          <div className="flex items-center">회원가입일자</div>
        </div>
        <div className=" w-full flex justify-between items-center">
          <div className="mt-4 border border-gray-300 p-2 rounded w-full bg-blue-200">
            {signinTime}
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
      <div
        className="mx-auto mt-8 h-[100px] w-[600px] p-6"
        style={{ border: '2px solid #646CFF' }}
      >
        <div
          style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            height: '100%',
          }}
        >
          <p className="font-bold">회원 탈퇴</p>
          <button onClick={() => setShowPopup(true)}>➔</button>
        </div>
      </div>
      {showPopup && (
        <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-black bg-opacity-40">
          <div
            className="mx-auto mt-8 h-[450px] w-[800px] p-6 bg-white rounded-lg "
            style={{ border: '2px solid #646CFF' }}
          >
            <h2 className="text-center font-bold" style={{ fontSize: '24px' }}>
              정말 회원 탈퇴를 하시겠습니까?
            </h2>
            <p className="mt-16 text-center">
              회원 탈퇴를 위해 비밀번호를 입력해주세요.
            </p>
            <div className="text-center mt-4">
              <input
                type="password"
                onChange={handleWithdrawalPasswordChange}
                className="mt-6 border border-gray-300 p-2 rounded w-3/5"
              />
            </div>
            <div className="mt-16 flex justify-center space-x-60">
              <button
                onClick={handleWithdrawal}
                className="px-4 py-3 rounded bg-blue-600 text-white w-32"
              >
                네
              </button>
              <button
                onClick={() => setShowPopup(false)}
                className="px-4 py-3 rounded bg-gray-400 w-32 "
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
