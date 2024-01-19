import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginForm.css';
import axios from "axios";


const LoginForm = () => {
  // State variables for username, password, and navigation
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  // Event handler for username input change
  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
  };

  // Event handler for password input change
  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  // Event handler for sign-up button click
  const handleSignUpClick = () => {
    navigate('/signup');
  };

   // Async function to handle form submission and user login
  async function login(event) {
    event.preventDefault();
    try {
      // Make a POST request to the login endpoint
      await axios.post("http://localhost:8080/login", {
        withCredentials: false,
        username: username,
        password: password,
        }).then((res) => {
          console.log(res.data.message);
          // Set user-id in local storage if login is successful   
          localStorage.setItem("user-id", res.data.uid);
         // Handle different login outcomes
         if (res.data.message === "Username or Password do not match") {
           alert("Username does not exits");
         } 
         else if(res.data.message === "Login Success") { 
            navigate('/home');
         } 
          else { 
            alert("Username or Password do not match");
         }
      }, fail => {
       console.error(fail); 
      });
    }
     catch (err) {
      alert(err);
    }
  }

  // Render the login form
  return (
    <form onSubmit={login}>
      <div>
        {/* Username input */}
        <label>
          Username:
          <input
            type="text"
            value={username}
            onChange={handleUsernameChange}
            required
          />
        </label>
      </div>
      <div>
         {/* Password input */}
        <label>
          Password:
          <input
            type="password"
            value={password}
            onChange={handlePasswordChange}
            required
          />
        </label>
      </div>
       {/* Login and Sign Up buttons */}
      <div>
        <button type="submit">Login</button>
        <button type="button" onClick={handleSignUpClick}>
          Sign Up
        </button>
      </div>
    </form>
  );
};

export default LoginForm;
