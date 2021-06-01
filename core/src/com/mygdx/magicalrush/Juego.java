package com.mygdx.magicalrush;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;

public class Juego extends Game
{
    // Constantes públicas
    public static final float ANCHO_CAMARA = 1280;
    public static final float ALTO_CAMARA = 720;

    // Administra la carga de los assets del juego
    public final AssetManager assetManager = new AssetManager();

    //musica
    public Music fondo;

    @Override
    public void create() {

        // Agregamos un loader para los mapas
        assetManager.setLoader(TiledMap.class,
                new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load("musica/Menu.mp3", Music.class);
        assetManager.load("musica/Nivel.mp3", Music.class);
        assetManager.load("musica/BOSS.mp3", Music.class);
        assetManager.finishLoading();
        // Pantalla inicial
        setScreen(new Menu(this));
    }

    @Override
    public void render(){
        super.render();
        //assetManager.update();
    }

    public void reproducir(TipodeMusica tipodeMusica){

        switch (tipodeMusica){
            case MENU:
                fondo = assetManager.get("musica/Menu.mp3");
                break;
            case NIVEL:
                fondo = assetManager.get("musica/Nivel.mp3");
                break;
            case JEFE:
                fondo = assetManager.get("musica/BOSS.mp3");
                break;
        }

    }

    public void detener(){
        if(fondo.isPlaying()){
            fondo.stop();
        }
    }


    // Método accesor de assetManager
    public AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.clear();
    }

    public enum  TipodeMusica {
        MENU,
        NIVEL,
        JEFE
    }
}
