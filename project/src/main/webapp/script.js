// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

var user = "Guest";
var placeID = "";
var placeName = "";
var URLid = "";
var URLname = "";

function start () {
    displaySearchHistory();
    displayFavorites();
    displayPopular();
}

// Fetches the login status from the servlet. If user is logged in, display logout link
// If user is not logged in, display login link
function fetchLoginStatus () {
    fetch('/login').then(response => response.json()).then((login) => {
        const greeting = document.getElementById('login-greeting');
        const loginContainer = document.getElementById('login-container');
        user = login.loginInfo[0];
        var link = login.loginInfo[1];
        greeting.innerText = "Hello, " + user + "!";
        if (user.localeCompare("Guest") != 0){
            loginContainer.innerHTML = '<a href="' + link + '">Logout here</a>';
        } else {
            loginContainer.innerHTML = '<a href="' + link + '">Login here</a>';
        }
    });
}

fetchLoginStatus();

// When "History" button is hovered over, display current user's search history
function displaySearchHistory() {
    fetch('/search').then(response => response.json()).then((searches) => {
        const history = document.getElementById('history-content');
        history.innerHTML = "";
        for (const search in searches) {
            history.appendChild(createHistoryElement(searches[search]));
        }
    });
}

// Creates a search history element
function createHistoryElement(search) {
    const searchElement = document.createElement('a');
    searchElement.innerText = search;
    // When clicked on, search for that keyword
    searchElement.addEventListener("click", () => clearMarkers(), false);
    searchElement.addEventListener("click", () => displaySearch(search, currentRadius, numberOfPlaces), false);
    return searchElement;
}

// Display user's favorite restaurants in "Favorites" bar
function displayFavorites() {
    fetch('/favorites').then(response => response.json()).then((favorites) => {
        const favoritesBar = document.getElementById('favorites-bar');
        favoritesBar.innerHTML = "";
        for (const fav in favorites) {
            favoritesBar.appendChild(createFavoritesElement(favorites[fav]));
        }
    });
}

// Creates a favorites bar element
function createFavoritesElement(favorite) {
    placeName = favorite;
    const br = document.createElement('br');
    const favElement = document.createElement('a');
    favElement.innerText = favorite;
    favElement.appendChild(br);
    fetch('/placeInfo').then(response => response.json()).then((placeInfo) => {
        placeID = placeInfo[placeName];
    });
    favElement.addEventListener("click", () => openModal(placeID, placeName), false);
    return favElement;
}

// Modal opens displaying two options: go to restaurant page, or get shareable link
function openModal (placeID, placeName) {
    document.getElementById("favoritesModal").value = placeID + "," + placeName;
    document.getElementById("favoritesModal").style.display = "block";
}

// When the user clicks on the modal's X button, close it
function closeModal () {
    document.getElementById("favoritesModal").style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    const modal = document.getElementById("favoritesModal");
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// Go to restaurant page
function visitPage() {
    // call function to go to that restaurant's page
    const name = document.getElementById("favoritesModal").value;
    const li = name.split(",");
    selectMarker(li[0], li[1]);
    document.getElementById("favoritesModal").style.display = "none";
}

// Constructs a shortened URL that leads to the chosen restaurant
function shareableURL() {
    const info = document.getElementById("favoritesModal").value;
    const li = info.split(",");
    URLid = li[0];
    URLname = li[1];
    const url = window.location.href+ "&placeID=" + URLid + "&placeName=" + URLname;
    alert("Shareable link: " + url);
} 

// Called once user has selected their preferred filters and pressed the Search button
// Stores filter choices
function filterChoices() {
    const radius = document.querySelector('#mySelectRadius');
    const type = document.querySelector('#mySelectType');
    const price = document.querySelector('#mySelectPrice');
}

// Only display the top 5 most popular restaurants
function displayPopular() {
    fetch('/popular').then(response => response.json()).then((popular) => {
        const popularList = document.getElementById('popular-list');
        popularList.innerHTML = "";

        // Get list of restaurants with the 5 highest popularity scores
        var popScores = [];
        var popNames = [];
        for (const key in popular) {
            popScores.push(popular[key]);
            popNames.push(key);
        }
        var maxLength = 5;
        if (popScores.length < 5) {
            maxLength = popScores.length;
        } 
        for (var i = 0; i < maxLength; i++) {
            const highestScore = Math.max.apply(Math, popScores);
            if (highestScore <= 0) {
                break;
            }
            const index = popScores.indexOf(highestScore);
            if (index > -1) {
                popScores.splice(index, 1);
            }
            const restaurantName = popNames[index];
            popularList.appendChild(createPopularElement(restaurantName, highestScore));
            popNames.splice(index, 1);
        }
    });
}

// Creates a Popular List element
function createPopularElement(restaurant, score) {
    placeName = restaurant;
    const popElement = document.createElement('a');
    const br = document.createElement('br');
    var endString = " users";
    if (score == 1) {
        endString = " user";
    }
    popElement.innerText = restaurant + "; Favorited by " + score + endString;
    popElement.appendChild(br);
    fetch('/placeInfo').then(response => response.json()).then((placeInfo) => {
        placeID = placeInfo[placeName];
    });
    popElement.addEventListener("click", () => openModal(placeID, placeName), false);
    return popElement;
}

// Displays store if placeID is present in url
params = new Map(window.location.search.slice(1,Infinity).split("&").map(x => x.split("=")));
if (params.has("placeID") && map.get("placeID") != undefined) {
	showCatalogue(params.get("placeID"));
}

//Every 1 second, search history, favorites, popular list are updated
setInterval(async function(){ 
    displaySearchHistory();
    displayFavorites();
	displayPopular();
}, 1 * 1 * 1000);