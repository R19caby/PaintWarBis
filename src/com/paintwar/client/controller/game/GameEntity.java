package com.paintwar.client.controller.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

import com.paintwar.client.logger.Logger;
import com.paintwar.client.model.communication.gameio.GameIOReceiver;
import com.paintwar.client.view.pages.game.GamePage;
import com.paintwar.client.view.pages.game.elements.DrawZone;
import com.paintwar.client.view.pages.game.listeners.FrameResizeListener;
import com.paintwar.server.service.game.DrawServerProxy;
import com.paintwar.server.service.game.IDrawServerProxy;


public class GameEntity implements IGameEntity {
	
	private int drawCount;
	private DrawZone drawZone;
	private GameIOReceiver ioClient;
	private HashMap<String, DrawingProxy> drawings;
	private List<Thread> threads;
	private JFrame currentWindow;
	private GamePage gamepage;
	
	GameEntity() {
		drawings = new HashMap<String, DrawingProxy>();
		threads = new ArrayList<Thread>();
		drawCount = 0;
		currentWindow = new JFrame();
		gamepage = new GamePage(this, threads);
		this.drawZone = gamepage.getDrawZone();
		
		currentWindow.addComponentListener(new FrameResizeListener(gamepage));
		currentWindow.getContentPane().add(gamepage, BorderLayout.CENTER);
	}
	
	public void setIoClient(GameIOReceiver ioClient) {
		this.ioClient = ioClient;
	}

	private void deleteAllDrawings() {
		drawings.clear();
		drawCount = 0;
	}
	
	@Override
	public void startGame() {
		this.deleteAllDrawings();
	}

	@Override 
	public String paintClient(Point p1, Point p2, Color c) {
		//create drawing on server and retrieve draw name 
		String drawName = ioClient.createDrawing(p1, p2, c, DrawingProxy.RECTANGLE);
		Logger.print("[Game] Putting drawing named " + drawName);
		return drawName;
	}
	
	@Override
	public DrawingProxy addDrawing(IDrawServerProxy proxy, String name, Point p1, Point p2, Color c) {
		//create drawing
		DrawingProxy drawing = new DrawingProxy(proxy, p1, p2, c, DrawingProxy.RECTANGLE);
		
		//add to the list
		drawings.put(name, drawing);
		
		//update zone
		drawZone.addDrawing(name, p1, p2, c);
		
		return drawing;
	}
	
	@Override
	public void removeDrawing(String name) {
		drawZone.deleteDrawing(name);
	}
	
	@Override
	public void updateCoordPaint(String name, Point p1, Point p2) {
		drawZone.updateDraw(name, p1, p2);
	}
	
	@Override
	public void updateCoordPaint(String name, Point p) {
		drawZone.updateEndPointDraw(name, p);
		DrawingProxy drawToChange = drawings.get(name);
	}
	
	@Override
	public void updateCoordPaintClient(String name, Point p) {
		DrawingProxy drawToChange = drawings.get(name);
		if (drawToChange != null) {
			ioClient.updateBoundsrequest(name, drawToChange.getCoord(), p);
		} else {
			Logger.print("[Game] Couldn't find drawing proxy to change coord");
		}
	}

	public void startFilling(String name) {
		ioClient.startFilling(name);
	}
	
	public void updateFilling(String name, Double percent) {
		drawZone.updateFilling(name, percent);
	}
	
	public boolean hasDrawing(String name) {
		return drawings.containsKey(name);
	}
	
	public void openGame() {
		currentWindow.setVisible(true);
		currentWindow.setSize(700, 400);
		gamepage.updatePage(700, 400);
	}
	
	public void closeGame() {
		//close window
		currentWindow.setVisible(false);
		currentWindow.dispose();
		currentWindow = null;
		
		//stop all threads
		for (Thread thread : threads) {
			thread.interrupt();
		}
	}

}
