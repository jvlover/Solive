import { useForm, Controller } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import BackgroundImg from '../../assets/background.png';
import { useNavigate } from 'react-router-dom';
import { studentSignup } from '../../api';

export type StudentFormData = {
  nickname: string;
  loginId: string;
  loginPassword: string;
  passwordConfirm: string;
  email: string;
  gender: number;
};

const schema = yup.object().shape({
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
  gender: yup.number().required('성별을 선택해주세요.'),
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
      nickname: '',
      loginId: '',
      loginPassword: '',
      passwordConfirm: '',
      email: '',
    },
  });

  const onSubmit = async (data: StudentFormData) => {
    const signupData = {
      nickname: data.nickname,
      loginId: data.loginId,
      loginPassword: data.loginPassword,
      passwordConfirm: data.passwordConfirm,
      email: data.email,
      masterCodeId: 1,
      gender: data.gender,
    };

    try {
      await studentSignup(signupData, () => navigate('/login'));
    } catch (error) {
      alert(error.message);
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
          <Controller
            control={control}
            name="gender"
            render={({ field }) => (
              <div>
                <label>
                  <input
                    type="radio"
                    value="1"
                    checked={field.value === 1}
                    onChange={() => field.onChange(1)}
                    className="mr-2"
                  />
                  남자
                </label>
                <label className="ml-4">
                  <input
                    type="radio"
                    value="2"
                    checked={field.value === 2}
                    onChange={() => field.onChange(2)}
                    className="mr-2"
                  />
                  여자
                </label>
              </div>
            )}
          />

          <p>{errors.gender?.message}</p>
          <button
            type="submit"
            className="mt-8 w-full bg-blue-500 text-white p-2 rounded-md"
          >
            회원가입
          </button>
        </form>
      </div>
    </div>
  );
};

export default StudentSignup;
