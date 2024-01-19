import React, { useState } from 'react';
import './SignupForm.css';
import { useNavigate } from 'react-router-dom';
import axios from "axios";

const SignupForm = () => {

  // State for form fields
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const navigate = useNavigate();

  // Hook for navigating between pages
  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
  };

  // Event handler for username input change
  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  // Event handler for password input change
  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  // Event handler for email input change
  async function save(event) {
    event.preventDefault();
    try {
       // Send signup request to the server
      await axios.post("http://localhost:8080/signup", {
      username: username,
      password: password,
      email: email,
      });
       // Display success message and navigate to the home page
      alert("User Registation Successfully");
      navigate('/');
    } catch (err) {
      // Display an alert in case of an error
      alert(err);
    }
  }

   // Render the signup form
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
