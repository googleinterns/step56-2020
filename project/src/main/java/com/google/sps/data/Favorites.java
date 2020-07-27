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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;

/** Class containing users' favorite restaurants. */
public final class Favorites {
    private String userEmail = "";
    DatastoreService datastore;
    Query query = new Query("Favorites");

    public Favorites() {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    public Favorites(DatastoreService ds) {
        datastore = ds;
    }   

    public void addToFavoritesList(String user, String placeID, String placeName) {
        System.out.println("adding to fav list");
        userEmail = user; 
        // Store favorite restaurant in Datastore
        Entity favoriteEntity = new Entity("Favorites");
        favoriteEntity.setProperty("user", user);
        favoriteEntity.setProperty("favoriteID", placeID); 
        favoriteEntity.setProperty("favoriteName", placeName);     
        datastore.put(favoriteEntity);
    }

    
    public void removeFromFavoritesList(String user, String placeID, String placeName) {
        System.out.println("removing from fav list");
        userEmail = user; 
        // Remove favorite restaurant in Datastore
        Filter propertyFilter = new FilterPredicate("user", FilterOperator.EQUAL, userEmail);
        Query q = new Query("Favorites").setFilter(propertyFilter);
        PreparedQuery results = datastore.prepare(q);
        for (Entity entity : results.asIterable()) {
            String fav = (String) entity.getProperty("favoriteName");
            if (fav.equals(placeName)) {
                //datastore.remove(entity);
                datastore.delete(entity.getKey());
            }
            break;
        }
    }

    public List<String> getFavorites() {
        List<String> favorites = new ArrayList<>();
        // Load user's favorites from Datastore
        Filter propertyFilter = new FilterPredicate("user", FilterOperator.EQUAL, userEmail);
        Query q = new Query("Favorites").setFilter(propertyFilter);
        PreparedQuery results = datastore.prepare(q);
        for (Entity entity : results.asIterable()) {
            String fav = (String) entity.getProperty("favoriteName");
            if (!favorites.contains(fav)) {
                favorites.add(fav);
            }
        }
        System.out.println("getFavorites(): " + favorites);
        return favorites;
    }
}
