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
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.sps.data.Popular;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/** Popular Tests */
@RunWith(JUnit4.class)
public final class PopularTest {
    Query query; 

    private static final String placeID_1 = "1234";
    private static final String placeID_2 = "2345";
    private static final String placeID_3 = "1357";

    private static final String placeName_1 = "Azuma";
    private static final String placeName_2 = "Bistro 808";
    private static final String placeName_3 = "Chat 19";

    private static final Long score_0 = 0L;
    private static final Long score_1 = 1L;
    private static final Long score_2 = 2L;
    private static final Long score_3 = 3L;

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void addNewPlaceToPopular() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Popular popular = new Popular(datastore);

        popular.addToPopularList(placeID_1, placeName_1); 
        Map<String, Long> actual = popular.getPopular();
        Map<String, Long> expected = new HashMap<>();
        expected.put(placeName_1, score_1);

        Assert.assertEquals(expected, actual);
    } 

    @Test
    public void addMultiplePlacesToPopular() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Popular popular = new Popular(datastore);

        popular.addToPopularList(placeID_1, placeName_1); 
        popular.addToPopularList(placeID_2, placeName_2); 
        popular.addToPopularList(placeID_3, placeName_3); 
        Map<String, Long> actual = popular.getPopular();
        Map<String, Long> expected = new HashMap<>();
        expected.put(placeName_1, score_1);
        expected.put(placeName_2, score_1);
        expected.put(placeName_3, score_1);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addPlacesToPopularMultipleTimes() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Popular popular = new Popular(datastore);

        popular.addToPopularList(placeID_1, placeName_1); 
        popular.addToPopularList(placeID_2, placeName_2); 
        popular.addToPopularList(placeID_2, placeName_2); 
        popular.addToPopularList(placeID_2, placeName_2); 
        popular.addToPopularList(placeID_3, placeName_3); 
        popular.addToPopularList(placeID_3, placeName_3); 
        Map<String, Long> actual = popular.getPopular();
        Map<String, Long> expected = new HashMap<>();
        expected.put(placeName_1, score_1);
        expected.put(placeName_2, score_3);
        expected.put(placeName_3, score_2);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void removeFromPopular() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Popular popular = new Popular(datastore);

        popular.addToPopularList(placeID_1, placeName_1); popular.removeFromPopularList(placeID_1, placeName_1); 
        Map<String, Long> actual = popular.getPopular();
        Map<String, Long> expected = new HashMap<>();
        expected.put(placeName_1, score_0);
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void removeMultipleFromPopular() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Popular popular = new Popular(datastore);

        popular.addToPopularList(placeID_1, placeName_1); 
        popular.addToPopularList(placeID_1, placeName_1); 
        popular.addToPopularList(placeID_2, placeName_2); 
        popular.addToPopularList(placeID_2, placeName_2); 
        popular.addToPopularList(placeID_2, placeName_2); 
        popular.addToPopularList(placeID_3, placeName_3); 

        popular.removeFromPopularList(placeID_1, placeName_1); 
        popular.removeFromPopularList(placeID_2, placeName_2); 
        popular.removeFromPopularList(placeID_3, placeName_3); 

        Map<String, Long> actual = popular.getPopular();
        Map<String, Long> expected = new HashMap<>();
        expected.put(placeName_1, score_1);
        expected.put(placeName_2, score_2);
        expected.put(placeName_3, score_0);

        Assert.assertEquals(expected, actual);
    } 

}


