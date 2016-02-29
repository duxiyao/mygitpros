package com.kjstudy.service;

import java.util.TimerTask;

import org.kymjs.kjframe.KJService;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.ViewInject;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.guard.Watcher;
import com.imbase.R;
import com.kjstudy.bean.ETSUserInfo;
import com.kjstudy.bean.EntityT;
import com.kjstudy.bean.data.TSStudentInfo;
import com.kjstudy.bean.data.TSTeacherInfo;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.core.net.Req;
import com.kjstudy.core.thread.ThreadManager;
import com.kjstudy.core.util.BroadCastUtil;
import com.kjstudy.core.util.DBUtil;
import com.kjstudy.core.util.Global;
import com.kjstudy.core.util.IntentNameUtil;
import com.kjstudy.core.util.JsonUtil;
import com.kjstudy.core.util.MyTimer;
import com.kjstudy.maputil.MapLocation;
import com.kjstudy.maputil.MapLocation.LocationListener;

public class ServiceMainData extends KJService {

    private final static int START_REALTIME_LOC = 4352532;

    private final static int EXE_TASK_EVE = 4352533;

    private final static int LAST_LOGIN = 4352534;

    // private SeriesMapLocation mSML;

    private MyTimer mTimer, mTimer1;

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
        case START_REALTIME_LOC:
            if (mTimer == null)
                mTimer = new MyTimer();
            mTimer.start(60, new TimerTask() {

                @Override
                public void run() {
                    sendMsg(getOsEmptyMsg(EXE_TASK_EVE));
                }
            });
            // mSML = new SeriesMapLocation(60000, new LocationListener() {
            //
            // @Override
            // public void onReceiveLocation(BDLocation location) {
            // TSUserInfo m = Global.getCURUSER();
            // if (m != null && m.getId() != -1) {
            // String latlng = String.valueOf(location.getLongitude())
            // + "," + String.valueOf(location.getLatitude());
            // String ubid = String.valueOf(m.getId());
            // Req.upRealtimePos(ubid, "", latlng, new HttpCallBack() {
            //
            // });
            // }
            // }
            // });
            break;
        case EXE_TASK_EVE:
            MapLocation.getInstance().setLLis(new LocationListener() {

                @Override
                public void onReceiveLocation(BDLocation location) {
                    // TSUserInfo m = Global.getCURUSER();
                    // if (m != null && m.getId() != -1) {
                    String latlng = String.valueOf(location.getLongitude())
                            + "," + String.valueOf(location.getLatitude());
                    // String ubid = String.valueOf(m.getId());
                    String ubid = "17";
                    Req.upRealtimePos(ubid, "", latlng, new HttpCallBack() {
                        public void onSuccess(String t) {
                            System.out.println(t);
                        };

                        public void onFinish() {
                            System.out.println("loc req finish");
                        };
                    });
                }
                // }
            }).startLocation();
            break;
        case LAST_LOGIN:
            Global.lastLoginUser();
            break;
        default:
            break;
        }
    }

    void r(Runnable r) {
        ThreadManager.getInstance().exeRunnable(r);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Notification notification = new Notification(R.drawable.ic_launcher,
        // getText(R.string.app_name),
        // System.currentTimeMillis());
        // Intent notificationIntent = new Intent(this, ServiceMainData.class);
        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
        // notificationIntent, 0);
        // notification.setLatestEventInfo(this, getText(R.string.hello_world),
        // getText(R.string.none_string), pendingIntent);
        // notification.flags=Notification.FLAG_ONGOING_EVENT;
        // startForeground(Notification.FLAG_ONGOING_EVENT, notification);

        setFilters(IntentNameUtil.SERVICE_ACTION_ON_REQ_STU_TEA_DATA,
                IntentNameUtil.SERVICE_ACTION_ON_UP_REAL_TIME_POS,
                IntentNameUtil.SERVICE_ACTION_ON_STOP_REAL_TIME_POS);
        sendMsg(getOsEmptyMsg(LAST_LOGIN));
        mTimer1 = new MyTimer();
        mTimer1.start(10, new TimerTask() {

            @Override
            public void run() {
//                System.out.println("I am running!!");
                 sendMsg(getOsEmptyMsg(EXE_TASK_EVE));
            }
        });
    }

    @Override
    protected void dealBroadcase(Intent intent) {
        super.dealBroadcase(intent);
        String action = intent.getAction();
        if (IntentNameUtil.SERVICE_ACTION_ON_REQ_STU_TEA_DATA.equals(action)) {
            r(new Runnable() {

                @Override
                public void run() {
                    TSUserInfo m = Global.getCURUSER();
                    if (m == null)
                        return;
                    Req.getBasicInfo(m.getId(), new HttpCallBack() {
                        @Override
                        public void onSuccess(String t) {
                            ETSUserInfo user = JsonUtil.json2Obj(t,
                                    ETSUserInfo.class);
                            if (user != null && user.getCode() == 0
                                    && user.getData() != null) {
                                TSUserInfo u = user.getData();
                                if (!DBUtil.getDB().isExists(TSUserInfo.class,
                                        "id=" + u.getId())) {
                                    DBUtil.save(u);
                                } else {
                                    DBUtil.update(u, "id=" + u.getId());
                                }
                                if (u.getId() != -1)
                                    Global.setCURUSER(String.valueOf(u.getId()));
                            } else {
                                if (user != null)
                                    ViewInject.toast(user.getMsg());
                            }
                            BroadCastUtil
                                    .sendBroadCast(IntentNameUtil.ON_ALTER_PERSONAL_INFO_SUCCESS);
                        }
                    });

                    Req.getStudentInfo(m.getId(), new HttpCallBack() {
                        @Override
                        public void onSuccess(String t) {
                            EntityT<TSStudentInfo> et = JsonUtil.json2ET(t,
                                    TSStudentInfo.class);
                            if (et.getCode() == 0) {
                                TSStudentInfo stu = et.getData();
                                if (!DBUtil.isExists(TSStudentInfo.class,
                                        "ubId=" + stu.getUbId()))
                                    DBUtil.save(stu);
                                else
                                    DBUtil.update(stu, "ubId=" + stu.getUbId());
                                BroadCastUtil
                                        .sendBroadCast(IntentNameUtil.ON_ALTER_PERSONAL_INFO_SUCCESS);
                            } else {
                                ViewInject.toast(et.getMsg());
                            }
                        }
                    });

                    Req.getTeacherInfo(m.getId(), new HttpCallBack() {
                        @Override
                        public void onSuccess(String t) {
                            EntityT<TSTeacherInfo> et = JsonUtil.json2ET(t,
                                    TSTeacherInfo.class);
                            if (et.getCode() == 0) {
                                TSTeacherInfo tea = et.getData();

                                if (!DBUtil.isExists(TSTeacherInfo.class,
                                        "ubId=" + tea.getUbId()))
                                    DBUtil.save(tea);
                                else
                                    DBUtil.update(tea, "ubId=" + tea.getUbId());

                            } else {
                                ViewInject.toast(et.getMsg());
                            }
                        }
                    });
                }
            });
        } else if (IntentNameUtil.SERVICE_ACTION_ON_UP_REAL_TIME_POS
                .equals(action)) {
            sendMsg(getOsEmptyMsg(START_REALTIME_LOC));
        } else if (IntentNameUtil.SERVICE_ACTION_ON_STOP_REAL_TIME_POS
                .equals(action)) {
            if (mTimer != null)
                mTimer.stop();
            // mSML.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // flags = START_STICKY;
        // return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mTimer != null)
            mTimer.stop();
        stopForeground(true);
        super.onDestroy();
        System.out.println("onDestroy");
    }
}
