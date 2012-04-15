/*
 *  Copyright 2012 Cedric Gatay
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.srmvision.tools.wicketdebug.idea.ApplicationComponent;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;

/**
 * Simple HTTP Web server responding with the className it captures (in the cn GET argument)
 * Called by the browser plugin
 * @author cgatay
 */
public class IDEAHttpHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if ("GET".equalsIgnoreCase(requestMethod)) {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/plain");
            //header to allow an Ajax Call from any host
            responseHeaders.set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, 0);
            OutputStream responseBody = exchange.getResponseBody();

            final StringTokenizer stringTokenizer =
                    new StringTokenizer(exchange.getRequestURI().getQuery(), "=");
            while (stringTokenizer.hasMoreTokens()) {
                final String s = stringTokenizer.nextToken();
                if ("cn".equalsIgnoreCase(s) && stringTokenizer.hasMoreTokens()) {
                    final String clazzName = stringTokenizer.nextToken();
                    ClassNavigator.scheduleClassOpening(clazzName);
                    responseBody.write(clazzName.getBytes());
                }
            }
            responseBody.close();
        }
    }
}
