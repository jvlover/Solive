import { atom } from 'recoil';

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

export const articleListState = atom<Article[]>({
  key: 'articleListState',
  default: [],
});
