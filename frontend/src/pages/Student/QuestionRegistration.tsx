import { ChangeEvent, FormEvent, useState } from 'react';
import axios from 'axios';
import { userState } from '../../recoil/user/userState';
import { useRecoilValue } from 'recoil';

type Subject = {
  label: string;
  value: number;
  subSubjects: {
    label: string;
    value: number;
    details: {
      label: string;
      value: number;
    }[];
  }[];
};

const subjects: Subject[] = [
  {
    label: '수학',
    value: 100,
    subSubjects: [
      {
        label: '수1',
        value: 10,
        details: [
          { label: '지수로그', value: 1 },
          { label: '삼각함수', value: 2 },
          { label: '극한', value: 3 },
        ],
      },
      {
        label: '수2',
        value: 20,
        details: [
          { label: '극한', value: 1 },
          { label: '미분', value: 2 },
          { label: '적분', value: 3 },
        ],
      },
      {
        label: '확률과 통계',
        value: 30,
        details: [
          { label: '확률', value: 1 },
          { label: '통계', value: 2 },
        ],
      },
      {
        label: '기하',
        value: 40,
        details: [
          { label: '이차곡선', value: 1 },
          { label: '벡터', value: 2 },
          { label: '공간도형', value: 3 },
        ],
      },
    ],
  },
  {
    label: '과학',
    value: 200,
    subSubjects: [
      {
        label: '물리',
        value: 10,
        details: [
          { label: '전자기장', value: 1 },
          { label: '에너지', value: 2 },
        ],
      },
      {
        label: '화학',
        value: 20,
        details: [
          { label: '원자', value: 1 },
          { label: '분자', value: 2 },
        ],
      },
      {
        label: '생명',
        value: 30,
        details: [
          { label: '유전', value: 1 },
          { label: '소화', value: 2 },
        ],
      },
      {
        label: '지구',
        value: 40,
        details: [{ label: '천체', value: 0 }],
      },
    ],
  },
];

