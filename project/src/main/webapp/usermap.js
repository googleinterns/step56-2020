// get filters
// max distance
// use maps, applys to make userMap


var currentZoom = 12;
var currentRadius = 5000;
// Google Places api has a cap of 20 results
var numberOfPlaces = 20;
var unitDistance = 1;
var markerList = [];
var searchQuery = "restaurant";
var currentLocation, placesPromise;

var infoWindow = new google.maps.InfoWindow;
var map = new google.maps.Map(document.getElementById('map'), {
	center: {lat: 0, lng: 0},
	zoom: 6
});

async function initMap() {
	currentLocation = await getCurrentLocation();
	infoWindow.open(map);
	infoWindow.setContent("Current Location");
	resetMap(currentLocation, searchQuery, currentZoom, currentRadius, numberOfPlaces);
}

function resetMap(center, query, zoom, radius, numberOfPlaces) {
	centerAndZoom(currentLocation, currentZoom);
	clearMarkers();
	displaySearch(searchQuery, currentRadius, numberOfPlaces);
}

function displaySearch(query, radius, displayNumber) {
	placesPromise = getSearchedPlaces(map, radius, query);
	placesPromise.then((li) => li.slice(0, displayNumber).forEach((x) => markerList.push(addMarker(map, x.geometry.location, x.name, x.icon, x.id))));
}

function clearMarkers() {
	markerList.forEach((x) => x.setMap(null));
	markerList = [];
}

function centerAndZoom(center, zoom) {
	map.setCenter(center);
	map.setZoom(zoom);
	infoWindow.setPosition(map.center);
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


function addMarker(map, location, labelText, imageLink, id) {
	var marker = new google.maps.Marker({
		position: location,
		label: labelText,
		icon: imageLink,
		map: map
	});
	marker.addListener("click", function() {
		showCatalogue(id);
		
	});
	return marker;
}

function displaySearchResults() {
	searchQuery = document.getElementById("search-input").value;
        var oReq = new XMLHttpRequest();
        oReq.open("POST", "/search");
        oReq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        oReq.send(`search-input=${searchQuery}`);
	console.log("display test ",searchQuery);
	clearMarkers();
	displaySearch(searchQuery, currentRadius, numberOfPlaces);
}

function radiusChange(sel) {
	currentRadius = parseInt(sel.value);
	console.log("Test ",currentRadius);
	clearMarkers();
	displaySearch(searchQuery, currentRadius, numberOfPlaces);
}



initMap();

//Every 30 seconds, if the new location is unitDistance away from the previous location the map reloads, distance measured in taxicab metric
setInterval(async function(){ 
	newCurrentLocation = await getCurrentLocation();
	if(Math.abs(newCurrentLocation.lat - currentLocation.lat) + Math.abs(newCurrentLocation.lng - currentLocation.lng) >= unitDistance && nearbyMode == 1) {
		resetMap(newCurrentLocation, searchQuery, currentZoom, currentRadius, numberOfPlaces);
	}
}, 1 * 30 * 1000);

