package com.firemanagement.activities.Admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firemanagement.AppClient;
import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.db.Admin;
import com.firemanagement.db.User;
import com.firemanagement.utils.ErrorRec;
import com.firemanagement.utils.ImageUtils;
import com.firemanagement.utils.T;
import com.firemanagement.view.CustomTitleBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class PersonalCenterActivity extends BaseActivity {

    private CustomTitleBar mCtbPersonalCenter;
    private TextView mTvName;
    private ImageView mIvHeadPortrait;
    private EditText mEtUsername;
    private Spinner mSpinnerName;
    private EditText mEtAge;
    private EditText mEtPosition;
    private Spinner mSpinnerSubordinateDetachment;
    private EditText mEtPhone;
    private List<String> subordinateDetachmentlist;
    private AlertDialog alertDialog;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initLayout() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mCtbPersonalCenter = (CustomTitleBar) findViewById(R.id.ctb_personal_center);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mIvHeadPortrait = (ImageView) findViewById(R.id.iv_head_portrait);
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mSpinnerName = (Spinner) findViewById(R.id.spinner_name);
        mEtAge = (EditText) findViewById(R.id.et_age);
        mEtPosition = (EditText) findViewById(R.id.et_position);
        mSpinnerSubordinateDetachment = (Spinner) findViewById(R.id.spinner_subordinate_detachment);
    }

    @Override
    protected void init() {
        initUI();
        initListener();
    }

    private void initListener() {
        mCtbPersonalCenter.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                String rightTitle = mCtbPersonalCenter.getRightTitle();
                if (rightTitle.equals("修改")) {
                    mCtbPersonalCenter.setRightTitle("保存");
                    updateEtSpinnerStatus(true);
                } else if (rightTitle.equals("保存")) {
                    //获取输入框和下拉列表数据
                    getData();
                }
            }
        });
        mIvHeadPortrait.setOnClickListener(v -> {
            showAlertDialog();
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalCenterActivity.this);
        alertDialog = builder.create();
        View inflate = View.inflate(PersonalCenterActivity.this, R.layout.main_tag3_dialog, null);

        alertDialog.setView(inflate);
        alertDialog.show();
        TextView mCardView1;
        TextView mCardView2;
        TextView mCardView3;

        mCardView1 = (TextView) inflate.findViewById(R.id.cardView1);
        mCardView2 = (TextView) inflate.findViewById(R.id.cardView2);
        mCardView3 = (TextView) inflate.findViewById(R.id.cardView3);
        mCardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        mCardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(PersonalCenterActivity.this)
                        .openCamera(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                alertDialog.dismiss();
            }
        });
        mCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(PersonalCenterActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    final String[] filePaths = new String[selectList.size()];
                    for (int i = 0; i < selectList.size(); i++) {
                        filePaths[i] = selectList.get(i).getPath();
                    }
                    BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                        @Override
                        public void onSuccess(List<BmobFile> files, List<String> urls) {
                            //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                            //2、urls-上传文件的完整url地址
                            if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                                //do something
                                User currentUser = BmobUser.getCurrentUser(User.class);
                                BmobQuery<Admin> query = new BmobQuery<>();
                                query.addWhereEqualTo("UserObjectId", currentUser.getObjectId());
                                query.findObjects(new FindListener<Admin>() {
                                    @Override
                                    public void done(List<Admin> list, BmobException e) {
                                        if (e == null) {
                                            Admin admin = new Admin();
                                            admin.setAvatar(files.get(0));
                                            admin.update(list.get(0).getObjectId(), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        T.showShort("修改成功");
                                                        ImageUtils.setCircleBitmap(admin.getAvatar().getUrl(), mIvHeadPortrait);
                                                    } else {
                                                        ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                                                        Log.i(TAG, "done: -------------" + e.getErrorCode() + "  " + e.getMessage());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                            }
                        }

                        @Override
                        public void onError(int statuscode, String errormsg) {
                            T.showShort("错误码" + statuscode + ",错误描述：" + errormsg);
                        }

                        @Override
                        public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                        }
                    });
                    break;
            }
        }
    }


    private void getData() {
        String username = mEtUsername.getText().toString().trim();
        String phone = mEtPhone.getText().toString().trim();
        String age = mEtAge.getText().toString().trim();
        String position = mEtPosition.getText().toString().trim();
        String sex = (String) mSpinnerName.getSelectedItem();
        String subordinateDetachment = (String) mSpinnerSubordinateDetachment.getSelectedItem();


        //判断是否空
        if (TextUtils.isEmpty(username)) {
            T.showShort("用户名不能问为空");
            return;
        }//判断是否空
        if (TextUtils.isEmpty(phone)) {
            T.showShort("手机号不能问为空");
            return;
        }
        if (age == null) {
            age = "0";
        }
        updateUser(username, phone, age, position, sex, subordinateDetachment);
    }

    private void updateUser(String username, String phone, String age, String position, String sex, String subordinateDetachment) {
        User currentUser = BmobUser.getCurrentUser(User.class);
        User user = new User();
        if (BmobUser.getCurrentUser(User.class).getUsername().equals(username)) {
        } else {
            user.setUsername(username);
        }
        user.setMobilePhoneNumber(phone);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //继续修改admin表中的参数
                    updateDataAdmin(age, position, sex, subordinateDetachment, username, phone);
                } else {
                    Log.i(TAG, "done: ----------" + e.getErrorCode() + "   " + e.getMessage());
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                }
            }
        });
    }

    private void updateDataAdmin(String age, String position, String sex, String subordinateDetachment, String username, String phone) {
        BmobQuery<Admin> query = new BmobQuery<>();
        query.addWhereEqualTo("UserObjectId", BmobUser.getCurrentUser(User.class).getObjectId());
        query.findObjects(new FindListener<Admin>() {
            @Override
            public void done(List<Admin> list, BmobException e) {
                if (e == null) {
                    String objectId = list.get(0).getObjectId();
                    Admin admin = new Admin();
                    admin.setPhone(phone);
                    admin.setUsername(username);
                    admin.setLoginUserName(username);
                    admin.setAge(Integer.parseInt(age));
                    admin.setAccountStatus(1);
                    admin.setPosition(position);
                    admin.setSubordDetachment(subordinateDetachment);
                    admin.setOperate(list.get(0).getOperate());
                    if (sex.equals("男")) {
                        admin.setSex(1);
                    } else if (sex.equals("女")) {
                        admin.setSex(0);
                    } else {
                        admin.setSex(2);
                    }
                    admin.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                mCtbPersonalCenter.setRightTitle("修改");
                                updateEtSpinnerStatus(false);
                                mTvName.setText(username);
                                T.showShort("修改成功");
                            } else {
                                Log.i(TAG, "done: ----------" + e.getErrorCode() + "   " + e.getMessage());
                                ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });
    }

    private void initUI() {
        subordinateDetachmentlist = new ArrayList<>();
        subordinateDetachmentlist.add("昌吉地区公安消防支队");
        subordinateDetachmentlist.add("昌吉消防支队奇台县大队");
        subordinateDetachmentlist.add("昌吉消防支队阜康市大队");
        subordinateDetachmentlist.add("昌吉消防支队淮东油田大队");
        subordinateDetachmentlist.add("--请选择--");
        User currentUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<Admin> query = new BmobQuery<>();
        query.addWhereEqualTo("UserObjectId", currentUser.getObjectId());
        query.findObjects(new FindListener<Admin>() {
            @Override
            public void done(List<Admin> list, BmobException e) {
                Admin admin = list.get(0);
                mTvName.setText(currentUser.getUsername());
                mEtUsername.setText(currentUser.getUsername());
                mEtAge.setText(admin.getAge() + "");
                mEtPhone.setText(currentUser.getMobilePhoneNumber());
                mEtPosition.setText(admin.getPosition());
                if (admin.getAvatar() != null) {
                    ImageUtils.setCircleBitmap(admin.getAvatar().getUrl(), mIvHeadPortrait);
                }
                switch (admin.getSex()) {
                    case 0:
                        mSpinnerName.setSelection(1);
                        break;
                    case 1:
                        mSpinnerName.setSelection(0);
                        break;
                    default:
                        mSpinnerName.setSelection(2);
                        break;
                }
                for (int i = 0; i < subordinateDetachmentlist.size(); i++) {
                    if (subordinateDetachmentlist.get(i).equals(admin.getSubordDetachment())) {
                        mSpinnerSubordinateDetachment.setSelection(i);
                        break;
                    } else {
                        mSpinnerSubordinateDetachment.setSelection(4);
                    }
                }
                mCtbPersonalCenter.setRightTitle("修改");
                updateEtSpinnerStatus(false);
            }
        });
    }

    /**
     * 修改输入框和下拉列表是否可点击
     *
     * @param status
     */
    private void updateEtSpinnerStatus(boolean status) {
        mEtUsername.setEnabled(status);
        mEtPhone.setEnabled(status);
        mEtAge.setEnabled(status);
        mEtPosition.setEnabled(status);
        mSpinnerName.setEnabled(status);
        mSpinnerSubordinateDetachment.setEnabled(status);
    }
}
