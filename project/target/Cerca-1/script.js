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

function start () {
    displaySearchHistory();
    displayFavorites();
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

// Display user's favorite restaurants in "Favorites" bar
function displayFavorites() {
    fetch('/favorites').then(response => response.json()).then((favorites) => {
        const favoritesBar = document.getElementById('favorites-bar');
        for (const fav in favorites) {
            favoritesBar.appendChild(createFavoritesElement(favorites[fav]));
        }
    });
}

// Creates a favorites bar element
function createFavoritesElement(favorite) {
  const favElement = document.createElement('a');
  favElement.innerText = favorite;
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
    //
}

// Constructs a shortened URL that leads to the chosen restaurant
function shareableURL() {
    const currentURL = window.location.href;
    //shareableURL = ;
    alert("Shareable link: " + currentURL);
}

// Called once user has selected their preferred filters and pressed the Search button
// Stores filter choices
function filterChoices() {
    const radius = document.getElementById("mySelectRadius").selectedIndex;
    const type = document.getElementById("mySelectType").selectedIndex + 4;
    const price = document.getElementById("mySelectPrice").selectedIndex + 9;
    console.log(document.getElementsByTagName("option")[radius].value);
    console.log(document.getElementsByTagName("option")[type].value);
    console.log(document.getElementsByTagName("option")[price].value);
}