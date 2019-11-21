package com.firemanagement.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.firemanagement.AppClient;
import com.firemanagement.R;
import com.firemanagement.activities.Admin.ManagerActivity;
import com.firemanagement.activities.Customer.MainActivity;
import com.firemanagement.db.Admin;
import com.firemanagement.db.User;
import com.firemanagement.utils.DateUtils;
import com.firemanagement.utils.ErrorRec;
import com.firemanagement.utils.L;
import com.firemanagement.utils.RegexUtils;
import com.firemanagement.utils.SpUtils;
import com.firemanagement.utils.StaticClass;
import com.firemanagement.utils.T;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    private TextView mTvRegister;
    private EditText mEtNumber;
    private EditText mEtPassword;
    private TextView mTvForgetPassowrd;
    private CheckBox mCbRemrmberPassword;
    private CheckBox mCbAutoLogin;
    private Button mBtnLogin;
    private AlertDialog alertDialog;
    private Disposable disposable;
    private AlertDialog forgetPasswordDialog;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initLayout() {
        mTvRegister = (TextView) findViewById(R.id.tv_register);
        mEtNumber = (EditText) findViewById(R.id.et_number);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mTvForgetPassowrd = (TextView) findViewById(R.id.tv_forget_passowrd);
        mCbRemrmberPassword = (CheckBox) findViewById(R.id.cb_remrmber_password);
        mCbAutoLogin = (CheckBox) findViewById(R.id.cb_auto_login);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
    }

    @Override
    protected void init() {
        initUI();
        initListener();
    }

    /**
     * 初始化数据回显
     */
    private void initUI() {
        //用户名和密码回显
        Boolean isRememberPassword = (Boolean) SpUtils.get(AppClient.mContext, StaticClass.isRememberPassword, false);
        Boolean isAutoLogin = (Boolean) SpUtils.get(AppClient.mContext, StaticClass.isAutoLogin, false);
        mCbRemrmberPassword.setChecked(isRememberPassword);
        mCbAutoLogin.setChecked(isAutoLogin);
        if (isRememberPassword) {
            mEtPassword.setText((String) SpUtils.get(AppClient.mContext, StaticClass.PASSWORD, ""));
            mEtNumber.setText((String) SpUtils.get(AppClient.mContext, StaticClass.USERNAME, ""));
        }

        if (isAutoLogin) {
            login();
        }
    }

    private void initListener() {
        //用户登录
        mBtnLogin.setOnClickListener(v -> {
            login();
        });
        //用户注册
        mTvRegister.setOnClickListener(v -> {
            showRegisterDialog();
        });
        //忘记密码
        mTvForgetPassowrd.setOnClickListener(v -> {
            showForgetPasswordDialog();
        });
    }

    /**
     * 忘记密码功能
     */
    private void showForgetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        forgetPasswordDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_forgetpassword, null);
        EditText mEtNewPhoneNumber;
        EditText mEtNewVerificationCode;
        Button mBtnNewVerificationCode;
        EditText mEtNewPassword;
        Button mBtnNewDialogCancel;
        Button mBtnResetPwd;
        mEtNewPhoneNumber = (EditText) view.findViewById(R.id.et_new_phone_number);
        mEtNewVerificationCode = (EditText) view.findViewById(R.id.et_new_verification_code);
        mBtnNewVerificationCode = (Button) view.findViewById(R.id.btn_new_verification_code);
        mEtNewPassword = (EditText) view.findViewById(R.id.et_new_password);
        mBtnNewDialogCancel = (Button) view.findViewById(R.id.btn_new_dialog_cancel);
        mBtnResetPwd = (Button) view.findViewById(R.id.btn_reset_pwd);
        forgetPasswordDialog.setView(view);
        forgetPasswordDialog.setCanceledOnTouchOutside(false);
        forgetPasswordDialog.show();

        //发送验证码
        mBtnNewVerificationCode.setOnClickListener(v -> sendSmsCode(mEtNewPhoneNumber.getText().toString(), mBtnNewVerificationCode));
        mBtnNewDialogCancel.setOnClickListener(v -> forgetPasswordDialog.dismiss());
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
                    forgetPasswordDialog.dismiss();
                } else {
                    Log.i(TAG, "done: -------------" + e.getErrorCode() + "---" + e.getMessage());
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                }
            }
        });
    }


    /**
     * 显示注册的对话框
     */
    private void showRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.layout_register_dialog, null);
        builder.setView(view);
        EditText mEtPhoneNumber;
        EditText mEtVerificationCode;
        Button mBtnVerificationCode;
        EditText mEtPassword;
        Button mBtnDialogCancel;
        Button mBtnDialogRegister;
        mEtPhoneNumber = (EditText) view.findViewById(R.id.et_phone_number);
        mEtVerificationCode = (EditText) view.findViewById(R.id.et_verification_code);
        mBtnVerificationCode = (Button) view.findViewById(R.id.btn_verification_code);
        mEtPassword = (EditText) view.findViewById(R.id.et_password);
        mBtnDialogCancel = (Button) view.findViewById(R.id.btn_dialog_cancel);
        mBtnDialogRegister = (Button) view.findViewById(R.id.btn_dialog_register);
        EditText mEtUname;
        mEtUname = (EditText) view.findViewById(R.id.et_uname);

        //发送验证码
        mBtnVerificationCode.setOnClickListener(v -> sendSmsCode(mEtPhoneNumber.getText().toString(), mBtnVerificationCode));
        mBtnDialogCancel.setOnClickListener(v -> alertDialog.dismiss());
        //注册
        mBtnDialogRegister.setOnClickListener(v -> register(mEtUname.getText().toString().trim(),
                mEtVerificationCode.getText().toString().trim(),
                mEtPhoneNumber.getText().toString().trim(),
                mEtPassword.getText().toString().trim()));
        alertDialog = builder.create();
        alertDialog.show();

        Long sendTime = (Long) SpUtils.get(AppClient.mContext, StaticClass.DATE_DIFFERENCE, 0L);
        //发送验证码按钮状态回显
        Date date = new Date();
        Long dateDifference = DateUtils.getDateDifference(date, new Date(sendTime));
        if (dateDifference > 0 && dateDifference < 60) {
            sendCodeStatus(mBtnVerificationCode, 60 - Integer.valueOf(dateDifference.toString()));
        }
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

    /**
     * 注册的业务逻辑
     *
     * @param uname 用户名
     * @param code  验证码
     * @param phone 手机号
     * @param pwd   密码
     */
    private void register(String uname, String code, String phone, String pwd) {

        if (checkParams(uname, phone, pwd)) return;

        //进行手机号注册
        BmobUser.signOrLoginByMobilePhone(phone, code, new LogInListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    //保存用户
                    saveUser(bmobUser.getObjectId(), pwd, uname, phone);
                } else {
                    T.showShort("验证码验证失败：" + e.getErrorCode());
                }
            }
        });
    }

    private void saveUser(String objectId, String pwd, String uName, String phone) {
        User currentUser = BmobUser.getCurrentUser(User.class);
        currentUser.setPassword(pwd);
        currentUser.setUsername(uName);
        currentUser.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                try {
                    if (e == null) {
                        //添加权限信息
                        addPermissionInformation(objectId, uName, phone);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * 添加权限信息
     *
     * @param objectId
     */
    private void addPermissionInformation(String objectId, String uName, String phone) {
        Admin admin = new Admin();
        admin.setUserObjectId(objectId);
        admin.setUsername(uName);
        admin.setLoginUserName(uName);
        admin.setAccountStatus(1);
        admin.setAdmin(0);
        admin.setAge(18);
        admin.setPostStatus(1);
        admin.setPersonnelStatus("无");
        admin.setPhone(phone);
        admin.setPosition("无");
        admin.setSubordDetachment("无");
        admin.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //验证完成后,关闭弹窗
                    alertDialog.dismiss();
                    T.showShort("注册成功,请使用账号+密码登录");
                }
            }
        });
    }

    /**
     * 参数判断,是否符合要求
     *
     * @param uname
     * @param phone
     * @param pwd
     * @return
     */
    private boolean checkParams(String uname, String phone, String pwd) {
        if (TextUtils.isEmpty(uname)) {
            T.showShort("姓名不能为空");
            return true;
        }
        //验证手机号码
        if (!RegexUtils.checkMobile(phone)) {
            T.showShort("手机号码有误");
            return true;
        }

        if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
            T.showShort("密码长度最少为6位");
            return true;
        }
        return false;
    }

    /**
     * 用户登录
     */
    private void login() {
        String username = mEtNumber.getText().toString().trim();
        //验证手机号码
        if (TextUtils.isEmpty(username)) {
            T.showShort("请输入用户名密码");
            return;
        }

        String pwd = mEtPassword.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            T.showShort("请输入用户名密码");
            return;
        }


        //进行登录

        //查找admin表中的登录用户名
        BmobQuery<Admin> adminBmobQuery = new BmobQuery<>();
        adminBmobQuery.addWhereEqualTo("username", username);
        Log.i(TAG, "login: ----------------*************" + username);
        adminBmobQuery.findObjects(new FindListener<Admin>() {
            @Override
            public void done(List<Admin> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        BmobUser.loginByAccount(list.get(0).getLoginUserName(), pwd, new LogInListener<User>() {
                            @Override
                            public void done(User user, BmobException e) {
                                Log.i(TAG, "done: ---------" + username + "---" + pwd);
                                if (e == null) {
                                    //查询登录用的权限
                                    searchLoginUserCompetence(user.getObjectId(), username, pwd);
                                } else {
                                    //登录失败
                                    T.showShort("密码错误 请重新输入");
                                    L.e("--------------登录失败错误信息:" + e.getErrorCode() + "----" + e.getMessage());
                                }
                            }

                        });
                    }
                }
            }
        });


    }

    private void searchLoginUserCompetence(String objectId, String phoneStr, String pwd) {
        BmobQuery<Admin> query = new BmobQuery<>();
        query.addWhereEqualTo("UserObjectId", objectId);
        query.findObjects(new FindListener<Admin>() {
            @Override
            public void done(List<Admin> list, BmobException e) {
                Log.i(TAG, "done: --------------------------");
                try {
                    if (e == null) {
                        Log.i(TAG, "done: -------------------------");
                        Admin admin = list.get(0);
                        //判断账户是否注销
                        if (admin.getAccountStatus() == 1) {
                            //保存密码
                            savePassword(phoneStr, pwd);
                            //跳转到下个界面
                            gotoMainActivity(admin.getAdmin());
                        } else if (admin.getAccountStatus() == 0) {
                            T.showShort("当前用户不存在");
                        }
                    } else {
                        Log.i(TAG, "done: --------------" + e.getErrorCode() + "---" + e.getMessage());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }


    private void gotoMainActivity(int adamin) {
        switch (adamin) {
            case 0:
                //普通权限
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case 1:
                //管理员权限
                startActivity(new Intent(this, ManagerActivity.class));
                finish();
                break;
        }
    }

    /**
     * 保存密码的业务逻辑
     *
     * @param phoneStr 用户名
     * @param pwd      密码
     */
    private void savePassword(String phoneStr, String pwd) {
        boolean isAutoLogin = mCbAutoLogin.isChecked();
        boolean isRememberPassword = mCbRemrmberPassword.isChecked();
        SpUtils.put(AppClient.mContext, StaticClass.isAutoLogin, isAutoLogin);
        SpUtils.put(AppClient.mContext, StaticClass.isRememberPassword, isRememberPassword);
        if (isAutoLogin || isRememberPassword) {
            SpUtils.put(AppClient.mContext, StaticClass.USERNAME, phoneStr);
            SpUtils.put(AppClient.mContext, StaticClass.PASSWORD, pwd);
        } else {
            SpUtils.put(AppClient.mContext, StaticClass.USERNAME, "");
            SpUtils.put(AppClient.mContext, StaticClass.PASSWORD, "");
        }
    }

}
