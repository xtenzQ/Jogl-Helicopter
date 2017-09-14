/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yourorghere;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;
import static org.yourorghere.Lab6.heli;

/**
 *
 * @author RichardKelly
 */
public class Terrain {

    private double positionX;
    private double positionZ;

    private int arrayPositionX;
    private int arrayPositionZ;
    private int[] riftX;
    private int[] riftZ;

    private double[][] vertexX;
    private double[][] vertexZ;

    private double[][] height;
    private int width;
    private int length;

    private double scale;
    private double[][] textureX;
    private double[][] textureY;
    private Texture texture;
    private boolean hasTexture;

    private double size;
    private double step;
    private int count;

    private double recountDistance;
    private double DV;
    private double DT;
    private int DA;
    
    public double max;
    public double min;

    public Terrain(double size, int count) {
        positionX = 0;
        positionZ = 0;
        arrayPositionX = 0;
        arrayPositionZ = 0;

        this.count = count;
        this.size = size;
        step = size / count;
        vertexMapInitialization();

        width = count;
        length = count;
        height = new double[width][length];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                height[i][j] = 0;
            }
        }

        riftX = new int[count];
        riftZ = new int[count];
        riftIndexDispose();

        scale = 1;
        textureMapInitialization();
        setRecountDistance(step * 5);
    }

    public double getHeight(Vector3 vec) {
        int i = (vec.x >= 0) ? (int) ((vec.x % (step * width)) / step) : width + (int) ((vec.x % (step * width)) / step);
        int j = (vec.z >= 0) ? (int) ((vec.z % (step * length)) / step) : length + (int) ((vec.z % (step * length)) / step);
        return height[i][j];
    }

    private void vertexMapInitialization() {
        vertexX = new double[count][count];
        vertexZ = new double[count][count];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                vertexX[i][j] = i * step - size * 0.5;
                vertexZ[i][j] = j * step - size * 0.5;
            }
        }
    }

    private void textureMapInitialization() {
        textureX = new double[count][count];
        textureY = new double[count][count];
        double dStep = (double) 1 / count;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                textureX[i][j] = i * dStep * scale;
                textureY[i][j] = j * dStep * scale;
            }
        }
    }

    private void riftIndexDispose() {
        int x = arrayPositionX;
        int z = arrayPositionZ;
        for (int i = 0; i < count; i++) {
            while (x >= width) {
                x -= width;
            }
            while (z >= length) {
                z -= length;
            }
            riftX[i] = x;
            riftZ[i] = z;
            x++;
            z++;
        }
    }

    public void setScale(double scale) {
        this.scale = scale;
        DT = (DV / size) * scale;
        textureMapInitialization();
    }

    public void setTexture(Texture texture) {
        if (texture != null) {
            this.texture = texture;
            hasTexture = true;
        } else {
            System.out.println("Texture load error");
        }
    }

    public void setHeightMap(double[][] heightMap) {
        if (heightMap != null) {
            height = heightMap;
            width = height.length;
            length = height[0].length;
            max = min = heightMap[0][0];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < length; j++) {
                    if (heightMap[i][j] < min) {
                        min = heightMap[i][j];
                    }
                    if (heightMap[i][j] > max) {
                        max = heightMap[i][j];
                    }
                }
            }
        } else {
            System.out.println("Invalid height map");
        }
    }

    public void setRecountDistance(double distance) {
        if (distance >= step) {
            recountDistance = distance;
            DA = (int) (recountDistance / step);
            DV = DA * step;
            DT = (DV / size) * scale;
        } else {
            System.out.println("Invalid recount distance");
        }
    }

    public void dispose() {
        double dx = MainCamera.position.x - positionX;
        double dz = MainCamera.position.z - positionZ;
        if (dx > recountDistance || dx < -recountDistance || dz > recountDistance || dz < -recountDistance) {
            double dpw, dpl, dtw, dtl;
            int coeff;

            coeff = (int) (dx / recountDistance);
            dpw = coeff * DV;
            arrayPositionX += coeff * DA;
            dtw = coeff * DT;

            coeff = (int) (dz / recountDistance);
            dpl = coeff * DV;
            arrayPositionZ += coeff * DA;
            dtl = coeff * DT;

            while (arrayPositionX >= width) {
                arrayPositionX -= width;
            }
            while (arrayPositionX < 0) {
                arrayPositionX += width;
            }
            while (arrayPositionZ >= length) {
                arrayPositionZ -= length;
            }
            while (arrayPositionZ < 0) {
                arrayPositionZ += length;
            }
            positionX += dpw;
            positionZ += dpl;
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < count; j++) {
                    vertexX[i][j] += dpw;
                    vertexZ[i][j] += dpl;
                    textureX[i][j] += dtw;
                    textureY[i][j] += dtl;
                }
            }
            riftIndexDispose();
        }
    }

    public void draw(GL gl) {
        int inci = 1;
        int incj = 1;
        if (hasTexture) {
            gl.glColor3d(1, 1, 1);
            gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureObject());
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            gl.glBegin(GL.GL_TRIANGLES);
            double c1, c2, c3, c4;
            for (int i = 0; i < count - 1; i++) {
                for (int j = 0; j < count - 1; j++) {
                    c1 = (height[riftX[i]][riftZ[j]] - min) / (max - min);
                    c1 *= c1;
                    c2 = (height[riftX[inci]][riftZ[j]] - min) / (max - min);
                    c2 *= c2;
                    c3 = (height[riftX[i]][riftZ[incj]] - min) / (max - min);
                    c3 *= c3;
                    c4 = (height[riftX[inci]][riftZ[incj]] - min) / (max - min);
                    c4 *= c4;
                    gl.glColor3d(c1, c1, c1);
                    //gl.glColor3d(c1, 0, 0);
                    gl.glTexCoord2d(textureX[i][j], textureY[i][j]);
                    gl.glVertex3d(vertexX[i][j], height[riftX[i]][riftZ[j]], vertexZ[i][j]);
                    gl.glColor3d(c2, c2, c2);
                    //gl.glColor3d(c2, 0, 0);
                    gl.glTexCoord2d(textureX[inci][j], textureY[inci][j]);
                    gl.glVertex3d(vertexX[inci][j], height[riftX[inci]][riftZ[j]], vertexZ[inci][j]);
                    gl.glColor3d(c3, c3, c3);
                    //gl.glColor3d(c3, 0, 0);
                    gl.glTexCoord2d(textureX[i][incj], textureY[i][incj]);
                    gl.glVertex3d(vertexX[i][incj], height[riftX[i]][riftZ[incj]], vertexZ[i][incj]);                  
                    gl.glColor3d(c4, c4, c4);
                    //gl.glColor3d(c4, 0, 0);
                    gl.glTexCoord2d(textureX[inci][incj], textureY[inci][incj]);
                    gl.glVertex3d(vertexX[inci][incj], height[riftX[inci]][riftZ[incj]], vertexZ[inci][incj]);
                    gl.glColor3d(c3, c3, c3);
                    //gl.glColor3d(c3, 0, 0);
                    gl.glTexCoord2d(textureX[i][incj], textureY[i][incj]);
                    gl.glVertex3d(vertexX[i][incj], height[riftX[i]][riftZ[incj]], vertexZ[i][incj]);
                    gl.glColor3d(c2, c2, c2);
                    //gl.glColor3d(c2, 0, 0);
                    gl.glTexCoord2d(textureX[inci][j], textureY[inci][j]);
                    gl.glVertex3d(vertexX[inci][j], height[riftX[inci]][riftZ[j]], vertexZ[inci][j]);
                    incj++;
                }
                incj = 1;
                inci++;
            }
            gl.glEnd();
//            float i = (float) (heli.position.x % (step * width));
//            float j = (float) (heli.position.z % (step * length));
//            float h = (float) getHeight(heli.position);
//            gl.glBegin(GL.GL_QUADS);
//            gl.glVertex3f(5f + i, h, 5f + j);
//            gl.glVertex3f(5f + i, h, -5f + j);
//            gl.glVertex3f(-5f + i, h, -5f + j);
//            gl.glVertex3f(-5f + i, h, 5f + j);
//            gl.glEnd();
        } else {
            gl.glColor3d(0.5, 0.5, 0.5);
            for (int i = 0; i < count - 1; i++) {
                for (int j = 0; j < count - 1; j++) {

                    gl.glBegin(GL.GL_LINE_LOOP);
                    gl.glVertex3d(vertexX[i][j], height[riftX[i]][riftZ[j]], vertexZ[i][j]);
                    gl.glVertex3d(vertexX[inci][j], height[riftX[inci]][riftZ[j]], vertexZ[inci][j]);
                    gl.glVertex3d(vertexX[i][incj], height[riftX[i]][riftZ[incj]], vertexZ[i][incj]);
                    gl.glEnd();

                    gl.glBegin(GL.GL_LINE_LOOP);
                    gl.glVertex3d(vertexX[inci][incj], height[riftX[inci]][riftZ[incj]], vertexZ[inci][incj]);
                    gl.glVertex3d(vertexX[i][incj], height[riftX[i]][riftZ[incj]], vertexZ[i][incj]);
                    gl.glVertex3d(vertexX[inci][j], height[riftX[inci]][riftZ[j]], vertexZ[inci][j]);
                    gl.glEnd();

                    incj++;
                }
                incj = 1;
                inci++;
            }
        }
    }

    public float[][] createForest(int count) {
        float[][] t = new float[3][count];

        int i;
        int j;

        for (int k = 0; k < count; k++) {
            i = (int) (Math.random() * width);
            j = (int) (Math.random() * length);

            t[0][k] = (float) (i * step - size * 0.5);
            t[1][k] = (float) (height[i][j]);
            t[2][k] = (float) (j * step - size * 0.5);
        }
        return t;
    }

}
