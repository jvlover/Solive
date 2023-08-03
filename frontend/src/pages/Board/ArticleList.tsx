import { useCallback, useEffect, useState } from 'react';
import { userState } from '../../recoil/user/userState';
import { Article, ArticlePage } from '../../recoil/atoms';
import { fetchArticles } from '../../api';
import { useNavigate } from 'react-router-dom';
import { ReactComponent as EmptyHeart } from '../../assets/empty_heart.svg';
import { ReactComponent as FullHeart } from '../../assets/full_heart.svg';
import { ReactComponent as Pencil } from '../../assets/pencil.svg';
import { ReactComponent as Eye } from '../../assets/eye.svg';
import { ReactComponent as Photo } from '../../assets/photo.svg';
import { ArrowRightIcon, ArrowLeftIcon } from '@heroicons/react/24/outline';
import {
  Accordion,
  AccordionBody,
  AccordionHeader,
  Breadcrumbs,
  Button,
  IconButton,
  Input,
  Typography,
} from '@material-tailwind/react';

function ArticleList(): JSX.Element {
  //const user = useRecoilValue(userState);
  const navigate = useNavigate();
  const [keyword, setKeyword] = useState<string>('');
  const [activePage, setActivePage] = useState<number>(1);
  const [articlePages, setArticlePages] = useState<ArticlePage>();
  const [firstNum, setFirstNum] = useState<number>(1);
  const [pageLength, setPageLength] = useState<number>(1);
  //console.log(user);

  // API를 호출하여 게시글 목록을 가져오는 함수
  const fetchAndSetArticles = useCallback(
    async (page: number, orderBy?: string) => {
      const articlePages = await fetchArticles(keyword, page, orderBy);
      setArticlePages(articlePages);
      setActivePage(page);
      const firstNum = page % 5 ? page - (page % 5) + 1 : page - 4;
      const pageLength = Math.min(5, articlePages.totalPages - firstNum + 1);
      setFirstNum(firstNum);
      setPageLength(pageLength);
    },
    [],
  );

  // useEffect를 사용하여 ArticleList 컴포넌트가 마운트될 때
  // fetchAndSetArticles 함수를 호출하여 초기 게시글 목록을 가져옴
  useEffect(() => {
    fetchAndSetArticles(1);
  }, [fetchAndSetArticles]);

  // 게시글 클릭 시 게시글 상세로 이동
  const handleArticleClick = (articleId: number) => {
    navigate(`/board/${articleId}`);
  };

  // 글쓰기 버튼 클릭 시 게시글 등록으로 이동
  const handleArticleRegistClick = () => {
    navigate('/board/regist');
  };

  const getItemProps = (index: number) =>
    ({
      variant: activePage === index ? 'filled' : 'text',
      color: activePage === index ? 'indigo' : 'blue-gray',
      className: 'rounded-full focus:outline-none',
      onClick: () => setActivePage(index),
    }) as any;

  const next = () => {
    if (activePage == articlePages?.totalPages) return;

    fetchAndSetArticles(activePage + 1);
  };

  const prev = () => {
    if (activePage == 1) return;

    fetchAndSetArticles(activePage - 1);
  };

  return (
    <div className="flex justify-center">
      <div className="w-[60%] min-w-fit">
        <Typography variant="h2" className="m-5">
          공지사항
        </Typography>
        <div className="relative flex ml-3 mr-3">
          {/* 검색어를 입력하는 input 요소 */}
          <Input
            variant="outlined"
            size="lg"
            color="deep-purple"
            label="제목으로 검색"
            type="text"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            className="pr-20 h-max"
            containerProps={{
              className: 'min-w-0',
            }}
          />
          <button
            className={`${
              keyword ? 'btn-primary' : 'btn-secondary'
            } !absolute right-1 top-1 rounded`}
            disabled={!keyword}
            onClick={() => fetchAndSetArticles(1)}
          >
            검색
          </button>
        </div>
        <div>
          <div className="flex justify-between">
            <Breadcrumbs separator="·" className="m-3 bg-transparent">
              {/* 정렬 구현해야함 */}
              <div
                className="text-blue-gray-600"
                onClick={() => fetchAndSetArticles(1, 'time')}
              >
                최신순
              </div>
              <div
                className="text-blue-gray-600"
                onClick={() => fetchAndSetArticles(1, 'likeCount')}
              >
                좋아요순
              </div>
            </Breadcrumbs>
            {/* 글 작성 페이지로 넘어가게(관리자만 보이게) */}
            <button
              className="btn-primary m-3"
              onClick={() => handleArticleRegistClick()}
            >
              <div className="flex justify-center items-center">
                <Pencil className="w-4 h-4 mr-1" />
                글쓰기
              </div>
            </button>
          </div>
          {articlePages?.content.map((article: Article) => (
            <Accordion
              open={true}
              key={article.id}
              className="border-t border-b"
            >
              <AccordionHeader
                className="border-none rounded-none pb-2 hover:text-solive-100 focus:outline-none"
                onClick={() => handleArticleClick(article.id)}
              >
                {article.title}
              </AccordionHeader>
              <AccordionBody className="pt-0 pl-6">
                {article.content.length < 60
                  ? article.content
                  : article.content.substring(0, 60).concat(' ...')}
              </AccordionBody>
              <AccordionBody className="flex justify-between pt-0 pl-6">
                <Breadcrumbs separator="·">
                  <div>{article.author}</div>
                  <div>{article.time.replace('T', ' ')}</div>
                </Breadcrumbs>
                <div className="flex items-center">
                  {/* 이미지 있는 글이면 아이콘으로 표시 */}
                  {article.articlePicturePathNames?.length ? (
                    <>
                      <Photo />
                      <div className="mr-3"></div>
                    </>
                  ) : (
                    ''
                  )}
                  {/* 내가 좋아요 했으면 FullHeart */}
                  <EmptyHeart />
                  <Typography className="m-1 mr-3 font-medium text-blue-gray-600">
                    {article.likeCount}
                  </Typography>
                  <Eye />
                  <Typography className="m-1 mr-3 font-medium text-blue-gray-600">
                    {article.viewCount}
                  </Typography>
                </div>
              </AccordionBody>
            </Accordion>
          ))}
        </div>
        <div className="flex justify-center items-center gap-4 m-3">
          <Button
            variant="text"
            color="blue-gray"
            className="flex items-center gap-2"
            onClick={prev}
            disabled={activePage == 1}
          >
            <ArrowLeftIcon strokeWidth={2} className="h-4 w-4" /> 이전
          </Button>
          <div className="flex items-center gap-2">
            {Array<number>(pageLength)
              .fill(0)
              .map((_, index: number) => (
                <IconButton
                  key={index}
                  {...getItemProps(firstNum + index)}
                  onClick={() => fetchAndSetArticles(firstNum + index)}
                >
                  {firstNum + index}
                </IconButton>
              ))}
          </div>
          <Button
            variant="text"
            color="blue-gray"
            className="flex items-center gap-2"
            onClick={next}
            disabled={activePage == articlePages?.totalPages}
          >
            다음
            <ArrowRightIcon strokeWidth={2} className="h-4 w-4" />
          </Button>
        </div>
      </div>
    </div>
  );
}

export default ArticleList;
