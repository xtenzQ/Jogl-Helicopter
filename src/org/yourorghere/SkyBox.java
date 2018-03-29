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
public class SkyBox {
        
    public Texture texture;
    
    public float radius;
    
    public SkyBox(Texture texture) {
        
        this.texture = texture;
        radius = 10;
    }
    
    /**
     * Прорисовка SkyBox
     * @param gl контекст отрисовки
     */
    public void draw(GL gl) {

        gl.glColor3d(1, 1, 1);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureObject());
        gl.glBegin(GL.GL_QUADS);
        // Левая грань
        gl.glTexCoord2f(.2505f, .334f);
        gl.glVertex3f(-radius, radius, -radius);        
        gl.glTexCoord2f(0, .334f);
        gl.glVertex3f(-radius, radius, radius);     
        gl.glTexCoord2f(0, .666f);      
        gl.glVertex3f(-radius, -radius, radius);      
        gl.glTexCoord2f(.2505f, .666f);
        gl.glVertex3f(-radius, -radius, -radius);
        // Правая грань
        gl.glTexCoord2f(.75f, .334f);
        gl.glVertex3f(radius, radius, radius);
        gl.glTexCoord2f(.4996f, .334f);
        gl.glVertex3f(radius, radius, -radius);        
        gl.glTexCoord2f(.4996f, .666f);
        gl.glVertex3f(radius, -radius, -radius);       
        gl.glTexCoord2f(.75f, .666f);
        gl.glVertex3f(radius, -radius, radius);
        // Пердняя грань
        gl.glTexCoord2f(.4996f, .334f);
        gl.glVertex3f(radius, radius, -radius);
        gl.glTexCoord2f(.2505f, .334f);
        gl.glVertex3f(-radius, radius, -radius);   
        gl.glTexCoord2f(.2505f, .666f);
        gl.glVertex3f(-radius, -radius, -radius);       
        gl.glTexCoord2f(.4996f, .666f);
        gl.glVertex3f(radius, -radius, -radius);
        // Задняя грань
        gl.glTexCoord2f(1.0f, .334f);
        gl.glVertex3f(-radius, radius, radius);
        gl.glTexCoord2f(.75f, .334f);
        gl.glVertex3f(radius, radius, radius);
        gl.glTexCoord2f(.75f, .666f);
        gl.glVertex3f(radius, -radius, radius);
        gl.glTexCoord2f(1.0f, .666f);
        gl.glVertex3f(-radius, -radius, radius);
        // Верхняя грань
        gl.glTexCoord2f(.2505f, 0.334f);
        gl.glVertex3f(-radius, radius, -radius);
        gl.glTexCoord2f(.4996f, 0.334f);
        gl.glVertex3f(radius, radius, -radius);
        gl.glTexCoord2f(.4996f, 0f);
        gl.glVertex3f(radius, radius, radius);
        gl.glTexCoord2f(.2505f, 0f);
        gl.glVertex3f(-radius, radius, radius);
        // Нижняя грань
        gl.glTexCoord2f(.2505f, 1.0f);
        gl.glVertex3f(-radius, -radius, radius);
        gl.glTexCoord2f(.4996f, 1.0f);
        gl.glVertex3f(radius, -radius, radius);
        gl.glTexCoord2f(.4996f, 0.666f);
        gl.glVertex3f(radius, -radius, -radius);
        gl.glTexCoord2f(.2505f, 0.666f);
        gl.glVertex3f(-radius, -radius, -radius);
        gl.glEnd();
    }
    
}
