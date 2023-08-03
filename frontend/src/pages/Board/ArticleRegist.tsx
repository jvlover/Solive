import { ChangeEvent, useRef, useState } from 'react';
import { registArticle } from '../../api';
import { useNavigate } from 'react-router-dom';
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

const ArticleRegist = () => {
  const [title, setTitle] = useState<string>('');
  const [content, setContent] = useState<string>('');
  const [files, setFiles] = useState<FileList>();
  const [isOpenDialog, setIsOpenDialog] = useState<boolean>(false);

  const navigate = useNavigate();
  const imageInput = useRef<HTMLInputElement>(); // file input을 버튼으로 대체하기 위함

  const regist = async () => {
    // TODO: 추후 유저 id, mastercode 제대로 넘겨줘야함
    await registArticle(0, 2000, title, content, files);
    navigate('/board');
  };

  const onChangeFiles = (e: ChangeEvent<HTMLInputElement>) => {
    const fileList = e.target.files;
    if (fileList !== null) {
      setFiles(fileList);
    }
  };

  const handleOpen = () => {
    if (title && content) {
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
          게시글 작성
        </Typography>
        <Input
          label="제목"
          type="text"
          onChange={(e) => setTitle(e.target.value)}
        ></Input>
        <Textarea
          label="내용"
          size="lg"
          className="h-[400px] max-h-screen"
          onChange={(e) => setContent(e.target.value)}
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
                navigate('/board');
              }}
            >
              취소
            </Button>
            <Tooltip
              open={(!title || !content) && onmouseenter}
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
                등록
              </button>
            </Tooltip>
          </div>
          <Dialog size="xs" open={isOpenDialog} handler={handleOpen}>
            <DialogBody>글을 등록하시겠습니까?</DialogBody>
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
                onClick={regist}
              >
                확인
              </button>
            </DialogFooter>
          </Dialog>
        </div>
      </Card>
    </div>
  );
};
export default ArticleRegist;
