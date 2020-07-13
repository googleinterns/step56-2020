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

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.Favorites;
import com.google.gson.Gson;

@WebServlet("/favorites")
public class FavoritesServlet extends HttpServlet {
  Favorites favorites = new Favorites();
  UserService userService = UserServiceFactory.getUserService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {    
    // Get current user's email address
    String user = userService.getCurrentUser().getEmail();

    // !! Check on this !!
    // Get the restaurant ID from the server
    String placeID = request;

    // Store user's favorited restaurant
    favorites.addToFavoritesList(user, placeID);

    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(favorites.getFavorites());
    response.getWriter().println(json);
  }
}
