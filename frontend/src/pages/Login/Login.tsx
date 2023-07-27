import React from 'react';
import { useForm, Controller } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import axios from 'axios';
import { useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
import { useNavigate } from 'react-router-dom';
import BackgroundImg from '../../assets/background.png';

const schema = yup.object().shape({
  id: yup.string().required('아이디는 필수입니다.'),
  password: yup.string().required('비밀번호는 필수입니다.'),
});

function Login() {
  const navigate = useNavigate(); // useNavigate 훅을 사용
  const setUser = useSetRecoilState(userState);
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
  });

  const onSubmit = async (data) => {
    try {
      const response = await axios.post('YOUR_LOGIN_API_ENDPOINT', data);
      const user: User = response.data;
      setUser(user);
      if (user.master_code_id === 2) {
        navigate('/teacher');
      } else if (user.master_code_id === 1) {
        navigate('/student');
      }
    } catch (error) {
      console.error(error);
      alert('로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인해주세요.');
    }
  };

  return (
    <div
      className="flex items-center justify-center h-screen"
      style={{
        backgroundImage: `url(${BackgroundImg})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      <div className="bg-white rounded-lg shadow-lg w-1/3 h-1/2 py-12 px-10 right-1/3 fixed top-1/4">
        <h2 className="text-2xl font-bold mb-12 text-black">로그인</h2>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Controller
            control={control}
            name="id"
            defaultValue=""
            render={({ field }) => (
              <input
                {...field}
                placeholder="아이디"
                className="block w-full mb-8 border-2 border-gray-200 p-2 rounded-md"
              />
            )}
          />
          <p>{errors.id?.message}</p>

          <Controller
            control={control}
            name="password"
            defaultValue=""
            render={({ field }) => (
              <input
                {...field}
                type="password"
                placeholder="비밀번호"
                className="block w-full mb-8 border-2 border-gray-200 p-2 rounded-md"
              />
            )}
          />
          <p>{errors.password?.message}</p>

          <button
            type="submit"
            className="w-full bg-blue-500 text-white p-2 rounded-md"
          >
            로그인
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
