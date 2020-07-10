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

package com.google.sps.data;
import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Class containing user search history. */
public final class Search {
  // List of user's search history
  private final List<String> searches = new ArrayList<>();
  private String userEmail = "";

  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  Query query = new Query("Search");

  public void addToSearchList(String user, String search) {
    userEmail = user; 
    // Store search in Datastore
    Entity searchEntity = new Entity("Search");
    searchEntity.setProperty("user", user);
    searchEntity.setProperty("search", search);   
    datastore.put(searchEntity);
  }

  public List<String> getSearches() {
    // Load user's searches from Datastore
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      String user = (String) entity.getProperty("user");
      if (user.equals(userEmail)) {
        String search = (String) entity.getProperty("search");
        if (!searches.contains(search)) {
            searches.add(search);
        }
      }
    }
    return searches;
  }

}
