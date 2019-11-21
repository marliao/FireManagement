package com.firemanagement.activities.Admin;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.firemanagement.AppClient;
import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.utils.DateUtils;
import com.firemanagement.utils.ErrorRec;
import com.firemanagement.utils.L;
import com.firemanagement.utils.RegexUtils;
import com.firemanagement.utils.SpUtils;
import com.firemanagement.utils.StaticClass;
import com.firemanagement.utils.T;
import com.firemanagement.view.CustomTitleBar;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UpdatePasswordActivity extends BaseActivity {

    private CustomTitleBar mCtbUpdatePassword;
    private EditText mEtNewPhoneNumber;
    private EditText mEtNewVerificationCode;
    private Button mBtnNewVerificationCode;
    private EditText mEtNewPassword;
    private Button mBtnNewDialogCancel;
    private Button mBtnResetPwd;
    private Disposable disposable;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_update_password;
    }

    @Override
    protected void initLayout() {
        mCtbUpdatePassword = (CustomTitleBar) findViewById(R.id.ctb_update_password);
        mEtNewPhoneNumber = (EditText) findViewById(R.id.et_new_phone_number);
        mEtNewVerificationCode = (EditText) findViewById(R.id.et_new_verification_code);
        mBtnNewVerificationCode = (Button) findViewById(R.id.btn_new_verification_code);
        mEtNewPassword = (EditText) findViewById(R.id.et_new_password);
        mBtnNewDialogCancel = (Button) findViewById(R.id.btn_new_dialog_cancel);
        mBtnResetPwd = (Button) findViewById(R.id.btn_reset_pwd);
    }

    @Override
    protected void init() {
        mCtbUpdatePassword.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        //发送验证码
        mBtnNewVerificationCode.setOnClickListener(v -> sendSmsCode(mEtNewPhoneNumber.getText().toString(), mBtnNewVerificationCode));
        mBtnNewDialogCancel.setOnClickListener(v -> finish());
        //重置密码
        mBtnResetPwd.setOnClickListener(v -> resetPwd(mEtNewPhoneNumber.getText().toString().trim(),
                mEtNewVerificationCode.getText().toString().trim(),
                mEtNewPassword.getText().toString().trim()));

        Long sendTime = (Long) SpUtils.get(AppClient.mContext, StaticClass.DATE_DIFFERENCE, 0L);
        //发送验证码按钮状态回显
        Date date = new Date();
        Long dateDifference = DateUtils.getDateDifference(date, new Date(sendTime));
        if (dateDifference > 0 && dateDifference < 60) {
            sendCodeStatus(mBtnNewVerificationCode, 60 - Integer.valueOf(dateDifference.toString()));
        }
    }

    private void resetPwd(String phone, String code, String pwd) {
        if (!RegexUtils.checkMobile(phone)) {
            T.showShort("手机号码有误");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            T.showShort("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
            T.showShort("密码长度最少为6位");
            return;
        }
        BmobUser.resetPasswordBySMSCode(code, pwd, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    T.showShort("密码重置成功，请使用手机号+密码登录");
                } else {
                    Log.i(TAG, "done: -------------" + e.getErrorCode() + "---" + e.getMessage());
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                }
            }
        });
    }

    /**
     * 发送验证码的业务
     *
     * @param phone 手机号码
     */
    private void sendSmsCode(String phone, Button mBtnVerificationCode) {
        L.e("----phone-----------" + phone);
        //验证手机号码
        if (!RegexUtils.checkMobile(phone)) {
            T.showShort("手机号码有误");
            return;
        }

        //请求发送验证码
        BmobSMS.requestSMSCode(phone, null, new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    T.showShort("发送验证码成功，短信ID：" + smsId + "\n");
                    //按钮倒计时
                    sendCodeStatus(mBtnVerificationCode, 60);
                    //记录发送验证码的时间
                    SpUtils.put(AppClient.mContext, StaticClass.DATE_DIFFERENCE, new Date().getTime());
                } else {
                    //更详细的以后再说
                    // T.showShort("发送验证码失败请稍后重试");
                    T.showShort(e.getErrorCode() + "--" + e.getMessage());
                }
            }
        });

    }

    /**
     * 发送短信按钮倒计时
     *
     * @param mBtnVerificationCode
     * @param seconds
     */
    private void sendCodeStatus(Button mBtnVerificationCode, int seconds) {
        mBtnVerificationCode.setBackgroundResource(R.drawable.bg_gray_button);
        mBtnVerificationCode.setEnabled(false);
        disposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(seconds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mBtnVerificationCode.setText((seconds - aLong) + "秒后获取");
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mBtnVerificationCode.setText("获取验证码");
                        mBtnVerificationCode.setEnabled(true);
                        mBtnVerificationCode.setBackgroundResource(R.drawable.bg_red_button);
                    }
                }).subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅
        if (disposable != null) {
            if (disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

}