const QuestionRegistration = () => {
  const [title, setTitle] = useState('');
  const [files, setFiles] = useState<File[]>([]);
  const [nickname, setNickname] = useState('');
  const [subject, setSubject] = useState('');
  const [subSubject, setSubSubject] = useState('');
  const [detail, setDetail] = useState('');
  const [description, setDescription] = useState('');
  const [preview, setPreview] = useState<string>('');

  const user = useRecoilValue(userState);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    if (!user) {
      alert('로그인이 필요합니다.');
      return;
    }

    const dataObject = {
      title: title,
      subject: subject,
      subSubject: subSubject,
      detail: detail,
      description: description,
      studentId: user.id.toString(),
    };

    const formData = new FormData();

    formData.append('dataObject', JSON.stringify(dataObject));

    files.forEach((file, index) => {
      formData.append(`file${index}`, file);
    });

    try {
      const response = await axios.post('/question', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      if (response.data.success) {
        alert('문제가 등록되었습니다.');
      } else if (response.data.error.code === 'NO_IMAGE') {
        alert('문제 등록에 실패했습니다. 이미지 파일이 없습니다.');
      } else if (response.data.error.code === 'IMAGE_UPLOAD_FAIL') {
        alert('문제 등록에 실패했습니다. 이미지 업로드에 실패했습니다.');
      } else {
        alert('문제 등록에 실패했습니다. 알 수 없는 오류입니다.');
      }
    } catch (error) {
      console.error('Failed to submit question: ', error);
    }
  };

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      if (e.target.files.length > 5) {
        alert('파일은 최대 5개까지 업로드 할 수 있습니다.');
        return;
      }

      const fileList = Array.from(e.target.files);
      setFiles(fileList);

      const reader = new FileReader();
      reader.onloadend = () => {
        setPreview(reader.result as string);
      };
      reader.readAsDataURL(fileList[0]);
    } else {
      setFiles([]);
      setPreview('');
    }
  };

  const selectedSubject = subjects.find((s) => s.value === Number(subject));
  const selectedSubSubject = selectedSubject?.subSubjects.find(
    (ss) => ss.value === Number(subSubject),
  );

  return (
    <div className="flex text-black pt-24">
      <div className="w-1/2 p-5">
        <form onSubmit={handleSubmit} className="space-y-3">
          <label className="block bg-light-blue-200 p-3 rounded-md">
            <span className="text-gray-700" style={{ fontWeight: '900' }}>
              제목
            </span>
            <input
              className="mt-1 block w-full h-16 border-2 border-light-blue-500 rounded-md shadow-sm bg-blue-200"
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </label>
          <label className="block bg-light-blue-200 p-3 rounded-md">
            <span className="text-gray-700" style={{ fontWeight: '900' }}>
              사진
            </span>
            <input
              className="mt-1 block w-full h-16 border-2 border-light-blue-500 rounded-md shadow-sm bg-blue-200"
              type="file"
              onChange={handleFileChange}
              multiple
            />
          </label>
          {preview && (
            <div className="mt-3 flex justify-center">
              <img
                src={preview}
                alt="Preview"
                className="object-contain h-[500px] w-[600px]"
              />
            </div>
          )}
          <button
            className="mt-3 px-4 py-2 bg-blue-600 text-white rounded"
            type="submit"
          >
            등록
          </button>
        </form>
      </div>
      <div className="w-1/2 p-5">
        <form className="space-y-3">
          <label className="block bg-light-blue-200 p-3 rounded-md">
            <span className="text-gray-700" style={{ fontWeight: '900' }}>
              닉네임
            </span>
            <input
              className="mt-1 block w-full h-16 border-2 border-light-blue-500 rounded-md shadow-sm bg-blue-200"
              type="text"
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
            />
          </label>
          <label className="block bg-light-blue-200 p-3 rounded-md">
            <span className="text-gray-700" style={{ fontWeight: '900' }}>
              과목
            </span>
            <select
              className="mt-1 block w-full h-16 border-2 border-light-blue-500 rounded-md shadow-sm bg-blue-200"
              value={subject}
              onChange={(e) => setSubject(e.target.value)}
            >
              <option value="">-- 과목을 선택해주세요 --</option>
              {subjects.map((item) => (
                <option key={item.value} value={item.value}>
                  {item.label}
                </option>
              ))}
            </select>
          </label>
          {selectedSubject && (
            <label className="block bg-light-blue-200 p-3 rounded-md">
              <span className="text-gray-700">세부 과목:</span>
              <select
                className="mt-1 block w-full h-16 border-2 border-light-blue-500 rounded-md shadow-sm bg-blue-200"
                value={subSubject}
                onChange={(e) => setSubSubject(e.target.value)}
              >
                <option value="">-- 세부 과목을 선택해주세요 --</option>
                {selectedSubject.subSubjects.map((item) => (
                  <option key={item.value} value={item.value}>
                    {item.label}
                  </option>
                ))}
              </select>
            </label>
          )}
          {selectedSubSubject && (
            <label className="block bg-light-blue-200 p-3 rounded-md">
              <span className="text-gray-700">세부 주제:</span>
              <select
                className="mt-1 block w-full h-16 border-2 border-light-blue-500 rounded-md shadow-sm bg-blue-200"
                value={detail}
                onChange={(e) => setDetail(e.target.value)}
              >
                <option value="">-- 세부 주제를 선택해주세요 --</option>
                {selectedSubSubject.details.map((item) => (
                  <option key={item.value} value={item.value}>
                    {item.label}
                  </option>
                ))}
              </select>
            </label>
          )}
          <label className="block bg-light-blue-200 p-3 rounded-md">
            <span className="text-gray-700" style={{ fontWeight: '900' }}>
              설명
            </span>
            <textarea
              className="mt-1 block w-full h-48 border-2 border-light-blue-500 rounded-md shadow-sm bg-blue-200"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </label>
        </form>
      </div>
    </div>
  );
};

export default QuestionRegistration;
