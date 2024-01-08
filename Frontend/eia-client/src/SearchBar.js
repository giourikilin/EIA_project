import React from 'react';
import './SearchBar.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';

const SearchBar = ({ handleButtonClick, handleInputChange }) => {

  return (
    <div className="search-bar">
        <input
            type="text"
            placeholder="Search..."
            onChange={(e) => handleInputChange(e.target.value)}
        />
        <button onClick={handleButtonClick}>
            <FontAwesomeIcon icon={faSearch} />
        </button>
    </div>
  );
};

export default SearchBar;