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

import com.google.sps.data.Message;
import com.google.sps.data.Chat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
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
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/** Favorites Tests */
@RunWith(JUnit4.class)
public final class ChatTest {
	Query query; 

	private static final String userEmail_1 = "srosset@google.com";
	private static final String userEmail_2 = "sophia@google.com";

	private static final String placeID_1 = "1234";
	private static final String placeID_2 = "2345";
	private static final String placeID_3 = "1357";

	private static final String placeName_1 = "Azuma";
	private static final String placeName_2 = "Bistro 808";
	private static final String placeName_3 = "Chat 19";

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
	public void testAddNewMessage() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		List<Message> actual;
		List<Message> expected = new ArrayList<>();
		Message testMessage = new Message("Alice", "Bob", "Test", 0l);

		expected.add(testMessage);
		Chat.addMessage(testMessage, datastore);

		actual = Chat.getMessageChain("Alice", "Bob", datastore);
		Assert.assertEquals(expected.get(0).getMessage(), actual.get(0).getMessage());
	}

	@Test
	public void testMessageChainSorted() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		List<Message> actual;
		List<Message> expected = new ArrayList<>();
		Message testMessage1 = new Message("Alice", "Bob", "Test", 0l);
		Message testMessage2 = new Message("Alice", "Bob", "Test", 1l);

		expected.add(testMessage1);

		expected.add(testMessage2);
		Chat.addMessage(testMessage2, datastore);
		Chat.addMessage(testMessage1, datastore);

		actual = Chat.getMessageChain("Alice", "Bob", datastore);
		Assert.assertEquals(expected.get(0).getMessage(), actual.get(0).getMessage());
		Assert.assertEquals(expected.get(1).getMessage(), actual.get(1).getMessage());
	}

	@Test
	public void testBadRecipients() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		List<Message> actual;
		List<Message> expected = new ArrayList<>();

		Chat.addMessage(new Message("Alice", "Alice", "Test", 0l), datastore);

		actual = Chat.getMessageChain("Alice", "Alice", datastore);
		Assert.assertEquals(expected, actual);
	}	

	@Test
	public void testNoEmptyMessage() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		List<Message> actual;
		List<Message> expected = new ArrayList<>();

		Chat.addMessage(new Message("Alice", "Bob", "", 0l), datastore);

		actual = Chat.getMessageChain("Alice", "Bob", datastore);
		Assert.assertEquals(expected, actual);
	}
} 


