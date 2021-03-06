package com.huirong.ui.appsfrg.childmodel.mission;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huirong.R;
import com.huirong.base.BaseActivity;
import com.huirong.common.MyException;
import com.huirong.dialog.DateChooseWheelViewDialog;
import com.huirong.dialog.Loading;
import com.huirong.helper.UserHelper;
import com.huirong.inject.ViewInject;
import com.huirong.model.ContactsEmployeeModel;
import com.huirong.utils.LogUtils;
import com.huirong.utils.PageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 添加 任务
 */

public class AddMissionActivity extends BaseActivity {

    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;


    //标题
    @ViewInject(id = R.id.et_missionTitle)
    EditText et_missionTitle;

    //内容
    @ViewInject(id = R.id.et_missionContent)
    EditText et_missionContent;

    //任务类型
    @ViewInject(id = R.id.layout_type, click = "missionType")
    LinearLayout layout_type;
    @ViewInject(id = R.id.tv_type)
    TextView tv_type;

    //地点
    @ViewInject(id = R.id.et_place)
    EditText et_place;

    //保修人
    @ViewInject(id = R.id.et_repairman)
    EditText et_repairman;

    //联系方式
    @ViewInject(id = R.id.et_repairWay)
    EditText et_repairWay;

    //备注
    @ViewInject(id = R.id.et_remark)
    EditText et_remark;

    //维修维护人
    @ViewInject(id = R.id.layout_maintanMan, click = "forAddApprover")
    LinearLayout layout_maintanMan;
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;

    //完成时间
    @ViewInject(id = R.id.layout_completeTime, click = "startTimeBefore")
    LinearLayout layout_completeTime;
    @ViewInject(id = R.id.tv_completeTime)
    TextView tv_completeTime;


    //变量
    private String planTitle;
    private String planAdvance;
    private String planCompleteTime;
    private String planContent;
    private String type;
    private String place;
    private String repairman;
    private String repairWay;
    private String remark;
    private String approvalID = "";


    //常量
    public static final int POST_SUCCESS = 15;
    public static final int POST_FAILED = 16;
    public static final String TAG = "工作计划";//图片展示


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_misssion_add);

        initMyView();
    }

    private void initMyView() {
        tv_title.setText(getResources().getString(R.string.msn_title_add));
    }

    public void forCommit(View view) {

        planTitle = et_missionTitle.getText().toString();
        remark = et_remark.getText().toString();
        planContent = et_missionContent.getText().toString();
        place = et_place.getText().toString();
        repairman = et_repairman.getText().toString().trim();
        repairWay = et_repairWay.getText().toString().trim();

        if (TextUtils.isEmpty(planTitle)) {
            PageUtil.DisplayToast("标题不能为空");
            return;
        }

        if (TextUtils.isEmpty(planContent)) {
            PageUtil.DisplayToast("内容不能为空");
            return;
        }
        if (TextUtils.isEmpty(type)) {
            PageUtil.DisplayToast("任务类型不能为空");
            return;
        }

        if (TextUtils.isEmpty(planCompleteTime)) {
            PageUtil.DisplayToast("时间不能为空");
            return;
        }


        if (TextUtils.isEmpty(approvalID)) {
            PageUtil.DisplayToast("审批人不能为空");
            return;
        }

        Loading.run(AddMissionActivity.this, new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject js = new JSONObject();
                    js.put("misssiontheme", planTitle);
                    js.put("misssionContent", planContent);
                    js.put("misssionType", type);
                    js.put("misssionSite", place);
                    js.put("maintainMan", approvalID);
                    js.put("repairsMan", repairman);
                    js.put("ContactWay", repairWay);
                    js.put("finishtime", planCompleteTime);
                    js.put("remark", remark);
                    js.put("payoutman", UserHelper.getCurrentUser().getEmployeeID());

                    UserHelper.addMission(AddMissionActivity.this, js);
                    sendMessage(POST_SUCCESS);
                } catch (MyException e) {
                    sendMessage(POST_FAILED, e.getMessage());

                } catch (JSONException e) {
                    LogUtils.e(TAG, e.getMessage());
                }
            }
        });

    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case POST_SUCCESS:
                PageUtil.DisplayToast(getResources().getString(R.string.approval_success));
                //                clear();
                this.finish();
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
        }
    }


    /**
     * 添加审批人
     *
     * @param view
     */
    public void forAddApprover(View view) {
        myStartForResult(ContactsMissionActivity.class, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0) {
            //判断返回值是否为空
            List<ContactsEmployeeModel> list = new ArrayList<>();
            if (data != null && data.getSerializableExtra("data") != null) {
                list = (List<ContactsEmployeeModel>) data.getSerializableExtra("data");
            }
            if (list.size() >= 2) {
                PageUtil.DisplayToast("只能选择一个联系人");
                return;
            } else {
                StringBuilder name = new StringBuilder();
                StringBuilder employeeId = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    name.append(list.get(i).getsEmployeeName() + "  ");
                    employeeId.append(list.get(i).getsEmployeeID() + ",");
                }

                approvalID = getApprovalID(employeeId.toString());
                Log.d("SJY", "approvalID=" + approvalID);
                tv_Requester.setText(name);
            }

        }
    }

    /*
     *处理字符串，去除末尾逗号
     */
    private String getApprovalID(String str) {
        if (str.length() > 1) {
            return str.substring(0, str.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * 任务类型
     *
     * @param view
     */
    public void missionType(View view) {
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setTitle(getResources().getString(R.string.msn_type));
        final String[] data = getResources().getStringArray(R.array.msn_maintanType);
        buidler.setSingleChoiceItems(data, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                type = data[which];
                tv_type.setText(data[which].trim());
                dialog.dismiss();
            }
        });
        buidler.show();
    }

    /**
     * 时间
     *
     * @param view
     */
    public void startTimeBefore(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(AddMissionActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        planCompleteTime = time;
                        tv_completeTime.setText(time);
                    }
                });
        //        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("开始时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * back
     *
     * @param view
     */
    public void forBack(View view) {
        this.finish();
    }


}
