package com.krishna.game;

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

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	int score=0;
	BitmapFont font;
	//0-pause State 1-running state 2-end state
	int gameState=0;
	//details of man
	Texture[] man;
	Texture fizzyman;
	int manState=0;
	int pause=0;
	float gravity=0.6f;
	float velocity=0.0f;
	int manY;
	int maxHeight;
	Rectangle manRectangle;
	//details of coin
	ArrayList<Integer> coinXs=new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangle=new ArrayList<Rectangle>();
	int countCoin=0;
	Texture coin;
	//details of bomb
	ArrayList<Integer> bombXs=new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle>bombRectangle=new ArrayList<Rectangle>();
	int countBomb=0;
	Texture bomb;

	Random random;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		fizzyman=new Texture("dizzy-1.png");
		manY=Gdx.graphics.getHeight()/2;
		maxHeight=Gdx.graphics.getHeight()-man[0].getHeight();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		random=new Random();


	}
	public void makecoin()
	{
		float height= (maxHeight) * random.nextFloat();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}
	public void makeBomb()
	{
		float height= (maxHeight) * random.nextFloat();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gameState==0)
		{
			if(Gdx.input.justTouched())
				gameState=1;
		}
		else if(gameState==1)
		{
			//coins
			if(countCoin==0)
			{
				makecoin();
			}
			coinRectangle.clear();
			for(int i=0;i<coinXs.size();i++)
			{
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));

				coinXs.set(i,coinXs.get(i)-15);

			}
			//bombs
			if(countBomb==0)
			{
				makeBomb();
			}
			bombRectangle.clear();
			for(int i=0;i<bombXs.size();i++)
			{
				batch.draw(bomb,bombXs.get(i),bombYs.get(i));
				bombRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
				bombXs.set(i,bombXs.get(i)-20);

			}
			//man settings
			if(Gdx.input.justTouched()) {
				velocity = -20;
			}
			if(pause==0) {
				manState = (manState + 1) % 4;
			}
			if(manY<=0)
				manY=0;
			if(manY>maxHeight ) {
				manY = maxHeight;
			}
			batch.draw(man[manState], Gdx.graphics.getWidth() / 2-man[0].getWidth()/2, manY);
			manRectangle=new Rectangle(Gdx.graphics.getWidth() / 2-man[0].getWidth()/2, manY,man[0].getWidth(),man[0].getHeight());
			//checking for collision with coins-reward points
			for(int i=0;i<coinRectangle.size();i++)
			{
				if(Intersector.overlaps(coinRectangle.get(i),manRectangle))
				{
					score++;
					coinRectangle.remove(i);
					coinXs.remove(i);
					coinYs.remove(i);
					break;
				}
			}
			//checking for collision with bomb the end game
			for(int i=0;i<bombRectangle.size();i++)
			{
				if(Intersector.overlaps(bombRectangle.get(i),manRectangle))
				{
					gameState=2;
				}
			}
			//changes for next render
			velocity+=gravity;
			manY-=velocity;
			pause=(pause+1)%6;
			countCoin=(countCoin+1)%50;
			countBomb=(countBomb+1)%150;
		}
		else
		{
			batch.draw(fizzyman, Gdx.graphics.getWidth() / 2-man[0].getWidth()/2, manY);
			//reset all values for next play

			if(Gdx.input.justTouched()) {
				gameState = 0;
				score=0;
				manY=Gdx.graphics.getHeight()/2;
				coinYs.clear();
				coinXs.clear();
				coinRectangle.clear();
				bombRectangle.clear();
				bombXs.clear();
				bombYs.clear();
				velocity=0;
				pause=0;
				countBomb=0;
				countCoin=0;
				manState=0;
			}
		}



		font.draw(batch,String.valueOf(score),100,200);

		batch.end();


	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
