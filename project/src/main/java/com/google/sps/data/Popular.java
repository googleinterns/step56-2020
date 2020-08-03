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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;

/** Class containing restaurants' popularity scores (# of times 'favorited'). */
public final class Popular {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Popular");

    public Popular() {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    public Popular(DatastoreService ds) {
        datastore = ds;
    }   

    // Store restaurant's popularity score (# times it has been 'favorited') in Datastore
    public void addToPopularList(String placeID, String placeName) {
        // Load restaurants'popularity scores from Datastore to check if place already exists in popular list
        Filter propertyFilter = new FilterPredicate("placeID", FilterOperator.EQUAL, placeID);
        Query q = new Query("Popular").setFilter(propertyFilter);
        PreparedQuery results = datastore.prepare(q);
        for (Entity entity : results.asIterable()) {
            // Increment popularity score by 1
            long curPopularity = (long) entity.getProperty("score");
            entity.setProperty("score", curPopularity + 1); 
            datastore.put(entity);
            return;
        }

        Entity popularEntity = new Entity("Popular");
        popularEntity.setProperty("placeID", placeID);
        popularEntity.setProperty("placeName", placeName);
        popularEntity.setProperty("score", 1);   
        datastore.put(popularEntity);
    }

    public void removeFromPopularList(String placeID, String placeName) {
        Filter propertyFilter = new FilterPredicate("placeID", FilterOperator.EQUAL, placeID);
        Query q = new Query("Popular").setFilter(propertyFilter);
        PreparedQuery results = datastore.prepare(q);
        for (Entity entity : results.asIterable()) {
            // Decrement popularity score by 1
            long curPopularity = (long) entity.getProperty("score");
            entity.setProperty("score", curPopularity - 1); 
            datastore.put(entity);
            return;
        }
    }

    // Returns a list of all popular restaurants and their popularity scores
    public Map<String, Long> getPopular() {
        Map<String, Long> popular = new HashMap<>();
        // Load popular restaurants from Datastore
        PreparedQuery results = datastore.prepare(query);
        for (Entity entity : results.asIterable()) {
            String place = (String) entity.getProperty("placeName");
            long popularityScore = (long) entity.getProperty("score");
            popular.put(place, popularityScore);
        }
        System.out.println("getPopular(): " + popular);
        return popular;
    }
}
