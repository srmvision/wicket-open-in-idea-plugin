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

import com.intellij.ide.util.PropertiesComponent;

/**
 * Utility class used to manipulate properties
 * @author cgatay
 */
public class PluginProperties {
    public static final int DEFAULT_PORT = 10462;
    public static final String PORT_KEY = "com.srmvision.tools.wicketopen";

    public static int getPort() {
        final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        final String value = propertiesComponent.getValue(PORT_KEY);
        int port = DEFAULT_PORT;
        try {
            port = Integer.valueOf(value);
        } catch (Exception e) {
            resetPort();
        }
        return port;
    }

    public static void resetPort() {
        setPort(DEFAULT_PORT);
    }

    public static void setPort(final int port) {
        final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        propertiesComponent.setValue(PORT_KEY, String.valueOf(port));
    }
}
