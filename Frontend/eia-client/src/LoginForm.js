import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginForm.css';
import axios from "axios";


const LoginForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const handleSignUpClick = () => {
    navigate('/signup');
  };


  async function login(event) {
    event.preventDefault();
    try {
      await axios.post("http://localhost:8080/login", {
        username: username,
        password: password,
        }).then((res) => {
          console.log(res.data.message);      
          localStorage.setItem("user-id", res.data.uid);
         
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

  return (
    <form onSubmit={login}>
      <div>
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
