import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import SignupSelect from './pages/Signup/SignupSelect';
import Home from './pages/Home';
import Signup from './pages/Signup/Signup';
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
import SolvePoint from './pages/Student/mypage/SolvePoint';
import QuestionManagement from './pages/Student/mypage/QuestionManagement';
import Teacher from './pages/Teacher/TeacherMain';
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
        <Route path="/student/mypage/profile" element={<Profile />} />
        <Route path="/student/mypage/privacy" element={<Privacy />} />
        <Route path="/student/mypage/solvepoint" element={<SolvePoint />} />
        <Route
          path="/student/mypage/questionmanagement"
          element={<QuestionManagement />}
        />
        <Route path="/teacher" element={<Teacher />} />
      </Routes>
      <FooterNav />
    </Router>
  );
};

export default App;
