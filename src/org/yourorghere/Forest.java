/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yourorghere;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;

/**
 *
 * @author RichardKelly
 */
public class Forest {

    public Texture texture;

    public float[] x;
    public float[] y;
    public float[] z;

    public float halfWidth;
    public float height;

    public Forest() {
        halfWidth = 15;
        height = 33;
    }

    public void loadTexture(String address) {
        texture = TextureLoader.load(address);
    }

    public void setForest(float[] x, float[] y, float[] z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void drawForest(GL gl) {
        gl.glColor3d(1, 1, 1);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureObject());
        for (int i = 0; i < x.length; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(x[i], y[i], z[i]);
            gl.glRotated(MainCamera.angles.y, 0, 1, 0);
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex2f(halfWidth, height);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex2f(halfWidth, 0);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex2f(-halfWidth, 0);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex2f(-halfWidth, height);
            gl.glEnd();
            gl.glPopMatrix();
        }
    }
}
