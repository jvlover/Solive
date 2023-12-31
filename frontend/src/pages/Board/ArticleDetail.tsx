import { useNavigate, useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';
import { Article } from '../../recoil/atoms';
import { userState } from '../../recoil/user/userState';
import { fetchArticleById, likeArticle } from '../../api';
import { ReactComponent as FullHeart } from '../../assets/full_heart.svg';
import { ReactComponent as EmptyHeart } from '../../assets/empty_heart.svg';
import { ReactComponent as Eye } from '../../assets/eye.svg';
import { ReactComponent as Pencil } from '../../assets/pencil.svg';
import {
  Breadcrumbs,
  Card,
  CardBody,
  CardFooter,
  Typography,
} from '@material-tailwind/react';
const ArticleDetail = () => {
  const { id } = useParams();
  const user = useRecoilValue(userState);
  const [article, setArticle] = useState<Article | null>();

  const navigate = useNavigate();

  useEffect(() => {
    fetchArticle();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fetchArticle = async () => {
    const article = await fetchArticleById(Number(id));
    setArticle(article);
  };

  const like = async () => {
    const res = await likeArticle(user?.id, article?.id);
    if (res) {
      alert('게시글이 추천되었습니다.');
    } else {
      alert('이미 추천된 게시글입니다.');
    }
  };

  return (
    <div className="flex justify-center my-5">
      <div className="flex justify-center w-[80%] min-h-full min-w-fit">
        <Card className="flex mt-5 max-w-[60%]">
          <CardBody>
            <Typography variant="h2" className="mb-3">
              {article?.title}
            </Typography>
            <div className="flex items-end justify-between mb-1">
              <Breadcrumbs className="p-1 bg-transparent">
                <div>{article?.author}</div>
                <div>{article?.time.replace('T', ' ').slice(0, -7)} 작성</div>
              </Breadcrumbs>
              <div className="flex items-center justify-center">
                <FullHeart />
                <div className="ml-1 mr-3">{article?.likeCount}</div>
                <Eye />
                <div className="ml-1">{article?.viewCount}</div>
              </div>
            </div>
            <hr></hr>
            {article?.articlePicturePathNames?.map((filePath: string) => (
              <div>
                <img src={filePath}></img>
              </div>
            ))}
            <Typography className="mt-3">{article?.content}</Typography>
          </CardBody>
          <CardFooter className="flex justify-center">
            {/* 작성자랑 로그인된 유저랑 똑같으면 보이게 */}
            <button
              className={
                user.masterCodeId === 3
                  ? 'pl-6 pr-6 mr-4 btn-primary'
                  : 'hidden'
              }
              onClick={() => navigate(`/board/modify/${id}`)}
            >
              <div className="flex items-center justify-center">
                <Pencil className="w-4 h-4 mr-1" />
                수정
              </div>
            </button>
            {/* 좋아요 누른 적 있으면 누른 적 있다고 alert 없으면 좋아요 api 호출 */}
            <button className="btn-primary" onClick={like}>
              <div className="flex items-center justify-center">
                <EmptyHeart className="w-4 h-4 mr-1 stroke-white" />
                좋아요
              </div>
            </button>
          </CardFooter>
        </Card>
      </div>
    </div>
  );
};

export default ArticleDetail;
