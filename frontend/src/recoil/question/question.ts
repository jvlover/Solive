import { selector } from 'recoil';
import axios from 'axios';
import { userState } from '../user/userState';

export interface Question {
  id: number;
  studentId: number;
  masterCodeId: number;
  title: string;
  description: string | null;
  time: Date;
  isMatched: boolean;
  lastUpdateTime: Date;
  deletedAt: Date | null;
  path: string;
}

export const latestQuestionsSelector = selector<Question[] | null>({
  key: 'LatestQuestions',
  get: async () => {
    try {
      const response = await axios.get(
        'https://i9a107.p.ssafy.io/api/init/time',
      );
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },
});

export const relatedQuestionsSelector = selector<Question[] | null>({
  key: 'RelatedQuestions',
  get: async ({ get }) => {
    const user = get(userState);
    if (!user || user.masterCodeId !== 2) return null;

    try {
      const response = await axios.get(
        'https://i9a107.p.ssafy.io/api/init/favorite',
        {
          headers: { 'access-token': user.accessToken },
        },
      );
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },
});

export const allQuestionsSelector = selector<Question[] | null>({
  key: 'AllQuestions',
  get: async ({ get }) => {
    const user = get(userState);
    if (!user) return null;

    try {
      const response = await axios.get(`http://localhost:8080/allQuestions`);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },
});