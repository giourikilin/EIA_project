import React, { useState, useEffect} from 'react';
import axios from 'axios';
import './Home.css';
import SearchBar from './SearchBar';
import ItemPost from './ItemPost';

const Home = () => {
  const [postdata, setPosts] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');

  const handleInputChange = (term) => {
    setSearchTerm(term);
  };

  useEffect(() => {
    async function fetchItems() {
      try {
        const response = await axios.post("http://localhost:8080/loadposts", {
          searchTerm: null,
          user_id: parseInt(localStorage.getItem("user-id"), 10),
        });
        if (Array.isArray(response.data)) {
          const transformedData = response.data.map((item) => ({
            title: item.title,
            pic: item.pic,
            ing: item.ing,
            vid_url: item.vid_url,
            coord: { lat: parseFloat(localStorage.getItem("lat")), lng: parseFloat(localStorage.getItem("long")) }
          }));
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

  const handleButtonClick = async () => {
    try {
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
      const response = await axios.post("http://localhost:8080/searchPosts", {
        searchTerm: searchTerm,
        user_id: parseInt(localStorage.getItem("user-id"), 10),
      });
      if (Array.isArray(response.data)) {
        const newData = response.data.map((item) => ({
          title: item.title,
          pic: item.pic,
          ing: item.ing,
          vid_url: item.vid_url,
          coord: { lat: parseFloat(localStorage.getItem("lat")), lng: parseFloat(localStorage.getItem("long")) }
        }));
        setPosts((postdata) => [...postdata, ...newData]);
      } else {
        console.error('Received data is not an array or is empty:', response.data);
      }
    } catch (error) {
      console.error('Error searching:', error);
    }
  };

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
                {postdata.map((item, index) => (
                  <ItemPost key={index} item={item}/>                  
                ))}
              </div>
          </div>
      </div>
    );
  };
  
  export default Home;