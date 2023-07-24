import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar';
import Signup from './pages/Signup/Signup';
import Home from './pages/Home';
import StudentSignup from './pages/Signup/StudentSignup';
import TeacherSignup from './pages/Signup/TeacherSignup';

function App(): JSX.Element {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/signup" element={<Signup />} />
        <Route path="/" element={<Home />} />
        <Route path="/signup/StudentSignup" element={<StudentSignup />} />
        <Route path="/signup/TeacherSignup" element={<TeacherSignup />} />
      </Routes>
    </Router>
  );
}

export default App;
