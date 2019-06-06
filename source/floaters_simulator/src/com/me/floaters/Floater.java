package com.me.floaters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Floater {
	private static final byte TYPESQNT = 6;
	private static final byte TEXTQNT = (byte) (3*TYPESQNT);
	private static Texture[] texture = new Texture[TEXTQNT];
	private Sprite sprite;
	private float keepdisplacingtime, keepdisplacingtime2;
	private float lastxamount = 0, lastyamount = 0;
	private float alpha = 0.8f;
	private boolean displacementrestarted = true;
	private short ymax;
	
	protected static void init(){
		byte aux, folder;
		for (int i=0; i<TEXTQNT; i++){
			aux = (byte) ((i % TYPESQNT) + 1);
			folder = (byte) ((i / TYPESQNT) + 1);
			texture[i] = new Texture(Gdx.files.internal("floaters/type" + folder + "/" + aux + ".png"));
		}
	}
	
	
	public Floater(String texturepath, float scale){
		sprite = new Sprite(new Texture(Gdx.files.internal(texturepath)));
		sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Random rg = new Random();
		keepdisplacingtime2 = rg.nextFloat()*3;
		short aux = (short) ((rg.nextBoolean()) ? 200 : -200);
		sprite.setPosition(rg.nextInt(Gdx.graphics.getWidth()) + aux, rg.nextInt(Gdx.graphics.getHeight()) + aux/2);
		sprite.setScale(scale);
		if (rg.nextBoolean()) sprite.rotate90(rg.nextBoolean());
		sprite.flip(rg.nextBoolean(), rg.nextBoolean());
		ymax = (short) rg.nextInt(500);
	}
	
	public void displace(float x, float y){	
		lastxamount = x; lastyamount = y;
		smalldisplace(x, y);
		if (displacementrestarted){
			keepdisplacingtime = keepdisplacingtime2;
			displacementrestarted = false;
		}

	}
	
	public void smalldisplace(float x, float y){
		short width = (short) Gdx.graphics.getWidth();
		short height = (short) Gdx.graphics.getHeight();
		if (this.sprite.getX() > width + 300 || sprite.getY() > height + 300 || sprite.getX() < -300 || sprite.getY() < -300) return;
		sprite.setPosition(sprite.getX() + x, sprite.getY() + y);
	}
	
	public void draw(SpriteBatch batch, float delta){
		Random rg = new Random();
		if (keepdisplacingtime > 0){
			keepdisplacingtime -= delta;
			byte div = 10;
			short dx = (short) ((lastxamount < 0) ? ((rg.nextFloat()/div)*-1) :  (rg.nextFloat()/div));
			short dy = (short) ((lastyamount < 0) ? ((rg.nextFloat()/div)*-1) :  (rg.nextFloat()/div));
			displace(lastxamount + dx, lastyamount + dy);
		}else if (keepdisplacingtime < 0) {
			keepdisplacingtime = 0;
			displacementrestarted = true;
		}else if (keepdisplacingtime == 0){
			displacementrestarted = true;
			alpha = ((alpha - delta/2) > 0.3f) ? alpha - delta/2 : 0.3f;
			byte aux = (byte) ((rg.nextBoolean()) ? 1 : -1);
			if (this.sprite.getY() > (300 - ymax)) smalldisplace(aux*(rg.nextInt(12)/100f), -1*(rg.nextInt(20)/100f));
		}
		sprite.draw(batch, alpha);
	}
	
	public void resetAlpha(){
		alpha = 0.8f;
	}

}
