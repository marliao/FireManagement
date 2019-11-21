package com.firemanagement.frags.Customer;


import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firemanagement.AppClient;
import com.firemanagement.R;
import com.firemanagement.db.Expert;
import com.firemanagement.frags.BaseFragment;
import com.firemanagement.utils.ErrorRec;
import com.firemanagement.utils.T;
import com.firemanagement.view.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpertInquiryFragment extends BaseFragment {

    private EditText mEtSearch;
    private ImageView mIvSearch;
    private RecyclerView mRvExpertInquiry;
    private TextView mTvExpertInquiry;


    @Override
    protected int getLayout() {
        return R.layout.fragment_expert_inquiry;
    }

    @Override
    protected void initView(View view) {
        mEtSearch = (EditText) view.findViewById(R.id.et_search);
        mIvSearch = (ImageView) view.findViewById(R.id.iv_search);
        mRvExpertInquiry = (RecyclerView) view.findViewById(R.id.rv_expert_inquiry);
        mTvExpertInquiry = (TextView) view.findViewById(R.id.tv_expert_inquiry);
    }

    @Override
    protected void init() {
        initlistener();
        getAllExpert();
    }

    private void initlistener() {
        mIvSearch.setOnClickListener(v -> {
            String trim = mEtSearch.getText().toString().trim();
            if (TextUtils.isEmpty(trim)) {
                T.showShort("请输入要查询的内容");
                return;
            }
            searchData(trim);
        });
    }

    private void searchData(String trim) {
        BmobQuery<Expert> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<Expert>() {
            @Override
            public void done(List<Expert> list, BmobException e) {
                if (e == null) {
                    List<Expert> expertList=new ArrayList<>();
                    try {
                        for (Expert expert : list) {
                            //查询所有数据利用string的contains函数模糊判断，实现模糊查询
                            if (expert.getExpertField()!=null||
                                    expert.getName()!=null||
                                    expert.getPhone()!=null||
                                    expert.getPosition()!=null||
                                    expert.getPost()!=null||
                                    expert.getSubordDetachment()!=null){
                                if (expert.getExpertField().contains(trim)||
                                        expert.getName().contains(trim)||
                                        expert.getPhone().contains(trim)||
                                        expert.getPosition().contains(trim)||
                                        expert.getPost().contains(trim)||
                                        expert.getSubordDetachment().contains(trim)){
                                    expertList.add(expert);
                                }
                            }

                        }
                    } catch (Exception e1) {
                        expertList.clear();
                        e1.printStackTrace();
                    }
                    if (expertList.size() == 0) {
                        mRvExpertInquiry.setVisibility(View.GONE);
                        mTvExpertInquiry.setVisibility(View.VISIBLE);
                    } else {
                        mTvExpertInquiry.setVisibility(View.GONE);
                        mRvExpertInquiry.setVisibility(View.VISIBLE);
                    }
                    initData(expertList);
                } else {
                    Log.i(TAG, "done: -------------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(getActivity(), e.getErrorCode());
                }
            }
        });
    }

    private void getAllExpert() {
        BmobQuery<Expert> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<Expert>() {
            @Override
            public void done(List<Expert> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        mRvExpertInquiry.setVisibility(View.GONE);
                        mTvExpertInquiry.setVisibility(View.VISIBLE);
                    } else {
                        mTvExpertInquiry.setVisibility(View.GONE);
                        mRvExpertInquiry.setVisibility(View.VISIBLE);
                    }
                    initData(list);
                } else {
                    Log.i(TAG, "done: -------------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(getActivity(), e.getErrorCode());
                }
            }
        });
    }

    private void initData(List<Expert> expertList) {
        mRvExpertInquiry.setHasFixedSize(true);
        mRvExpertInquiry.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        mRvExpertInquiry.setAdapter(myAdapter);
        myAdapter.add(expertList);
    }

    public class MyAdapter extends RecyclerAdapter<Expert> {
        @Override
        protected int getItemViewType(int position, Expert expert) {
            return R.layout.item_expert_show;
        }

        @Override
        protected ViewHolder<Expert> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<Expert> {
            public TextView mTvPosterStatus;
            public TextView mTvUserName;
            public TextView mTvSex;
            public TextView mTvDuty;
            public TextView mTvPost;
            public TextView mTvPhone;
            public TextView mTvSubordinateDetachment;
            public CardView mCdExpert;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.mTvPosterStatus = (TextView) itemView.findViewById(R.id.tv_poster_status);
                this.mTvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
                this.mTvSex = (TextView) itemView.findViewById(R.id.tv_sex);
                this.mTvDuty = (TextView) itemView.findViewById(R.id.tv_duty);
                this.mTvPost = (TextView) itemView.findViewById(R.id.tv_post);
                this.mTvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
                this.mTvSubordinateDetachment = (TextView) itemView.findViewById(R.id.tv_subordinate_detachment);
                this.mCdExpert = (CardView) itemView.findViewById(R.id.cd_expert);
            }

            @Override
            protected void onBind(Expert expert, int position) {
                mTvUserName.setText(expert.getName());
                mTvPhone.setText(expert.getPhone());
                if (expert.getSex() == 1) {
                    mTvSex.setText("男");
                } else if (expert.getSex() == 0) {
                    mTvSex.setText("女");
                } else {
                    mTvSex.setText("性别不明");
                }
                mTvDuty.setText(expert.getPosition());
                mTvPost.setText(expert.getPost());
                mTvSubordinateDetachment.setText(expert.getSubordDetachment());
                if (expert.getPostStatus() == 1) {
                    mTvPosterStatus.setText("在位（在岗）");
                } else {
                    if (expert.getPersonnelStatus().equals("无")) {
                        mTvPosterStatus.setText("不在位");
                    } else {
                        mTvPosterStatus.setText(expert.getPersonnelStatus());
                    }
                }

            }
        }

    }

}
