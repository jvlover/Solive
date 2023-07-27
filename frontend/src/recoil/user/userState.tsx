import { atom } from 'recoil';

interface CommonUserFields {
  id: number;
  login_id: string;
  login_password: string;
  refresh_token: string | null;
  master_code_id: number | null;
  state_id: number | null;
  nickname: string;
  email: string;
  picture_url: string | null;
  picture_name: string | null;
  introduce: string | null;
  gender: number | null;
  experience: number;
  signin_time: Date;
  deleted_at: Date | null;
}

interface TeacherFields {
  user_id: number;
  subject_id: number | null;
  solved_count: number;
  rating_sum: number;
  rating_count: number;
  solve_point: number;
}

interface StudentFields {
  user_id: number;
  question_count: number;
  solve_point: number;
}

type User = CommonUserFields & (TeacherFields | StudentFields);

export const userState = atom<User | null>({
  key: 'userState',
  default: null,
});
