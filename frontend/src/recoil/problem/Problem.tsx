import { selector } from 'recoil';
import { userState } from '../user/userState';
import axios from 'axios';

interface Problem {
  id: number;
  studentId: number;
  masterCodeId: number;
  title: string;
  description: string | null;
  time: Date;
  isMatched: boolean;
  lastUpdateTime: Date;
  deletedAt: Date | null;
}

export const latestProblemsSelector = selector<Problem[] | null>({
  key: 'LatestProblems',
  get: async ({ get }) => {
    const user = get(userState);
    if (!user) return null;

    try {
      const response = await axios.get(
        `YOUR_API_URL/latest?teacherId=${user.id}`,
      );
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },
});

export const relatedProblemsSelector = selector<Problem[] | null>({
  key: 'RelatedProblems',
  get: async ({ get }) => {
    const user = get(userState);
    if (!user) return null;

    try {
      const response = await axios.get(
        `YOUR_API_URL/related?teacherId=${user.id}`,
      );
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },
});

export const allProblemsSelector = selector<Problem[] | null>({
  key: 'AllProblems',
  get: async ({ get }) => {
    const user = get(userState);
    if (!user) return null;

    try {
      const response = await axios.get(
        `YOUR_API_URL/allProblems?teacherId=${user.id}`,
      );
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },
});
