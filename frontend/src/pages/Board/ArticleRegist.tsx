import { Card, Input, Textarea, Typography } from '@material-tailwind/react';
import { ChangeEvent, useState } from 'react';

function ArticleRegist(): JSX.Element {
  const [title, setTitle] = useState<string>('');
  const [content, setContent] = useState<string>('');
  const [files, setFiles] = useState<FileList>();

  const submit = () => {};

  const onChangeFiles = (e: ChangeEvent<HTMLInputElement>) => {
    const fileList = e.target.files;
    if (fileList !== null) {
      setFiles(fileList);
    }
  };
  return (
    <div className="flex justify-center">
      <Card className="relative flex mt-5 w-[60%] min-w-fit">
        <Typography variant="h2">글 작성</Typography>
        <form onSubmit={submit}>
          <Input
            label="제목"
            type="text"
            onChange={(e) => setTitle(e.target.value)}
          ></Input>
          <Textarea
            label="내용"
            onChange={(e) => setContent(e.target.value)}
          ></Textarea>
          <input type="file" multiple onChange={onChangeFiles}></input>
          <button className="btn-primary">등록</button>
        </form>
      </Card>
    </div>
  );
}
export default ArticleRegist;
