package com.github.tvbox.osc.base;

import androidx.multidex.MultiDexApplication;

import com.github.tvbox.osc.callback.EmptyCallback;
import com.github.tvbox.osc.callback.LoadingCallback;
import com.github.tvbox.osc.data.AppDataManager;
import com.github.tvbox.osc.server.ControlManager;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.OkGoHelper;
import com.kingja.loadsir.core.LoadSir;
import com.orhanobut.hawk.Hawk;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * @author pj567
 * @date :2020/12/17
 * @description:
 */
public class App extends MultiDexApplication {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initParams();
        // OKGo
        OkGoHelper.init();
        // 初始化Web服务器
        ControlManager.init(this);
        //初始化数据库
        AppDataManager.init();
        LoadSir.beginBuilder()
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .commit();
        AutoSizeConfig.getInstance().setCustomFragment(true).getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);
    }

    private void initParams() {
        // Hawk
        Hawk.init(this).build();
        Hawk.put(HawkConfig.DEBUG_OPEN, false);

        // 首页选项
        putDefault(HawkConfig.HOME_REC, 2);                  //推荐: 0=豆瓣热播, 1=站点推荐, 2=观看历史
        // 播放器选项
        putDefault(HawkConfig.PLAY_SCALE, 0);                //画面缩放: 0=默认, 1=16:9, 2=4:3, 3=填充, 4=原始, 5=裁剪
        putDefault(HawkConfig.PLAY_TYPE, 2);                 //播放器: 0=系统, 2=Exo
        // 系统选项
        putDefault(HawkConfig.SEARCH_VIEW, 0);               //搜索展示: 0=文字列表, 1=缩略图
        putDefault(HawkConfig.PARSE_WEBVIEW, true);          //嗅探Webview: true=系统自带, false=XWalkView
        putDefault(HawkConfig.DOH_URL, 0);                   //安全DNS: 0=关闭, 1=腾讯, 2=阿里, 3=360

        // 渲染方式
        putDefault(HawkConfig.PLAY_RENDER, 1);               //渲染方式 0=TextureView, 1=SurfaceView
    }

    private void putDefault(String key, Object value) {
        if (!Hawk.contains(key)) Hawk.put(key, value);
    }

    public static App getInstance() {
        return instance;
    }
}