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

/** Class containing users' favorite restaurants. */
public final class Favorites {
    private String userEmail = "";

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Favorites");

    public void addToFavoritesList(String user, String placeID, String placeName) {
        userEmail = user; 
        // Store favorite restaurant in Datastore
        Entity favoriteEntity = new Entity("Favorites");
        favoriteEntity.setProperty("user", user);
        favoriteEntity.setProperty("favoriteID", placeID); 
        favoriteEntity.setProperty("favoriteName", placeName);     
        datastore.put(favoriteEntity);
    }

    public List<String> getFavorites() {
        List<String> favorites = new ArrayList<>();
        // Load user's favorites from Datastore
        PreparedQuery results = datastore.prepare(query);
        for (Entity entity : results.asIterable()) {
            String user = (String) entity.getProperty("user");
            if (user.equals(userEmail)) {
                String fav = (String) entity.getProperty("favoriteName");
                if (!favorites.contains(fav)) {
                    favorites.add(fav);
                }
            }
        }
        return favorites;
    }
}
