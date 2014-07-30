package com.dewav.intexzone2;

//import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


/**
 * @author Administrator
 *
 * @Description: 充电界面的过程
 *
 */

public class ChargingInfoActivity extends Activity
{
  public static final String ACTION_UPDATE_VIEW = "com.dewav.charge.UPDATE_VIEW";
  private static final String CHARGING_END_TIME = "Charging End Time : ";
  private static final String CHARGING_SATURATION_LEVEL = "Charging saturation level : ";
  private static final String CURRENT_START_TIME = "Current start time : ";
  private static final int DEFAULT_POWER_LEVEL = 0;
  private static final int DEFAULT_POWER_SCALE = 100;
  private static final String DURATION = "Duration : ";
  private static final String END_TIME = "End time : ";
  private static final String LAST_CHARGING = "Last charging : ";
  private static final int POWER_PERCENT_MAX_WIDTH = 250;
  private static final String TAG = "ChargingInfoActivity";
  private TextView chargingLogDetails;
  private TextView currChargingInfo;
  private ImageView currPowerPercentImg;
  private TextView currPowerPercentText;
  private String currStartTime = null;
  private List<ChargingInfo> infoList;
  private boolean isCharging = false;
  private boolean isFirstPlug = true;
  private TextView lastInfo;
  private long startMillis;
  
  private BroadcastReceiver updateViewReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (!"com.dewav.charge.UPDATE_VIEW".equals(paramIntent.getAction()))
        return;
      
      ChargingInfoActivity.this.updateChargingLogDetails();
      ChargingInfoActivity.this.updateLastInfo();
      ChargingInfoActivity.this.updateCurrentChargingInfo();
    }
  };
  
/*
 * 更新充电log的详细信息
 * 
 */
  private void updateChargingLogDetails()
  {
    this.infoList = new LogFileParase().readLogFile();
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < this.infoList.size(); ++i)
    {
      ChargingInfo localChargingInfo = (ChargingInfo)this.infoList.get(i);
      localStringBuilder.append("\t" + (i + 1) + ".\t");
      localStringBuilder.append("Charging saturation level : ");
      localStringBuilder.append(localChargingInfo.getChargingSaturationLevel());
      localStringBuilder.append("%\n\t\t\t");
      localStringBuilder.append("Duration : ");
      localStringBuilder.append(localChargingInfo.getduration());
      localStringBuilder.append("min\n\t\t\t");
      localStringBuilder.append("Charging End Time : ");
      localStringBuilder.append(localChargingInfo.getChargingEndTime());
      localStringBuilder.append("\n\n");
    }
    
    if (this.infoList.size() == 0)
      localStringBuilder.append("\tno log!");
    this.chargingLogDetails.setText(localStringBuilder.toString());
  }
  
