import { useEffect, useState } from 'react';
import { useRecoilValue, useRecoilState } from 'recoil';
import { userState } from '../../recoil/user/userState';
import { articleListState, Article } from '../../recoil/atoms';
import { fetchArticles } from '../../api';
import { useNavigate } from 'react-router-dom';
import { ReactComponent as EmptyHeart } from '../../assets/empty_heart.svg';
import { ReactComponent as FullHeart } from '../../assets/full_heart.svg';
import { ReactComponent as Pencil } from '../../assets/pencil.svg';
import { ReactComponent as Eye } from '../../assets/eye.svg';
import { ReactComponent as Photo } from '../../assets/photo.svg';
import {
  Accordion,
  AccordionBody,
  AccordionHeader,
  Breadcrumbs,
  Input,
  Typography,
} from '@material-tailwind/react';

function ArticleList(): JSX.Element {
  //const user = useRecoilValue(userState);
  const navigate = useNavigate();
  const [keyword, setKeyword] = useState<string>('');
  const [articleList, setArticleList] = useRecoilState(articleListState);
  //console.log(user);

  // useEffect를 사용하여 ArticleList 컴포넌트가 마운트될 때
  // fetchAndSetArticles 함수를 호출하여 초기 게시글 목록을 가져옴
  useEffect(() => {
    fetchAndSetArticles();
  }, []);

  // API를 호출하여 게시글 목록을 가져오는 함수
  const fetchAndSetArticles = async () => {
    const articles = await fetchArticles(keyword);
    setArticleList(articles);
  };

  // 게시글 클릭 시 게시글 상세로 이동
  const handleArticleClick = (articleId: number) => {
    navigate(`/board/${articleId}`);
  };

  // 글쓰기 버튼 클릭 시 게시글 등록으로 이동
  const handleArticleRegistClick = () => {
    navigate('/board/regist');
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
            onClick={fetchAndSetArticles}
          >
            검색
          </button>
        </div>
        <div>
          <div className="flex justify-between">
            <Breadcrumbs separator="·" className="m-3 bg-transparent">
              {/* 정렬 구현해야함 */}
              <div className="text-blue-gray-600">최신순</div>
              <div className="text-blue-gray-600">좋아요순</div>
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
          {articleList.map((article: Article, index: number) => (
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
                {article.content}
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
      </div>
    </div>
  );
}

export default ArticleList;
