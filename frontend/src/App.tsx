import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Signup from './pages/Signup/Signup';
import Home from './pages/Home';
import StudentSignup from './pages/Signup/StudentSignup';
import TeacherSignup from './pages/Signup/TeacherSignup';
import Login from './pages/Login/Login';
import NotFoundImage from './assets/404.png';
import ArticleList from './pages/Board/ArticleList';
import HeaderNav from './components/HeaderNav';
import ArticleDetail from './pages/Board/ArticleDetail';
import ArticleRegist from './pages/Board/ArticleRegist';

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
      </Routes>
    </Router>
  );
}

export default App;
