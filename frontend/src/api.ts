import axios from 'axios';
import { Article, ArticlePage } from './recoil/atoms';
import { SignupFormData } from './pages/Signup/Signup';
import { User } from './recoil/user/userState';
import { UserProfile } from './pages/MyPage/Profile';

const BASE_URL = 'https://i9a107.p.ssafy.io/api';
const BOARD_BASE_URL = `${BASE_URL}/board`;
const CHARGE_URL = `${BASE_URL}/user/charge`;
const CASHOUT_URL = `${BASE_URL}/user/cashout`;

export const fetchArticles = async (
  keyword: string,
  pageNum: number,
  orderBy?: string,
): Promise<ArticlePage> => {
  let url = `${BOARD_BASE_URL}/auth?keyword=${keyword}&page=${
    pageNum - 1
  }&size=5`;
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
    const response = await axios.get<Article>(
      `${BOARD_BASE_URL}/auth/${articleId}`,
    );
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
  solvePoint: number,
  token: string,
): Promise<{ success: boolean; solvePoint?: number }> => {
  try {
    const response = await axios.put(
      `${CHARGE_URL}`,
      { solvePoint },
      { headers: { 'access-token': token } },
    );

    return {
      success: response.data.success,
      solvePoint: response.data.data,
    };
  } catch (error) {
    console.error('Error charging: ', error);
    return { success: false };
  }
};

export const signup = async (
  signupData: SignupFormData,
  onSuccess: () => void,
): Promise<void> => {
  const response = await axios.post(BASE_URL + '/user/auth', signupData);
  if (response.data.success === true) {
    onSuccess();
  } else {
    alert(response.data.error.message);
  }
};

export const loginUser = async (loginData: {
  loginId: string;
  loginPassword: string;
}): Promise<User> => {
  const response = await axios.post(BASE_URL + '/user/auth/login', loginData);

  if (response.data.success === true) {
    return response.data.data;
  } else {
    throw new Error(
      '로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인해주세요.',
    );
  }
};

