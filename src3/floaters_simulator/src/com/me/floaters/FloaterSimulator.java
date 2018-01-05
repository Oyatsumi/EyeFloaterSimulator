package com.me.floaters;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FloaterSimulator implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texturedark, texturelight;
	private static Sprite backgrounddark, backgroundlight;
	protected static Sprite lightcursor, painel, painelup, size; 
	protected static Sprite slider, click, reset, credits, movecursor;
	private static float light = 0; //from 0 to 1
	private static float lightcursory;
	protected static boolean drawoptions = true;
	private static float alphar = 0, clicklapse = 0f;
	protected static ArrayList<Floater> floaters = new ArrayList<Floater>();
	protected static float floatersize = 1f;
	protected static boolean drawcredits = false, drawmove = false;
	
	@Override
	public void create() {		
		System.out.println("Made by Érick Oliveira Rodrigues.");
		Floater.init();
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
				
		slider = new Sprite(new Texture(Gdx.files.internal("slider.png")));
		lightcursor = new Sprite(new Texture(Gdx.files.internal("cursor.png")));
		
		painel = new Sprite(new Texture(Gdx.files.internal("painel_down.png")));
		painelup = new Sprite(new Texture(Gdx.files.internal("painel_up.png")));
		size = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("size.png"))), 128, 0, 64, 256);
		click = new Sprite(new Texture(Gdx.files.internal("click.png")));
		reset = new Sprite(new Texture(Gdx.files.internal("reset.png")));
		credits = new Sprite(new Texture(Gdx.files.internal("c.png")));
		movecursor = new Sprite(new Texture(Gdx.files.internal("cursor_move.png")));
		
		texturedark = new Texture(Gdx.files.internal("panorama_dark.png"));
		texturedark.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		texturelight = new Texture(Gdx.files.internal("panorama_light.png"));
		texturelight.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texturedark, 0, 0, 4096, 1024);
		
		backgrounddark = new Sprite(region);
		backgroundlight = new Sprite(texturelight, 0, 0, 4096, 1024);
		//sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		//sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		backgrounddark.setPosition(0, 0);
		
		texturedark = null;
		texturelight = null;
		
		Gdx.input.setInputProcessor(new IProcessor());
	}

	@Override
	public void dispose() {
		batch.dispose();
		//texturedark.dispose();
		//texturelight.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		backgroundlight.setPosition(backgrounddark.getX(), backgrounddark.getY());
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.disableBlending();
		if (light != 1) backgrounddark.draw(batch);
		batch.enableBlending();
		backgroundlight.draw(batch, light);
		
		for (int i=0; i<floaters.size(); i++){
			floaters.get(i).draw(batch, Gdx.graphics.getDeltaTime());
		}
		
		if (drawmove) movecursor.draw(batch);
		
		if (drawoptions){
			if (drawcredits) credits.draw(batch);
			reset.draw(batch, alphar);
			alphar = (alphar + Gdx.graphics.getDeltaTime()*2 < 1) ? alphar + Gdx.graphics.getDeltaTime()*2 : 1;
			slider.draw(batch, alphar);
			lightcursor.draw(batch, alphar);
			painel.draw(batch, alphar);
			painelup.draw(batch, alphar);
			size.draw(batch, alphar);
			if (clicklapse > 0){
				click.draw(batch, alphar);
				clicklapse -= Gdx.graphics.getDeltaTime();
			}else if (clicklapse < 0) clicklapse = 0;
		}
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		backgrounddark.setPosition(0, 0);
		camera.setToOrtho(false,width, height);
		if (height > backgrounddark.getHeight()){
			backgrounddark.setRegionHeight(height);
			backgroundlight.setRegionHeight(height);
		}
		credits.setPosition(width/2 - credits.getWidth()/2, height/2 - credits.getHeight()/2);
		slider.setPosition(width - 30, height/2 - slider.getHeight()/2 - 30);
		lightcursor.setPosition(width - 30, slider.getY() + 63 + lightcursory);
		painel.setPosition(width/2 - painel.getWidth()/2, -12);
		painelup.setPosition(width/2 - painelup.getWidth()/2, height - 52);
		size.setPosition(10, height/2 - size.getHeight()/2);
		reset.setPosition(70, 50);
	}

	@Override
	public void pause() {
		System.exit(0);
	}

	@Override
	public void resume() {

	}
	
	protected static void setLight(int i){//i from 0 to 4
		int y = (int) slider.getY();
		lightcursor.setY(y + 38*i + 63);
		lightcursory = 38*i;
		light = 2.25f*i;
	}
	
	protected static void moveBackground(int multx, int multy){
		if (((backgrounddark.getX() + multx) < 0) && (((backgrounddark.getWidth() + backgrounddark.getX()) + multx) >= Gdx.graphics.getWidth())){
			backgrounddark.setX(backgrounddark.getX() + multx);
		}
		if (((backgrounddark.getY() + multy) < 0) && (((backgrounddark.getHeight() + backgrounddark.getY()) + multy) >= Gdx.graphics.getHeight())){
			backgrounddark.setY(backgrounddark.getY() + multy);
		}
		//else if ((backgrounddark.getY() + multy) < 0) return;
		//else if ((backgrounddark.getX() + backgrounddark.getWidth() + multx) > Gdx.graphics.getWidth()) return;
		//else if ((backgrounddark.getY() + backgrounddark.getHeight() + multx) < Gdx.graphics.getHeight()) return;
		//backgrounddark.setPosition(backgrounddark.getX() + multx, backgrounddark.getY() + multy);
		//backgroundlight.setPosition(backgroundlight.getX() + multx, backgroundlight.getY() + multy);
	}
	
	protected static void unDrawOptions(){
		drawoptions = false;
		alphar = 0;
	}
	protected static void reDrawOptions(){
		drawoptions = true;
	}
	
	
	protected static void setFloaterN(int i, int xclick, int yclick){
		clicklapse = 0.1f;
		click.setPosition(xclick - 32, Gdx.graphics.getHeight() - yclick - 33);
		
		byte folder = (byte) ((i % 6 != 0) ? Math.abs(i/6) + 1 : Math.abs(i/6));
		byte filen = (byte) ((i % 6 != 0) ? (i % 6) : i/(i/6));
		floaters.add(new Floater("floaters/type" + folder + "/" + filen + ".png", floatersize));
	}
}
