import { useParams } from 'react-router-dom';
import { Article } from '../../recoil/atoms';
import { useEffect, useState } from 'react';
import { fetchArticleById } from '../../api';
import { ReactComponent as FullHeart } from '../../assets/full_heart.svg';
import { ReactComponent as EmptyHeart } from '../../assets/empty_heart.svg';
import { ReactComponent as Eye } from '../../assets/eye.svg';
import { ReactComponent as Pencil } from '../../assets/Pencil.svg';
import {
  Breadcrumbs,
  Card,
  CardBody,
  CardFooter,
  Typography,
} from '@material-tailwind/react';
function ArticleDetail(): JSX.Element {
  const { id } = useParams();
  const [article, setArticle] = useState<Article | null>();

  useEffect(() => {
    fetchArticle();
  }, [id]);

  const fetchArticle = async () => {
    const article = await fetchArticleById(Number(id));
    setArticle(article);
  };

  return (
    <div className="flex justify-center">
      <div className="w-[60%] min-w-fit">
        <Card className="relative flex mt-5">
          <CardBody>
            <Typography variant="h2" className="mb-3">
              {article?.title}
            </Typography>
            <div className="flex justify-between items-end mb-1">
              <Breadcrumbs className="p-1 bg-transparent">
                <div>{article?.author}</div>
                <div>{article?.time.replace('T', ' ')} 작성</div>
              </Breadcrumbs>
              <div className="flex justify-center items-center">
                <FullHeart />
                <div className="ml-1 mr-3">{article?.likeCount}</div>
                <Eye />
                <div className="ml-1">{article?.viewCount}</div>
              </div>
            </div>
            <hr></hr>
            {article?.articlePicturePathNames?.map((filePath: string) => (
              // 나중에 S3랑 연동
              <div>
                <div>{filePath}</div>
                <img src={filePath}></img>
              </div>
            ))}
            <Typography className="mt-3">{article?.content}</Typography>
          </CardBody>
          <CardFooter className="flex justify-center">
            {/* 작성자랑 로그인된 유저랑 똑같으면 보이게 */}
            <button className="btn-primary pl-6 pr-6 mr-4">
              <div className="flex justify-center items-center">
                <Pencil className="w-4 h-4 mr-1" />
                수정
              </div>
            </button>
            {/* 좋아요 누른 적 있으면 누른 적 있다고 alert 없으면 좋아요 api 호출 */}
            <button className="btn-primary">
              <div className="flex justify-center items-center">
                <EmptyHeart className="w-4 h-4 mr-1 stroke-white" />
                좋아요
              </div>
            </button>
          </CardFooter>
        </Card>
      </div>
    </div>
  );
}

export default ArticleDetail;
