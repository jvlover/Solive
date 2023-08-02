import { useNavigate, useParams } from 'react-router-dom';
import { Article } from '../../recoil/atoms';
import { ChangeEvent, useEffect, useRef, useState } from 'react';
import { fetchArticleById, modifyArticle } from '../../api';
import {
  Button,
  Card,
  Dialog,
  DialogBody,
  DialogFooter,
  Input,
  Textarea,
  Tooltip,
  Typography,
} from '@material-tailwind/react';

function ArticleModify(): JSX.Element {
  const { id } = useParams();
  const [article, setArticle] = useState<Article | null>(null);
  const [files, setFiles] = useState<FileList>();
  const [isOpenDialog, setIsOpenDialog] = useState<boolean>(false);

  const navigate = useNavigate();
  const imageInput = useRef();

  useEffect(() => {
    fetchArticle();
  }, []);

  const fetchArticle = async () => {
    const article = await fetchArticleById(Number(id));
    setArticle(article);
  };

  const modify = async () => {
    // 추후 userId, mastercode 제대로 넘기기
    await modifyArticle(0, 2000, article, files);
    navigate(`/board/${id}`);
  };

  const onChangeFiles = (e: ChangeEvent<HTMLInputElement>) => {
    const fileList = e.target.files;
    if (fileList !== null) {
      setFiles(fileList);
    }
  };

  const handleOpen = () => {
    if (article?.title && article?.content) {
      setIsOpenDialog(!isOpenDialog);
    }
  };

  return (
    <div className="flex justify-center">
      <Card
        shadow={false}
        className="flex flex-col gap-6 mt-5 mb-10 w-[60%] min-w-fit"
      >
        <Typography variant="h2" color="blue-gray">
          게시글 수정
        </Typography>
        <Input
          label="제목"
          type="text"
          value={article?.title}
          onChange={(e) => setArticle({ ...article, title: e.target.value })}
        ></Input>
        <Textarea
          label="내용"
          size="lg"
          className="h-[400px] max-h-screen"
          value={article?.content}
          onChange={(e) => setArticle({ ...article, content: e.target.value })}
        ></Textarea>
        <div className="flex justify-between items-center">
          <input
            type="file"
            multiple
            onChange={onChangeFiles}
            ref={imageInput}
            className="hidden"
          ></input>
          <div className="flex justify-start items-center">
            <button
              className="btn-primary"
              onClick={() => imageInput.current.click()}
            >
              이미지 업로드
            </button>
            <Typography className="ml-3">
              현재 선택된 파일: {files ? files?.length : 0}개
            </Typography>
          </div>
          <div className="flex justify-center">
            <Button
              variant="text"
              color="red"
              className="mr-1 focus:outline-none focus:ring-red-300 focus:ring-1"
              onClick={() => {
                navigate(`/board/${id}`);
              }}
            >
              취소
            </Button>
            <Tooltip
              open={(!article?.title || !article?.content) && onmouseenter}
              animate={{
                mount: { scale: 1, y: 0 },
                unmount: { scale: 0, y: 25 },
              }}
              className="border border-blue-gray-50 bg-white px-4 py-3 shadow-xl shadow-black/10 text-blue-gray-800"
              content="내용을 필수로 입력해주세요"
            >
              <button
                type="submit"
                className="btn-primary px-6 py-3 font-sans text-xs font-semibold"
                onClick={handleOpen}
              >
                수정
              </button>
            </Tooltip>
          </div>
          <Dialog size="xs" open={isOpenDialog} handler={handleOpen}>
            <DialogBody>글을 수정하시겠습니까?</DialogBody>
            <DialogFooter>
              <Button
                variant="text"
                color="red"
                className="mr-1 focus:outline-none focus:ring-red-300 focus:ring-1"
                onClick={handleOpen}
              >
                취소
              </Button>
              <button
                className="btn-primary px-6 py-3 font-sans text-xs font-semibold"
                onClick={modify}
              >
                확인
              </button>
            </DialogFooter>
          </Dialog>
        </div>
      </Card>
    </div>
  );
}
export default ArticleModify;
