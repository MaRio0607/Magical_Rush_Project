package com.mygdx.magicalrush;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Item {

    private Sprite sprite;
    private int tipo;

    public Item (Texture texture, float x, float y, int tipo){
        sprite = new Sprite(texture);
        sprite.setPosition(x,y);
        this.tipo = tipo;
    }

    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
    }

    // Dibuja el botón
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public float getY() {
        return sprite.getY();
    }

    public float getX() {
        return sprite.getX();
    }

    public float getTipo() {return tipo;}

}
