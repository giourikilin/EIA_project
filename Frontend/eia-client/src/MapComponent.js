// import React, { useEffect } from 'react';

// const MapComponent = ({ mapCoordinates }) => {
//   useEffect(() => {
//     // Load Google Maps API script dynamically
//     const loadMap = () => {
//       const script = document.createElement('script');
//       script.src = `https://maps.googleapis.com/maps/api/js?key=AIzaSyAEIWIXKu0QWUP83yWI5FoDJ2xtsCmRYm0&libraries=places`;
//       script.async = true;
//       script.defer = true;
//       script.onload = initMap;
//       document.head.appendChild(script);
//     };

//     const initMap = () => {
//       const map = new window.google.maps.Map(document.getElementById('map'), {
//         center: mapCoordinates,
//         zoom: 12
//       });

//       const service = new window.google.maps.places.PlacesService(map);
//       service.textSearch({
//         query: 'supermarkets',
//         location: map.getCenter(),
//         radius: 5000
//       }, callback);

//       function callback(results, status) {
//         if (status === window.google.maps.places.PlacesServiceStatus.OK) {
//           for (let i = 0; i < results.length; i++) {
//             createMarker(results[i]);
//           }
//         }
//       }

//       function createMarker(place) {
//         const marker = new window.google.maps.Marker({
//           map: map,
//           position: place.geometry.location
//         });
//       }
//     };

//     loadMap();
//   }, [mapCoordinates]);

//   return  <div id="map" style={{ height: '300px', width: '100%' }}></div>;
// };

// export default MapComponent;