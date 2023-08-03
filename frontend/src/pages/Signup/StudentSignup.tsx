import { useForm, Controller } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import axios from 'axios';
import BackgroundImg from '../../assets/background.png';
import { useNavigate } from 'react-router-dom';

type FormData = {
  name: string;
  nickname: string;
  loginId: string;
  loginPassword: string;
  passwordConfirm: string;
  email: string;
};

const schema = yup.object().shape({
  name: yup.string().required('이름은 필수입니다.'),
  nickname: yup.string().required('닉네임은 필수입니다.'),
  loginId: yup.string().required('아이디는 필수입니다.'),
  loginPassword: yup.string().required('비밀번호는 필수입니다.'),
  passwordConfirm: yup
    .string()
    .oneOf([yup.ref('loginPassword'), ''], '비밀번호가 일치하지 않습니다.')
    .required('비밀번호 확인은 필수입니다.'),
  email: yup
    .string()
    .email('유효한 이메일이 아닙니다.')
    .required('이메일은 필수입니다.'),
});

const StudentSignup = () => {
  const navigate = useNavigate();
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
    defaultValues: {
      name: '',
      nickname: '',
      loginId: '',
      loginPassword: '',
      passwordConfirm: '',
      email: '',
    },
  });

  const onSubmit = async (data: FormData) => {
    const signupData = {
      name: data.name,
      nickname: data.nickname,
      loginId: data.loginId,
      loginPassword: data.loginPassword,
      email: data.email,
    };

    try {
      const response = await axios.post('/user', signupData);
      console.log(response.data);

      if (response.data.success === true) {
        navigate('/login');
      }
    } catch (error) {
      console.error(error);

      alert('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
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
            name="loginId"
            render={({ field }) => (
              <input
                {...field}
                placeholder="아이디"
                className="block w-full mb-2 border-2 border-gray-200 p-2 rounded-md"
              />
            )}
          />
          <p>{errors.loginId?.message}</p>

          <Controller
            control={control}
            name="loginPassword"
            render={({ field }) => (
              <input
                {...field}
                type="password"
                placeholder="비밀번호"
                className="block w-full mb-2 border-2 border-gray-200 p-2 rounded-md"
              />
            )}
          />
          <p>{errors.loginPassword?.message}</p>

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
};

export default StudentSignup;
