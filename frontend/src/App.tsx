import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Signup from './pages/Signup/Signup';
import Home from './pages/Home';
import StudentSignup from './pages/Signup/StudentSignup';
import TeacherSignup from './pages/Signup/TeacherSignup';
import Login from './pages/Login/Login';
import NotFoundImage from './assets/404.png';
import QuestionRegistration from './pages/Student/QuestionRegistration';
import Profile from './pages/Student/mypage/Profile';
import ArticleList from './pages/Board/ArticleList';
import HeaderNav from './components/HeaderNav';
import ArticleDetail from './pages/Board/ArticleDetail';
import ArticleRegist from './pages/Board/ArticleRegist';
import ArticleModify from './pages/Board/ArticleModify';


function App(): JSX.Element {
  return (
    <Router>
      <HeaderNav />
      <Routes>
        <Route path="/signup" element={<Signup />} />
        <Route path="/" element={<Home />} />
        <Route path="/board" element={<ArticleList />} />
        <Route path="/board/:id" element={<ArticleDetail />} />
        <Route path="/board/regist" element={<ArticleRegist />} />
        <Route path="/board/modify/:id" element={<ArticleModify />} />
        <Route path="/signup/StudentSignup" element={<StudentSignup />} />
        <Route path="/signup/TeacherSignup" element={<TeacherSignup />} />
        <Route path="/login" element={<Login />} />
        <Route
          path="*"
          element={
            <div className="flex flex-col items-center justify-center h-screen">
              <img
                src={NotFoundImage}
                alt="Page not found"
                className="w-1/4 h-auto object-contain"
              />
              <h1 className="mt-20">404 not found</h1>
            </div>
          }
        />
        <Route
          path="student/questionregistration"
          element={<QuestionRegistration />}
        />
        <Route path="/student/mypage/profile" element={<Profile />} />
      </Routes>
    </Router>
  );
}

export default App;
