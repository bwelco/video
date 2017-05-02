package com.elliott.supervideoplayer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elliott.supervideoplayer.model.VideoBean;

import java.util.ArrayList;
import java.util.Collections;

/**
 */
public class VideoBeanDaoHelper {
    VideoBeanDao noteDao;
    public VideoBeanDaoHelper(Context context) {
        DaoMaster.DevOpenHelper  helper = new DaoMaster.DevOpenHelper(context, "VideoBean-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        noteDao = daoSession.getVideoBeanDao();
    }

    public ArrayList<VideoBean> getDatasByType(int type){
        ArrayList<VideoBean> list = (ArrayList<VideoBean>) noteDao.queryBuilder().where(VideoBeanDao.Properties.Type.eq(type)).list();
        Collections.sort(list);
        return list;
    }

    public void insertBean(VideoBean bean){
        noteDao.insert(bean);
    }

    public void deleteBean(VideoBean bean){
        noteDao.delete(bean);
    }

    public void updateBean(VideoBean bean){
        noteDao.update(bean);
    }


}
