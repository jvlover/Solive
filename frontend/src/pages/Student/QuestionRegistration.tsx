import { ChangeEvent, FormEvent, useRef, useState } from 'react';
import { userState } from '../../recoil/user/userState';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { submitQuestion, getNewAccessToken } from '../../api';
import { Typography } from '@material-tailwind/react';
import { useNavigate } from 'react-router-dom';


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
  const [subject, setSubject] = useState('');
  const [subSubject, setSubSubject] = useState('');
  const [detail, setDetail] = useState('');
  const [description, setDescription] = useState('');
  const [preview, setPreview] = useState<string>('');

  const user = useRecoilValue(userState);
  const setUser = useSetRecoilState(userState);


  const image = useRef<HTMLInputElement>();

  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: FormEvent) => {
    setShowModal(true);
    e.preventDefault();

    if (!user) {
      alert('로그인이 필요합니다.');
      return;
    }

    const data = {
      title: title,
      subject: subject,
      subSubject: subSubject,
      detail: detail,
      description: description,
    };

    const formData = new FormData();

    formData.append(
      'registInfo',
      new Blob([JSON.stringify(data)], { type: 'application/json' }),
    );

    files.forEach((file, index) => {
      formData.append(`file${index}`, file);
    });

    const result = await submitQuestion(formData, user.accessToken);

    if (result.success) {
      alert('문제가 등록되었습니다.');
    } else if (result.error === 'NO_IMAGE') {
      alert('문제 등록에 실패했습니다. 이미지 파일이 없습니다.');
    } else if (result.error === 'IMAGE_UPLOAD_FAIL') {
      alert('문제 등록에 실패했습니다. 이미지 업로드에 실패했습니다.');
    } else if (result.error === 'JWT_TOKEN_EXPIRED_EXCEPTION') {
      const newAccessToken = await getNewAccessToken(user.refreshToken);
      if (newAccessToken) {
        setUser({
          ...user,
          accessToken: newAccessToken,
        });
        submitQuestion(formData, user.accessToken);
      }
    } else {
      console.error('Failed to load problems:', result.error);
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

  const handleConfirm = () => {
    setShowModal(false);
    navigate('/student');
  };

  const selectedSubject = subjects.find((s) => s.value === Number(subject));
  const selectedSubSubject = selectedSubject?.subSubjects.find(
    (ss) => ss.value === Number(subSubject),
  );

  return (
    <div>
      <Typography variant="h2" className="ml-[18vw] mt-4 font-[Pretendard]">
        문제 등록
      </Typography>
      <div className="flex justify-center">
        <div className="flex w-[70vw] min-w-[900px] text-black">
          <div className="w-1/2 px-5">
            <form className="space-y-8">
              <div className="flex justify-center mt-10">
                {preview ? (
                  <img
                    src={preview}
                    alt="Preview"
                    className="object-contain h-[373px] w-full border-2 border-gray-300 rounded-md"
                  />
                ) : (
                  <div className="flex justify-center items-center h-[373px] w-full border-2 border-gray-300 rounded-md text-blue-gray-700 text-2xl">
                    사진을 등록해주세요
                  </div>
                )}
              </div>
              <div className="flex items-center justify-start">
                <input
                  className="w-full p-2 border border-gray-300 rounded bg-opacity-30 bg-solive-200 min-w-[200px] min-h-[42px]"
                  value={
                    files.length > 0
                      ? files[0].name
                      : '현재 등록된 사진이 없습니다.'
                  }
                  disabled
                ></input>
                <input
                  type="file"
                  onChange={handleFileChange}
                  ref={image}
                  className="hidden"
                  multiple
                />
                <button
                  className="ml-5 btn-primary px-0 w-[120px] h-[42px] flex items-center justify-center"
                  type="button"
                  onClick={() => image.current.click()}
                >
                  파일 찾기
                </button>
              </div>
            </form>
          </div>
          <div className="w-1/2 px-5">
            <form className="flex flex-col justify-end space-y-3">
              <label className="block p-3 rounded-md">
                <span className="font-bold text-gray-700">제목</span>
                <input
                  className="block w-full h-16 p-2 mt-1 border-2 border-gray-300 rounded-md shadow-sm bg-solive-200 bg-opacity-30"
                  type="text"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                />
              </label>
              <div className="flex justify-between">
                <label className="block w-full p-3 rounded-md">
                  <span className="font-bold text-gray-700">과목</span>
                  <select
                    className="block w-full h-16 p-2 mt-1 border-2 border-gray-300 rounded-md shadow-sm bg-solive-200 bg-opacity-30"
                    value={subject}
                    onChange={(e) => setSubject(e.target.value)}
                  >
                    <option value="">--</option>
                    {subjects.map((item) => (
                      <option key={item.value} value={item.value}>
                        {item.label}
                      </option>
                    ))}
                  </select>
                </label>
                <label className="block w-full p-3 rounded-md">
                  <span className="font-bold text-gray-700">세부 과목</span>
                  <select
                    className="block w-full h-16 p-2 mt-1 border-2 border-gray-300 rounded-md shadow-sm bg-solive-200 bg-opacity-30"
                    value={subSubject}
                    onChange={(e) => setSubSubject(e.target.value)}
                  >
                    <option value="">--</option>
                    {selectedSubject &&
                      selectedSubject.subSubjects.map((item) => (
                        <option key={item.value} value={item.value}>
                          {item.label}
                        </option>
                      ))}
                  </select>
                </label>
                <label className="block w-full p-3 rounded-md">
                  <span className="font-bold text-gray-700">세부 주제</span>
                  <select
                    className="block w-full h-16 p-2 mt-1 border-2 border-gray-300 rounded-md shadow-sm bg-solive-200 bg-opacity-30 "
                    value={detail}
                    onChange={(e) => setDetail(e.target.value)}
                  >
                    <option value="">--</option>
                    {selectedSubSubject &&
                      selectedSubSubject?.details.map((item) => (
                        <option key={item.value} value={item.value}>
                          {item.label}
                        </option>
                      ))}
                  </select>
                </label>
              </div>
              <label className="block p-3 rounded-md">
                <span className="font-bold text-gray-700">설명</span>
                <textarea
                  className="block w-full h-48 p-2 mt-1 border-2 border-gray-300 rounded-md shadow-sm resize-none bg-solive-200 bg-opacity-30"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </label>
            </form>
          </div>
        </div>
      </div>
      <div className="flex justify-center">
        <button
          className="w-[300px] px-4 py-2 mt-5 mb-10 text-white border-gray-300 rounded  bg-solive-200"
          onClick={handleSubmit}
        >
          등록
        </button>
      </div>
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
          <div className="bg-white p-8 rounded-lg relative w-72 h-60">
            <h3>문제 등록이 완료되었습니다!</h3>
            <h3> 매칭이 완료 될 때 까지 잠시만 기다려주세요.</h3>
            <button
              className="mt-4 px-4 py-2 bg-blue-500 text-white rounded"
              onClick={handleConfirm}
            >
              확인
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default QuestionRegistration;
