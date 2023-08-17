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
      <div className="flex items-start justify-start mt-5">
        <div className="flex flex-wrap w-1/3 p-4 -mt-2 h-96 gap-y-11">
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
              {amount.toLocaleString('ko-KR')}원
              <span
                className={`absolute left-4 top-1/2 transform -translate-y-1/2 w-6 h-6 ${
                  selectedIndex === index
                    ? 'border-4 border-solive-200'
                    : 'border-2 border-black'
                } rounded-full bg-white`}
              ></span>
            </button>
          ))}
          <div className="relative flex items-center w-48 h-12">
            <span
              className={`absolute top-1/2 transform -translate-y-1/2 left-4 w-6 h-6 ${
                selectedIndex === amounts.length
                  ? 'border-4 border-solive-200'
                  : 'border-2 border-black'
              } rounded-full bg-white z-10`}
              onClick={() => setSelectedIndex(amounts.length)}
            ></span>
            <input
              type="text"
              placeholder="직접 입력"
              className="block w-full h-12 p-2 pl-16 border-2 border-gray-400 rounded-md"
              onChange={(e) => {
                handleClick(Number(e.target.value));
                setSelectedIndex(amounts.length);
              }}
            />
          </div>
        </div>
        <div className="flex flex-col items-center">
          <div className="flex flex-col items-center justify-center p-4 border border-gray-200 rounded-md w-96 h-72">
            <div>
              <p className="flex items-center mb-2 text-2xl">
                {user?.nickname} 님의 SolvePoint는 {user?.solvePoint}
                <img src={sp} alt="SolvePoint Icon" className="w-6 h-6 ml-1" />
              </p>
            </div>
            <div className="flex flex-col justify-center h-48 gap-2 text-lg rounded-md w-80 bg-blue-50">
              <p className="flex items-center p-2 rounded-md bg-solve-200">
                현재 보유 SP: {user?.solvePoint.toLocaleString('ko-KR')}
                <img src={sp} alt="SolvePoint Icon" className="w-6 h-6 ml-1" />
              </p>
              <p className="flex items-center p-2 rounded-md bg-solve-200">
                출금 금액: {selectedAmount.toLocaleString('ko-KR')}원
              </p>
              <hr className="border-t-2 border-solive-200" />
              <p className="flex items-center p-2 rounded-md bg-solve-200">
                출금 후 SP:{' '}
                {(user?.solvePoint - selectedAmount).toLocaleString('ko-KR')}
                <img src={sp} alt="SolvePoint Icon" className="w-6 h-6 ml-1" />
              </p>
            </div>
          </div>

          <div className="flex flex-col items-center p-4 mt-4 text-lg w-96 h-44">
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
              className="w-full p-2 mt-4 font-bold text-white bg-solive-200"
              onClick={Click}
            >
              SolvePoint 출금하기
            </button>
          </div>
        </div>
      </div>
      {showModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="relative p-8 bg-white rounded-lg w-80 h-45">
            <button
              className="absolute top-0 right-0 p-2"
              onClick={() => setShowModal(false)}
            >
              X
            </button>
            <h3 className="text-center">출금 금액</h3>
            <p className="text-center">{selectedAmount}원</p>
            <button
              className="w-full p-2 mt-4 font-bold text-white bg-blue-200"
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
