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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.Messages;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Entry point for the plugin, contains the configuration components and manages the web server
 * @author cgatay
 */
public class WicketDebugEntryPoint implements ApplicationComponent, Configurable {
    private JTextField portTextfield;
    private JPanel contentPanel;
    private JButton resetButton;
    private HttpServer server;

    void runWebServer() {
        final int port = PluginProperties.getPort();
        try {
            InetSocketAddress addr = new InetSocketAddress(port);
            server = HttpServer.create(addr, 0);

            server.createContext("/", new IDEAHttpHandler());
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
        } catch (IOException e) {
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                public void run() {
                    Messages.showMessageDialog("Wicket Open In IDEA : unable to start webserver on port "
                                               + port + ", please check plugin configuration.",
                                               "Plugin Error", Messages.getErrorIcon());
                }
            });
        }
    }


    void stopWebServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    public void initComponent() {
        runWebServer();
    }

    public void disposeComponent() {
        stopWebServer();
    }


    @NotNull
    @Override
    public String getComponentName() {
        return "Wicket Open In IDEA";
    }


    @Nls
    @Override
    public String getDisplayName() {
        return "Wicket Open In IDEA";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public JComponent createComponent() {
        updateUI();
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                PluginProperties.resetPort();
                updateUI();
            }
        });
        return contentPanel;
    }

    private void updateUI() {
        portTextfield.setText(String.valueOf(PluginProperties.getPort()));
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                stopWebServer();
                Integer port;
                try {
                    port = Integer.valueOf(portTextfield.getText());
                } catch (Exception nex) {
                    port = PluginProperties.DEFAULT_PORT;
                }
                PluginProperties.setPort(port);
                updateUI();
                runWebServer();
            }
        });
    }

    @Override
    public void reset() {
    }

    @Override
    public void disposeUIResources() {
    }
}
