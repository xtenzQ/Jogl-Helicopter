package org.yourorghere;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ObjLoader {

    // Строки вершины
    private static ArrayList<String> V;
    // Строки текстурных координат
    private static ArrayList<String> VT;
    // Строки индексов полигонов
    private static ArrayList<String> F;
    /*
     Трёхмерный массив полигонов
     Первый индекс - номер полигона
     Второй индекс - номер вершины (1, 2, 3, 4)
     Третий индекс - координата (x, y, z)
     */
    public static float[][][] coords;
    /*
     Трёхмерный массив текстурных полигонов
     Первый индекс - номер полигона
     Второй индекс - номер текстурной вершины (1, 2, 3, 4)
     Третий индекс - текстурная координата (u, v)
     */
    public static float[][][] coordsT;

    private ObjLoader() {
    }

    public static void load(String filename) {
        V = new ArrayList();
        VT = new ArrayList();
        F = new ArrayList();
        try {
            Scanner myS = new Scanner(new FileReader(filename));
            while (myS.hasNext()) {
                String temp = myS.nextLine();
                if (!"".equals(temp)) {
                    if (temp.charAt(0) == 'v') {
                        // Вершина
                        if (temp.charAt(1) == ' ') {
                            V.add(temp);
                            // Текстурная вершина
                        } else {
                            VT.add(temp);
                        }
                    }
                    if (temp.charAt(0) == 'f') {
                        F.add(temp);
                    }
                }
            }
            F = clear(F);
            V = clear(V);
            VT = clear(VT);
            input();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ObjLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ObjLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static ArrayList<String> clear(ArrayList<String> A) {
        for (int i = 0; i < A.size(); i++) {
            String temp = A.get(i).substring(2);
            String[] split = temp.trim().split("/");
            temp = "";
            for (String split1 : split) {
                temp += split1;
                temp += " ";
            }
            A.set(i, temp.trim());
        }
        return (A);
    }

    public static float parseFloat(String digit) {
        float newDigit;
        boolean negative = false;
        boolean fraction = false;

        long numeral = 0;
        int degreese = 1;

        for (int i = 0; i < digit.length(); i++) {
            if (digit.charAt(i) == '-') {
                negative = true;
            }
            if (digit.charAt(i) == '.' || digit.charAt(i) == ',') {
                fraction = true;
            }
            if ((int) digit.charAt(i) > 47 && (int) digit.charAt(i) < 58) {
                numeral = numeral * 10 + (int) digit.charAt(i) - 48;
                if (fraction) {
                    degreese = degreese * 10;
                }
            }
        }
        if (negative) {
            numeral = -numeral;
        }
        if (fraction) {
            newDigit = (float) numeral / degreese;
        } else {
            newDigit = numeral;
        }
        return newDigit;
    }

    public static void input() {
        coords = new float[F.size()][4][3];
        coordsT = new float[F.size()][4][2];
        for (int i = 0; i < F.size(); i++) {
            Scanner mySF = new Scanner(F.get(i));
            String[] splitF = F.get(i).trim().split(" ");
            for (int in = 0; in < splitF.length / 2; in++) {
                int num = mySF.nextInt() - 1;
                String[] splitV = V.get(num).trim().split(" ");
                for (int j = 0; j < 3; j++) {
                    coords[i][in][j] = Float.valueOf(splitV[j]);
                }
                num = mySF.nextInt() - 1;
                String[] splitVT = VT.get(num).trim().split(" ");
                for (int j = 0; j < 2; j++) {
                    if (j == 0) {
                        coordsT[i][in][j] = parseFloat(splitVT[j]);
                    } else {
                        // Cinema 4D fix (инвертированные координаты)
                        coordsT[i][in][j] = 1 - parseFloat(splitVT[j]);
                    }

                }
            }
            if (splitF.length == 6) {
                coords[i][3] = coords[i][0];
                coordsT[i][3] = coordsT[i][0];
            }
        }
    }

}
