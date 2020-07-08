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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import com.google.sps.data.Search;
import com.google.sps.data.Login;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Servlet that handles search data */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {
  
  Search searches = new Search();
  Query query = new Query("Search");
  UserService userService = UserServiceFactory.getUserService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Load searches from Datastore
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = ds.prepare(query);

    Search existing_searches = new Search();
    for (Entity entity : results.asIterable()) {
      String user = (String) entity.getProperty("user");
      String search = (String) entity.getProperty("search");
      existing_searches.addToSearchList(user, search);
    }
    response.setContentType("application/json");
    String json = new Gson().toJson(existing_searches.getSearches());
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the search input from the form
    String search = getParameter(request, "search-input", "");
    response.setContentType("text/html;");

    //get current user's email address
    String user = userService.getCurrentUser().getEmail();

    //add user's new search to the search HashMap
    searches.addToSearchList(user, search);
    response.getWriter().println(searches.getSearches());

    //store searches as entities in Datastore
    Entity searchEntity = new Entity("Search");
    searchEntity.setProperty("user", user);
    searchEntity.setProperty("search", search);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(searchEntity);

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }


  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}


