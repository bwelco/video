package com.elliott.supervideoplayer.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by bwelco on 2017/5/18.
 */

public class VideoFragment extends Fragment {

    /**
     */
    private String[] mVideoTestPath = new String[]{
            "http://omjfzo9d3.bkt.clouddn.com/video_20170426_162819.mp4",
            "http://omjfzo9d3.bkt.clouddn.com/video_20170426_162920.mp4",
            "http://omjfzo9d3.bkt.clouddn.com/video_20170427_153640.mp4"
    };
    private String[] mVideoTestPathName = new String[]{
            "home1", "home2", "home3"
    };

    private long[] mTimes = new long[]{1493186505000L, 1493200905000L, 1493287305000L};


}
