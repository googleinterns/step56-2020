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
import com.google.sps.data.Popular;
import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Class containing Chat between users */
public final class Chat {
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public void addMessage(String writer, String recipient, String message, long timestamp) {
		Entity messageEntity = new Entity("Message");
		messageEntity.setProperty("writer", writer);
		messageEntity.setProperty("recipient", recipient);   
		messageEntity.setProperty("message", message);   
		messageEntity.setProperty("timestamp", timestamp);   
		datastore.put(messageEntity);
	}

	public List<Message> getMessageChain(String userA, String userB) {
		List<Message> messages = new ArrayList<>();
		Query query = new Query("Message").addSort("timestamp", Query.SortDirection.DESCENDING);
		// Load user's favorites from Datastore
		PreparedQuery results = datastore.prepare(query);
		for (Entity entity : results.asIterable()) {
			String writer = (String) entity.getProperty("writer");
			String recipient = (String) entity.getProperty("recipient");
			if ((userA.equals(writer) && userB.equals(recipient)) || (userB.equals(writer) && userA.equals(recipient))) {
				messages.add(new Message(writer, recipient, (String) entity.getProperty("message"), (Long) entity.getProperty("timestamp")));
			}
		}
		return messages;
	}

}
