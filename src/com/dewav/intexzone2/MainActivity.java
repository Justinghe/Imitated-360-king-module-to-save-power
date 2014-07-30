package com.dewav.intexzone2;

//import android.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity
{
  private static final String ITEM_IMAGE_VIEW = "ItemImageView";
  private static final String ITEM_TEXT_VIEW = "ItemTextView";
  List<HashMap<String, Object>> data = new ArrayList();
  private int[] imageRes = { R.drawable.ic_nq_vault,R.drawable.ic_nq_security,R.drawable.ic_intex_case,R.drawable.auto_call_record,R.drawable.charging,R.drawable.ic_important_day};
  private String[] itemName = { "NQ Vault", "NQ SECURITY", "Intex Care", "Auto Call Record", "Charging Info", "Important Day" };
  public ArrayList<String> mData = new ArrayList();
  private GridView mGridView;
  private boolean mHasNotNQSecurity;
  private boolean mHasNotNQVault;
  private boolean mHasNotRealFootball;
  private boolean mHasNotVubbleXSlice;
  private static final String TAG = "MainActivity";
  
  private boolean getAppInstall(String paramString)
  {
    PackageInfo localPackageInfo1;
    
 try{
      PackageInfo localPackageInfo2 = getPackageManager().getPackageInfo(paramString, 0);
      localPackageInfo1 = localPackageInfo2;
      if (localPackageInfo1 != null)
      return false;
    }catch (PackageManager.NameNotFoundException localNameNotFoundException){
      localNameNotFoundException.printStackTrace();
      localPackageInfo1 = null;
    }
      return true;
  }
  
  @Override
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_main);
    //setContentView(R.layout.charging_info);
    
    this.mGridView = ((GridView)findViewById(R.id.MyGridView));
    this.mGridView.setOnItemClickListener(new GridViewItemOnClick());
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    this.mGridView = ((GridView)findViewById(R.id.MyGridView));
    if ((this.mData != null) || (this.mData.size() > 0)){
      this.mData.clear();
    }
    if ((this.data != null) || (this.data.size() > 0)){
      this.data.clear();
    }
    int i = this.imageRes.length;
    
    Log.e(TAG, "imageRes.length: " + i);
 
    int j = 0;
    if (j < i)
    {
      if ((j == 0) && (!getAppInstall("com.netqin.ps")))
      {
        ++j;
      }
      else 
if (((j == 1) && (!getAppInstall("com.nqmobile.antivirus20"))) || ((j == 5) && (!getAppInstall("com.dewav"))))
      {
        HashMap localHashMap = new HashMap();
        localHashMap.put("ItemImageView", Integer.valueOf(this.imageRes[j]));
        localHashMap.put("ItemTextView", this.itemName[j]);
        this.data.add(localHashMap);
        this.mData.add(this.itemName[j]);
      }
    }
    SimpleAdapter localSimpleAdapter = new SimpleAdapter(this, this.data, R.layout.item, new String[] { "ItemImageView", "ItemTextView" }, new int[] { R.id.ItemImageView, R.id.ItemTextView });
    this.mGridView.setAdapter(localSimpleAdapter);
    this.mGridView.setOnItemClickListener(new GridViewItemOnClick());
  }

  public class GridViewItemOnClick  implements AdapterView.OnItemClickListener
  {
    public GridViewItemOnClick()
    {
    	//this.onItemClick(paramAdapterView, paramView, paramInt, paramLong)
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      Intent localIntent = new Intent();
      localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      Log.e("GridViewItemOnClick", "  =  " + ((String)MainActivity.this.mData.get(paramInt)).toString());
      
      if ("NQ Vault".equals(((String)MainActivity.this.mData.get(paramInt)).toString())){
        localIntent.setClassName("com.netqin.ps", "com.netqin.ps.privacy.HideActivity");
      }else
      //while (true)
      //{
        //MainActivity.this.startActivity(localIntent);
        //return;
        if ("NQ SECURITY".equals(((String)MainActivity.this.mData.get(paramInt)).toString())){
          localIntent.setClassName("com.nqmobile.antivirus20", "com.netqin.antivirus.AntiVirusSplash");
        }else
        if ("Intex Care".equals(((String)MainActivity.this.mData.get(paramInt)).toString())){
          localIntent.setClassName("com.mmt.intex.servicecenter", "com.mmt.intex.servicecenter.MainActivity");
        }else
        if ("Real Football".equals(((String)MainActivity.this.mData.get(paramInt)).toString())){
          localIntent.setClassName("com.gameloft.android.GloftRF14", "com.gameloft.android.GloftRF14.Start");
        }else
        if ("Bubble X Slice".equals(((String)MainActivity.this.mData.get(paramInt)).toString())){
          localIntent.setClassName("sk.inlogic.bubblexslice", "com.veniso.mtrussliband.core.GameSMTo");
        }else
        if ("Auto Call Record".equals(((String)MainActivity.this.mData.get(paramInt)).toString())){
          localIntent.setAction("Auto_Call_Record");
        }else
        if ("Charging Info".equals(((String)MainActivity.this.mData.get(paramInt)).toString())){
          localIntent.setClassName("com.dewav.intexzone2", "com.dewav.intexzone2.ChargingInfoActivity");
        }else
        if (!"Important Day".equals(((String)MainActivity.this.mData.get(paramInt)).toString())){
        localIntent.setClassName("com.dewav", "com.dewav.activity.ImportantDaysActivity");
        }
      
        localIntent.setClassName("com.dewav.intexzone2", "com.dewav.intexzone2.ChargingInfoActivity");
        
        MainActivity.this.startActivity(localIntent);
      //}
    }
  }
}
