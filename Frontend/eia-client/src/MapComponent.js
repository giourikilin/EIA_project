import React, {useEffect, useRef} from 'react';
const mapCoordinates = {
        lat: parseFloat(localStorage.getItem("lat")),
        lng: parseFloat(localStorage.getItem("long")),
      };
  
  window.initMap = () => {
    const map = new window.google.maps.Map(document.getElementById('map'), {
      center: mapCoordinates,
      zoom: 12,
      streetViewControl: false
    });
  
    const service = new window.google.maps.places.PlacesService(map);
    service.textSearch({
      query: 'supermarket',
      location: map.getCenter(),
      radius: 3000
    }, callback);
  
    function callback(results, status) {
      if (status === window.google.maps.places.PlacesServiceStatus.OK) {
        for (let i = 0; i < results.length; i++) {
          createMarker(results[i]);
        }
      }
    }
  
    const infoWindow = new window.google.maps.InfoWindow();
  
    function createMarker(place) {
      const marker = new window.google.maps.Marker({
        map: map,
        position: place.geometry.location
      });
  

      marker.addListener('click', () => {
        const request = {
          placeId: place.place_id, 
          fields: ['name', 'formatted_address', 'rating', 'photos', 'website', 'opening_hours']
        };
  
        service.getDetails(request, (placeDetails, status) => {
          if (status === window.google.maps.places.PlacesServiceStatus.OK) {
            const photoUrl = placeDetails.photos && placeDetails.photos[0].getUrl({
                maxWidth: 200, 
                maxHeight: 150
              });

              const openingHours = placeDetails.opening_hours
              ? `<p>Opening Hours: ${placeDetails.opening_hours.weekday_text.join('<br>')}</p>`
              : '';
  
              const storeWebsite = placeDetails.website
              ? `<p><a href="${placeDetails.website}" target="_blank" rel="noopener noreferrer">Store Website</a></p>`
              : '';
  
              const content = `
              <div>
                <h3>${placeDetails.name}</h3>
                <p>${placeDetails.formatted_address}</p>
                <p>Rating: ${placeDetails.rating || 'N/A'}</p>
                ${openingHours}
                ${storeWebsite}
                ${photoUrl ? `<img src="${photoUrl}" alt="Place Photo" />` : ''}
              </div>
            `;

            infoWindow.setContent(content);
            infoWindow.open(map, marker);
          } else {
            console.error('Error fetching place details:', status);
          }
        });
      });
    }
  };
  
  const MapComponent = () => {
    const mapRef = useRef(null);
  
    useEffect(() => {
      const loadMap = () => {
        const script = document.createElement('script');
        script.src = `https://maps.googleapis.com/maps/api/js?key=AIzaSyC3xrvtbutXbt__arWWp0idwNbKZZnOlIc&libraries=places&callback=initMap`;
        script.async = true;
        script.defer = true;
        document.head.appendChild(script);
        mapRef.current = window.initMap;
      };
  
      loadMap();
    }, [mapCoordinates]);
  
    useEffect(() => {
      return () => {
        if (mapRef.current && mapRef.current.infoWindow) {
          mapRef.current.infoWindow.close();
        }
      };
    }, []);
  
    return <div id="map" style={{ height: '450px', width: '100%' }}></div>;
  };
  
  export default MapComponent;
