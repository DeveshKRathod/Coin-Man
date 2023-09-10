/*package com.devesh.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background; //creating background variable
	Texture[] man;
	int manState = 0;
	int pause = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(pause<15) pause++;
		else{
			if(manState<3) manState++;
			else manState=0;
		}
		batch.draw(man[manState],Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
		//batch.draw(man[1],Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}*/
package com.devesh.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState = 0;
	int pause = 0;
	float gravity = 0.2f;
	float velocity = 0;
	int manY = 0;
	Rectangle manRectangle;
	BitmapFont font;
	BitmapFont data;
	Texture dizzy;

	int score = 0;
	int gameState = 0;

	Random random;

	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles =  new ArrayList<Rectangle>();
	Texture coin;
	int coinCount;

	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangles =  new ArrayList<Rectangle>();
	Texture bomb;
	int bombCount;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");

		dizzy = new Texture("dizzy-1.png");


		manY = Gdx.graphics.getHeight() / 2;

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		random = new Random();

		font = new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().setScale(10);

		data = new BitmapFont();
		data.setColor(Color.RED);
		data.getData().setScale(8);

	}

	public void makeCoin() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makeBomb() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState==1){
			//game is live
			// BOMB
			if (bombCount < 150) {
				bombCount++;
			} else {
				bombCount = 0;
				makeBomb();
			}

			bombRectangles.clear();
			for (int i=0;i < bombXs.size();i++) {
				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i) - 16);
				bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}

			// COINS
			if (coinCount < 40) {
				coinCount++;
			} else {
				coinCount = 0;
				makeCoin();
			}

			coinRectangles.clear();
			for (int i=0;i < coinXs.size();i++) {
				batch.draw(coin, coinXs.get(i), coinYs.get(i));
				coinXs.set(i, coinXs.get(i) - 8);
				coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
			}

			//touch
			if (Gdx.input.justTouched()) {
				velocity = -15;
			}

			//avatar running
			if (pause < 8) {
				pause++;
			} else {
				pause = 0;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}
			//jumping
			velocity += 2*gravity;
			manY -= velocity;

			if (manY <= 0) {
				manY = 0;
			}
		}
		else if(gameState==0){
			//waiting to start
			if(Gdx.input.justTouched()){
				gameState = 1;
			}
		}
		else if(gameState==2){
			//game over!!

			if(Gdx.input.justTouched()){
				gameState = 1;
				manY = Gdx.graphics.getHeight() / 2;
				score = 0;
				velocity = 0;
				coinXs.clear();
				coinYs.clear();
				coinRectangles.clear();
				coinCount = 0;
				bombXs.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombCount = 0;
			}
		}

		if(gameState==2){
			batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}
		else {
			batch.draw(man[manState],Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}

		//overlaps
		manRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY, man[manState].getWidth(), man[manState].getHeight());

		//coin collection
		for (int i=0; i < coinRectangles.size();i++) {
			if (Intersector.overlaps(manRectangle, coinRectangles.get(i))) {
				score++;

				coinRectangles.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		//bomb collision
		for (int i=0; i < bombRectangles.size();i++) {
			if (Intersector.overlaps(manRectangle, bombRectangles.get(i))) {
				if(manY - 100 < 0)
					data.draw(batch,"GAME-OVER !!",Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2 - 250 , 900);
				else
					data.draw(batch,"GAME-OVER !!",Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2 - 250 , manY-50);
				Gdx.app.log("Bomb!", "Collision!");
				gameState = 2;
			}
		}

		font.draw(batch,String.valueOf(score),50,300);

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}

