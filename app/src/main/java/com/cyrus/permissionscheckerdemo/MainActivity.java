package com.cyrus.permissionscheckerdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final String WRITE_EXTERNAL_STORAGE_PERMISSION
            = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int REQUEST_CODE = 0;

    private EditText mEtFileName;
    private EditText mEtFileContent;
    private Button mBtnWrite;
    private Button mBtnRead;

    private PermissionsChecker mPermissionsChecker;
    private FileManager mFileManager;
    private String mFileName;
    private String mFileContent;
    private boolean mHasExternalStoragePermissionGot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUtils();
        initView();
        initListener();
    }

    private void initView() {
        mEtFileName = (EditText) findViewById(R.id.et_file_name);
        mEtFileContent = (EditText) findViewById(R.id.et_file_content);
        mBtnWrite = (Button) findViewById(R.id.btn_write);
        mBtnRead = (Button) findViewById(R.id.btn_read);
    }

    private void initListener() {
        mBtnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToSdCard();
            }
        });

        mBtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFromSdCard();
            }
        });
    }

    private void initUtils() {
        mPermissionsChecker = new PermissionsChecker(this);
        mFileManager = new FileManager(this);
    }

    private void writeToSdCard() {
        checkExternalStoragePermission();
        if (mHasExternalStoragePermissionGot) {
            mFileName = mEtFileName.getText().toString();
            mFileContent = mEtFileContent.getText().toString();
            mFileManager.writeToExternal(mFileName, mFileContent);
        }
    }

    private void readFromSdCard() {
        checkExternalStoragePermission();
        if (mHasExternalStoragePermissionGot) {
            mFileName = mEtFileName.getText().toString();
            mFileContent = mFileManager.readFromExternal(mFileName);
            mEtFileContent.setText(mFileContent);
        }
    }

    private void checkExternalStoragePermission() {
        if (mPermissionsChecker.lacksPermissions(WRITE_EXTERNAL_STORAGE_PERMISSION)) {
            Intent intent = new Intent(this, PermissionsActivity.class);
            intent.putExtra(PermissionsActivity.EXTRA_EXTERNAL,
                    new String[]{WRITE_EXTERNAL_STORAGE_PERMISSION});
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mHasExternalStoragePermissionGot = true;
            } else if (resultCode == RESULT_CANCELED) {
                mHasExternalStoragePermissionGot = false;
            }
        }
    }

}
