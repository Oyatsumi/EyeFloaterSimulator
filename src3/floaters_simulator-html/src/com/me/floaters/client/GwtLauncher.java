package com.me.floaters.client;

import com.me.floaters.FloaterSimulator;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.GwtGraphics;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {

        GwtApplicationConfiguration config = new GwtApplicationConfiguration(800, 600);
        final VerticalPanel panel = new VerticalPanel();
        final int width = Window.getClientWidth();
        final int height = Window.getClientHeight();
        panel.setWidth(width + "px");
        //panel.setWidth("" + config.width + "%");
        panel.setHeight(height + "px");
        Window.addResizeHandler(new ResizeHandler() {
                
            public void onResize(ResizeEvent event) {
                int height = event.getHeight() - 4;
                int width = event.getWidth() - 4;
                panel.setHeight(height + "px");
                panel.setWidth(width + "px");
                GwtGraphics gwtGraphics = (GwtGraphics) Gdx.graphics;
                gwtGraphics.setDisplayMode(width, height, false);
            }

        });

        
        RootPanel.get().add(panel);
        config.rootPanel = panel;
        config.width = width - 10;
        config.height = height - 10;
        return config;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new FloaterSimulator();
	}
}