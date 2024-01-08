import React, { useState } from 'react';
import './SignupForm.css';
import { useNavigate } from 'react-router-dom';
import axios from "axios";



const SignupForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const navigate = useNavigate();

  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  async function save(event) {
    event.preventDefault();
    try {
      await axios.post("http://localhost:8080/signup", {
      username: username,
      password: password,
      email: email,
      });
      alert("User Registation Successfully");
      navigate('/');
    } catch (err) {
      alert(err);
    }
  }
  return (
    <form onSubmit={save}>
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
        <label>
          Email:
          <input
            type="email"
            value={email}
            onChange={handleEmailChange}
            required
          />
        </label>
      </div>
      <div>
        <button type="submit">Sign Up</button>
      </div>
    </form>
  );
};

export default SignupForm;
