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

package com.google.sps.servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Chat;
import com.google.sps.data.Message;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
  Chat chat = new Chat();
  UserService userService = UserServiceFactory.getUserService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {    
    response.setContentType("application/json");
    String writer = userService.getCurrentUser().getEmail(); 
    String recipient = request.getParameter("recipient");
    List<Message> chain = chat.getMessageChain(writer,recipient);
    String json = new Gson().toJson(chain);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the restaurant ID from the server
    String writer = userService.getCurrentUser().getEmail(); 
    String recipient = request.getParameter("recipient");
    String message = request.getParameter("message");
    Long timestamp = Long.parseLong(request.getParameter("timestamp"));

    chat.addMessage(writer, recipient, message, timestamp);


    response.setContentType("text/html;");
    response.getWriter().println("Success");
  }

}
