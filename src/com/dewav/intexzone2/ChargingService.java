package com.dewav.intexzone2;

//import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.Time;

public class ChargingService extends Service
{
  private static final int CHARGE_IN = 2;
  private static final int CHARGE_OUT = 3;
  private static final int CHARGING_SERVICE_NOTIFICATION = 1;
  private static final String TAG = "ChargingService";
  private static final int UPDATE_VIEW = 4;
  public static String currStartTime;
  public static boolean isPlugIn = false;
  public static int mLevel;
  public static int mScale;
  public static long startMillis;
  private NotificationManager manager;
  private int outPer = 0;

  
  private BroadcastReceiver batteryChangeReceiver = new BroadcastReceiver(){
	  
    @Override
    public void onReceive(Context paramContext, Intent paramIntent){
      int i;
      int j;
      int k;
      if ("android.intent.action.BATTERY_CHANGED".equals(paramIntent.getAction()))
      {
        i = paramIntent.getIntExtra("plugged", 0);
        j = paramIntent.getIntExtra("level", 0);
        ChargingService.mLevel = j;
        k = paramIntent.getIntExtra("scale", 100);
        ChargingService.mScale = k;
        if (!ChargingService.isPlugIn){

        if (i == 0)
        {
          ChargingService.isPlugIn = false;
          ChargingService.this.mHandler.sendMessage(ChargingService.this.mHandler.obtainMessage(CHARGE_OUT, j, k));
        }else{
          ChargingService.isPlugIn = true;
          ChargingService.this.mHandler.sendMessage(ChargingService.this.mHandler.obtainMessage(CHARGE_IN, j, k));
        }
      }
    
        ChargingService.this.mHandler.sendEmptyMessage(UPDATE_VIEW);
    }
  }
 };
 
  private Handler mHandler = new Handler()
  {
	@Override
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      case CHARGE_IN:
        ChargingService.this.doNotifyNotification();
        Time localTime = new Time();
        localTime.setToNow();
        ChargingService.currStartTime = LogFileParase.formatTime(localTime);
        ChargingService.startMillis = System.currentTimeMillis();
        break;
        
      case CHARGE_OUT:
        ChargingService.this.manager.cancel(CHARGING_SERVICE_NOTIFICATION);
        int i = (int)((System.currentTimeMillis() - ChargingService.startMillis) / 60000L);
        //ChargingService.access$202(ChargingService.this, 100 * paramMessage.arg1 / paramMessage.arg2);
        outPer = ((paramMessage.arg1 * 100) / paramMessage.arg2);
        new LogFileParase().WriteLogFile(ChargingService.this.outPer, i);
        ChargingService.currStartTime = null;
        ChargingService.startMillis = 0L;
        break;
        
      case UPDATE_VIEW:
        Intent localIntent = new Intent();
        localIntent.setAction("com.dewav.charge.UPDATE_VIEW");
        ChargingService.this.sendBroadcast(localIntent);
        break;
        
      default:
      	break;
      }
    }
  };
  
  private void doNotifyNotification()
  {
    Notification localNotification = new Notification();
    localNotification.icon = R.drawable.charging;
    localNotification.tickerText = "Charging Info";
    localNotification.flags = Notification.FLAG_NO_CLEAR;
    Intent localIntent = new Intent();
    localIntent.putExtra("from_charge", true);
    localIntent.setClassName("com.dewav.intexzone2", "com.dewav.intexzone2.ChargingInfoActivity");
    localNotification.setLatestEventInfo(this, "Charging Info", "Show full charging information", PendingIntent.getActivity(this, 0, localIntent, 0));
    this.manager.notify(CHARGING_SERVICE_NOTIFICATION, localNotification);
  }

  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }

  @Override
  public void onCreate()
  {
    super.onCreate();
    this.manager = ((NotificationManager)getSystemService("notification"));
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.intent.action.BATTERY_CHANGED");
    registerReceiver(this.batteryChangeReceiver, localIntentFilter);
  }

  @Override
  public void onDestroy()
  {
    unregisterReceiver(this.batteryChangeReceiver);
    super.onDestroy();
  }
}