// get filters
// max distance
// use maps, applys to make userMap


const apiKey = "AIzaSyDEVKOx6yO8pM10Jj39HXSkS4HYEv8nBJ8";
var currentZoom = 6;
var currentRadius = 5000;
var nearbyPlaces = [];
var currentLocation;

var map = new google.maps.Map(document.getElementById('map'), {
	center: {lat: 0, lng: 0},
	zoom: 6
});

async function initMap() {
	currentLocation = await getCurrentLocation();
	map.setCenter(currentLocation);
	displayMap(map,currentZoom);
	setNearbyPlaces(map, currentLocation, currentRadius, 'resturant');
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

function setNearbyPlaces(map, clocation, radius, type) {
	request = {
		location: clocation,
		radius: String(radius),
		type: [type]
	};
	service = new google.maps.places.PlacesService(map);
	service.nearbySearch(request, placesCallback);
}

function placesCallback(results, pstatus) {
	if (pstatus == google.maps.places.PlacesServiceStatus.OK) {
		for(let i = 0; i < results.length; i++){
			nearbyPlaces.push(results[i]);
		}
	}
}

initMap();
var userMap = nearbyPlaces.map((x) => x.id);
