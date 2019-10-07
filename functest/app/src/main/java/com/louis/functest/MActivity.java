package com.louis.functest;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MActivity extends AppCompatActivity {


    Button bt_front,bt_back,bt_auth,bt_stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);
        bt_auth=(Button)findViewById(R.id.bt_auth);
        bt_front=(Button)findViewById(R.id.bt_front);
        bt_back=(Button)findViewById(R.id.bt_back);
        bt_stop=(Button)findViewById(R.id.bt_stop);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //call();
                }
                else{
                    Toast.makeText(this, "权限获得失败", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }

    }
}
