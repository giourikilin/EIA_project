import React from 'react';
import './ItemPost.css'
import MapComponent from './MapComponent';
import axios from 'axios';

const ListItem = ({ item }) => {

  const { title, pic, ing, vid_url, coord } = item;


  const handleSaveRecipe = async () => {
    try {
      const response = await axios.post("http://localhost:8080/savePost", {
        userID: parseInt(localStorage.getItem("user-id"), 10),  
        title: item.title,
        pic: item.pic,
        ing: item.ing,
        vid_url: item.vid_url,
      });
      if (response.ok) {
        console.log('Recipe saved successfully');
      } else {
        console.error('Failed to save recipe');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <div className="list-item">
      <h3>{title}</h3>

      {pic && <img src={pic} alt="Placeholder" />}

      <p>{ing}</p>

      {vid_url && (
        <div className="youtube-video">
          <iframe
            title="YouTube Video"
            width="560"
            height="315"
            src={`https://www.youtube.com/embed/${vid_url}`}
            allowFullScreen
          ></iframe>
        </div>
      )}

      {coord && (      
      <div className="map-container">
        <MapComponent userLocation={coord}/>
      </div>)}
      <div>
        <button type="button"  onClick={handleSaveRecipe}>Save Recipe</button>
      </div>
    </div>
  );
};

export default ListItem;