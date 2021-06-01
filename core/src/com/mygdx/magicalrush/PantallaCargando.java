package com.mygdx.magicalrush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaCargando implements Screen
{
    private Juego juego;

    // La cámara y vista principal
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;

    // Fondo
    private Texture textura;

    // Imagen cargando
    private Texture texturaCargando;
    private Sprite spriteCargando;

    private AssetManager assetManager;  // Asset manager principal

    public PantallaCargando(Juego juego) {
        this.juego = juego;
        this.assetManager = juego.getAssetManager();
    }

    @Override
    public void show() {
        // Crea la cámara/vista
        camara = new OrthographicCamera(Juego.ANCHO_CAMARA, Juego.ALTO_CAMARA);
        camara.position.set(Juego.ANCHO_CAMARA / 2, Juego.ALTO_CAMARA / 2, 0);
        camara.update();
        vista = new StretchViewport(Juego.ANCHO_CAMARA, Juego.ALTO_CAMARA, camara);

        batch = new SpriteBatch();

        // Cargar recursos
        assetManager.load("LOAD.png", Texture.class);
        assetManager.finishLoading();
        texturaCargando = assetManager.get("LOAD.png");
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(Juego.ANCHO_CAMARA / 2 - spriteCargando.getWidth() / 2,
                Juego.ALTO_CAMARA / 2 - spriteCargando.getHeight() / 2);

        cargarRecursos();
        crearObjetos();
    }


    // Carga los recursos a través del administrador de assets (siguiente pantalla)
    private void cargarRecursos() {
        // Cargar las texturas/mapas
        AssetManager assetManager = juego.getAssetManager();   // Referencia al assetManager

        assetManager.load("fondo_A.png", Texture.class);    // Cargar imagen
        // Se bloquea hasta que cargue todos los recursos
        assetManager.finishLoading();
        Gdx.app.log("cargarRecursos","Iniciando...");
        // Carga los recursos de la siguiente pantalla (PantallaJuego)
        assetManager.load("Mapa.tmx", TiledMap.class);  // Cargar info del mapa456124165214378494
        assetManager.load("SegundoNivel.tmx", TiledMap.class);
        assetManager.load("TercerNivel.tmx", TiledMap.class);
        assetManager.load("RUIS-Sheet.png", Texture.class);    // Cargar imagen
        // Texturas de los botones
        assetManager.load("derecha.png", Texture.class);
        assetManager.load("izquierda.png", Texture.class);
        assetManager.load("salto.png", Texture.class);
        assetManager.load("musica/Nivel.mp3", Music.class);

        // Fin del juego
        //assetManager.load("archivo.png", Texture.class);

        Gdx.app.log("cargarRecursos", "Terminando...");
    }
    private void crearObjetos() {
        AssetManager assetManager = juego.getAssetManager();   // Referencia al assetManager
        // Carga el mapa en memoria
        textura = assetManager.get("fondo_A.png");
    }

    @Override
    public void render(float delta) {

        // Actualizar carga
        actualizar();

        // Dibujar
        borrarPantalla();
        spriteCargando.setRotation(spriteCargando.getRotation() + 15);

        batch.setProjectionMatrix(camara.combined);

        // Entre begin-end dibujamos nuestros objetos en pantalla
        batch.begin();
        batch.draw(textura, 0, 0);

        spriteCargando.draw(batch);
        batch.end();
    }

    private void actualizar() {

        if (assetManager.update()) {
            // Terminó la carga, cambiar de pantalla
            juego.setScreen(new NivelTres(juego));
        } else {
            // Aún no termina la carga de assets, leer el avance
            float avance = assetManager.getProgress()*100;
            Gdx.app.log("Cargando",avance+"%");
        }
    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0.42f, 0.55f, 1, 1);    // r, g, b, alpha
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }


    @Override
    public void resize(int width, int height) {
        vista.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        texturaCargando.dispose();
        // Los assets se liberan a través del assetManager
        AssetManager assetManager = juego.getAssetManager();
        assetManager.unload("fondo_A.png");
        // Los assets de PantallaJuego se liberan en el método dispose de PantallaJuego
    }
}