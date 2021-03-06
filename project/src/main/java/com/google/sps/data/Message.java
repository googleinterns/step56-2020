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
public final class Message{
	private final String writer;
	private final String recipient;
	private final String message;
	private final long timestamp;
	private Entity entity = new Entity("Message");

	public Message (String writer, String recipient, String message, Long timestamp) {
		this.writer = writer;
		entity.setProperty("writer", writer);
		this.recipient = recipient;
		entity.setProperty("recipient", recipient);
		this.message = message;
		entity.setProperty("message", message);
		this.timestamp = timestamp;
		entity.setProperty("timestamp", timestamp);
	}

	public Entity getEntity() {
		return entity;
	}

	public String getWriter() {
		return writer;
	}

	public String getRecipient() {
		return recipient;
	}

	public String getMessage() {
		return message;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
