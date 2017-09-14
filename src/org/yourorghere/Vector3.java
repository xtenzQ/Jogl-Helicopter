/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yourorghere;

/**
 *
 * @author RichardKelly
 */
public class Vector3 {

    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(Vector3 vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    public void add(float dig) {
        x += dig;
        y += dig;
        z += dig;
    }

    public void add(double dig) {
        x += dig;
        y += dig;
        z += dig;
    }

    public void add(int dig) {
        x += dig;
        y += dig;
        z += dig;
    }

    public void mult(Vector3 vector) {
        x *= vector.x;
        y *= vector.y;
        z *= vector.z;
    }

    public void mult(float dig) {
        x *= dig;
        y *= dig;
        z *= dig;
    }

    public void mult(double dig) {
        x *= dig;
        y *= dig;
        z *= dig;
    }

    public void mult(int dig) {
        x *= dig;
        y *= dig;
        z *= dig;
    }

    public void normalize() {
        float m = magnitude();
        if (m != 0) {
            x /= m;
            y /= m;
            z /= m;
        }
    }

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
}