/*
 * 更新当前的充电信息，分为两种情形：1.插入耳机，2.不插入耳机 
 * 
 */
  @SuppressLint({"NewApi"})
  private void updateCurrentChargingInfo()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (ChargingService.isPlugIn){
      if (ChargingService.mLevel < DEFAULT_POWER_SCALE)
      {
    	  this.currPowerPercentImg.setImageResource(R.drawable.stat_sys_battery_charge);
    	  this.currPowerPercentImg.setBackground(null);
    	  this.currPowerPercentImg.setImageDrawable(null);
    	  this.currPowerPercentImg.setBackgroundResource(R.id.power_percent_img);
        
        updateCurrentPower();
        
        ViewGroup.LayoutParams localLayoutParams = ChargingInfoActivity.this.currPowerPercentImg.getLayoutParams();
        localLayoutParams.width = (POWER_PERCENT_MAX_WIDTH * DEFAULT_POWER_SCALE / ChargingService.mScale);
        this.currPowerPercentImg.setLayoutParams(localLayoutParams);
        
       if ((ChargingService.currStartTime != null) && (ChargingService.startMillis != 0L)){
        Time localTime = new Time();
        localTime.setToNow();
        
        currStartTime = LogFileParase.formatTime(localTime);
        startMillis = System.currentTimeMillis();
        }else{
        currStartTime = ChargingService.currStartTime;
        startMillis = ChargingService.startMillis;
       }
        
        this.currStartTime = currStartTime;
        this.startMillis = startMillis;
        
      //this.currChargingInfo.setText(localStringBuilder.toString());
        
        int i = (int)((System.currentTimeMillis() - this.startMillis) / 60000L);
        localStringBuilder.append("Current start time : ");
        localStringBuilder.append(this.currStartTime);
        localStringBuilder.append("\n");
        localStringBuilder.append("Duration : ");
        localStringBuilder.append("" + i + " min");
      }else{
	  
      //this.currChargingInfo.setText(localStringBuilder.toString());
     
      //this.currPowerPercentImg.setImageDrawable(null);
      //this.currPowerPercentImg.setBackgroundResource(R.id.power_percent_img);
      //updateCurrentPower();
      
      this.currStartTime = ChargingService.currStartTime;
      this.startMillis = ChargingService.startMillis;
      
      this.currPowerPercentImg.setImageDrawable(null);
      this.currPowerPercentImg.setBackgroundResource(R.id.power_percent_img);
      updateCurrentPower();
      localStringBuilder.append("Current start time : ");
      localStringBuilder.append("0\n");
      localStringBuilder.append("Duration : ");
      localStringBuilder.append("0 min");
    }
      this.currChargingInfo.setText(localStringBuilder.toString());
  }
 }
  
  private void updateCurrentPower()
  {
    updateCurrentPower(ChargingService.mLevel, ChargingService.mScale);
  }

  private void updateCurrentPower(int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0){
      return;
    }
    
    ViewGroup.LayoutParams localLayoutParams = this.currPowerPercentImg.getLayoutParams();
    localLayoutParams.width = (paramInt1 * POWER_PERCENT_MAX_WIDTH / paramInt2);
    
    this.currPowerPercentImg.setLayoutParams(localLayoutParams);
    int i = paramInt1 * DEFAULT_POWER_SCALE / paramInt2;
    this.currPowerPercentText.setText("" + i + " %");
  }

/*
 * 更新最近的充电信息，因为最近充电信息都默认为第一个log
 * 
 */
  private void updateLastInfo()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (this.infoList.size() > 0)
    {
      ChargingInfo localChargingInfo = (ChargingInfo)this.infoList.get(0);
      localStringBuilder.append("Last charging : ");
      localStringBuilder.append(localChargingInfo.getChargingSaturationLevel());
      localStringBuilder.append("%\n");
      localStringBuilder.append("Duration : ");
      localStringBuilder.append(localChargingInfo.getduration());
      localStringBuilder.append("min\n");
      localStringBuilder.append("End time : ");
      localStringBuilder.append(localChargingInfo.getChargingEndTime());
      localStringBuilder.append("\n");
    }
    else {
      localStringBuilder.append("\nThere is no charging log!");
      localStringBuilder.append("\n");
    }  
    
      this.lastInfo.setText(localStringBuilder.toString());
  }

 /*
  * 
  * 
  */
  //@Override
  public void onBackPressed(View paramView)
  {
    Intent localIntent = getIntent();
    if (localIntent.getBooleanExtra("from_charge", false))
    {
      localIntent.setClassName("com.dewav.intexzone2", "com.dewav.intexzone2.MainActivity");
      startActivity(localIntent);
    }
    finish();
  }
  
  @Override
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
    
    setContentView(R.layout.charging_info);
    getWindow().setFeatureInt(Window.FEATURE_OPTIONS_PANEL, R.layout.title);
    
    startService(new Intent(this, ChargingService.class));
    this.lastInfo = ((TextView)findViewById(R.id.last_info));
    this.currPowerPercentText = ((TextView)findViewById(R.id.power_percent_text));
    this.currPowerPercentImg = ((ImageView)findViewById(R.id.power_percent_img));
    this.currChargingInfo = ((TextView)findViewById(R.id.curr_charging_info));
    this.chargingLogDetails = ((TextView)findViewById(R.id.charging_log_details));
    this.chargingLogDetails.setMovementMethod(ScrollingMovementMethod.getInstance());
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.dewav.charge.UPDATE_VIEW");
    
    registerReceiver(this.updateViewReceiver, localIntentFilter);
    
    updateChargingLogDetails();
    updateLastInfo();
    updateCurrentChargingInfo();
  }

  @Override
  public void onDestroy()
  {
    unregisterReceiver(this.updateViewReceiver);
    super.onDestroy();
  }
}
