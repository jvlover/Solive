import { useState } from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../../recoil/user/userState';
import { chargeSolvePoint } from '../../../api';
import { useNavigate } from 'react-router-dom';

const PointChargePage = () => {
  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const [selectedAmount, setSelectedAmount] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [isAgreed, setIsAgreed] = useState(false);
  const amounts = [10000, 30000, 50000, 100000];

  // 화면 보려면 여기 주석
  const navigate = useNavigate();

  // 화면 보려면 여기 주석
  if (!user) {
    navigate('/login');
    return null;
  }

  const handleClick = (amount: number) => {
    setSelectedAmount(amount);
  };

  const Click = () => {
    if (selectedAmount === 0 || !isAgreed) return;
    setShowModal(true);
  };

  const handleCharge = async () => {
    // 화면 보려면 바로 아래 한줄 주석
    if (!user || !user.accessToken) return;

    const result = await chargeSolvePoint(selectedAmount, user.accessToken);

    if (result.success && result.solvePoint !== undefined) {
      setUser({
        ...user,
        solvePoint: result.solvePoint,
      });

      setShowModal(false);
    } else {
      console.error('Charge failed');
    }
  };

  return (
    <div className="pt-4">
      <div>
        <h1 className="ml-36 font-semibold">마이페이지</h1>
      </div>
      <hr className="mt-4 mx-auto w-7/10 border-none h-1 bg-blue-200" />

      <div className="flex justify-center mt-20 items-start">
        <div
          className="border-4 border-blue-200 p-4 flex flex-wrap justify-center"
          style={{
            width: '800px',
            height: '500px',
            gap: '20px',
            paddingTop: '20px',
          }}
        >
          {amounts.map((amount, index) => (
            <button
              key={index}
              className="bg-white border-2 border-gray-400 text-black font-bold w-56 h-12"
              onClick={() => handleClick(amount)}
            >
              {amount}원
            </button>
          ))}
          <input
            type="text"
            placeholder="직접 입력"
            className="block w-56 h-12 border-2 border-gray-400 p-2 rounded-md"
            onChange={(e) => handleClick(Number(e.target.value))}
          />
        </div>
        <div>
          <div className="ml-4 border-4 border-blue-200 w-96 h-72 p-4">
            <div>
              <p className="mb-2" style={{ fontSize: '28px' }}>
                {user?.nickname} 님의 SolvePoint는 {user?.solvePoint}
              </p>
            </div>
            <div
              className="text-lg border-4 border-blue-200"
              style={{
                width: '340px',
                height: '180px',
                gap: '20px',
                paddingTop: '20px',
              }}
            >
              <p>현재 보유 SP: {user?.solvePoint}</p>
              <p className="mt-4">충전 금액: {selectedAmount}</p>
              <p className="mt-4">
                충전 후 금액: {user?.solvePoint + selectedAmount}
              </p>
            </div>
          </div>
          <div className="ml-4 border-4 border-blue-200 w-96 h-44 p-4 mt-8 text-mg">
            <label className="flex items-center">
              <input
                type="checkbox"
                checked={isAgreed}
                onChange={() => setIsAgreed(!isAgreed)}
                className="mr-2"
              />
              내용을 확인하였으며 금액에 동의하겠습니까?
            </label>
            <button
              className="bg-blue-200 text-white font-bold p-2 mt-4 w-full"
              onClick={Click}
            >
              SolvePoint 충전하기
            </button>
          </div>
        </div>
      </div>
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
          <div className="bg-white p-8 rounded-lg relative w-80 h-45">
            <button
              className="absolute top-0 right-0 p-2"
              onClick={() => setShowModal(false)}
            >
              X
            </button>
            <h3 className="text-center">충전 금액</h3>
            <p className="text-center">{selectedAmount}원</p>
            <button
              className="bg-blue-200 text-white font-bold p-2 mt-4 w-full"
              onClick={handleCharge}
            >
              SolvePoint 충전
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default PointChargePage;
