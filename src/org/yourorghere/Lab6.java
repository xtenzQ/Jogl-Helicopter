package org.yourorghere;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_LINEAR;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

/**
 * Main.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel)
 * <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class Lab6 implements GLEventListener {

    public static Terrain terrain;
    public static SkyBox skybox;
    public static Forest forest;
    public static Helicopter heli;

    public static int WIDTH = 1600;
    public static int HEIGHT = 900;

    public static void main(String[] args) {
        Frame frame = new Frame("Лабораторная работа №6");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new Lab6());
        Controller controller = new Controller();
        canvas.addKeyListener(controller);
        canvas.addMouseListener(controller);
        canvas.addMouseMotionListener(controller);
        canvas.addMouseWheelListener(controller);
        frame.add(canvas);
        frame.setSize(WIDTH, HEIGHT);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.

        gl.glClearColor(.9f, .9f, 1, 0);

        gl.glEnable(GL.GL_FOG);
        gl.glFogi(GL.GL_FOG_MODE, GL_LINEAR);
        gl.glFogfv(GL.GL_FOG_COLOR, new float[]{.3686f, .3686f, .4313f, 0}, 0);
        gl.glFogf(GL.GL_FOG_DENSITY, 0.35f);
        gl.glFogf(GL.GL_FOG_START, 200);             // Глубина, с которой начинается туман
        gl.glFogf(GL.GL_FOG_END, 400);               // Глубина, где туман заканчивается.

        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_ALPHA_TEST);
        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_TEXTURE_2D);

        terrain = new Terrain(1000, 100);
        terrain.setScale(4);
        terrain.setRecountDistance(50);
        terrain.setTexture(TextureLoader.load("images/terrain0.jpg"));
        terrain.setScale(10);
        TerrainLoader.heightCoeff = 150;
        terrain.setHeightMap(TerrainLoader.loadHeightMap("terrains/terrain.raw", 100, 100, 200, 300));

        float[][] t = terrain.createForest(100);

        skybox = new SkyBox(TextureLoader.load("images/sky.png"));

        forest = new Forest();
        forest.loadTexture("images/tree.png");
        forest.setForest(t[0], t[1], t[2]);

        heli = new Helicopter();
        heli.position.y = 100;
        MainCamera.position.y = 120;
        MainCamera.angles.y = 180;
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 10000.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        Controller.controll();
        GL gl = drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glRotated(-MainCamera.angles.x, 1, 0, 0);
        gl.glRotated(-MainCamera.angles.y, 0, 1, 0);
        gl.glRotated(-MainCamera.angles.z, 0, 0, 1);

        gl.glDisable(GL.GL_DEPTH_TEST);
        skybox.draw(gl);
        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glTranslated(-MainCamera.position.x, -MainCamera.position.y, -MainCamera.position.z);

        terrain.draw(gl);
        terrain.dispose();

        forest.drawForest(gl);

        heli.draw(gl);
        heli.physImpact();
        heli.accel.z = (float) 0;

        gl.glFlush();

    }

    public static double timeStep;
    public static int FPS;
    public static int frameCounter = 0;

    public void fixFPS() {
        frameCounter++;
        if (System.currentTimeMillis() - timeStep > 1000) {
            timeStep = System.currentTimeMillis();
            FPS = frameCounter;
            frameCounter = 0;
        }
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}
