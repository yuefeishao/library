package com.yuefei.library.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by shao on 2018/4/17.
 */
@Entity
public class GiftEntity  {
    @Id
    private Long id;
    private String giftId;
    @Unique
    private String giftUrl;
    private String filePath;
    private String fileName;

    @Generated(hash = 635805699)
    public GiftEntity(Long id, String giftId, String giftUrl, String filePath,
            String fileName) {
        this.id = id;
        this.giftId = giftId;
        this.giftUrl = giftUrl;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    @Generated(hash = 1699619307)
    public GiftEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftUrl() {
        return giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
