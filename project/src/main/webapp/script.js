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

//Fetches the login status from the servlet. If user is logged in, display logout link
//if user is not logged in, display login link
function fetchLoginStatus () {
    fetch('/login').then(response => response.json()).then((login) => {
        const greeting = document.getElementById('login-greeting');
        greeting.innerText = "Hello " + login.loginInfo[0];
        
        if (login.loginInfo[0].localeCompare("Guest") != 0){
            const loginContainer = document.getElementById('login-container');
            loginContainer.innerHTML = '<a href="' + login.loginInfo[1] + '">Logout here</a>';
        } else {
            const loginContainer = document.getElementById('login-container');        
            loginContainer.innerHTML = '<a href="' + login.loginInfo[1] + '">Login here</a>';
        }
    });
}

fetchLoginStatus();
