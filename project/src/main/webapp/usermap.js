// get filters
// max distance
// use maps, applys to make userMap


const apiKey = "AIzaSyDEVKOx6yO8pM10Jj39HXSkS4HYEv8nBJ8";
var currentZoom = 6;
var currentRadius = 5000;
var currentLocation;
var nearbyMarkerList = [];
var searchedMarkerList = [];

//List of Places Objects for other processing
//var nearbyPlaces = [];

//List of IDs for Tamajong
//var usermap = [];

//Promise for a list of nearby places
var nearbyPlacesPromise;


//Promise for a list of searched places
var searchedPlacesPromise;

var map = new google.maps.Map(document.getElementById('map'), {
	center: {lat: 0, lng: 0},
	zoom: 6
});

async function initMap() {
	currentLocation = await getCurrentLocation();
	map.setCenter(currentLocation);
	displayMap(map,currentZoom);
	nearbyPlacesPromise = getSearchedPlaces(map, currentRadius, 'restaurant');
	nearbyPlacesPromise.then((li) => li.forEach((x) => addMarker(nearbyMarkerList, map, x.geometry.location, x.name, x.icon)));
}

function getCurrentLocation() {
	if (navigator.geolocation) {
		return new Promise((resolve, reject) => {
			navigator.geolocation.getCurrentPosition(function(position) {
				resolve({
					lat: position.coords.latitude,
					lng: position.coords.longitude
				});
			}, function() {
				console.log("Geolocation Failure");
				resolve({});
			});
		});
	} else {
		console.log("Unsupported browser");
	}
}

function displayMap(map ,zoom) {
	var infoWindow = new google.maps.InfoWindow;
	map.setZoom(zoom);
	infoWindow.setPosition(map.center);
	infoWindow.setContent('Current Location');
	infoWindow.open(map);
}

function getNearbyPlaces(map, radius, type) {
	request = {
		location: map.center,
		radius: String(radius),
		type: [type]
	};
	service = new google.maps.places.PlacesService(map);
	return new Promise((resolve, reject) => service.nearbySearch(request, (results, pstatus) => resolve(placesCallback(results, pstatus))));
}

function getSearchedPlaces(map, radius, query) {
	request = {
		location: map.center,
		radius: String(radius),
		query: query
	};
	service = new google.maps.places.PlacesService(map);
	return new Promise((resolve, reject) => service.textSearch(request, (results, pstatus) => resolve(placesCallback(results, pstatus))));
}

function placesCallback(results, pstatus) {
	var placesList = [];
	if (pstatus == google.maps.places.PlacesServiceStatus.OK) {
		for(let i = 0; i < results.length; i++){
			placesList.push(results[i]);
		}
	}
	return placesList;
}


function addMarker(markerList, map, location, labelText, imageLink) {
  var marker = new google.maps.Marker({
    position: location,
    label: labelText,
    icon: imageLink,
    map: map
  });
  markerList.push(marker);
}

function displaySearchResults() {
	searchedPlacesPromise = getSearchedPlaces(map, currentRadius, document.getElementById("searchbox").value);
	nearbyMarkerList.forEach((x) => x.setMap(null));
	searchedPlacesPromise.then((li) => li.forEach((x) => addMarker(searchedMarkerList, map, x.geometry.location, x.name, x.icon)));
}


initMap();



