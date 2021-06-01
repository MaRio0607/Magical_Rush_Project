package com.mygdx.magicalrush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Menu implements Screen
{
    // Referencia al objeto de tipo Game (tiene setScreen para cambiar de pantalla)
    private Juego juego;

    // La cámara y vista principal
    private OrthographicCamera camara;
    private Viewport vista;

    // Objeto para dibujar en la pantalla
    private SpriteBatch batch;

    // Fondo
    private Texture texturaMenu;
    private Texture texturaB;
    private Texture texturaC;

    // botones
    private Texture texturaPlay;
    private Texture texturaAbout;
    private Texture texturaInst;
    private Boton btnPlay;
    private Boton btnAbout;
    private Boton btnInst;

    //musica
    private Music music;

    public Menu(Juego juego) {
        this.juego = juego;
        music = juego.assetManager.get("musica/Menu.mp3",Music.class);
        music.setLooping(true);
        music.play();
    }

    /*
    Se ejecuta al mostrar este Screen como pantalla de la app
     */
    @Override
    public void show() {
        // Crea la cámara/vista
        camara = new OrthographicCamera(Juego.ANCHO_CAMARA, Juego.ALTO_CAMARA);
        camara.position.set(Juego.ANCHO_CAMARA / 2, Juego.ALTO_CAMARA / 2, 0);
        camara.update();
        vista = new StretchViewport(Juego.ANCHO_CAMARA, Juego.ALTO_CAMARA, camara);

        batch = new SpriteBatch();

        cargarRecursos();
        crearObjetos();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        //musica
        juego.reproducir(Juego.TipodeMusica.MENU);
    }

    // Carga los recursos a través del administrador de assets
    private void cargarRecursos() {
        // Cargar las texturas/mapas
        AssetManager assetManager = juego.getAssetManager();   // Referencia al assetManager


        assetManager.load("fondo_A.png", Texture.class);    // Cargar imagen
        assetManager.load("magical_rush.png",Texture.class);
        assetManager.load("btones.png",Texture.class);
        assetManager.load("about.png", Texture.class);
        assetManager.load("inst.png",Texture.class);
        assetManager.load("play.png", Texture.class);
        assetManager.load("musica/Menu.mp3", Music.class);
        // Texturas de los botones

        // Se bloquea hasta que cargue todos los recursos
        assetManager.finishLoading();
    }

    private void crearObjetos() {
        AssetManager assetManager = juego.getAssetManager();   // Referencia al assetManager
        // Carga el mapa en memoria
        texturaMenu = assetManager.get("fondo_A.png");
        texturaB=assetManager.get("magical_rush.png");
        texturaC = assetManager.get("btones.png");
        texturaPlay = assetManager.get("play.png");
        texturaAbout = assetManager.get("about.png");
        texturaInst=assetManager.get("inst.png");


        btnAbout = new Boton(texturaAbout);
        btnAbout.setPosicion(500,150);
        btnPlay = new Boton(texturaPlay);
        btnPlay.setPosicion(490,350);
        btnInst=new Boton(texturaInst);
        btnInst.setPosicion(450,250);


    }

    /*
    Dibuja TODOS los elementos del juego en la pantalla.
    Este método se está ejecutando muchas veces por segundo.
     */
    @Override
    public void render(float delta) { // delta es el tiempo entre frames (Gdx.graphics.getDeltaTime())

        // Dibujar
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);

        // Entre begin-end dibujamos nuestros objetos en pantalla
        batch.begin();

        batch.draw(texturaMenu, 0, 0);
        batch.draw(texturaB,330,400);
        batch.draw(texturaC, 390, 100);
        btnAbout.render(batch);
        btnPlay.render(batch);
        btnInst.render(batch);
        batch.end();
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

    // Libera los assets
    @Override
    public void dispose() {
        // Los assets se liberan a través del assetManager
        AssetManager assetManager = juego.getAssetManager();
        assetManager.unload("fondo_A.jpg");
        assetManager.unload("magical_rush.png");
        assetManager.unload("btones.png");
        assetManager.unload("play.png");
        assetManager.unload("about.png");
        assetManager.unload("inst.png");

        music.stop();
    }

    /*
    Clase utilizada para manejar los eventos de touch en la pantalla
     */
    public class ProcesadorEntrada extends InputAdapter
    {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla

        /*
        Se ejecuta cuando el usuario PONE un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            return true;    // Indica que ya procesó el evento
        }

        /*
        Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
         */
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            if (btnPlay.contiene(x,y)){
                juego.setScreen(new PantallaCargando(juego));

                juego.detener();

            } else if (btnAbout.contiene(x,y)){
                juego.setScreen(new AcercaDe(juego));
            }else if (btnInst.contiene(x,y)){
                juego.setScreen(new Instrucciones(juego));
            }
            return true;    // Indica que ya procesó el evento
        }


        // Se ejecuta cuando el usuario MUEVE el dedo sobre la pantalla
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            transformarCoordenadas(screenX, screenY);

            return true;
        }


        private void transformarCoordenadas(int screenX, int screenY) {
            // Transformar las coordenadas de la pantalla física a la cámara HUD
            coordenadas.set(screenX, screenY, 0);
            camara.unproject(coordenadas);
            // Obtiene las coordenadas relativas a la pantalla virtual
            x = coordenadas.x;
            y = coordenadas.y;
        }
    }

}
