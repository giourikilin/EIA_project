import React, { useState, useEffect} from 'react';
import axios from 'axios';
import './Home.css';
import SearchBar from './SearchBar';
import ItemPost from './ItemPost';

const Home = () => {
  // State to store posts data
  const [postdata, setPosts] = useState([]);
  // State to store search term
  const [searchTerm, setSearchTerm] = useState('');

   // Handler to update search term
  const handleInputChange = (term) => {
    setSearchTerm(term);
  };

  // Fetch posts data on component mount
  useEffect(() => {
    async function fetchItems() {
      try {
        // Make a POST request to load posts for the logged-in user
        const response = await axios.post("http://localhost:8080/loadposts", {
          searchTerm: null,
          user_id: parseInt(localStorage.getItem("user-id"), 10),
        });
        // If the response data is an array, transform and set the posts data
        if (Array.isArray(response.data)) {
          const transformedData = response.data.map((item) => ({
            title: item.title,
            pic: item.pic,
            ing: item.ing,
            vid_url: item.vid_url,
            coord: { lat: parseFloat(localStorage.getItem("lat")), lng: parseFloat(localStorage.getItem("long")) }
          }));
          // Set posts data with a delay for a visual effect
          for (let i = 0; i < transformedData.length; i++) {
            await new Promise((resolve) => setTimeout(resolve, 800));
            setPosts((postdata) => [...postdata, transformedData[i]]);
          }
        } else {
          console.error('Received data is not an array or is empty:', response.data);
        }
      }
       catch (err) {
        alert(err);
      }
    }
    fetchItems();
  },[]);

  // Handler to handle button click for searching posts
  const handleButtonClick = async () => {
    try {
      // Get user's geolocation if supported
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
          localStorage.setItem("lat",position.coords.latitude)
          localStorage.setItem("long", position.coords.longitude)
        }, (error) => {
          console.error("Error getting user's location:", error);
        });
    } else {
      console.log('Geolocation is not supported by this browser.');
    } 
      console.log(searchTerm);
      // Make a POST request to search for posts based on the search term
      const response = await axios.post("http://localhost:8080/searchPosts", {
        searchTerm: searchTerm,
        user_id: parseInt(localStorage.getItem("user-id"), 10),
      });
      // If the response data is an array, transform and update the posts data
      if (Array.isArray(response.data)) {
        const newData = response.data.map((item) => ({
          title: item.title,
          pic: item.pic,
          ing: item.ing,
          vid_url: item.vid_url,
          coord: { lat: parseFloat(localStorage.getItem("lat")), lng: parseFloat(localStorage.getItem("long")) }
        }));
        // Update posts data
        setPosts((postdata) => [...postdata, ...newData]);
      } else {
        console.error('Received data is not an array or is empty:', response.data);
      }
    } catch (error) {
      console.error('Error searching:', error);
    }
  };

   // Render the component
    return (
      <div className="home-page">
        <h1>Welcome to the Home Page</h1>
          <div className="search-container">
            <SearchBar
              handleInputChange={handleInputChange}
              handleButtonClick={handleButtonClick}
            />
          </div>
          <div className="vertical-section">
            <h2>Recipes</h2> 
              <div className="object-list">
                {/* Render ItemPost component for each post in the posts data */}
                {postdata.map((item, index) => (
                  <ItemPost key={index} item={item}/>                  
                ))}
              </div>
          </div>
      </div>
    );
  };
  
  export default Home;