import React from 'react';
import { Link } from 'react-router-dom';
import logo from '../assets/logo.png';

function Navbar(): JSX.Element {
  return (
    <nav className="fixed w-full flex items-center justify-between flex-wrap bg-white p-4 z-10">
      <div className="flex items-center flex-shrink-0 mr-6">
        <img src={logo} alt="Logo" className="h-10 w-auto" />
      </div>
      <div className="w-full block flex-grow lg:flex lg:items-center lg:w-auto">
        <div className="text-sm lg:flex-grow"></div>
        <div>
          <Link
            to="/"
            className="block mt-4 lg:inline-block lg:mt-0 text-black hover:text-white mr-4"
          >
            Home
          </Link>
          <Link
            to="/signup"
            className="block mt-4 lg:inline-block lg:mt-0 text-black hover:text-white mr-4"
          >
            Signup
          </Link>
          {/* 수정 navbar에 로그인 추가 */}
          <Link
            to="/login"
            className="block mt-4 lg:inline-block lg:mt-0 text-black hover:text-white mr-4"
          >
            Login
          </Link>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
