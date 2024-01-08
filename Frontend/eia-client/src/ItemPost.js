import React from 'react';
import './ItemPost.css'
//import MapComponent from './MapComponent'; 

const ListItem = ({ item }) => {

  // const { title, picture, ingredients, video_url, local_map } = item;
  const { title, pic, ing, vid_url } = item;

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

      {/* {local_map && (
        <div className="map-section">
          <MapComponent mapCoordinates={local_map} />
        </div>
      )} */}
    </div>
  );
};

export default ListItem;