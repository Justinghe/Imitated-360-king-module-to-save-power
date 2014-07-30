package com.dewav.intexzone2;

public class ChargingInfo
{
  private String mChargingEndTime;
  private int mChargingSaturationLevel;
  private int mDuration;

  public String getChargingEndTime()
  {
    return this.mChargingEndTime;
  }

  public int getChargingSaturationLevel()
  {
    return this.mChargingSaturationLevel;
  }

  public int getduration()
  {
    return this.mDuration;
  }

  public void setChargingEndTime(String paramString)
  {
    this.mChargingEndTime = paramString;
  }

  public void setChargingSaturationLevel(int paramInt)
  {
    this.mChargingSaturationLevel = paramInt;
  }

  public void setDuration(int paramInt)
  {
    this.mDuration = paramInt;
  }
}
