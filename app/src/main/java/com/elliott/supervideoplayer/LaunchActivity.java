package com.elliott.supervideoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.elliott.supervideoplayer.adapter.VideoAdapter;
import com.elliott.supervideoplayer.db.VideoBeanDaoHelper;
import com.elliott.supervideoplayer.model.VideoBean;
import com.elliott.supervideoplayer.utils.BitmapUtil;
import com.elliott.supervideoplayer.utils.ConfigUtil;
import com.elliott.supervideoplayer.utils.DensityUtils;
import com.elliott.supervideoplayer.utils.DialogUtils;
import com.elliott.supervideoplayer.utils.ExceptionHandler;
import com.elliott.supervideoplayer.utils.RandomUtil;
import com.elliott.supervideoplayer.utils.T;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import static android.R.attr.path;

public class LaunchActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
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

    /**


     */
    private String[] mVideoLiveTestPath = new String[]{

    };
    private String[] mVideoLivePathName = new String[]{
            "RTMP", "m3u8 "
    };
    private LinearLayout mLinearLayout;
    private LinearLayout linearLayout;
    private LinearLayout lineaylayout2;
    private ImageView ImageView2;
    private ImageView ImageView3;
    private ArrayList<View> mViewList;
    private SparseArray<ArrayList<VideoBean>> mDataMaps;
    private SparseArray<VideoAdapter> mAdapters;
    private View monitorView;
    private WebView webView;
    private RelativeLayout police;
    private RelativeLayout screen;
    private SwipeRefreshLayout refreshLayout;
    /**
     * 底部菜单按钮
     */
    FloatingActionMenu floatingActionMenu;

    VideoBeanDaoHelper helper;
    /**
     * 0表示在线视频  1表示直播
     */
    private int currentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * catch unexpected error
         */
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        setContentView(R.layout.activity_main);
        initViews();
        initMenus();
        initDatas();
        initTestDatas();
        showCurrentListView();


    }

    /**
     * 添加测试数据
     */
    private void initTestDatas() {
        ArrayList<VideoBean> datasByList = helper.getDatasByType(currentType);
        if (datasByList.size() <= 0) {
            //在线视频
            for (int i = 0; i < mVideoTestPath.length; i++) {
                VideoBean bean = new VideoBean();
                bean.setVideoLink(mVideoTestPath[i]);
                bean.setVideoName(mVideoTestPathName[i]);
                bean.setTime(mTimes[i]);
                bean.setType(currentType);
                helper.insertBean(bean);
                showCurrentTypeData(bean);
            }
            //直播流
            for (int i = 0; i < mVideoLiveTestPath.length; i++) {
                VideoBean bean = new VideoBean();
                bean.setVideoLink(mVideoLiveTestPath[i]);
                bean.setVideoName(mVideoLivePathName[i]);
                bean.setType(1);
                helper.insertBean(bean);
                mDataMaps.get(1).add(bean);
            }
        }
    }

    private void switchToVideo() {
        mLinearLayout.removeAllViews();
        mLinearLayout.addView(mViewList.get(0));
    }

    private void switchToMonitor() {
        mLinearLayout.removeAllViews();
        mLinearLayout.addView(monitorView);
    }

    private void showCurrentListView() {
        mLinearLayout.removeAllViews();
        mLinearLayout.addView(mViewList.get(currentType));
    }

    private void initViews() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayout_content);
        linearLayout = (LinearLayout) findViewById(R.id.button_layout_2);
        lineaylayout2 = (LinearLayout) findViewById(R.id.button_layout_3);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handTitleIndexImg(0);
                //showCurrentListView();
                switchToVideo();
            }
        });
        lineaylayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handTitleIndexImg(1);
                showCurrentListView();
                switchToMonitor();
            }
        });
        ImageView2 = (ImageView) findViewById(R.id.button_line_img2);
        ImageView3 = (ImageView) findViewById(R.id.button_line_img3);
        monitorView = LayoutInflater.from(this).inflate(R.layout.monitor_view, null, false);
        webView = (WebView) monitorView.findViewById(R.id.webview);
        monitorView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        String uri = "http://" + ConfigUtil.getIp(this) + ":8080/?action=stream";
        webView.loadUrl(uri);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        refreshLayout = (SwipeRefreshLayout) monitorView.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        handTitleIndexImg(0);
        police = (RelativeLayout) monitorView.findViewById(R.id.police_layout);
        screen = (RelativeLayout) monitorView.findViewById(R.id.screen_layout);
        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ConfigUtil.getPhone(LaunchActivity.this)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setDrawingCacheEnabled(true);
                Bitmap tBitmap = webView.getDrawingCache();
                tBitmap = tBitmap.createBitmap(tBitmap);
                webView.setDrawingCacheEnabled(false);
                if (tBitmap != null) {
                    if (saveImageToGallery(LaunchActivity.this, tBitmap)) {
                        Toast.makeText(getApplicationContext(), "截图成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "截图失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "截图失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handTitleIndexImg(int position) {
        currentType = position;
        ArrayList<View> list = new ArrayList<>();
        list.add(ImageView2);
        list.add(ImageView3);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            list.get(i).setVisibility(View.INVISIBLE);
        }
        list.get(position).setVisibility(View.VISIBLE);
    }

    private void initMenus() {
        ImageView menuimg = (ImageView) findViewById(R.id.menu_img);
        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        ImageView rlIcon1 = new ImageView(this);
        ImageView rlIcon2 = new ImageView(this);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.action_edit_light));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.setting));

        SubActionButton rlSub1 = rLSubBuilder.setContentView(rlIcon1).build();
        SubActionButton rlSub2 = rLSubBuilder.setContentView(rlIcon2).build();

        rlIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加
                showAddVideoItemDialog();
            }
        });

        rlIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaunchActivity.this, SettingActivity.class));

            }
        });
        floatingActionMenu = new FloatingActionMenu.Builder(this)
                .setStartAngle(190)
                .setEndAngle(250)
                .addSubActionView(rlSub1)
                .addSubActionView(rlSub2)
                .attachTo(menuimg)
                .build();
    }

    private void showAddVideoItemDialog() {
        final DialogUtils dialogUtils = new DialogUtils(this, R.layout.normal_dialog);
        dialogUtils.setOnClickListener(R.id.btn_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //插入listview
                EditText linkEd = (EditText) dialogUtils.getView(R.id.dialog_link_ed);
                EditText nameEd = (EditText) dialogUtils.getView(R.id.dialog_name_ed);

                if (!TextUtils.isEmpty(linkEd.getText().toString()) && !TextUtils.isEmpty(nameEd.getText().toString())) {
                    VideoBean bean = new VideoBean();
                    bean.setVideoLink(linkEd.getText().toString());
                    bean.setVideoName(nameEd.getText().toString());
                    bean.setType(currentType);
                    bean.setTime(System.currentTimeMillis());
                    helper.insertBean(bean);
                    showCurrentTypeData(bean);
                } else {
                    T.showLong(LaunchActivity.this, "请求填写正确的流视频");
                }
                dialogUtils.close();
            }
        }).setOnClickListener(R.id.btn_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtils.close();
            }
        }).show();
    }

    private void showCurrentTypeData(VideoBean bean) {
        mDataMaps.get(currentType).add(bean);
        Collections.sort(mDataMaps.get(currentType));
        mAdapters.get(currentType).notifyDataSetChanged();
    }

    private void deleteCurrentTypeData(int position) {
        VideoBean bean = mDataMaps.get(currentType).get(position);
        helper.deleteBean(bean);
        mDataMaps.get(currentType).remove(bean);
        mAdapters.get(currentType).notifyDataSetChanged();
    }

    private void initDatas() {
        helper = new VideoBeanDaoHelper(this);
        mViewList = new ArrayList<View>();
        mDataMaps = new SparseArray<>();
        mAdapters = new SparseArray<>();
        for (int i = 0; i < 2; i++) {
            SwipeMenuListView listview = new SwipeMenuListView(this);
            ArrayList<VideoBean> dataList = helper.getDatasByType(i);
            VideoAdapter videoAdapter = new VideoAdapter(this, dataList, R.layout.video_item_layout);
            listview.setAdapter(videoAdapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    VideoBean bean = mDataMaps.get(currentType).get(position);
                    goToVideoPlayerPage(bean);
                }
            });
            SwipeMenuCreator creator = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                            0x3F, 0x25)));
                    // set item width
                    deleteItem.setWidth(DensityUtils.dp2px(LaunchActivity.this, 90));
                    // set a icon
                    deleteItem.setIcon(R.drawable.ic_delete);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            };
            listview.setMenuCreator(creator);
            listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            deleteCurrentTypeData(position);
                            break;
                    }
                    // false : close the menu; true : not close the menu
                    return false;
                }
            });
            mDataMaps.put(i, dataList);
            mAdapters.put(i, videoAdapter);
            mViewList.add(listview);
        }
    }

    private void goToVideoPlayerPage(VideoBean bean) {
        if (currentType == 0) {
            Intent appIntent = new Intent(this, VideoViewActivity.class);
            appIntent.putExtra(VideoViewActivity.VIDEO_PATH, bean.getVideoLink());
            startActivity(appIntent);
        } else {
            try {
                Intent appIntent = new Intent(this, VideoViewLiveActivity.class);
                appIntent.putExtra(VideoViewLiveActivity.VIDEO_PATH, bean.getVideoLink());
                startActivity(appIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (floatingActionMenu != null && floatingActionMenu.isOpen()) {
            floatingActionMenu.close(true);
        }
        return super.onTouchEvent(event);
    }


    public boolean saveImageToGallery(Context context, Bitmap bmp) {
        String fileName = RandomUtil.getRandomLetters(6) + ".jpg";
        String picturnPath = this.getExternalCacheDir() + File.separator + fileName;

        boolean saveSuccess = BitmapUtil.saveBitmap(bmp, new File(picturnPath));

        if (saveSuccess) {
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        picturnPath, fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

        }

        return saveSuccess;

    }

    @Override
    public void onRefresh() {
        String uri = "http://" + ConfigUtil.getIp(this) + ":8080/?action=stream";
        webView.loadUrl(uri);
        refreshLayout.setRefreshing(false);
    }
}