export async function getMyProblems(
  userState: number,
  masterCodeMiddle: number,
  masterCodeLow: number,
  matchingState: number,
  keyword: string,
  sort: string,
  pageNum: number,
  accessToken: string,
) {
  type Problem = {
    questionId: number;
    path: string;
    title: string;
    masterCodeName: string;
    createTime: string;
    matching_state: number;
  };
  type ApiResponse<T> = {
    success: boolean;
    data: T;
    error?: string;
  };
  try {
    const response = await axios.get<ApiResponse<Problem[]>>(
      BASE_URL + '/matched/my',
      {
        headers: { 'access-token': accessToken },
        params: {
          userState: userState,
          masterCodeMiddle: masterCodeMiddle,
          masterCodeLow: masterCodeLow,
          matchingState: matchingState,
          keyword: keyword,
          sort: sort,
          pageNum: pageNum,
        },
      },
    );
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
    const response = await axios.post(BASE_URL + '/user/auth/refresh', {
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

export async function submitQuestion(
  formData: FormData,
  accessToken: string,
): Promise<{ success: boolean; data?: any; error?: any }> {
  try {
    const response = await axios.post(BASE_URL + '/question', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'access-token': accessToken,
      },
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

export const getProfile = async (
  accessToken: string,
): Promise<{ success: boolean; data?: any; error?: any }> => {
  interface ProfileResponse {
    success: boolean;
    data: UserProfile;
  }
  try {
    const response = await axios.get<ProfileResponse>(BASE_URL + '/user', {
      headers: { 'access-token': accessToken },
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
};

export async function questionSearch(
  subjectNum: number,
  subSubjectNum: number,
  searchKeyword: string,
  order: string,
  pageNum: number,
  accessToken: string,
): Promise<{ success: boolean; data?: any; error?: any }> {
  try {
    const response = await axios.get(
      `${BASE_URL}/question?masterCodeMiddle=${subjectNum}&masterCodeLow=${subSubjectNum}&keyword=${searchKeyword}&sort=${order}&pageNum=${pageNum}`,
      {
        headers: { 'access-token': accessToken },
      },
    );

    if (response.data.success) {
      return {
        success: response.data.success,
        data: response.data.data,
      };
    } else {
      throw new Error('Failed to get questions');
    }
  } catch (error) {
    let errorCode;
    if (error.response && error.response.data && error.response.data.error) {
      errorCode = error.response.data.error.code;
    }
    return { success: false, error: errorCode || error };
  }
}

export const modifyProfile = async (
  nickname: string,
  introduce: string,
  gender: number,
  profileImage: File | null,
  accessToken: string,
  teacherSubjectName: number,
): Promise<{ path?: string; success: boolean }> => {
  const profileData = {
    nickname: nickname,
    introduce: introduce,
    gender: gender,
    teacherSubjectName: teacherSubjectName,
  };
  const formData = new FormData();

  formData.append(
    'userInfo',
    new Blob([JSON.stringify(profileData)], { type: 'application/json' }),
  );

  if (profileImage) {
    formData.append('files', profileImage);
  }

  try {
    const response = await axios.put(BASE_URL + '/user', formData, {
      headers: {
        'access-token': accessToken,
        'Content-Type': 'multipart/form-data',
      },
    });
    return { success: response.data.success, path: response.data.data };
  } catch (error) {
    return { success: false };
  }
};

export const getPrivacy = async (
  accessToken: string,
): Promise<{
  success: boolean;
  email?: string;
  signinTime?: string;
  error?: any;
}> => {
  try {
    const response = await axios.get(BASE_URL + '/user/privacy', {
      headers: { 'access-token': accessToken },
    });
    return {
      success: response.data.success,
      email: response.data.data.email,
      signinTime: response.data.data.signinTime,
    };
  } catch (error) {
    let errorCode;
    if (error.response && error.response.data && error.response.data.error) {
      errorCode = error.response.data.error.code;
    }
    return { success: false, error: errorCode || error };
  }
};

export const modifyPassword = async (
  oldPassword: string,
  newPassword: string,
  accessToken: string,
): Promise<{
  success: boolean;
  error?: any;
}> => {
  try {
    const data = {
      oldPassword: oldPassword,
      newPassword: newPassword,
    };
    const response = await axios.put(BASE_URL + '/user/password', data, {
      headers: { 'access-token': accessToken },
    });
    return {
      success: response.data.success,
    };
  } catch (error) {
    let errorCode;
    if (error.response && error.response.data && error.response.data.error) {
      errorCode = error.response.data.error.code;
    }
    return { success: false, error: errorCode || error };
  }
};

export const withdrawalUser = async (
  accessToken: string,
): Promise<{
  success: boolean;
  error?: any;
}> => {
  try {
    const response = await axios.put(BASE_URL + '/user/delete', null, {
      headers: { 'access-token': accessToken },
    });
    return {
      success: response.data.success,
    };
  } catch (error) {
    let errorCode;
    if (error.response && error.response.data && error.response.data.error) {
      errorCode = error.response.data.error.code;
    }
    return { success: false, error: errorCode || error };
  }
};

export const logoutUser = async (
  userId: number,
): Promise<{ success: boolean; error?: any }> => {
  try {
    const response = await axios.put(BASE_URL + '/user/auth/logout', {
      userId: userId,
    });
    return {
      success: response.data.success,
    };
  } catch (error) {
    let errorCode;
    if (error.response && error.response.data && error.response.data.error) {
      errorCode = error.response.data.error.code;
    }
    return { success: false, error: errorCode || error };
  }
};
export async function getTeachers(accessToken: string) {
  type Teacher = {
    path: string;
    nickname: string;
    masterCodeName: string;
    rating: number;
  };

  type ApiResponse<T> = {
    success: boolean;
    data: T;
    error?: string;
  };

  try {
    const response = await axios.get<ApiResponse<Teacher[]>>(
      BASE_URL + '/user/onlineteacher',
      {
        headers: { 'access-token': accessToken },
      },
    );
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

export async function getQuestionById(id: number, accessToken: string) {
  type Question = {
    userNickname: string;
    title: string;
    description: string;
    path: string[];
    masterCodeName: string;
    masterCodeCategory: string;
    createTime: string;
    state: string;
  };

  type ApiResponse<T> = {
    success: boolean;
    data: T;
    error?: string;
  };

  try {
    const response = await axios.get<ApiResponse<Question>>(
      BASE_URL + `/question/${id}`,
      {
        headers: { 'access-token': accessToken },
      },
    );
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

type ApplyData = {
  solvePoint: string;
  estimatedTime: string;
  questionId: number;
};

type ApplyResponse = {
  success: boolean;
  message?: string;
};

export const applyQuestion = async (
  applyData: ApplyData,
  accessToken: string,
): Promise<ApplyResponse> => {
  const response = await axios.post<ApplyResponse>(
    BASE_URL + '/apply',
    applyData,
    {
      headers: {
        'access-token': accessToken,
      },
    },
  );

  return response.data;
};

export interface Teacher {
  applyId: number;
  teacherSubjectName: string;
  path: string;
  teacherNickname: string;
  solvePoint: string;
  estimatedTime: string;
  ratingSum: number;
  ratingCount: number;
}

export interface GetTeachersListParams {
  questionId: number;
  sort: string;
  isFavorite: boolean;
}

export const getTeachersList = async (
  params: GetTeachersListParams,
  accessToken: string,
): Promise<{ success: boolean; data?: Teacher[]; error?: string }> => {
  try {
    const response = await axios.get<{ success: boolean; data: Teacher[] }>(
      BASE_URL + '/apply',
      {
        headers: { 'access-token': accessToken },
        params: {
          questionId: params.questionId,
          sort: params.sort,
          isFavorite: params.isFavorite,
        },
      },
    );

    return {
      success: response.data.success,
      data: response.data.data,
    };
  } catch (error) {
    let errorCode = error?.response?.data?.error?.code;

    return {
      success: false,
      error: errorCode || 'UNKNOWN_ERROR',
    };
  }
};

export async function applyToTeacher(applyId: number, accessToken: string) {
  try {
    const response = await axios.post(
      BASE_URL + '/matched',
      { applyId },
      {
        headers: {
          'access-token': accessToken,
        },
      },
    );
    return response.data;
  } catch (error) {
    console.error(error);
    return { success: false, error: error.message };
  }
}

export interface FavoriteTeacher {
  path: string;
  teacherNickName: string;
  teacherSubjectName: string;
  ratingSum: number;
  ratingCount: number;
}

export const getFavorites = async (
  accessToken: string,
): Promise<{ success: boolean; data?: FavoriteTeacher[]; error?: any }> => {
  interface FavoritesResponse {
    success: boolean;
    data: FavoriteTeacher[];
  }

  try {
    const response = await axios.get<FavoritesResponse>(
      BASE_URL + '/user/favorite',
      {
        headers: { 'access-token': accessToken },
      },
    );
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
};

export const teacherSolvePoint = async (
  solvePoint: number,
  token: string,
): Promise<{ success: boolean; solvePoint?: number }> => {
  try {
    const response = await axios.put(
      `${CASHOUT_URL}`,
      { solvePoint },
      { headers: { 'access-token': token } },
    );
    return {
      success: response.data.success,
      solvePoint: response.data.data,
    };
  } catch (error) {
    console.error('Error charging: ', error);
    return { success: false };
  }
};

export async function applyToRate(
  applyId: number,
  rating: number,
  accessToken: string,
) {
  console.log(applyId);
  console.log(rating);
  console.log(accessToken);
  try {
    const response = await axios.put(
      BASE_URL + '/user/rate',
      { applyId, rating },
      {
        headers: {
          'access-token': accessToken,
        },
      },
    );
    return response.data;
  } catch (error) {
    console.error(error);
    return { success: false, error: error.message };
  }
}

export const getReplayUrl = async (
  id: number,
  accessToken: string,
): Promise<{ success: boolean; data?: { videoUrl: string } }> => {
  try {
    const response = await axios.get(BASE_URL + `/matched/video/${id}`, {
      headers: { 'access-token': accessToken },
    });
    return {
      success: response.data.success,
      data: response.data.data,
    };
  } catch (error) {
    return { success: false };
  }
};

type TeacherQuestion = {
  questionId: number;
  path: string;
  title: string;
  masterCodeName: string;
  createTime: string;
  userNickname: string;
};

type ApiResponse<T> = {
  success: boolean;
  data: T;
  error?: string;
};

export async function getMyRelated(
  accessToken: string,
): Promise<{ success: boolean; data?: TeacherQuestion[]; error?: any }> {
  try {
    const response = await axios.get<ApiResponse<TeacherQuestion[]>>(
      BASE_URL + '/question/init/favorite',
      {
        headers: { 'access-token': accessToken },
      },
    );

    if (response.data.success) {
      return {
        success: response.data.success,
        data: response.data.data,
      };
    } else {
      throw new Error('Failed to get related questions');
    }
  } catch (error) {
    let errorCode;
    if (error.response && error.response.data && error.response.data.error) {
      errorCode = error.response.data.error.code;
    }
    return { success: false, error: errorCode || error };
  }
}

export async function getLatest(
  accessToken: string,
): Promise<{ success: boolean; data?: TeacherQuestion[]; error?: any }> {
  try {
    const response = await axios.get<ApiResponse<TeacherQuestion[]>>(
      BASE_URL + '/question/init/time',
      {
        headers: { 'access-token': accessToken },
      },
    );

    if (response.data.success) {
      return {
        success: response.data.success,
        data: response.data.data,
      };
    } else {
      throw new Error('Failed to get latest questions');
    }
  } catch (error) {
    let errorCode;
    if (error.response && error.response.data && error.response.data.error) {
      errorCode = error.response.data.error.code;
    }
    return { success: false, error: errorCode || error };
  }
}

export async function favoriteTeacher(applyId: number, accessToken: string) {
  try {
    const response = await axios.post(
      BASE_URL + '/user/favorite/add',
      { applyId },
      {
        headers: {
          'access-token': accessToken,
        },
      },
    );
    return response.data;
  } catch (error) {
    console.error(error);
    return { success: false, error: error.message };
  }
}
