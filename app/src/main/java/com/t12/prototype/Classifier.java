package com.t12.prototype;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Classifier {

    //임시 ui에서 실행할 때는 MainActivity에서 실행하여 activity 인자가 없었으나 함수화 시키면서 인자 추가함.
    public String classify(Activity activity, Intent data){
        String result;

        float[][][][] input = new float[1][128][128][3];
        float[][] output = new float[1][4]; //아빠, 할아버지, 할머니, 엄마

        try {
            int batchNum = 0;
            //input으로 넣어줄 데이터를 intent로 받아옴
            InputStream buf = activity.getContentResolver().openInputStream(data.getData());
            Bitmap bitmap = BitmapFactory.decodeStream(buf);
            buf.close();

            //128*128*3
            for (int x = 0; x < 128; x++) {
                for (int y = 0; y < 128; y++) {
                    int pixel = bitmap.getPixel(x, y);
                    input[batchNum][x][y][0] = Color.red(pixel) / 1.0f;
                    input[batchNum][x][y][1] = Color.green(pixel) / 1.0f;
                    input[batchNum][x][y][2] = Color.blue(pixel) / 1.0f;
                }
            }

            Interpreter lite = getTfliteInterpreter(activity, "converted_model.tflite");
            lite.run(input, output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int index=0;
        for(int i=0; i<4; i++){
            if(output[0][i] >= output[0][index]){
                index = i;

            }
        }

        if (index == 0) {
            result ="아빠";
        }
        else if (index == 1) {
            result ="할아버지";
        }
        else if (index == 2) {
            result ="할머니";
        }
        else {
             result ="엄마";
        }

        return result;
    }

    private Interpreter getTfliteInterpreter(Activity activity, String modelPath) {
        //임시 ui에서는 MainActivity에서 실행했기 때문에 activity에 MainActivity.this를 넣고 실행했습니다.
        try {
            return new Interpreter(loadModelFile(activity, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
