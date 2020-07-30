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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.Popular;
import com.google.gson.Gson;

@WebServlet("/popular")
public class PopularServlet extends HttpServlet {
    Popular popular = new Popular();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {    
        response.setContentType("application/json");
        String json = new Gson().toJson(popular.getPopular());
        System.out.println("Popular servlet: " + json);
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the restaurant ID and name from the server
        String placeID = request.getParameter("placeID");
        String placeName = request.getParameter("placeName");
        String addToList = request.getParameter("addOrRemove");

        // Update restaurant's popularity ('favorited') score
        if (addToList.equals("add")) {
            popular.addToPopularList(placeID, placeName);
        } else {
            popular.removeFromPopularList(placeID, placeName);
        }

        response.setContentType("text/html;");
        response.getWriter().println(popular.getPopular());
    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
