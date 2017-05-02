package com.elliott.supervideoplayer.adapter;

import android.content.Context;

import com.elliott.supervideoplayer.R;
import com.elliott.supervideoplayer.model.VideoBean;
import com.elliott.supervideoplayer.utils.CommonAdapter;
import com.elliott.supervideoplayer.utils.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class VideoAdapter extends CommonAdapter<VideoBean> {

    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");

    public VideoAdapter(Context context, List mDatas, int layoutId) {
        super(context, mDatas, layoutId);
    }

    public void setDatas(ArrayList<VideoBean> list) {
        this.mDatas = list;
    }

    @Override
    public void convert(ViewHolder holder, VideoBean item) {

        String time = formater.format(new Date(item.getTime()));
        holder.setText(R.id.item_name_tv, item.getVideoName())
                .setText(R.id.item_time, time);
    }

}
