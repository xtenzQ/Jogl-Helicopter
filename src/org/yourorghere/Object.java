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
public class Object {

    public Vector3 position;
    public Vector3 angles;

    private float[][][] vertex;
    private float[][][] vertexT;

    private Texture texture;

    public Object(String address) {
        ObjLoader.load(address);
        vertex = ObjLoader.coords;
        vertexT = ObjLoader.coordsT;
        position = new Vector3(0, 0, 0);
        angles = new Vector3(0, 0, 0);
    }

    public void loadTexture(String address) {
        texture = TextureLoader.load(address);
    }
    
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void draw(GL gl) {
        gl.glColor3d(1, 1, 1);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureObject());
        gl.glPushMatrix();
        gl.glTranslatef(position.x, position.y, position.z);
        gl.glRotated(angles.x, 1, 0, 0);
        gl.glRotated(angles.y, 0, 1, 0);
        gl.glRotated(angles.z, 0, 0, 1);
        gl.glBegin(GL.GL_QUADS);
        for (int i = 0; i < vertexT.length; i++) {
            gl.glTexCoord2f(vertexT[i][0][0], vertexT[i][0][1]);
            gl.glVertex3f(vertex[i][0][0], vertex[i][0][1], vertex[i][0][2]);
            gl.glTexCoord2f(vertexT[i][1][0], vertexT[i][1][1]);
            gl.glVertex3f(vertex[i][1][0], vertex[i][1][1], vertex[i][1][2]);
            gl.glTexCoord2f(vertexT[i][2][0], vertexT[i][2][1]);
            gl.glVertex3f(vertex[i][2][0], vertex[i][2][1], vertex[i][2][2]);
            gl.glTexCoord2f(vertexT[i][3][0], vertexT[i][3][1]);
            gl.glVertex3f(vertex[i][3][0], vertex[i][3][1], vertex[i][3][2]);
        }
        gl.glEnd();
        gl.glPopMatrix();
    }

}
