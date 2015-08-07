package com.tv.service.aidl;
import com.tv.service.aidl.ISwitchSourceListener;
interface IOverseaTvService
{
	void initSwitchSourceListener(ISwitchSourceListener iSwitchSourceListener);
	void setSource();
}