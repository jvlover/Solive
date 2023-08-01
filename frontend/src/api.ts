import axios from 'axios';
import { Article } from './recoil/atoms';

const BOARD_BASE_URL = 'http://localhost:8080/board';

export const fetchArticles = async (keyword: string): Promise<Article[]> => {
  try {
    const response = await axios.get<Article[]>(
      `${BOARD_BASE_URL}?keyword=${keyword}`,
    );
    return response.data.data.content;
  } catch (error) {
    console.error('Error fetching articles: ', error);
    return [];
  }
};

export const fetchArticleById = async (
  articleId: number,
): Promise<Article | null> => {
  try {
    const response = await axios.get<Article>(`${BOARD_BASE_URL}/${articleId}`);
    console.log(response.data);
    return response.data.data;
  } catch (error) {
    console.error(
      `Error fetching article with articleId ${articleId}: `,
      error,
    );
    return null;
  }
};
