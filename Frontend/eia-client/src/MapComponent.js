import React, { useEffect, useState } from 'react';
 

const MapComponent = ({ userLocation }) => {
  // State for supermarkets data
  const [supermarkets, setSupermarkets] = useState([]);
  // Reference to the map container
  const mapContainerRef = React.useRef(null);
  // Generate a random map ID
  const mapId = `map-${Math.random().toString(36).substring(7)}`;

  // Effect for initializing Google Maps API script
  useEffect(() => {
    if (window.google && window.google.maps) {
      window.initMap();
    } else {
      // Create and append a script for Google Maps API
      const script = document.createElement('script');
      script.src = `https://maps.googleapis.com/maps/api/js?key=AIzaSyB6BioztfY7RHuPgHWknVHz9daP3GgfGgU&libraries=places&callback=initMap`;
      script.async = true;
      script.defer = true;

      document.head.appendChild(script);

      // Attach load event listener for script and cleanup on component unmount
      script.addEventListener('load', window.initMap);

      return () => {
        document.head.removeChild(script);
        delete window.initMap;
      };
    }
  }, [userLocation]);

 
  // Effect for rendering the map and markers when supermarkets data changes
  useEffect(() => {
    if (supermarkets.length > 0 && window.google) {
      const map = new window.google.maps.Map(mapContainerRef.current, {
        center: userLocation,
        zoom: 15,
        streetViewControl: false
      });

      const markers = [];

      // Iterate through supermarkets and create markers
      supermarkets.forEach(supermarket => {
        const marker = new window.google.maps.Marker({
          position: supermarket.geometry.location,
          map,
          title: supermarket.name,
        });
        markers.push(marker);

        // Add click event listener to each marker to show details
        const infoWindow = new window.google.maps.InfoWindow();
        const service = new window.google.maps.places.PlacesService(document.createElement('div'));

        marker.addListener('click', () => {
          // Fetch details for the clicked supermarket
          const request = {
            placeId: supermarket.place_id, 
            fields: ['name', 'formatted_address', 'rating', 'photos', 'website', 'opening_hours']
          };
    
          service.getDetails(request, (placeDetails, status) => {
            if (status === window.google.maps.places.PlacesServiceStatus.OK) {
              // Store picture 
              const photoUrl = placeDetails.photos && placeDetails.photos[0].getUrl({
                  maxWidth: 200, 
                  maxHeight: 150
                });
  
                // Openning hours details
                const openingHours = placeDetails.opening_hours
                ? `<p>Opening Hours: ${placeDetails.opening_hours.weekday_text.join('<br>')}</p>`
                : '';
    
                // Store url
                const storeWebsite = placeDetails.website
                ? `<p><a href="${placeDetails.website}" target="_blank" rel="noopener noreferrer">Store Website</a></p>`
                : '';
    
                // Combining details
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
  
              // Display the info window on the map
              infoWindow.setContent(content);
              infoWindow.open(map, marker);
            } else {
              console.error('Error fetching place details:', status);
            }
          });
        });
      });
    }
  }, [userLocation, supermarkets]);


  // Google Maps API callback function for fetching nearby supermarkets
  window.initMap = () => {
    try {
      const request = {
        location: userLocation,
        radius: 3000,
        type: 'supermarket',
      };

      const service = new window.google.maps.places.PlacesService(document.createElement('div'));

      // Fetch nearby supermarkets and set the state
      service.nearbySearch(request, (results, status) => {
        if (status === window.google.maps.places.PlacesServiceStatus.OK) {
          setSupermarkets(results);
        } else {
          console.error('Failed to fetch supermarkets:', status);
        }
      });
    } catch (error) {
      console.error('Error fetching supermarkets:', error);
    }
  };

  // Render the map container
  return <div ref={mapContainerRef} id={mapId} style={{ height: '450px', width: '100%' }} />;
};

export default MapComponent;