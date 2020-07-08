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
import java.util.HashMap;
import java.util.Map;

/** Class containing user search history. */

public final class Search {
  // List of users and their search history
  // key = user's email, value = list of user's previous searches
  private final Map<String, List<String>> searches = new HashMap<>();
  
  public void addToSearchList(String user, String search) {
    List<String> existingSearches = searches.get(user); 
    //if list does not exist, create it
    if (existingSearches == null) {
        existingSearches = new ArrayList<String>();
        existingSearches.add(search);
        searches.put(user, existingSearches);
    } else if (!existingSearches.contains(search)){
        //if search does not exist in the list, add it
        existingSearches.add(search);
        searches.put(user, existingSearches);
    }
    System.out.println(searches);
  }

  public Map<String, List<String>> getSearches() {
      return searches;
  }

  public List<String> getSearchHistory(String user) {
    List<String> searchHistory = searches.get(user);
    return searchHistory;
  }

}
