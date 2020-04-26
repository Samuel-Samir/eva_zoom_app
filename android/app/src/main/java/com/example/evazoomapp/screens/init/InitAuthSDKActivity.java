package com.example.evazoomapp.screens.init;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.evazoomapp.R;
import com.example.evazoomapp.joinmeeting.UserLoginCallback;
import com.example.evazoomapp.screens.emailuserlogin.EmailUserLoginActivity;
import com.example.evazoomapp.screens.mymeeting.MyMeetingActivity;
import com.example.evazoomapp.screens.startjoinmeeting.LoginUserStartJoinMeetingActivity;
import com.example.evazoomapp.screens.startjoinmeeting.RawDataMeetingActivity;

import us.zoom.sdk.InMeetingNotificationHandle;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.ZoomApiError;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;


public class InitAuthSDKActivity extends Activity implements InitAuthSDKCallback,
        MeetingServiceListener, UserLoginCallback.ZoomDemoAuthenticationListener, View.OnClickListener {

    private final static String TAG = "ZoomSDKExample";

    private Button mBtnEmailLogin;
    private View layoutJoin;
    private View mProgressPanel;
    private EditText numberEdit;
    private EditText nameEdit;
    private ZoomSDK mZoomSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mZoomSDK = ZoomSDK.getInstance();
        if (mZoomSDK.isLoggedIn()) {
            finish();
            showEmailLoginUserStartJoinActivity();
            return;
        }

        setContentView(R.layout.activity_init_auth_s_d_k);

        mBtnEmailLogin = (Button) findViewById(R.id.btnEmailLogin);
        mBtnEmailLogin.setOnClickListener(this);

        mProgressPanel = (View) findViewById(R.id.progressPanel);

        layoutJoin = findViewById(R.id.layout_join);
        numberEdit = findViewById(R.id.edit_join_number);
        nameEdit = findViewById(R.id.edit_join_name);
        mProgressPanel.setVisibility(View.GONE);

        InitAuthSDKHelper.getInstance().initSDK(this, this);

        if (mZoomSDK.isInitialized()) {
            mBtnEmailLogin.setVisibility(View.VISIBLE);
            layoutJoin.setVisibility(View.VISIBLE);

            ZoomSDK.getInstance().getMeetingService().addListener(this);
            ZoomSDK.getInstance().getMeetingSettingsHelper().enable720p(true);
        } else {
            mBtnEmailLogin.setVisibility(View.GONE);
            layoutJoin.setVisibility(View.GONE);
        }
    }

    InMeetingNotificationHandle handle=new InMeetingNotificationHandle() {

        @Override
        public boolean handleReturnToConfNotify(Context context, Intent intent) {
            intent = new Intent(context, MyMeetingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            if(!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.setAction(InMeetingNotificationHandle.ACTION_RETURN_TO_CONF);
            context.startActivity(intent);
            return true;
        }
    };

    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);

        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG).show();
            Log.e("Samuel","Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode);
        } else {
            ZoomSDK.getInstance().getMeetingSettingsHelper().enable720p(false);
            ZoomSDK.getInstance().getMeetingSettingsHelper().enableShowMyMeetingElapseTime(true);
            ZoomSDK.getInstance().getMeetingService().addListener(this);
            ZoomSDK.getInstance().getMeetingSettingsHelper().setCustomizedNotificationData(null, handle);
            if (mZoomSDK.tryAutoLoginZoom() == ZoomApiError.ZOOM_API_ERROR_SUCCESS) {
                UserLoginCallback.getInstance().addListener(this);
                showProgressPanel(true);
            } else {
                showProgressPanel(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(!mZoomSDK.isInitialized())
        {
            Toast.makeText(this,"Init SDK First",Toast.LENGTH_SHORT).show();
            InitAuthSDKHelper.getInstance().initSDK(this, this);
            return;
        }
        if (v.getId() == R.id.btnEmailLogin) {
            showEmailLoginActivity();
        }
    }



    @Override
    public void onZoomSDKLoginResult(long result) {
        if ((int) result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
            showEmailLoginUserStartJoinActivity();
            finish();
        } else {
            showProgressPanel(false);
        }
    }

    @Override
    public void onZoomSDKLogoutResult(long result) {

    }

    @Override
    public void onZoomIdentityExpired() {
        if (mZoomSDK.isLoggedIn()) {
            mZoomSDK.logoutZoom();
        }
    }

    @Override
    public void onZoomAuthIdentityExpired() {
        Log.e(TAG,"onZoomAuthIdentityExpired");
    }

    // join meeting
    public void onClickJoin(View view) {
        if(!mZoomSDK.isInitialized())
        {
            Toast.makeText(this,"Init SDK First",Toast.LENGTH_SHORT).show();
            InitAuthSDKHelper.getInstance().initSDK(this, this);
            return;
        }

        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            ZoomSDK.getInstance().getSmsService().enableZoomAuthRealNameMeetingUIShown(false);
        } else {
            ZoomSDK.getInstance().getSmsService().enableZoomAuthRealNameMeetingUIShown(true);
        }
        String number = numberEdit.getText().toString();
        String name = nameEdit.getText().toString();

        JoinMeetingParams params = new JoinMeetingParams();
        params.meetingNo = number;
        params.displayName = name;
        ZoomSDK.getInstance().getMeetingService().joinMeetingWithParams(this, params);
    }

    private void showProgressPanel(boolean show) {
        if (show) {
            mBtnEmailLogin.setVisibility(View.GONE);
            mProgressPanel.setVisibility(View.VISIBLE);
            layoutJoin.setVisibility(View.GONE);

        } else {

            mBtnEmailLogin.setVisibility(View.VISIBLE);
            mProgressPanel.setVisibility(View.GONE);
            layoutJoin.setVisibility(View.VISIBLE);
        }
    }

    private void showEmailLoginActivity() {
        Intent intent = new Intent(this, EmailUserLoginActivity.class);
        startActivity(intent);
    }





    private void showEmailLoginUserStartJoinActivity() {
        Intent intent = new Intent(this, LoginUserStartJoinMeetingActivity.class);
        startActivity(intent);
    }


    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode, int internalErrorCode) {
        Log.d(TAG,"onMeetingStatusChanged "+meetingStatus);
        if(!ZoomSDK.getInstance().isInitialized())
        {
            showProgressPanel(false);
            return;
        }
        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING) {
                showMeetingUi();
            }
        }
    }

    private void showMeetingUi() {
        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            SharedPreferences sharedPreferences = getSharedPreferences("UI_Setting", Context.MODE_PRIVATE);
            boolean enable = sharedPreferences.getBoolean("enable_rawdata", false);
            Intent intent = null;
            if (!enable) {
                intent = new Intent(this, MyMeetingActivity.class);
                intent.putExtra("from",3);
            } else {
                intent = new Intent(this, RawDataMeetingActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserLoginCallback.getInstance().removeListener(this);

        if(null!= ZoomSDK.getInstance().getMeetingService())
        {
            ZoomSDK.getInstance().getMeetingService().removeListener(this);
        }
        InitAuthSDKHelper.getInstance().reset();
    }
}
