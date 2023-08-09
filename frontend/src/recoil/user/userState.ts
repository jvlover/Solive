import { atom } from 'recoil';
import localStorageEffect from '../localStorageEffect';

interface CommonUserFields {
  id: number;
  loginId: string;
  refreshToken: string | null;
  masterCodeId: number | null;
  stateId: number | null;
  nickname: string;
  email: string;
  path: string | null;
  pictureName: string | null;
  introduce: string | null;
  gender: number | null;
  experience: number;
  signinTime: Date;
  accessToken: string;
}

export interface TeacherFields {
  userId: number;
  subjectId: number | null;
  solvedCount: number;
  ratingSum: number;
  ratingCount: number;
  solvePoint: number;
}

interface StudentFields {
  userId: number;
  questionCount: number;
  solvePoint: number;
}

export type User = CommonUserFields & (TeacherFields | StudentFields);

export const userState = atom<User | null>({
  key: 'userState',
  default: null,
  effects: [localStorageEffect('user')],
});
