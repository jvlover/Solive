import { useForm, Controller } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { useSetRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
import { useNavigate } from 'react-router-dom';
import { loginUser } from '../../api';
import BackgroundImg from '../../assets/background.png';
import logo from '../../assets/logo.png';
import { Input } from '@material-tailwind/react';

const schema = yup.object().shape({
  loginId: yup.string().required('아이디를 입력해주세요.'),
  loginPassword: yup.string().required('비밀번호를 입력해주세요.'),
});

interface LoginFormFields {
  loginId: string;
  loginPassword: string;
}

const Login = () => {
  const navigate = useNavigate();
  const setUser = useSetRecoilState(userState);

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
  });

  const onSubmit = async (data: LoginFormFields) => {
    try {
      const user = await loginUser(data);
      setUser(user);
      if (user.masterCodeId === 1) {
        navigate('/student');
      } else if (user.masterCodeId === 2) {
        navigate('/teacher');
      }
    } catch (error) {
      console.error(error);
      alert(error.message);
    }
  };

  return (
    <div
      className="flex items-center justify-center h-[90vh] min-h-[400px]"
      style={{
        backgroundImage: `url(${BackgroundImg})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      <div className="bg-white rounded-lg shadow-lg w-[30%] h-[60%] min-w-[500px] min-h-[400px] pt-6 pb-10 px-10 mt-0">
        <div className="flex justify-center">
          <img src={logo} alt="solive" className="w-40 h-auto mt-1 mb-8"></img>
        </div>
        <form onSubmit={handleSubmit(onSubmit)} className="mb-10">
          <Controller
            control={control}
            name="loginId"
            defaultValue=""
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
            <p className="mt-2 mb-4 text-xs text-blue-gray-400">
              {errors.loginId?.message}
            </p>
          ) : (
            <div className="my-10"></div>
          )}

          <Controller
            control={control}
            name="loginPassword"
            defaultValue=""
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
            <p className="mt-2 mb-4 text-xs text-blue-gray-400">
              {errors.loginPassword?.message}
            </p>
          ) : (
            <div className="my-10"></div>
          )}
          <button
            type="submit"
            className="w-full bg-solive-200 text-white font-semibold mt-3 p-2 rounded-md"
          >
            로그인
          </button>
        </form>
      </div>
    </div>
  );
};

export default Login;
