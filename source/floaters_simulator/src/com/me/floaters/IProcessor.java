package com.me.floaters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class IProcessor implements InputProcessor {
	private int lastx, lasty;
	private boolean starteddraggin = true;

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (character == 'c') FloaterSimulator.drawcredits = !FloaterSimulator.drawcredits;
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		final byte p = 34;
		if (FloaterSimulator.drawoptions){
			lastx = screenX; lasty = screenY;
		}
			if(screenX > FloaterSimulator.painel.getX() && screenX < FloaterSimulator.painel.getX() + FloaterSimulator.painel.getWidth()){
				final int blockwidth = (int) (FloaterSimulator.painel.getWidth() / 9);
				final int px = (int) FloaterSimulator.painel.getX();
				//painel superior
				if (screenY < FloaterSimulator.painelup.getHeight() - 15){
					for (int i=0; i<9; i++){
						if ((screenX > (i*blockwidth + px)) && (screenX < ((i + 1)*blockwidth + px))){
							FloaterSimulator.setFloaterN(i + 1, screenX, screenY);
							//System.out.println(i);
							return true;
						}
					}
				}else if (screenY > Gdx.graphics.getHeight() - FloaterSimulator.painel.getHeight() + 15){
					//painel inferior
					for (int i=0; i<9; i++){
						if ((screenX > (i*blockwidth + px)) && (screenX < ((i + 1)*blockwidth + px))){
							FloaterSimulator.setFloaterN(i + 10, screenX, screenY);
							//System.out.println(i);
							return true;
						}
					}
				}
				
				
			}
			if (screenX > FloaterSimulator.slider.getX() - 20){
				if (screenY < FloaterSimulator.slider.getY() + 250){
					for (int i=0; i<5; i++){
						if (screenY > FloaterSimulator.slider.getY() + 38 + p*(i+1)){
							FloaterSimulator.setLight(4 - i);
						}
					}
					return true;
				}
			}

			if(screenX < FloaterSimulator.size.getRegionWidth() + 10){
				final byte pd = 40;
				if ((screenY > FloaterSimulator.size.getY() + pd) && 
						(screenY < FloaterSimulator.size.getY() + FloaterSimulator.size.getHeight() - pd)){
					int initial = (int) (FloaterSimulator.size.getY() + pd);
					if (screenY > initial && screenY < initial + 2*pd){
						FloaterSimulator.size.setRegion(192, 0, 64, 256);
						FloaterSimulator.floatersize = 2;
						return true;
					}else if(screenY > initial + 2*pd && screenY < initial + 3*pd){
						FloaterSimulator.size.setRegion(128, 0, 64, 256);
						FloaterSimulator.floatersize = 1f;
						return true;
					}else{
						FloaterSimulator.size.setRegion(64, 0, 64, 256);
						FloaterSimulator.floatersize = 0.5f;
						return true;
					}
				}
			}
			if ((screenX > FloaterSimulator.reset.getX() && screenX < FloaterSimulator.reset.getX() + FloaterSimulator.reset.getWidth())
					&& (screenY < (Gdx.graphics.getHeight() - FloaterSimulator.reset.getY())) && (screenY > (Gdx.graphics.getHeight() - (FloaterSimulator.reset.getY() + FloaterSimulator.reset.getHeight())))){
				FloaterSimulator.floaters.clear();
				System.gc();
				FloaterSimulator.drawcredits = false;
				return true;
			}
		lastx = screenX;
		lasty = screenY;
		//System.out.println(screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		FloaterSimulator.reDrawOptions();
		FloaterSimulator.drawmove = false;
		starteddraggin = true;
		return false;
	}

	
	//private boolean px, py, starting = true;
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		final short sensitivity = 1, s2 = 2;
		FloaterSimulator.movecursor.setPosition(lastx - 32, Gdx.graphics.getHeight() - lasty - 32);
		/*
		boolean ax, ay;
		if ((screenX - lastx) < 0) ax = false; else ax = true;
		if ((screenY - lasty) < 0) ay = false; else ay = true;

		if (starting){
			px = ax;
			py = ay;
			starting = false;
		}
		
		if (px != ax || py != ay){
			px = ax; py = ay;
			lastx = screenX;
			lasty = screenY;
		}
		*/
		int varX = (screenX - lastx)/22;
		int varY = (screenY - lasty)/22;
		
		Random rg = new Random();
		if ((Math.abs((varX)) > sensitivity) || Math.abs((varY)) > sensitivity){
			FloaterSimulator.unDrawOptions();
			//FloaterSimulator.moveBackground((varX)/(s2 + rg.nextInt(s2)), (varY)/(s2 + rg.nextInt(s2)));
			FloaterSimulator.moveBackground((varX)*5/s2, -1*(varY)*5/s2);
		
			if (starteddraggin){
				FloaterSimulator.drawmove = true;
				for (int i=0; i<FloaterSimulator.floaters.size(); i++){
					FloaterSimulator.floaters.get(i).resetAlpha();
				}
				starteddraggin = false;
			}
			
			int aux = (rg.nextBoolean()) ? rg.nextInt(s2) : rg.nextInt(s2)*-1;
			int vel, vel2;
			for (int i=0; i<FloaterSimulator.floaters.size(); i++){
				aux = (rg.nextBoolean()) ? aux - rg.nextInt(1) : aux + rg.nextInt(1);
				vel = rg.nextInt(20); if (vel == 0) vel ++;
				vel2 = rg.nextInt(20);if (vel2 == 0) vel2 ++;
				byte multx, multy;
				if ((varX) < 0) multx = -1; else multx = 1;
				if ((varY) < 0) multy = -1; else multy = 1;
				FloaterSimulator.floaters.get(i).displace((-0.05f*vel*(varX)/s2 + multx*aux), 0.05f*vel2*((varY)/(s2)) + multy*(aux + 1)/4);
			}
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
