import { useState } from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
import { teacherSolvePoint } from '../../api';
import { useNavigate } from 'react-router-dom';
import sp from '../../assets/sp.png';

const TeacherSolve = () => {
  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);
  const [selectedAmount, setSelectedAmount] = useState(0);
  const [selectedIndex, setSelectedIndex] = useState<number | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [isAgreed, setIsAgreed] = useState(false);
  const amounts = [10000, 30000, 50000, 100000];

  const navigate = useNavigate();

  if (!user) {
    navigate('/login');
    return null;
  }

  const handleClick = (amount: number) => {
    setSelectedAmount(amount);
  };

  const Click = () => {
    let currentAmount = user.solvePoint;
    let withdrawalAmount = selectedAmount;
    if (withdrawalAmount > currentAmount && isAgreed) {
      alert('보유 금액이 부족합니다.');
      return;
    } else if (selectedAmount === 0 || !isAgreed) return;
    setShowModal(true);
  };

  const handleWithdraw = async () => {
    if (!user || !user.accessToken) return;

    const result = await teacherSolvePoint(selectedAmount, user.accessToken);

    if (result.success && result.solvePoint !== undefined) {
      setUser({
        ...user,
        solvePoint: result.solvePoint,
      });

      setShowModal(false);
    } else {
      navigate('./error');
    }
  };

  return (
    <div className="pt-4">
      <div className="flex justify-center mt-5 items-start">
        <div className="p-4 flex flex-wrap w-1/2 h-96">
          {amounts.map((amount, index) => (
            <button
              key={index}
              className={`bg-white border-2 border-gray-400 text-black font-bold w-48 h-12 relative ${
                index % 2 === 0 ? 'mr-5' : ''
              }`}
              onClick={() => {
                handleClick(amount);
                setSelectedIndex(index);
              }}
            >
              {amount}원
              <span
                className={`absolute left-4 top-1/2 transform -translate-y-1/2 w-6 h-6 ${
                  selectedIndex === index
                    ? 'border-4 border-solive-200'
                    : 'border-2 border-black'
                } rounded-full bg-white`}
              ></span>
            </button>
          ))}
          <div className="flex items-center relative w-48 h-12">
            <span
              className={`absolute top-1/2 transform -translate-y-1/2 left-3 w-6 h-6 ${
                selectedIndex === amounts.length
                  ? 'border-4 border-solive-200'
                  : 'border-2 border-black'
              } rounded-full bg-white z-10`}
              onClick={() => setSelectedIndex(amounts.length)}
            ></span>
            <input
              type="text"
              placeholder="직접 입력"
              className="block w-full h-12 pl-10 border-2 border-gray-400 p-2 rounded-md"
              onChange={(e) => {
                handleClick(Number(e.target.value));
                setSelectedIndex(amounts.length);
              }}
            />
          </div>
        </div>
        <div className="flex flex-col items-center ml-4">
          <div className="w-96 h-72 p-4 rounded-md border border-gray-200 flex flex-col justify-center items-center">
            <div>
              <p className="mb-2 text-2xl flex items-centes">
                {user?.nickname} 님의 SolvePoint는 {user?.solvePoint}
              </p>
            </div>
            <div className="text-lg w-80 h-48 bg-blue-50 rounded-md flex flex-col justify-center gap-2">
              <p className="flex items-center bg-solve-200 p-2 rounded-md">
                현재 보유 SP: {user?.solvePoint}
                <img src={sp} alt="SolvePoint Icon" className="ml-2 w-6 h-6" />
              </p>
              <p className="flex items-center bg-solve-200 p-2 rounded-md">
                출금 금액: {selectedAmount}
                <img src={sp} alt="SolvePoint Icon" className="ml-2 w-6 h-6" />
              </p>
              <hr className="border-t-2 border-solive-200" />
              <p className="flex items-center bg-solve-200 p-2 rounded-md">
                출금 후 금액: {user?.solvePoint - selectedAmount}
                <img src={sp} alt="SolvePoint Icon" className="ml-2 w-6 h-6" />
              </p>
            </div>
          </div>

          <div className="w-96 h-44 p-4 mt-4 text-lg flex flex-col items-center">
            <label className="flex items-center mb-4">
              <input
                type="checkbox"
                checked={isAgreed}
                onChange={() => setIsAgreed(!isAgreed)}
                className="mr-2"
              />
              내용을 확인하였으며 금액에 동의하겠습니까?
            </label>
            <button
              className="bg-solive-200 text-white font-bold p-2 mt-4 w-full"
              onClick={Click}
            >
              SolvePoint 출금하기
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
            <h3 className="text-center">출금 금액</h3>
            <p className="text-center">{selectedAmount}원</p>
            <button
              className="bg-blue-200 text-white font-bold p-2 mt-4 w-full"
              onClick={handleWithdraw}
            >
              SolvePoint 출금
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default TeacherSolve;
