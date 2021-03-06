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
import com.google.sps.data.Message;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Class containing Chat between users */
public final class Chat {

	public static void addMessage(String writer, String recipient, String message, long timestamp, DatastoreService datastore) {
		if(!writer.equals(recipient) && !message.equals("") && !writer.equals("") && !recipient.equals("")) {
			Message tmessage = new Message(writer, recipient, message, timestamp);
			datastore.put(tmessage.getEntity());
		}
	}

	public static void addMessage(Message message, DatastoreService datastore) {
		if(!message.getWriter().equals(message.getRecipient()) && !message.getMessage().equals("") && !message.getWriter().equals("") && !message.getRecipient().equals("")) {
			datastore.put(message.getEntity());
		}
	}

	public static String toJson(List<Message> chain){
		return new Gson().toJson(chain);
	}

	public static Filter createWriterRecipientFilter(String writer, String recipient) {
		return new CompositeFilter(CompositeFilterOperator.AND, Arrays.asList(
					new FilterPredicate("writer", FilterOperator.EQUAL, writer), 
					new FilterPredicate("recipient", FilterOperator.EQUAL, recipient)
					)
				);
	}

	public static List<Message> getMessageChain(String userA, String userB, DatastoreService datastore) {
		Query query;
		PreparedQuery results;
		List<Message> messages = new ArrayList<>();
		Filter writerAFilter = createWriterRecipientFilter(userA, userB);
		Filter writerBFilter = createWriterRecipientFilter(userB, userA); 
		query = new Query("Message")
			.setFilter(writerAFilter)
			.addSort("timestamp", SortDirection.ASCENDING);
		results = datastore.prepare(query);
		for (Entity entity : results.asIterable()) {
			messages.add(new Message(userA, userB, (String) entity.getProperty("message"), (Long) entity.getProperty("timestamp")));
		}

		query = new Query("Message")
			.setFilter(writerBFilter)
			.addSort("timestamp", SortDirection.DESCENDING);
		results = datastore.prepare(query);
		for (Entity entity : results.asIterable()) {
			messages.add(new Message(userB, userA, (String) entity.getProperty("message"), (Long) entity.getProperty("timestamp")));
		}
		return messages;
	}

}
