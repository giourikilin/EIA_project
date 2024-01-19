import React from 'react';
import './ItemPost.css'
import MapComponent from './MapComponent';
import axios from 'axios';


const ListItem = ({ item }) => {

  // Destructuring properties from the 'item' prop
  const { title, pic, ing, vid_url, coord } = item;

  // Handler to save the recipe by making a POST request
  const handleSaveRecipe = async () => {
    try {
      // Make a POST request to save the recipe
      const response = await axios.post("http://localhost:8080/savePost", {
        userID: parseInt(localStorage.getItem("user-id"), 10),  
        title: item.title,
        pic: item.pic,
        ing: item.ing,
        vid_url: item.vid_url,
      });
      // Check if the response indicates success or failure
      if (response.ok) {
        console.log('Recipe saved successfully');
      } else {
        console.error('Failed to save recipe');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  // Render the component
  return (
    <div className="list-item">
      <h3>{title}</h3>
      
      {/* Display an image if 'pic' is present */}
      {pic && <img src={pic} alt="Placeholder" />}

      <p>{ing}</p>

      {/* Display a YouTube video if 'vid_url' is present */}
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
       {/* Display a map if 'coord' is present */}
      {coord && (      
      <div className="map-container">
        <MapComponent userLocation={coord}/>
      </div>)}
      {/* Button to trigger the 'handleSaveRecipe' function */}
      <div>
        <button type="button"  onClick={handleSaveRecipe}>Save Recipe</button>
      </div>
    </div>
  );
};

export default ListItem;