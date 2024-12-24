package com.global.coffer.opengl.utils;

import android.content.res.AssetManager;

import com.global.coffer.CofferApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PCDParser {
    public static class Point {
        public float x, y, z;
        public int r, g, b; // 颜色信息

        public Point(float x, float y, float z, int r, int g, int b) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point(float x, float y, float z,int r) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.r = r;
        }
    }

    public List<Float> parsePCD(String filePath){
        List<Float> points = new ArrayList<>();
        try {
            // 获取AssetManager
            AssetManager assetManager = CofferApplication.Companion.getInstance().getAssets();
            // 打开位于assets文件夹下的文件
            InputStream is = assetManager.open(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            Float[] data = new Float[]{};

            // 跳过头部信息
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("DATA")) {
                    break;
                }
            }

            // 读取点云数据
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                float x = Float.parseFloat(parts[0]);
                float y = Float.parseFloat(parts[1]);
                float z = Float.parseFloat(parts[2]);
                points.add(x);
                points.add(y);
                points.add(z);
            }
            is.close();
            reader.close();
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
        return points;
    }

    public float[] parsePcdToFloat(String filePath){
        List<Float> points = new ArrayList<>();
        try {
            // 获取AssetManager
            AssetManager assetManager = CofferApplication.Companion.getInstance().getAssets();
            // 打开位于assets文件夹下的文件
            InputStream is = assetManager.open(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            Float[] data = new Float[]{};

            // 跳过头部信息
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("DATA")) {
                    break;
                }
            }

            // 读取点云数据
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                float x = Float.parseFloat(parts[0]);
                float y = Float.parseFloat(parts[1]);
                float z = Float.parseFloat(parts[2]);
                points.add(x);
                points.add(y);
                points.add(z);
            }
            is.close();
            reader.close();
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
        int length = points.size();
        float[] res = new float[length];
        for (int i = 0; i < length; i++) {
            res[i] = points.get(i);
        }
        return res;
    }
}

