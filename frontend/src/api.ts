import axios from 'axios';
import { Article, ArticlePage } from './recoil/atoms';
import { StudentFormData } from './pages/Signup/StudentSignup';
import { TeacherFormData } from './pages/Signup/TeacherSignup';
import { User } from './recoil/user/userState';

const BASE_URL = 'http://localhost:8080';
const BOARD_BASE_URL = `${BASE_URL}/board`;
const CHARGE_URL = `${BASE_URL}/charge`;

export const fetchArticles = async (
  keyword: string,
  pageNum: number,
  orderBy?: string,
): Promise<ArticlePage> => {
  let url = `${BOARD_BASE_URL}?keyword=${keyword}&page=${pageNum - 1}&size=5`;
  if (orderBy !== undefined) {
    url += `&sort=${orderBy},desc`;
  }

  try {
    const response = await axios.get<ArticlePage>(url);
    // @ts-ignore
    return response.data.data;
  } catch (error) {
    return { content: [], number: 0, totalPages: 0 };
  }
};

export const fetchArticleById = async (
  articleId: number,
): Promise<Article | null> => {
  try {
    const response = await axios.get<Article>(`${BOARD_BASE_URL}/${articleId}`);
    // @ts-ignore
    return response.data.data;
  } catch (error) {
    console.error(
      `Error fetching article with articleId ${articleId}: `,
      error,
    );
    return null;
  }
};

export const registArticle = async (
  userId: number,
  masterCodeId: number,
  title: string,
  content: string,
  files: FileList | undefined,
): Promise<void> => {
  const formData = new FormData();

  const data = {
    userId: userId,
    masterCodeId: masterCodeId,
    title: title,
    content: content,
  };

  formData.append(
    'registInfo',
    new Blob([JSON.stringify(data)], { type: 'application/json' }),
  );

  // undefined check
  if (files) {
    Array.from(files).forEach((el) => {
      formData.append('files', el);
    });
  }

  await axios.post(`${BOARD_BASE_URL}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

export const modifyArticle = async (
  userId: number,
  masterCodeId: number,
  article: Article | null,
  files: FileList | undefined,
): Promise<void> => {
  const formData = new FormData();

  const data = {
    userId: userId,
    articleId: article.id,
    masterCodeId: masterCodeId,
    title: article.title,
    content: article.content,
  };

  formData.append(
    'modifyInfo',
    new Blob([JSON.stringify(data)], { type: 'application/json' }),
  );

  if (files) {
    Array.from(files).forEach((el) => {
      formData.append('files', el);
    });
  }

  await axios.put(`${BOARD_BASE_URL}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

export const likeArticle = async (userId: number, articleId: number) => {
  try {
    const data = { userId: userId, articleId: articleId };

    const response = await axios.post(
      `${BOARD_BASE_URL}/like`,
      JSON.stringify(data),
      {
        headers: {
          'Content-Type': `application/json`,
        },
      },
    );
    return response.data.data;
  } catch (error) {
    return null;
  }
};

export const chargeSolvePoint = async (
  amount: number,
  token: string,
): Promise<{ success: boolean; solvePoint?: number }> => {
  try {
    const response = await axios.put(
      `${CHARGE_URL}`,
      { amount },
      { headers: { 'access-token': token } },
    );
    return {
      success: response.data.success,
      solvePoint: response.data.data.solvePoint,
    };
  } catch (error) {
    console.error('Error charging: ', error);
    return { success: false };
  }
};

export const studentSignup = async (
  signupData: StudentFormData,
  onSuccess: () => void,
): Promise<void> => {
  const response = await axios.post(BASE_URL + '/user', signupData);
  if (response.data.success === true) {
    onSuccess();
  } else {
    throw new Error('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
  }
};

export const teacherSignup = async (
  signupData: TeacherFormData,
  onSuccess: () => void,
): Promise<void> => {
  const response = await axios.post(BASE_URL + '/user', signupData);
  if (response.data.success === true) {
    onSuccess();
  } else {
    throw new Error('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
  }
};

export const loginUser = async (loginData: {
  loginId: string;
  loginPassword: string;
}): Promise<User> => {
  const response = await axios.post(BASE_URL + '/user/login', loginData);

  if (response.data.success === true) {
    return response.data.data;
  } else {
    throw new Error(
      '로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인해주세요.',
    );
  }
};

export async function getMyProblems(
  accessToken: string,
): Promise<{ success: boolean; data?: any; error?: any }> {
  try {
    const response = await axios.get(BASE_URL + '/matched/my', {
      headers: { accessToken: accessToken },
    });
    return {
      success: response.data.success,
      data: response.data.data,
    };
  } catch (error) {
    let errorCode;
    if (error.response && error.response.data && error.response.data.error) {
      errorCode = error.response.data.error.code;
    }
    return { success: false, error: errorCode || error };
  }
}

export async function getNewAccessToken(
  refreshToken: string,
): Promise<string | null> {
  try {
    const response = await axios.post(BASE_URL + '/user/refresh', {
      refreshToken: refreshToken,
    });
    if (response.data.success) {
      return response.data.accessToken;
    } else {
      throw new Error('Failed to refresh token');
    }
  } catch (error) {
    console.error(error);
    return null;
  }
}
