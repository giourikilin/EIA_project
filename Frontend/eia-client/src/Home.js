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
        userId: parseInt(localStorage.getItem("user-id"), 10),
      });
      if (Array.isArray(response.data)) {
        const newData = response.data.map((item) => ({
          title: item.title,
          pic: item.pic,
          ing: item.ing,
          vid_url: item.vid_url,
        }));
        setPosts(postdata => [...postdata, ...newData]);

      } else {
        console.error('Received data is not an array or is empty:', response.data);
      }
    } catch (error) {
      console.error('Error searching:', error);
    }
  };

  useEffect(() => {
    console.log(postdata.length);
  }, [postdata]);

  // useEffect(() => {
  //   async function fetchItems() {
  //     try {
  //       const response = await axios.post("http://localhost:8080/loadposts", {
  //         userid: parseInt(localStorage.getItem("user-id"), 10)
  //       });
  //       if (Array.isArray(response.data)) {
  //         const transformedData = response.data.map((item) => ({
  //           title: item.title,
  //           picture: item.picture,
  //           ingredients: item.ingredients,
  //           video_url: item.video_url,
  //           // local_map: item.local_map
  //         }));
  //         setPosts(transformedData);
  //       } else {
  //         console.error('Received data is not an array or is empty:', response.data);
  //       }
  //     }
  //      catch (err) {
  //       alert(err);
  //     }
  //   }

  //   fetchItems();
  // },[]);

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
                  <ItemPost key={index} item={item} />
                ))}
              </div>
          </div>
      </div>
      
    );
  };
  
  export default Home;