package com.yuefei.library.db;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.List;


/**
 * Created by shao on 2017/4/11.
 */

public class DbUtil {
    private  static DBManager dbManager;
    private static DbUtil instance;
    private static Context mContext;
    private  static Handler mWorkHandler;
    private static  HandlerThread mWorkThread;

    public static DbUtil getInstance(Context context) {
        if(instance==null){
                synchronized(DbUtil.class) {
                    if(instance == null) {
                        mContext=context;
                        instance = new DbUtil();
                    }
                }
        }
        return instance;
    }
    public static DbUtil init(Context context) {
        instance=new DbUtil();
        dbManager = DBManager.init(mContext);
        return instance;
    }
    public static void logout( ) {
         if(dbManager!=null){
             dbManager.uninit();
         }
    }
    /**
     * 构造方法
     *
     */
    private DbUtil( ) {
        setmHandler();
    }
    public  static void setmHandler(){
        mWorkThread = new HandlerThread("dbUtil");
        mWorkThread.start();
        mWorkHandler = new Handler(mWorkThread.getLooper());
    }


    public DaoSession getDaoSession() {
        if (dbManager != null && dbManager.getDaoSession() != null) {
            return dbManager.getDaoSession();
        } else {
            return null;
        }
    }

    public void saveGift(GiftEntity giftEntity) {
        getDaoSession().getGiftEntityDao().insertOrReplace(giftEntity);
    }

    public void saveGiftList(final List<GiftEntity> list) {
        mWorkHandler.post(new Runnable() {
            @Override
            public void run() {
                for (GiftEntity gift : list) {
                     saveGift(gift);
                }
            }
        });
    }

    public void getGiftList(final ResultCallBack resultCallBack) {
        mWorkHandler.post(new Runnable() {
            @Override
            public void run() {
                List<GiftEntity> result = getDaoSession().getGiftEntityDao().queryBuilder().list();
                resultCallBack.OnSuccess(result);
            }

        });
    }

    public GiftEntity getGiftEntity(String giftId) {
        GiftEntity giftEntity = getDaoSession().getGiftEntityDao().queryBuilder().where(GiftEntityDao.Properties.GiftId.eq(giftId)).unique();
        return giftEntity;
    }

    public void deleteGift(final GiftEntity gift) {
        mWorkHandler.post(new Runnable() {
            @Override
            public void run() {
                getDaoSession().getGiftEntityDao().delete(gift);
            }
        });
    }
}

