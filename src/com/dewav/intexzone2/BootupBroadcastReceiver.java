package com.dewav.intexzone2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootupBroadcastReceiver extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (!paramIntent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
      return;
    
    Log.e("egbert", "egbert -- BootupBroadcastReceiver");
    
    paramContext.startService(new Intent(paramContext, ChargingService.class));
  }
}

