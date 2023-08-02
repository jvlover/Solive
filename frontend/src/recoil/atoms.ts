//import { atom } from 'recoil';

export interface Article {
  id: number;
  author: string;
  title: string;
  content: string;
  viewCount: number;
  likeCount: number;
  reportCount: number;
  time: string;
  lastUpdateTime: string;
  articlePicturePathNames: string[] | null;
}

export interface ArticlePage {
  content: Article[];
  number: number;
  totalPages: number;
}

// export const articleListState = atom<Article[] | undefined>({
//   key: 'articleListState',
//   default: [],
// });
