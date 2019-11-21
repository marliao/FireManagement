package com.firemanagement.activities.Admin;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firemanagement.AppClient;
import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.bean.UpdateAdmin;
import com.firemanagement.db.Admin;
import com.firemanagement.db.User;
import com.firemanagement.utils.ErrorRec;
import com.firemanagement.utils.T;
import com.firemanagement.view.CustomTitleBar;
import com.firemanagement.view.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ModifyAdminRightsActivity extends BaseActivity {

    private CustomTitleBar mCtbUpdateAdmin;
    private RecyclerView mRvUpdateAdmin;
    private List<UpdateAdmin> updateAdminList;
    private MyAdapter myAdapter;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_modify_admin_rights;
    }

    @Override
    protected void initLayout() {
        mCtbUpdateAdmin = (CustomTitleBar) findViewById(R.id.ctb_update_admin);
        mRvUpdateAdmin = (RecyclerView) findViewById(R.id.rv_update_admin);

    }

    @Override
    protected void init() {
        initlistener();
        getAllUser();
    }

    private void initlistener() {
        mCtbUpdateAdmin.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    /**
     * 获取所有用户
     */
    private void getAllUser() {
        updateAdminList = new ArrayList<>();
        BmobQuery<User> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                try {
                    if (e == null) {
                        //移除当前用户
                        for (int i = 0; i < list.size(); i++) {
                            String objectId = list.get(i).getObjectId();
                            if (BmobUser.getCurrentUser(User.class).getObjectId().equals(objectId)) {
                                list.remove(i);
                            }
                        }
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getUsername().equals("admin")) {
                                list.remove(i);
                            }
                        }
                        //获取所有用户权限列表
                        getAllUserPermissionList(list);
                    } else {
                        ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                        Log.i(TAG, "done: ----------------------" + e.getErrorCode() + "   " + e.getMessage());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void getAllUserPermissionList(List<User> userList) {
        BmobQuery<Admin> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<Admin>() {
            @Override
            public void done(List<Admin> list, BmobException e) {
                try {
                    if (e == null) {
                        for (User user : userList) {
                            for (Admin admin : list) {
                                //判断用户账户是否被注销
                                if (admin.getAccountStatus() == 1) {
                                    //对比User表和Admin表数据，主键相同即是同一个用户
                                    if (user.getObjectId().equals(admin.getUserObjectId())) {
                                        UpdateAdmin updateAdmin = new UpdateAdmin();
                                        updateAdmin.setName(admin.getUsername());
                                        updateAdmin.setAdminObjectId(admin.getObjectId());
                                        updateAdmin.setAdminStatus(admin.getAdmin());
                                        updateAdmin.setOperate(admin.getOperate());
                                        updateAdminList.add(updateAdmin);
                                    }
                                }
                            }
                        }
                        //展示数据
                        initAdapter();
                    } else {
                        ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                        Log.i(TAG, "done: ----------------------" + e.getErrorCode() + "   " + e.getMessage());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void initAdapter() {
        mRvUpdateAdmin.setHasFixedSize(true);
        mRvUpdateAdmin.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myAdapter = new MyAdapter();
        mRvUpdateAdmin.setAdapter(myAdapter);
        myAdapter.add(updateAdminList);
    }

    public class MyAdapter extends RecyclerAdapter<UpdateAdmin> {

        @Override
        protected int getItemViewType(int position, UpdateAdmin updateAdmin) {
            return R.layout.item_update_admin_mananger;
        }

        @Override
        protected ViewHolder<UpdateAdmin> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<UpdateAdmin> {
            public TextView tv_name;
            public Button btn_admin_operating;
            public Button btn_allow_operate;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                this.btn_admin_operating = (Button) itemView.findViewById(R.id.btn_admin_operating);
                this.btn_allow_operate = (Button) itemView.findViewById(R.id.btn_allow_operte);
            }

            @Override
            protected void onBind(UpdateAdmin updateAdmin, int position) {
                tv_name.setText(updateAdmin.getName());
                switch (updateAdmin.getAdminStatus()) {
                    case 0:
                        btn_admin_operating.setText("添加管理员");
                        btn_admin_operating.setBackgroundResource(R.drawable.bg_red_button);
                        break;
                    case 1:
                        btn_admin_operating.setText("移除管理员");
                        btn_admin_operating.setBackgroundResource(R.drawable.bg_gray_button);
                        break;
                }
                switch (updateAdmin.getOperate()) {
                    case 0:
                        btn_allow_operate.setText("允许操作");
                        btn_allow_operate.setBackgroundResource(R.drawable.bg_red_button);
                        break;
                    case 1:
                        btn_allow_operate.setText("拒绝操作");
                        btn_allow_operate.setBackgroundResource(R.drawable.bg_gray_button);
                        break;
                }
                //赋予用户操作权限
                btn_allow_operate.setOnClickListener(v -> {
                    switch (updateAdmin.getOperate()) {
                        case 0://允许用户操作
                            updateOperateStatus(updateAdmin.getAdminObjectId(), 1, position);
                            break;
                        case 1://拒绝用户操作
                            updateOperateStatus(updateAdmin.getAdminObjectId(), 0, position);
                            break;
                    }
                });
                //赋予用户管理员权限
                btn_admin_operating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (updateAdmin.getAdminStatus()) {
                            case 0://添加管理员
                                updateAdminStatus(updateAdmin.getAdminObjectId(), 1, position);
                                break;
                            case 1://移除管理员
                                updateAdminStatus(updateAdmin.getAdminObjectId(), 0, position);
                                break;
                        }
                    }
                });
            }
        }
    }

    private void updateOperateStatus(String adminObjectId, int operate, int position) {
        Admin admin = new Admin();
        admin.setOperate(operate);
        admin.setAccountStatus(1);
        admin.update(adminObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    UpdateAdmin updateAdmin = updateAdminList.get(position);
                    updateAdmin.setOperate(operate);
                    myAdapter.notifyItemChanged(position);
                    T.showShort("设置成功！");
                } else {
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                    Log.i(TAG, "done: ----------------------" + e.getErrorCode() + "   " + e.getMessage());
                }
            }
        });
    }

    private void updateAdminStatus(String adminObjectId, int adminStatus, int position) {
        Admin admin = new Admin();
        admin.setAdmin(adminStatus);
        admin.setAccountStatus(1);
        admin.update(adminObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    UpdateAdmin updateAdmin = updateAdminList.get(position);
                    updateAdmin.setAdminStatus(adminStatus);
                    myAdapter.notifyItemChanged(position);
                    T.showShort("设置成功！");
                } else {
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                    Log.i(TAG, "done: ----------------------" + e.getErrorCode() + "   " + e.getMessage());
                }
            }
        });
    }

}
