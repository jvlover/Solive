import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import SignupSelect from './pages/Signup/SignupSelect';
import Home from './pages/Home';
import Signup from './pages/Signup/Signup';
import Login from './pages/Login/Login';
import QuestionRegistration from './pages/Student/QuestionRegistration';
import HeaderNav from './components/HeaderNav';
import Dial from './components/Dial';
import FooterNav from './components/FooterNav';
import ArticleList from './pages/Board/ArticleList';
import ArticleDetail from './pages/Board/ArticleDetail';
import ArticleRegist from './pages/Board/ArticleRegist';
import ArticleModify from './pages/Board/ArticleModify';
import Teacher from './pages/Teacher/TeacherMain';
import Error404 from './pages/Error404';
import MyPage from './pages/MyPage/MyPage';
import TeacherQuestion from './pages/Teacher/Question';

const App = () => {
  return (
    <Router>
      <HeaderNav />
      <Dial />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="*" element={<Error404 />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignupSelect />} />
        <Route path="/board" element={<ArticleList />} />
        <Route path="/board/:id" element={<ArticleDetail />} />
        <Route path="/board/regist" element={<ArticleRegist />} />
        <Route path="/board/modify/:id" element={<ArticleModify />} />
        <Route path="/signup/:userType" element={<Signup />} />
        <Route
          path="student/questionregistration"
          element={<QuestionRegistration />}
        />
        <Route path="/mypage/:pageName" element={<MyPage />} />
        <Route path="/teacher" element={<Teacher />} />
        <Route path="/teacher/question" element={<TeacherQuestion />} />
      </Routes>
      <FooterNav />
    </Router>
  );
};

export default App;
