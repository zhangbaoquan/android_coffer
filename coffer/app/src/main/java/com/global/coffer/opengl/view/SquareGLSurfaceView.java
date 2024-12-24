package com.global.coffer.opengl.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
public class SquareGLSurfaceView extends GLSurfaceView {


    public SquareGLSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public SquareGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
    }
}
