import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar';
import Signup from './pages/Signup/Signup';
import Home from './pages/Home';
import StudentSignup from './pages/Signup/StudentSignup';
import TeacherSignup from './pages/Signup/TeacherSignup';
import Login from './pages/Login/Login';
import QuestionRegistration from './pages/Student/QuestionRegistration';
import Profile from './pages/Student/mypage/Profile';
import Privacy from './pages/Student/mypage/Privacy';
import HeaderNav from './components/HeaderNav';
import Dial from './components/Dial';
import FooterNav from './components/FooterNav';
import ArticleList from './pages/Board/ArticleList';
import ArticleDetail from './pages/Board/ArticleDetail';
import ArticleRegist from './pages/Board/ArticleRegist';
import ArticleModify from './pages/Board/ArticleModify';
import Error404 from './pages/Error404';

const App = () => {
  return (
    <Router>
      <HeaderNav />
      <Dial />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="*" element={<Error404 />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/board" element={<ArticleList />} />
        <Route path="/board/:id" element={<ArticleDetail />} />
        <Route path="/board/regist" element={<ArticleRegist />} />
        <Route path="/board/modify/:id" element={<ArticleModify />} />
        <Route path="/signup/StudentSignup" element={<StudentSignup />} />
        <Route path="/signup/TeacherSignup" element={<TeacherSignup />} />
        <Route
          path="student/questionregistration"
          element={<QuestionRegistration />}
        />
        <Route path="/student/mypage/profile" element={<Profile />} />
        <Route path="/student/mypage/privacy" element={<Privacy />} />
      </Routes>
      <FooterNav />
    </Router>
  );
};

export default App;
