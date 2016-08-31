package com.cyrus.permissionscheckerdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class PermissionsActivity extends AppCompatActivity {

    public static final String EXTRA_EXTERNAL
            = "com.cyrus.permissionscheckerdemo.extra_external";
    private static final int PERMISSIONS_REQUEST_CODE = 0;

    private TextView mTextView;

    private PermissionsChecker mPermissionsChecker;
    private boolean mIsRequireCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_EXTERNAL)) {
            throw new RuntimeException("PermissionsActivity需要EXTRA_EXTERNAL启动");
        }
        setContentView(R.layout.activity_permissions);

        mTextView = (TextView) findViewById(R.id.tv_permissions_info);
        mTextView.setText("要读写SD卡，请先赋予应用权限");

        mPermissionsChecker = new PermissionsChecker(this);
        mIsRequireCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsRequireCheck) {
            String permissions[] = getIntent().getStringArrayExtra(EXTRA_EXTERNAL);
            if (mPermissionsChecker.lacksPermissions(permissions)) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
            } else {
                allPermissionsGranted();
            }
        } else {
            mIsRequireCheck = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            allPermissionsGranted();
        } else {
            mIsRequireCheck = false;
            showWarmingDialog();
        }
    }

    private void allPermissionsGranted() {
        setResult(RESULT_OK);
        finish();
    }

    private void showWarmingDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("操作需要权限，请点击“设置”-“权限”-打开所需权限。")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void openSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private boolean hasAllPermissionsGranted(int grantResults[]) {
        for (int grantResult : grantResults) {
            if (grantResult == PermissionChecker.PERMISSION_DENIED) {
                return false;
            }
        }

        return true;
    }


}
