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

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.sps.data.Favorites;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;

/** Favorites Tests */
/*
@RunWith(JUnit4.class)

public final class FavoritesTest {
    Favorites favorites = new Favorites();
    //DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    //Query query = new Query("Favorites");

    private static final String userEmail_1 = "srosset@google.com";

    private static final String placeID_1 = "1234";

    private static final String placeName_1 = "Azuma";
    
    //add favorites to database (expected) (addToFavoritesList)
    //call getFavorites() (actual)

    @Before
    public void clearDatabase() {        
        PreparedQuery results = datastore.prepare(query);
        for (Entity entity : results.asIterable()) {
            datastore.delete(entity.getKey());
        }
    }

    @Test
    public void addNewPlaceToFavorites() {
        favorites.addToFavoritesList(userEmail_1, placeID_1, placeName_1);
        List<String> actual = favorites.getFavorites();
        List<String> expected = new ArrayList<>();
        expected.add(placeName_1);

        Assert.assertEquals(expected, actual);
    }
    
} 
*/

