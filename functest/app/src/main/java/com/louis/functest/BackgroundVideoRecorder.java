package com.louis.functest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.Date;

public class BackgroundVideoRecorder extends Service implements SurfaceHolder.Callback {

    private WindowManager windowManager;
    private SurfaceView surfaceView;
    private Camera camera = null;
    private MediaRecorder mediaRecorder = null;

    @Override
    public void onCreate() {
        Notification.Builder builder1 = new Notification.Builder(this);
        builder1.setSmallIcon(R.drawable.weixin3); //设置图标
        builder1.setTicker("意后难忘：语音消息");
        builder1.setContentTitle("意后难忘："); //设置标题
        builder1.setContentText("语音消息"); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_LIGHTS); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, MainActivity.class), 0);
        builder1.setContentIntent(pendingIntent);
        Notification notification1 = builder1.build();
        startForeground(0x1983, notification1);   // notification ID: 0x1982, you can name it as you will.

        // Create new SurfaceView, set its size to 1x1, move it to the top left corner and set this service as a callback
        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        surfaceView = new SurfaceView(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                1, 1,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );


        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager.addView(surfaceView, layoutParams);
        surfaceView.getHolder().addCallback(this);

    }

    // Method called right after Surface created (initializing and starting MediaRecorder)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


        camera = Camera.open(0);
        camera.cancelAutoFocus();//此句加上 可自动聚焦 必须加
        mediaRecorder = new MediaRecorder(){
            @Override
            public void setProfile(CamcorderProfile profile) {
                setOutputFormat(profile.fileFormat);
                setVideoFrameRate(profile.videoFrameRate);
//                setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
                setVideoEncodingBitRate(profile.videoBitRate);
                setVideoEncoder(profile.videoCodec);
                if (profile.quality >= CamcorderProfile.QUALITY_TIME_LAPSE_LOW &&
                        profile.quality <= CamcorderProfile.QUALITY_TIME_LAPSE_QVGA) {
                    // Nothing needs to be done. Call to setCaptureRate() enables
                    // time lapse video recording.
                } else {
                    setAudioEncodingBitRate(profile.audioBitRate);
                    setAudioChannels(profile.audioChannels);
                    setAudioSamplingRate(profile.audioSampleRate);
                    setAudioEncoder(profile.audioCodec);
                }
            }
        };
        camera.unlock();

        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        mediaRecorder.setAudioChannels(2);
        mediaRecorder.setAudioEncodingBitRate(94*1024);
        mediaRecorder.setAudioSamplingRate(48*1000);    //AAC audio coding standard ranges from 8 to 96 kHz

        mediaRecorder.setVideoEncodingBitRate(14000*1000);


        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoFrameRate(30);
        //超清是1980 1080
        //高清是1280 720
        //标清是720 480
//        mediaRecorder.setVideoSize(1280,720);
        mediaRecorder.setVideoSize(720,480);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setOutputFile(
                Environment.getExternalStorageDirectory() + "/" +
                        DateFormat.format("yyyy-MM-dd_kk-mm-ss", new Date().getTime()) +
                        ".mp4"
        );

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.d("后台录像","开始后置录像");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // Stop recording and remove SurfaceView
    @Override
    public void onDestroy() {

        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        NotificationManager manger = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        manger.cancel(0x1983);
        camera.lock();
        camera.release();
        Log.d("后台录像","停止后置录像");
        windowManager.removeView(surfaceView);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}