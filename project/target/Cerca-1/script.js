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

// Fetches the login status from the servlet. If user is logged in, display logout link
// If user is not logged in, display login link
function fetchLoginStatus () {
    fetch('/login').then(response => response.json()).then((login) => {
        const greeting = document.getElementById('login-greeting');
        const loginContainer = document.getElementById('login-container');
        user = login.loginInfo[0];
        var link = login.loginInfo[1];
        greeting.innerText = "Hello " + user;
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
      console.log(searches);
      console.log(user);
      console.log(searches[user]);
      console.log(searches[user][0]);
    
    const history = document.getElementById('history-content');
    for (const search in searches[user]) {
        history.appendChild(createHistoryElement(searches[user][search]));
    }
  });
}

// Creates a search history element
function createHistoryElement(search) {
  const searchElement = document.createElement('a');
  searchElement.innerText = search;
  return searchElement;
}

function filterChoices() {
    const radius = document.getElementById("mySelectRadius").selectedIndex;
    const type = document.getElementById("mySelectType").selectedIndex + 4;
    const price = document.getElementById("mySelectPrice").selectedIndex + 9;
    console.log(document.getElementsByTagName("option")[radius].value);
    console.log(document.getElementsByTagName("option")[type].value);
    console.log(document.getElementsByTagName("option")[price].value);
}