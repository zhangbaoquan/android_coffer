package com.global.coffer.demoPage;

import static com.global.coffer.utils.GlideUtilsKt.saveFile;
import static com.tencent.mapsdk.internal.uh.getContext;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.global.coffer.CofferApplication;
import com.global.coffer.R;
import com.global.coffer.utils.GlideEngine;
import com.luck.picture.lib.basic.PictureMediaScannerConnection;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.engine.MediaPlayerEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnCallbackListener;
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.utils.DownloadFileUtils;
import com.luck.picture.lib.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SelectImgVideoActivity extends AppCompatActivity {

    public final static String SYSTEM_ALL = SelectMimeType.SYSTEM_IMAGE + "," + SelectMimeType.SYSTEM_VIDEO;

    private String imgUrl = "http://cn.bing.com/th?id=OHR.LargestCave_ZH-CN2069899703_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp";

    String str1 = "https://willand-s3-public-china.s3.cn-north-1.amazonaws.com.cn/ICON/car.png";
    String str2 = "https://willand-s3-public-china.s3.cn-north-1.amazonaws.com.cn/Test/video(19).mp4";

    String str3 = "content://media/external/video/media/1000037347";

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;

    private ArrayList<LocalMedia> data = new ArrayList<>();

    LocalMedia imageDate = new LocalMedia();
    LocalMedia videoDate = new LocalMedia();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_video_main);
        imageView1 = findViewById(R.id.img1);
        imageView2 = findViewById(R.id.img2);
        imageView3 = findViewById(R.id.img3);

        Glide.with(this).load(str2).into(imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageDate.setFileName(result.get(0).getFileName());
//                imageDate.setMimeType("image");
//                imageDate.setPath(str1);
                imageDate.setMimeType("video/mp4");
                imageDate.setPath(str2);
//                imageDate.setRealPath(result.get(0).getRealPath());
                data.clear();
                data.add(imageDate);
                PictureSelector.create(SelectImgVideoActivity.this)
                        .openPreview()
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .setVideoPlayerEngine(new MediaPlayerEngine())
//                        .isHidePreviewDownload(true)
                        .isVideoPauseResumePlay(true)
                        .isAutoVideoPlay(true)
                        .startActivityPreview(0, false, data);
            }
        });

        findViewById(R.id.b0).setOnClickListener(v -> {
            // 选择本地图片
            PictureSelector.create(SelectImgVideoActivity.this)
                    .openGallery(SelectMimeType.ofAll())
                    .setMaxSelectNum(9)
                    .setMaxVideoSelectNum(9)
                    .setImageEngine(GlideEngine.createGlideEngine() )
                    .setMinSelectNum(0)
                    .setImageSpanCount(3)
                    .setSelectionMode(SelectModeConfig.MULTIPLE)
                    .isPreviewImage(true)
                    .isDisplayCamera(true)
                    .isWithSelectVideoImage(true)
                    .isGif(false)
                    .isOpenClickSound(false)
                    .isPreviewFullScreenMode(true)
                    .isPreviewZoomEffect(true)
                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(ArrayList<LocalMedia> result) {
                            Log.i("haha_tag","有结果返回");
                            Glide.with(SelectImgVideoActivity.this)
                                    .load(result.get(0).getRealPath())
                                    .into(imageView1);
                            imageDate.setFileName(result.get(0).getFileName());
                            imageDate.setMimeType(result.get(0).getMimeType());
                            imageDate.setPath(result.get(0).getPath());
                            imageDate.setRealPath(result.get(0).getRealPath());
                        }

                        @Override
                        public void onCancel() {

                        }
                    });

        });

        findViewById(R.id.b1).setOnClickListener(v -> {
            // 选择本地视频 , 真正的文件资源是result.get(0).realPath
            PictureSelector.create(SelectImgVideoActivity.this)
                    .openGallery(SelectMimeType.ofVideo())
                    .setMaxSelectNum(9)
                    .setImageEngine(GlideEngine.createGlideEngine() )
                    .setMinSelectNum(0)
                    .setImageSpanCount(3)
                    .setSelectionMode(SelectModeConfig.MULTIPLE)
                    .isPreviewImage(true)
                    .isDisplayCamera(true)
                    .isGif(false)
                    .isOpenClickSound(false)
                    .isPreviewFullScreenMode(true)
                    .isPreviewZoomEffect(true)
                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(ArrayList<LocalMedia> result) {
                            Log.i("haha_tag","有结果返回");
                            data.clear();
                            data.add(result.get(0));
                            Glide.with(SelectImgVideoActivity.this)
                                    .load(result.get(0).getRealPath())
                                    .into(imageView2);
                            long duration = result.get(0).getDuration();
                            if(duration <= 0){
                                return;
                            }
                            int timeSeconds = (int)(duration / 1000);
                            Log.i("haha_tag","时长 ： "+formatSecondsTo00(timeSeconds));
                            videoDate.setFileName(result.get(0).getFileName());
                            videoDate.setMimeType(result.get(0).getMimeType());
                            videoDate.setPath(result.get(0).getPath());
                            videoDate.setRealPath(result.get(0).getRealPath());
                            videoDate.setDuration(result.get(0).getDuration());
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                data.add(imageDate);
                PictureSelector.create(SelectImgVideoActivity.this)
                        .openPreview()
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .isHidePreviewDownload(false)
                        .startActivityPreview(0, false, data);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                data.add(videoDate);
                PictureSelector.create(SelectImgVideoActivity.this)
                        .openPreview()
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .setVideoPlayerEngine(new MediaPlayerEngine())
                        .isHidePreviewDownload(false)
                        .isVideoPauseResumePlay(true)
                        .isAutoVideoPlay(true)

                        .setExternalPreviewEventListener(new OnExternalPreviewEventListener() {
                            @Override
                            public void onPreviewDelete(int position) {

                            }

                            @Override
                            public boolean onLongPressDownload(Context context, LocalMedia media) {
                                return false;
                            }
                        })
                        .startActivityPreview(0, false, data);
            }
        });

        findViewById(R.id.b2).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String[] type = new String[]{"image/*","video/*"};
            intent.setType(type[0]);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, type);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, 1);
        });

        findViewById(R.id.b3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(str2);
//                downloadTest(str1);
            }
        });


    }
    private void downloadFile(String url){
        Glide.with(this).asFile().load(url).listener(new RequestListener<File>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                String mineType;
                if(url.contains(".png")){
                    mineType = "image";
                } else {
                    mineType = "video";
                }
                Uri uri = saveFile(SelectImgVideoActivity.this, resource, mineType);
                if(uri != null){
                    Log.i("haha_tag","下载成功");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CofferApplication.Companion.getInstance(), "下载成功", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Log.i("haha_tag","下载失败");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CofferApplication.Companion.getInstance(), "下载失败", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                return false;
            }
        }).submit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String formatSecondsTo00(int timeSeconds) {
        int second = timeSeconds % 60;
        int minuteTemp = timeSeconds / 60;
        if (minuteTemp > 0) {
            int minute = minuteTemp % 60;
            int hour = minuteTemp / 60;
            if (hour > 0) {
                return (hour >= 10 ? (hour + "") : ("0" + hour)) + ":" + (minute >= 10 ? (minute + "") : ("0" + minute)) + ":"
                        + (second >= 10 ? (second + "") : ("0" + second));
            } else {
                return (minute >= 10 ? (minute + "") : ("0" + minute)) + ":" + (second >= 10 ? (second + "") : ("0" + second));
            }
        } else {
            return "00:" + (second >= 10 ? (second + "") : ("0" + second));
        }
    }
}
