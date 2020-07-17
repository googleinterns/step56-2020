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
import java.util.Map;
import java.util.HashMap;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Class containing restaurants' popularity scores (# of times 'favorited'). */
public final class Popular {
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  Query query = new Query("Popular");

  // Store restaurant's popularity score (# times it has been 'favorited') in Datastore
  public void addToPopularList(String placeID, String placeName) {
    // Load restaurants'popularity scores from Datastore to check if place already exists in popular list
    boolean alreadyExists = false;
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
        String place = (String) entity.getProperty("placeID");
        if (place.equals(placeID)) {
            alreadyExists = true;
            // Increment popularity score by 1
            int curPopularity = (int) entity.getProperty("score");
            entity.setProperty("score", curPopularity + 1); 
            break;
        }
    }
    if (!alreadyExists) {
        Entity popularEntity = new Entity("Popular");
        popularEntity.setProperty("placeID", placeID);
        popularEntity.setProperty("placeName", placeName);
        popularEntity.setProperty("score", 1);   
        datastore.put(popularEntity);
    }
  }

  // Returns a list of all popular restaurants and their popularity scores
  public Map<String, Integer> getPopular() {
    Map<String, Integer> popular = new HashMap<>();
    // Load popular restaurants from Datastore
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
        String place = (String) entity.getProperty("placeName");
        int popularityScore = (int) entity.getProperty("score");
        popular.put(place, popularityScore);
    }
    return popular;
  }
}
