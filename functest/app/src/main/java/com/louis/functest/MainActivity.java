package com.louis.functest;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback{
    Button frontcamera;
    Button backcamera;
    Button stop;
    int selectedCamara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedCamara=-1;
        PermissionUtils.requestMultiPermissions(this, mPermissionGrant);
        frontcamera=(Button)findViewById(R.id.button);
        backcamera=(Button)findViewById(R.id.button2);
        stop=(Button)findViewById(R.id.button3);
        frontcamera.setOnClickListener(this);
        backcamera.setOnClickListener(this);
        stop.setOnClickListener(this);
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (!Settings.canDrawOverlays(this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivityForResult(intent, 1);
//            } else {
//                //TODO do something you need
//            }
//        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button:{

                switch(selectedCamara){
                    //没有点击多余的操作
                    case -1:{

                        //选择了前摄像头
                        selectedCamara=0;

                        Toast.makeText(this, "网络访问失败，错误码：front_error", Toast.LENGTH_SHORT).show();
                        Intent startIntent=new Intent(this,FrontVideoRecorder.class);
                        startService(startIntent);
                    }break;
                    case 0:{
                        Toast.makeText(this, "多次失败，错误码：muti_front_error", Toast.LENGTH_SHORT).show();
                    }break;
                    case 1:{
                        Toast.makeText(this, "暂不支持双卡双待", Toast.LENGTH_SHORT).show();
                    }break;
                    default:break;
                }


            }
                break;
            case R.id.button2: {

                switch(selectedCamara){
                    //没有点击多余的操作
                    case -1:{

                        //选择了后摄像头
                        selectedCamara=1;

                        Toast.makeText(this, "请检查网络连接是否正常，错误码：back_error", Toast.LENGTH_SHORT).show();
                        Intent startIntent = new Intent(this, BackgroundVideoRecorder.class);
                        startService(startIntent);
                    }break;
                    case 0:{
                        Toast.makeText(this, "暂不支持双卡双待", Toast.LENGTH_SHORT).show();
                    }break;
                    case 1:{
                        Toast.makeText(this, "多次失败，错误码：back_error", Toast.LENGTH_SHORT).show();
                    }break;
                    default:break;
                }

            }
                break;
            case R.id.button3: {
                //停止录像
                //震动反馈

                switch(selectedCamara){
                    case 0:{
                        v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS);
                        //停止前摄像头
                        Intent stopIntent = new Intent(this, FrontVideoRecorder.class);
                        stopService(stopIntent);
                        selectedCamara=-1;
                        Toast.makeText(this, "网络访问成功，返回前一个界面", Toast.LENGTH_SHORT).show();
                    }break;
                    case 1:{
                        v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS);
                        //停止后摄像头
                        Intent stopIntent = new Intent(this, BackgroundVideoRecorder.class);
                        stopService(stopIntent);
                        selectedCamara=-1;
                        Toast.makeText(this, "检查网络连接成功，即将跳转下一个界面", Toast.LENGTH_SHORT).show();
                    }break;
                    case -1:{
                        //没有任何打开的摄像头
                        Toast.makeText(this, "按住说话", Toast.LENGTH_SHORT).show();
                    }break;
                    default:break;
                }


            }
                break;
            default:break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
//                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
//                case PermissionUtils.CODE_GET_ACCOUNTS:
//                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
//                    break;
//                case PermissionUtils.CODE_READ_PHONE_STATE:
//                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
//                    break;
//                case PermissionUtils.CODE_CALL_PHONE:
////                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
//                    break;
                case PermissionUtils.CODE_CAMERA:
//                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;
//                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
////                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
//                    break;
//                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
////                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
//                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
//                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
//                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_SYSTEM_ALERT_WINDOW:
//                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }




}
