import './App.css';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginForm from './LoginForm';
import SignupForm from './SignupForm';
import Home from './Home';


function App() {
  return ( 
        <Router>
            <Routes>
              <Route path="/" element={<LoginForm />} />
              <Route path="/signup" element={<SignupForm />} />
              <Route path="/home" element={<Home />} />
            </Routes>
        </Router>
  );
}

export default App;
