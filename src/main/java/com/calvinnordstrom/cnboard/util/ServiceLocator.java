package com.calvinnordstrom.cnpaint.util;

import com.calvinnordstrom.cnpaint.view.EditorControlsPane;
import com.calvinnordstrom.cnpaint.view.EditorPane;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static ServiceLocator instance;
    private final Map<String, MenuItem> menuItems = new HashMap<>();
    private final Map<EditorPane, EditorControlsPane> editorPanes = new HashMap<>();
    private final Map<String, Node> nodes = new HashMap<>();

    private ServiceLocator() {}

    public void register(String key, MenuItem value) {
        menuItems.put(key, value);
    }

    public MenuItem getMenuItem(String key) {
        return menuItems.get(key);
    }

    public void register(EditorPane key, EditorControlsPane value) {
        editorPanes.put(key, value);
    }

    public EditorControlsPane getEditorControlsPane(EditorPane key) {
        return editorPanes.get(key);
    }

    public void register(String key, Node value) {
        nodes.put(key, value);
    }

    public Node getNode(String key) {
        return nodes.get(key);
    }

    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }
}
