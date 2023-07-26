import React from 'react';
import { useForm, Controller } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import axios from 'axios';
import BackgroundImg from '../../assets/background.png';

const schema = yup.object().shape({
  name: yup.string().required('이름은 필수입니다.'),
  nickname: yup.string().required('닉네임은 필수입니다.'),
  id: yup.string().required('아이디는 필수입니다.'),
  password: yup.string().required('비밀번호는 필수입니다.'),
  passwordConfirm: yup
    .string()
    .oneOf([yup.ref('password'), null], '비밀번호가 일치하지 않습니다.')
    .required('비밀번호 확인은 필수입니다.'),
  email: yup
    .string()
    .email('유효한 이메일이 아닙니다.')
    .required('이메일은 필수입니다.'),
});

function StudentSignup(): JSX.Element {
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
    defaultValues: {
      // defaultValues 추가
      name: '',
      nickname: '',
      id: '',
      password: '',
      passwordConfirm: '',
      email: '',
    },
  });

  //   실제 사용할 api나오면 변경해야함.
  const onSubmit = async (data: any) => {
    try {
      const response = await axios.post('YOUR_API_ENDPOINT', data);
      console.log(response.data);
    } catch (error) {
      console.error(error);
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
      <div className="bg-white rounded-lg shadow-lg w-1/4 h-3/5 py-12 px-10 right-1/2 fixed top-1/4">
        <h2 className="text-2xl font-bold mb-6 text-black">회원가입</h2>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Controller
            control={control}
            name="name"
            render={({ field }) => (
              <input
                {...field}
                placeholder="이름"
                className="block w-full mb-2 border-2 border-gray-200 p-2 rounded-md"
              />
            )}
          />
          <p>{errors.name?.message}</p>

          <Controller
            control={control}
            name="nickname"
            render={({ field }) => (
              <input
                {...field}
                placeholder="닉네임"
                className="block w-full mb-2 border-2 border-gray-200 p-2 rounded-md"
              />
            )}
          />
          <p>{errors.nickname?.message}</p>

          <Controller
            control={control}
            name="id"
            render={({ field }) => (
              <input
                {...field}
                placeholder="아이디"
                className="block w-full mb-2 border-2 border-gray-200 p-2 rounded-md"
              />
            )}
          />
          <p>{errors.id?.message}</p>

          <Controller
            control={control}
            name="password"
            render={({ field }) => (
              <input
                {...field}
                type="password"
                placeholder="비밀번호"
                className="block w-full mb-2 border-2 border-gray-200 p-2 rounded-md"
              />
            )}
          />
          <p>{errors.password?.message}</p>

          <Controller
            control={control}
            name="passwordConfirm"
            render={({ field }) => (
              <input
                {...field}
                type="password"
                placeholder="비밀번호 확인"
                className="block w-full mb-2 border-2 border-gray-200 p-2 rounded-md"
              />
            )}
          />
          <p>{errors.passwordConfirm?.message}</p>

          <Controller
            control={control}
            name="email"
            render={({ field }) => (
              <input
                {...field}
                placeholder="이메일"
                className="block w-full mb-2 border-2 border-gray-200 p-2 rounded-md"
              />
            )}
          />
          <p>{errors.email?.message}</p>

          <button
            type="submit"
            className="w-full bg-blue-500 text-white p-2 rounded-md"
          >
            회원가입
          </button>
        </form>
      </div>
    </div>
  );
}

export default StudentSignup;
