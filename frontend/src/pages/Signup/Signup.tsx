import { useForm, Controller } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import BackgroundImg from '../../assets/background.png';
import { useNavigate, useParams } from 'react-router-dom';
import { signup } from '../../api';
import { Input, Radio } from '@material-tailwind/react';

export type SignupFormData = {
  nickname: string;
  loginId: string;
  loginPassword: string;
  passwordConfirm: string;
  email: string;
  gender: number;
};

const schema = yup.object().shape({
  nickname: yup
    .string()
    .required('닉네임은 필수입니다.')
    .matches(
      /^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]{1,10}$/,
      '1~10자의 한글, 영문 대/소문자, 숫자를 사용해주세요.',
    )
    .max(10, '10글자 이하로 입력해주세요.'),
  loginId: yup
    .string()
    .required('아이디는 필수입니다.')
    .matches(/^[a-z0-9]{2,16}$/, '2~16자의 영문 소문자, 숫자를 사용해주세요.')
    .min(2, '2글자 이상 16자 이하로 입력해주세요.')
    .max(16, '2글자 이상 16자 이하로 입력해주세요.'),
  loginPassword: yup
    .string()
    .required('비밀번호는 필수입니다.')
    .matches(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*#?&]).{8,16}$/,
      '8~16자의 영문 대/소문자, 숫자, 특수문자 각 1자 이상 사용해주세요.',
    ),
  passwordConfirm: yup
    .string()
    .oneOf([yup.ref('loginPassword'), ''], '비밀번호가 일치하지 않습니다.')
    .required('비밀번호 확인은 필수입니다.'),
  email: yup
    .string()
    .required('이메일은 필수입니다.')
    .email('유효한 이메일이 아닙니다.'),
  gender: yup.number().required('성별을 선택해주세요.'),
});

const Signup = () => {
  const { userType } = useParams();
  const navigate = useNavigate();
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
    mode: 'onChange',
    defaultValues: {
      nickname: '',
      loginId: '',
      loginPassword: '',
      passwordConfirm: '',
      email: '',
    },
  });

  const onSubmit = async (data: SignupFormData) => {
    const signupData = {
      nickname: data.nickname,
      loginId: data.loginId,
      loginPassword: data.loginPassword,
      passwordConfirm: data.passwordConfirm,
      email: data.email,
      masterCodeId: userType === 'student' ? 1 : 2,
      gender: data.gender,
    };

    try {
      await signup(signupData, () => navigate('/login'));
    } catch (error) {
      alert(error.message);
    }
  };

  return (
    <div
      className="flex items-center justify-center h-[90vh] min-h-[680px]"
      style={{
        backgroundImage: `url(${BackgroundImg})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      <div className="bg-white rounded-lg shadow-lg w-[30%] h-[90%] min-w-[500px] pt-6 pb-10 px-10">
        <h2 className="my-3 text-2xl font-bold text-center text-black">
          {`${userType === 'student' ? '학생' : '선생님'} 회원가입`}
        </h2>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Controller
            control={control}
            name="nickname"
            render={({ field }) => (
              <Input
                {...field}
                variant="standard"
                label="닉네임"
                labelProps={{
                  className:
                    'peer-focus:text-solive-200 after:border-solive-200 peer-focus:after:border-solive-200',
                }}
                className="focus:border-solive-200 "
              />
            )}
          />
          {errors.nickname?.message ? (
            <p className="my-2 text-xs text-blue-gray-400">
              {errors.nickname?.message}
            </p>
          ) : (
            <div className="my-8"></div>
          )}

          <Controller
            control={control}
            name="loginId"
            render={({ field }) => (
              <Input
                {...field}
                variant="standard"
                label="아이디"
                labelProps={{
                  className:
                    'peer-focus:text-solive-200 after:border-solive-200 peer-focus:after:border-solive-200',
                }}
                className="focus:border-solive-200"
              />
            )}
          />
          {errors.loginId?.message ? (
            <p className="my-2 text-xs text-blue-gray-400">
              {errors.loginId?.message}
            </p>
          ) : (
            <div className="my-8"></div>
          )}

          <Controller
            control={control}
            name="loginPassword"
            render={({ field }) => (
              <Input
                {...field}
                type="password"
                variant="standard"
                label="비밀번호"
                labelProps={{
                  className:
                    'peer-focus:text-solive-200 after:border-solive-200 peer-focus:after:border-solive-200',
                }}
                className="focus:border-solive-200"
              />
            )}
          />
          {errors.loginPassword?.message ? (
            <p className="my-2 text-xs text-blue-gray-400">
              {errors.loginPassword?.message}
            </p>
          ) : (
            <div className="my-8"></div>
          )}

          <Controller
            control={control}
            name="passwordConfirm"
            render={({ field }) => (
              <Input
                {...field}
                type="password"
                variant="standard"
                label="비밀번호 확인"
                labelProps={{
                  className:
                    'peer-focus:text-solive-200 after:border-solive-200 peer-focus:after:border-solive-200',
                }}
                className="focus:border-solive-200"
              />
            )}
          />
          {errors.passwordConfirm?.message ? (
            <p className="my-2 text-xs text-blue-gray-400">
              {errors.passwordConfirm?.message}
            </p>
          ) : (
            <div className="my-8"></div>
          )}

          <Controller
            control={control}
            name="email"
            render={({ field }) => (
              <Input
                {...field}
                variant="standard"
                label="이메일"
                labelProps={{
                  className:
                    'peer-focus:text-solive-200 after:border-solive-200 peer-focus:after:border-solive-200',
                }}
                className="focus:border-solive-200"
              />
            )}
          />
          {errors.email?.message ? (
            <p className="my-2 text-xs text-blue-gray-400">
              {errors.email?.message}
            </p>
          ) : (
            <div className="my-8"></div>
          )}

          <Controller
            control={control}
            name="gender"
            render={({ field }) => (
              <div className="flex items-center justify-between">
                <div className="text-sm text-blue-gray-500">성별</div>
                <div className="flex gap-5">
                  <Radio
                    name="gender"
                    label="남성"
                    value={1}
                    checked={field.value === 1}
                    onChange={() => field.onChange(1)}
                    className="before:w-5 before:h-5 hover:before:opacity-0 checked:border-solive-200"
                    containerProps={{
                      className: 'p-0 mx-2 my-1',
                    }}
                    iconProps={{
                      className: 'text-solive-200',
                    }}
                  ></Radio>
                  <Radio
                    name="gender"
                    label="여성"
                    value={2}
                    onChange={() => field.onChange(2)}
                    className="before:w-5 before:h-5 hover:before:opacity-0 checked:border-solive-200"
                    containerProps={{
                      className: 'p-0 mx-2 my-1',
                    }}
                    iconProps={{
                      className: 'text-solive-200',
                    }}
                  ></Radio>
                  <Radio
                    name="gender"
                    label="비공개"
                    value={0}
                    onChange={() => field.onChange(0)}
                    className="before:w-5 before:h-5 hover:before:opacity-0 checked:border-solive-200"
                    containerProps={{
                      className: 'p-0 mx-2 my-1',
                    }}
                    iconProps={{
                      className: 'text-solive-200',
                    }}
                  ></Radio>
                </div>
              </div>
            )}
          />
          {errors.gender?.message ? (
            <p className="mt-2 mb-4 text-xs text-blue-gray-400">
              {errors.gender?.message}
            </p>
          ) : (
            <div className="my-10"></div>
          )}

          <button
            type="submit"
            className="w-full p-2 font-semibold text-white rounded-md bg-solive-200"
          >
            회원가입
          </button>
        </form>
      </div>
    </div>
  );
};

export default Signup;
