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
        //start();
    });
}

fetchLoginStatus();

// When "History" button is hovered over, display current user's search history
function displaySearchHistory() {
    fetch('/search').then(response => response.json()).then((searches) => {
        const history = document.getElementById('history-content');
        for (const search in searches) {
            history.appendChild(createHistoryElement(searches[search]));
        }
    });
}

// Creates a search history element
function createHistoryElement(search) {
    const searchElement = document.createElement('a');
    searchElement.innerText = search;
    return searchElement;
}

/*
// Adds the selected restaurant to favorites
function addFavorite(placeID) {
	var oReq = new XMLHttpRequest();
    oReq.open("POST", "/favorites");
    oReq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    oReq.send(`placeID=${placeID}`);
}
*/

// Display user's favorite restaurants in "Favorites" bar
function displayFavorites() {
    fetch('/favorites').then(response => response.json()).then((favorites) => {
        console.log("favorites: " + favorites);
        const favoritesBar = document.getElementById('favorites-bar');
        favoritesBar.innerHTML = "";
        for (const fav in favorites) {
            favoritesBar.appendChild(createFavoritesElement(favorites[fav]));
        }
    });
}

// Creates a favorites bar element
function createFavoritesElement(favorite) {
    placeID = favorite;
    const br = document.createElement('br');
    const favElement = document.createElement('a');
    favElement.innerText = favorite;
    favElement.appendChild(br);
    //favElement.addEventListener("click", openModal(), false);
    return favElement;
}

// Modal opens displaying two options: go to restaurant page, or get shareable link
function openModal () {
    document.getElementById("favoritesModal").style.display = "block";
}

// When the user clicks on the modal's X button, close it
function closeModal () {
    document.getElementById("favoritesModal").style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    var modal = document.getElementById("favoritesModal");
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// Go to restaurant page
function visitPage() {
    const shareableURL = window.location.href+"?placeID=<placeID>";
    window.location.replace(shareableURL); 
}

// Constructs a shortened URL that leads to the chosen restaurant
function shareableURL() {
    //const currentURL = window.location.href;
    const shareableURL = window.location.href+"?placeID=<placeID>";
    alert("Shareable link: " + shareableURL);
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
    const popElement = document.createElement('a');
    const br = document.createElement('br');
    popElement.innerText = restaurant + "; Favorited by " + score + " users";
    popElement.appendChild(br);
    //popElement.addEventListener("click", openModal(), false);
    return popElement;
}

// Displays store if placeID is present in url
params = new Map(window.location.search.slice(1,Infinity).split("&").map(x => x.split("=")));
if (params.has("placeID") && map.get("placeID") != undefined) {
	showCatalogue(params.get("placeID"));
}
