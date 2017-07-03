package com.huirong.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huirong.R;
import com.huirong.application.MyApplication;
import com.huirong.base.BaseLoadMoreListAdapter;
import com.huirong.common.ImageLoadingConfig;
import com.huirong.model.MyCopyModel;
import com.huirong.widget.CircleTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Random;


/**
 * 我的抄送记录 适配
 *
 * @author
 */

public class ZOCopyLoadMoreListAdapter extends BaseLoadMoreListAdapter {
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;
    private Context context;

    public class WidgetHolder {
        public TextView tvType;//类型
        public TextView tvdetail;
        public CircleTextView tvName;
        public TextView tvTime;
        public TextView tvComment;//审批状态
    }

    public ZOCopyLoadMoreListAdapter(Context context, AdapterCallBack callBack) {
        super(context, callBack);
        this.context = context;
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(context));
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.ic_launcher);
    }

    @Override
    protected View inflateConvertView() {
        //一条记录的布局
        View view = inflater.inflate(R.layout.item_examination_common, new RelativeLayout(context), false);
        //该布局上的控件
        WidgetHolder holder = new WidgetHolder();
        holder.tvName = (CircleTextView) view.findViewById(R.id.tv_name);
        holder.tvdetail = (TextView) view.findViewById(R.id.tv_detail);
        holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
        holder.tvType = (TextView) view.findViewById(R.id.tv_type);
        holder.tvComment = (TextView) view.findViewById(R.id.tv_Comment);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void initViewData(final int position, View convertView) {
        WidgetHolder holder = (WidgetHolder) convertView.getTag();//获取控件管理实例
        //获取一条信息
        //?java.lang.ClassCastException: java.util.ArrayList cannot be cast to com.yvision.model.VisitorBModel
        MyCopyModel model = (MyCopyModel) entityList.get(position);
        holder.tvName.setText(model.getEmployeeName());
        holder.tvName.setBackgroundColor(ContextCompat.getColor(MyApplication.getInstance(), randomColor()));
        holder.tvTime.setText(model.getCreateTime());
        holder.tvType.setText(model.getApplicationType());
        holder.tvdetail.setText(model.getDetail());
        holder.tvComment.setText("");
    }

    //设置一条记录的随机颜色
    private int randomColor() {
        int[] colorArray = new int[]{R.color.pink, R.color.lightgreen, R.color.gray, R.color.yellow, R.color.common_color, R.color.aquamarine, R.color.brown};
        return colorArray[new Random().nextInt(6)];
    }

    public void destroy() {
        imgLoader.clearMemoryCache();
        imgLoader.destroy();
    }

}
