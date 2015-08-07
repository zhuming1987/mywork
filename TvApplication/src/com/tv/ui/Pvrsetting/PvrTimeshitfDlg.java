package com.tv.ui.Pvrsetting;

import java.io.File;
import java.util.ArrayList;
import android.os.SystemProperties;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.GridView;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.data.MenuItem;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvConfigTypes.EnumChOpType;
import com.tv.framework.plugin.tvfuncs.PvrManager;
import com.tv.framework.plugin.tvfuncs.PvrManager.EnumPvrRecordStatus;
import com.tv.framework.plugin.tvfuncs.PvrManager.PVR_AB_LOOP_STATUS;
import com.tv.framework.plugin.tvfuncs.PvrManager.PVR_MODE;
import com.tv.framework.plugin.tvfuncs.PvrManager.PVR_PLAYBACK_SPEED;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ChannelEdit.ChannelEditDialog;
import com.tv.ui.ListView.TvListViewDialog;
import com.tv.ui.SourceInfo.SourceInfoDialog;
import com.tv.ui.Teletext.TvTeletextDialog;
import com.tv.ui.base.TvBaseDialog;

public class PvrTimeshitfDlg extends TvBaseDialog
{
	private String TAG = "PvrTimeshitfDlg";
	public PvrTimeshitfView pvrtimeshitfview;
	
	//PVR 相关的
	private Context context;
	
	private PVR_MODE curPvrMode = PVR_MODE.E_PVR_MODE_NONE;
	private ArrayList<String> usbDriverLabel = new ArrayList<String>();
	private ArrayList<String> usbDriverPath = new ArrayList<String>();
	private int usbDriverCount = 0;
	public String selectPath;
	public String selectLabel;
	public GridView usbGridView;
	private String recordDiskPath = null;
	private String recordDiskLable = null;
	private boolean isWatchRcodFilInRcoding = false;
	private Handler handler = new Handler();
	Dialog sellectDistdlg = null;
	boolean selectfalg = false;
	Dialog pvrDlg = null;
	
	private static final int INVALID_TIME = 0xFFFFFFFF;
	private PVR_AB_LOOP_STATUS setPvrABLoop = PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_NONE;
    private int pvrABLoopStartTime = INVALID_TIME;
    private int pvrABLoopEndTime = INVALID_TIME;
	
    private int looptime ;
    private int currentlooptime ;
    
    private int A_progress = 0;
    
    //记录是pause还是play值
    public boolean keycode_pause_play_flag = true;
    
    private boolean isBrowserCalled = false;
    private boolean homeflag = false;
    
    private boolean recordflag = false;
    
    private UsbReceiver usbReceiver;
    
    boolean showmenu_flag = true;
    private KeyEvent tPreviousEvent;
    
    private QuickKeyListener quickKeyListener;
    
    private PvrManager pvrMngIst = null;
    
	//录制盘的
	
	private class UsbReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			
			Log.e(TAG, "UsbReceiver action: " + action);
			
			if (action.equals(Intent.ACTION_MEDIA_MOUNTED)
					|| action.equals(Intent.ACTION_MEDIA_EJECT))
			{
			}
			else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED))
			{
				//当前录制的U盘被移出，停止录制并提示
				Uri uri = intent.getData();
				String path = uri.getPath();
				
				Log.e(TAG, "UsbReceiver onReceive() remove usb path: " + path);
				
				if(path.equals(recordDiskPath))
				{
					//提示U盘已经被移出
					
					//停止录制
					pvrtimeshitfview.OnHandleRemoveDist();
					
				}
			}
			else if(action.equals("stoppvr"))
			{
				Log.e(TAG, "stoppvr call pvrtimeshitfview.OnHandleRemoveDist()");
				pvrtimeshitfview.OnHandleRemoveDist();
				//liujian20150428
				String UDflag = SystemProperties.get("mstar.sky.PowerOnTimerBootMode");			
				Log.i(TAG,"mstar.sky.PowerOnTimerBootMode="+UDflag);
			   if(UDflag.equals("ON"))  			
			   {
					Log.e(TAG, "SendHotKey power off");
					SystemProperties.set("mstar.sky.PowerOnTimerBootMode", "OFF");
					Intent intent1 = new Intent("com.android.sky.SendHotKey");
					intent1.putExtra("specialKey", 26);
					TvContext.context.sendBroadcast(intent1);

			  }					
			}
		}
	}
	
	public void registerUSBDetector()
	{
		IntentFilter iFilter;
		iFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
		iFilter.addDataScheme("file");
		context.registerReceiver(usbReceiver, iFilter);
		
		iFilter = new IntentFilter(Intent.ACTION_MEDIA_EJECT);
		iFilter.addDataScheme("file");
		context.registerReceiver(usbReceiver, iFilter);
		
		iFilter = new IntentFilter(Intent.ACTION_MEDIA_UNMOUNTED);
		iFilter.addDataScheme("file");
		context.registerReceiver(usbReceiver, iFilter);
		
		IntentFilter stopPVRFilter = new IntentFilter();
		stopPVRFilter.addAction("stoppvr");
		context.registerReceiver(usbReceiver, stopPVRFilter);
	}
	
	private void doPVRRecord(boolean type)
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "doPVRRecord() pvrMngIst is null");
			
			return;
		}
		
		if (type)
		{
			Log.e(TAG, "doPVRRecord() enter start record");
			
			if(pvrMngIst.isRecordPaused())
			{
				pvrMngIst.resumeRecord();
				
				return;
			}
			
			if(!pvrMngIst.isRecording())
			{
				Log.e(TAG, "doPVRRecord() recordDiskPath: " + recordDiskPath);
				
				String fat = "FAT";
				String ntfs = "NTFS";
				if(recordDiskPath.isEmpty())
				{
					Log.e(TAG, "doPVRRecord() recordDiskPath is empty");
					
					return;
				}
				
				if(recordDiskLable.regionMatches(5, fat, 0, 3))
				{
					Log.e(TAG, "doPVRRecord() call setPVRParas() recordDiskPath: " + recordDiskPath);
					
					pvrMngIst.setPVRParas(recordDiskPath, (short) 2);
				}
				else
				{
					//提示格式不支持
					pvrtimeshitfview.OnHandleUnSupportDist();
					return;
				}
			}
			else
			{
				
			}
			
			if(!pvrMngIst.isRecording())
			{
				Log.e(TAG, "doPVRRecord() call startRecord()");
				
				EnumPvrRecordStatus pvrStatus = pvrMngIst.startRecord();
				if(EnumPvrRecordStatus.E_SUCCESS == pvrStatus)
				{
					Log.e(TAG, "doPVRRecord() call startRecord() sucess");
					
					curPvrMode = PVR_MODE.E_PVR_MODE_RECORD;
				}
				else
				{
					Log.e(TAG, "doPVRRecord() call startRecord() false");
					
					//提示时移失败
					pvrtimeshitfview.OnRecordFalse(curPvrMode, pvrStatus);
				}
			}
		}
		else
		{
			Log.e(TAG, "doPVRRecord() enter stop record");
			
			//停止动画
			if(pvrMngIst.isPlaybacking())
			{
				Log.e(TAG, "doPVRRecord() call stopPlayback()");
				
				pvrMngIst.stopPlayback();
			}
			
			pvrMngIst.stopRecord();
		}
	}
	
	private void doPVRTimeShift(boolean type)
	{
		String msg = "";
		
		Log.e(TAG, "enter doPVRTimeShift()");
		
		if(null == pvrMngIst)
		{
			Log.e(TAG, "doPVRTimeShift() pvrMngIst is null");
			
			return;
		}
		
		pvrMngIst.stepInPlayback();
		
		if(type)
		{
			if(pvrMngIst.isRecordPaused())
			{
				pvrMngIst.resumeRecord();
				
				return;
			}
			
			
			pvrMngIst.setPVRParas(recordDiskPath, (short) 2);
			
			try 
			{
				Thread.sleep(100);
			} 
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			EnumPvrRecordStatus pvrStatus = pvrMngIst.startTimeShiftRecord();
		
			if(EnumPvrRecordStatus.E_SUCCESS == pvrStatus)
			{
				Log.e(TAG, "doPVRTimeShift() success");
				
				curPvrMode = PVR_MODE.E_PVR_MODE_TIME_SHIFT;
			}
			else
			{
				//提示时移失败
				Log.e(TAG, "doPVRTimeShift() false");
				pvrtimeshitfview.OnRecordFalse(curPvrMode, pvrStatus);
			}
			
		}
		else
		{
			//关闭动画
			if(pvrMngIst.isPlaybacking())
			{
				pvrMngIst.stopPlayback();
			}
			
			pvrMngIst.stopTimeShiftRecord();
		}
	}
	
	private void doBrowserPlayback()
	{
		if(null == pvrMngIst)
		{
			return;
		}
		
		String pvrFileName = pvrMngIst.getCurPlaybackingFileName();
		if (pvrFileName == null)
		{
			return;
		}
		
		String pvrFileServiceName = null;
		String pvrFileEventName = null;
		pvrFileServiceName = pvrMngIst.getFileServiceName(pvrFileName);
		pvrFileEventName = pvrMngIst.getFileEventName(pvrFileName);
		pvrtimeshitfview.SetServiceNameText(pvrFileServiceName);
		pvrtimeshitfview.SetEventNameText(pvrFileEventName);
		
		int totalTime = pvrMngIst.getRecordedFileDurationTime(pvrFileName);
		String totalTimeString = pvrMngIst.getTimeString(totalTime);
		pvrtimeshitfview.SetTotalRecordTimeText(totalTimeString);
		pvrtimeshitfview.SetRecordProgressbarMax(totalTime);
	}
	
    private void updateUSBInfo()
    {
    	if(null == pvrMngIst)
		{
			return;
		}
    	
    	int percent = pvrMngIst.usbDistUsePercent(recordDiskPath);
    	String usbName = pvrMngIst.getVolumeLabel(recordDiskPath);
    	pvrtimeshitfview.UpdateUsbInfo(percent,usbName);
    }
    
	private class PlayBackProgress extends Thread 
	{
		@Override
    	public void run()
		{
			if(null == pvrMngIst)
			{
				Log.e(TAG, "PlayBackProgress run() pvrMngIst is null");
				
				return;
			}
			
			super.run();
			
			if(pvrMngIst.isTimeShiftRecording() ||
        			pvrMngIst.isRecording())
			{
				new usbInfoUpdate().start();
			}
			
			String currentTimeString = pvrMngIst.getTimeString(0);
			pvrtimeshitfview.SetCurrentTimeText(currentTimeString);
			String ServiceName = pvrMngIst.GetCurrentServiceName();
			pvrtimeshitfview.SetServiceNameText(ServiceName);
			
			while((pvrMngIst.isPlaybacking() ||
        			pvrMngIst.isRecording()))
			{
				final int currentTime = pvrMngIst.getCurPlaybackTimeInSecond();
				final int total = (pvrMngIst.isRecording()&& !isWatchRcodFilInRcoding) ?
						pvrMngIst.getCurRecordTimeInSecond() : 
						pvrMngIst.getRecordedFileDurationTime(
						pvrMngIst.getCurPlaybackingFileName());
						
				handler.post(new Runnable()
				{
					@Override
					public void run()
					{
						pvrtimeshitfview.SetRecordProgressbarMax(total);
						
						recordflag = (!recordflag);
						pvrtimeshitfview.SetRecordStatusLayoutVisible(recordflag);
						
						String eventName = pvrMngIst.GetCurrentEventName();
						pvrtimeshitfview.SetEventNameText(eventName);
						
						if(setPvrABLoop == PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_AB)
						{
							currentlooptime = pvrMngIst.getCurPlaybackTimeInSecond() - pvrABLoopStartTime; 
							pvrtimeshitfview.SetRecordTextProgressbar("PVR", 0);
							String currentTimeString = pvrMngIst.getTimeString(currentTime);
							pvrtimeshitfview.SetCurrentTimeText(currentTimeString);
							pvrtimeshitfview.SetABLoopProgress(currentlooptime);
						}
						else
						{
							PVR_PLAYBACK_SPEED curPlayBackSpeed = PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_0X;
							curPlayBackSpeed = pvrMngIst.getPlaybackSpeed();
							if((PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X != curPlayBackSpeed) &&
									(currentTime <= 0 || currentTime >= total))
							{
								pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
								
								if(-1 != pvrtimeshitfview.revpicture)
								{
									pvrtimeshitfview.revpicture = -1;
									pvrtimeshitfview.setPlayer_revImagefocus();
								}
								
								if(-1 != pvrtimeshitfview.ffpicture)
								{
									pvrtimeshitfview.ffpicture = -1;
									pvrtimeshitfview.setPlayer_ffImagefocus();
								}
							}
							else
							{
								pvrtimeshitfview.SetRecordTextProgressbar("PVR", currentTime);
								String currentTimeString = pvrMngIst.getTimeString(currentTime);
								pvrtimeshitfview.SetCurrentTimeText(currentTimeString);
							}
						}
						
						String totalTimeString = pvrMngIst.getTimeString(total);
						pvrtimeshitfview.SetTotalRecordTimeText(totalTimeString);
						if (setPvrABLoop == PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_A)
						{
							int x = (pvrtimeshitfview.GetRecordProgress() - A_progress) * pvrtimeshitfview.GetRecordProgressWidth() / (pvrtimeshitfview.GetRecordProgressMax() == 0 ? 1 : pvrtimeshitfview.GetRecordProgressMax());
							looptime++;
							pvrtimeshitfview.SetABLoopProgressWidth(x);
							pvrtimeshitfview.SetABLoopProgressLayoutParams();
						}
						
//						if ((curPvrMode != PVR_MODE.E_PVR_MODE_TIME_SHIFT) &&
//                    			((pvrMngIst.isPlaybacking() && 
//                    			(((currentTime + 2) >= total)))||
//                    			(pvrMngIst.isPlaybacking() && 
//                    			pvrMngIst.isFastBackPlaying() &&
//                    			currentTime <= 2)))
//						{
//							pvrMngIst.stopPlaybackLoop();
//							pvrMngIst.stopPlayback();
//							if((curPvrMode != PVR_MODE.E_PVR_MODE_TIME_SHIFT) &&
//									pvrMngIst.isRecording())
//							{
//								pvrtimeshitfview.SetRecordTextProgressbar("PVR", 0);
//								String currentTimeString = pvrMngIst.getTimeString(0);
//								pvrtimeshitfview.SetCurrentTimeText(currentTimeString);
//								curPvrMode = PVR_MODE.E_PVR_MODE_RECORD;
//							}
//							
//							if(-1 != pvrtimeshitfview.revpicture)
//							{
//								pvrtimeshitfview.revpicture = -1;
//								pvrtimeshitfview.setPlayer_revImageunfocus();
//							}
//							
//							if(-1 != pvrtimeshitfview.ffpicture)
//							{
//								pvrtimeshitfview.ffpicture = -1;
//								pvrtimeshitfview.setPlayer_ffImageunfocus();
//							}
//							
//							keycode_pause_play_flag = true;
//							pvrtimeshitfview.paly_pause_flag = true;
//							pvrtimeshitfview.SetPlayer_PauseImage_requestFocus();
//							pvrtimeshitfview.setPlayer_PauseImage_playfocus();
//							//pvrtimeshitfview.setPlayer_ABRepeatImageSetAFocus();
//						}
						
					}
				});
				
				try 
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class BrowserCalledPlayBackProgress extends Thread
	{
		@Override
    	public void run() 
    	{
			if(null == pvrMngIst)
			{
				Log.e(TAG, "BrowserCalledPlayBackProgress run() pvrMngIst is null");
				
				return;
			}
			
    		super.run();
    		
    		while(pvrMngIst.isPlaybacking()) 
//    				&&pvrMngIst.isRecording()))
    		{
    			final int currentTime = pvrMngIst.getCurPlaybackTimeInSecond();
    			String filename = pvrMngIst.getCurPlaybackingFileName();
    			final int total = pvrMngIst.getRecordedFileDurationTime(filename);
    			
    			handler.post(new Runnable()
                {
                	@Override
                    public void run()
                    {
                		if(setPvrABLoop == PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_AB)
                		{
                			currentlooptime = pvrMngIst.getCurPlaybackTimeInSecond() - pvrABLoopStartTime; 
    						pvrtimeshitfview.SetRecordTextProgressbar("PVR", 0);
    						String currentTimeString = pvrMngIst.getTimeString(currentTime);
    						pvrtimeshitfview.SetCurrentTimeText(currentTimeString);
    						pvrtimeshitfview.SetABLoopProgress(currentlooptime);
                		}
                		else
						{
							PVR_PLAYBACK_SPEED curPlayBackSpeed = PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_0X;
							curPlayBackSpeed = pvrMngIst.getPlaybackSpeed();
							if((PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X != curPlayBackSpeed) &&
									(currentTime <= 0 /* || currentTime >= total */))
							{
								//重新播放
								pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
								
								if(-1 != pvrtimeshitfview.revpicture)
								{
									pvrtimeshitfview.revpicture = -1;
									pvrtimeshitfview.setPlayer_revImageunfocus();
								}
								
								if(-1 != pvrtimeshitfview.ffpicture)
								{
									pvrtimeshitfview.ffpicture = -1;
									pvrtimeshitfview.setPlayer_ffImageunfocus();
								}
							}
							else
							{
								pvrtimeshitfview.SetRecordTextProgressbar("PVR", currentTime);
								String currentTimeString = pvrMngIst.getTimeString(currentTime);
								pvrtimeshitfview.SetCurrentTimeText(currentTimeString);
							}
							
							if (setPvrABLoop == PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_A)
							{
								int x = (pvrtimeshitfview.GetRecordProgress() - A_progress) * pvrtimeshitfview.GetRecordProgressWidth() / (pvrtimeshitfview.GetRecordProgressMax() == 0 ? 1 : pvrtimeshitfview.GetRecordProgressMax());
								looptime++;
								pvrtimeshitfview.SetABLoopProgressWidth(x);
								pvrtimeshitfview.SetABLoopProgressLayoutParams();
							}
							
							if ((pvrMngIst.isPlaybacking() && 
									(currentTime >= total)))
							{
								if ((pvrABLoopStartTime == INVALID_TIME) && 
					            		(INVALID_TIME == pvrABLoopEndTime))
								{
									pvrMngIst.stopPlayback();
									//启动录制列表对话框
								}
								dismissUI();
							}
						}
                		
                    }
                });
    		}
    	}
	}
	
    private class usbInfoUpdate extends Thread 
    {
        @Override
        public void run()
        {
        	if(null == pvrMngIst)
			{
				Log.e(TAG, "usbInfoUpdate run() pvrMngIst is null");
				
				return;
			}
        	
        	super.run();
        	
        	while((pvrMngIst.isPlaybacking() ||
        			pvrMngIst.isRecording()))
        	{
        		handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        updateUSBInfo();
                    }
                });
        		
        		try 
        		{
					Thread.sleep(5000);
				} 
        		catch (InterruptedException e) 
        		{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
    }

	//end 录制盘的选择
	//PVR 相关的

	
	public PvrTimeshitfDlg() 
	{
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_PVR_TIMESHITH_DLG);
		context = TvContext.context;
		// TODO Auto-generated constructor stub
		pvrtimeshitfview = new PvrTimeshitfView(TvContext.context);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(pvrtimeshitfview);
		pvrtimeshitfview.setParentDialog(this);
		
		this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
		
		usbReceiver =  new UsbReceiver();
		registerUSBDetector();
		
		pvrMngIst = TvPluginControler.getInstance().getPvrManager();
	}
	
	private void onkeyPlay()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "onkeyPlay() pvrMngIst is null");
			
			return;
		}
		
		if(curPvrMode == PVR_MODE.E_PVR_MODE_NONE)
        {
            return;
        }
		
		//如果处于播放且为快进快退，则恢复正常播放，还原快进快退图标
		if((-1 != pvrtimeshitfview.revpicture) || (-1 != pvrtimeshitfview.ffpicture))
		{
			if(pvrMngIst.isPlaybacking())
			{
				pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
			}
		}
		
		if(-1 != pvrtimeshitfview.revpicture)
		{
			pvrtimeshitfview.revpicture = -1;
			pvrtimeshitfview.setPlayer_revImageunfocus();
		}
		
		if(-1 != pvrtimeshitfview.ffpicture)
		{
			pvrtimeshitfview.ffpicture = -1;
			pvrtimeshitfview.setPlayer_ffImageunfocus();
		}
		
		//针对属于时移、录制、播放等状态下进行处理
		if(curPvrMode == PVR_MODE.E_PVR_MODE_RECORD)
		{
			if(pvrMngIst.isPlaybacking())
			{
				switch(pvrMngIst.getPlaybackSpeed())
				{
				case E_PVR_PLAYBACK_SPEED_0X:
				{
					pvrMngIst.resumePlayback();
					
					break;
				}
				case E_PVR_PLAYBACK_SPEED_STEP_IN:
				{
					pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
					
					break;
				}
				case E_PVR_PLAYBACK_SPEED_2XFF:
                case E_PVR_PLAYBACK_SPEED_4XFF:
                case E_PVR_PLAYBACK_SPEED_8XFF:
                case E_PVR_PLAYBACK_SPEED_16XFF:
                case E_PVR_PLAYBACK_SPEED_32XFF:
                case E_PVR_PLAYBACK_SPEED_1XFB:
                case E_PVR_PLAYBACK_SPEED_2XFB:
                case E_PVR_PLAYBACK_SPEED_4XFB:
                case E_PVR_PLAYBACK_SPEED_8XFB:
                case E_PVR_PLAYBACK_SPEED_16XFB:
                case E_PVR_PLAYBACK_SPEED_32XFB:
                case E_PVR_PLAYBACK_SPEED_FF_1_2X:
                case E_PVR_PLAYBACK_SPEED_FF_1_4X:
                case E_PVR_PLAYBACK_SPEED_FF_1_8X:
                case E_PVR_PLAYBACK_SPEED_FF_1_16X:
                case E_PVR_PLAYBACK_SPEED_FF_1_32X:
                {
                	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
					
					break;
                }
                default:
                {
                	
                }
				}
			}
			else
			{
				String strFileName = pvrMngIst.getCurRecordingFileName();
				if(!pvrMngIst.startPlayback(strFileName))
				{
					
					return;
				}
				curPvrMode = PVR_MODE.E_PVR_MODE_PLAYBACK;
			}
		}
		else if(curPvrMode == PVR_MODE.E_PVR_MODE_TIME_SHIFT)
		{
			if(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_INVALID == 
					pvrMngIst.getPlaybackSpeed())
			{
				if(pvrMngIst.isAlwaysTimeShiftPlaybackPaused())
				{
					if(0 != pvrMngIst.startAlwaysTimeShiftPlayback())
					{
						return;
					}
				}
				else
				{
					if(pvrMngIst.startTimeShiftPlayback())
					{
						return;
					}
				}
			}
			else
			{
				switch(pvrMngIst.getPlaybackSpeed())
				{
				case E_PVR_PLAYBACK_SPEED_0X:
				{
					pvrMngIst.resumePlayback();
					
					break;
				}
				case E_PVR_PLAYBACK_SPEED_STEP_IN:
				{
					pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
					
					break;
				}
				case E_PVR_PLAYBACK_SPEED_2XFF:
                case E_PVR_PLAYBACK_SPEED_4XFF:
                case E_PVR_PLAYBACK_SPEED_8XFF:
                case E_PVR_PLAYBACK_SPEED_16XFF:
                case E_PVR_PLAYBACK_SPEED_32XFF:
                case E_PVR_PLAYBACK_SPEED_1XFB:
                case E_PVR_PLAYBACK_SPEED_2XFB:
                case E_PVR_PLAYBACK_SPEED_4XFB:
                case E_PVR_PLAYBACK_SPEED_8XFB:
                case E_PVR_PLAYBACK_SPEED_16XFB:
                case E_PVR_PLAYBACK_SPEED_32XFB:
                case E_PVR_PLAYBACK_SPEED_FF_1_2X:
                case E_PVR_PLAYBACK_SPEED_FF_1_4X:
                case E_PVR_PLAYBACK_SPEED_FF_1_8X:
                case E_PVR_PLAYBACK_SPEED_FF_1_16X:
                case E_PVR_PLAYBACK_SPEED_FF_1_32X:
                {
                	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
					
					break;
                }
                default:
                {
                	
                }
				}
			}
		}
		else if(curPvrMode == PVR_MODE.E_PVR_MODE_PLAYBACK)
		{
			switch(pvrMngIst.getPlaybackSpeed())
			{
			case E_PVR_PLAYBACK_SPEED_0X:
			{
				pvrMngIst.resumePlayback();
				
				break;
			}
			case E_PVR_PLAYBACK_SPEED_STEP_IN:
			{
				pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
				
				break;
			}
			case E_PVR_PLAYBACK_SPEED_2XFF:
            case E_PVR_PLAYBACK_SPEED_4XFF:
            case E_PVR_PLAYBACK_SPEED_8XFF:
            case E_PVR_PLAYBACK_SPEED_16XFF:
            case E_PVR_PLAYBACK_SPEED_32XFF:
            case E_PVR_PLAYBACK_SPEED_1XFB:
            case E_PVR_PLAYBACK_SPEED_2XFB:
            case E_PVR_PLAYBACK_SPEED_4XFB:
            case E_PVR_PLAYBACK_SPEED_8XFB:
            case E_PVR_PLAYBACK_SPEED_16XFB:
            case E_PVR_PLAYBACK_SPEED_32XFB:
            case E_PVR_PLAYBACK_SPEED_FF_1_2X:
            case E_PVR_PLAYBACK_SPEED_FF_1_4X:
            case E_PVR_PLAYBACK_SPEED_FF_1_8X:
            case E_PVR_PLAYBACK_SPEED_FF_1_16X:
            case E_PVR_PLAYBACK_SPEED_FF_1_32X:
            {
            	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
				
				break;
            }
            default:
            {
            	break;
            }
			}
		}
		
		//处理菜单项的禁用等
	}
	
	private void onkeyPause()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "onkeyPause() pvrMngIst is null");
			
			return;
		}
		
		if(curPvrMode == PVR_MODE.E_PVR_MODE_NONE)
        {
            return;
        }
		
		//如果处于播放且为快进快退，则恢复正常播放，还原快进快退图标
		if((-1 != pvrtimeshitfview.revpicture) || (-1 != pvrtimeshitfview.ffpicture))
		{
			if(pvrMngIst.isPlaybacking())
			{
				pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
			}
		}
		
		if(-1 != pvrtimeshitfview.revpicture)
		{
			pvrtimeshitfview.revpicture = -1;
			pvrtimeshitfview.setPlayer_revImageunfocus();
		}
		
		if(-1 != pvrtimeshitfview.ffpicture)
		{
			pvrtimeshitfview.ffpicture = -1;
			pvrtimeshitfview.setPlayer_ffImageunfocus();
		}
		
		if(pvrMngIst.isPlaybacking())
		{
			if(PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_NONE != setPvrABLoop)
			{
				setPvrABLoop = PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_NONE;
				pvrMngIst.stopPlaybackLoop();
				pvrtimeshitfview.SetABLoopProgressWidth(0);
				pvrtimeshitfview.SetABLoopProgressbarMax(0);
	            pvrtimeshitfview.SetABLoopProgressLayoutParams();
	            pvrtimeshitfview.SetABLoopProgressGone();
			}
			
			pvrMngIst.stepInPlayback();
		}
	}
	
	public void onClickPlayPause()
	{
		Log.e(TAG, "enter onClickPlayPause().keycode_pause_play_flag = " + keycode_pause_play_flag);
		
		if(null == pvrMngIst)
		{
			Log.e(TAG, "onClickPlayPause() pvrMngIst is null");
			
			return;
		}
		
		if(keycode_pause_play_flag)
		{
			onkeyPlay();
			keycode_pause_play_flag = false;
			pvrtimeshitfview.paly_pause_flag = false;
			
			pvrtimeshitfview.setPlayer_PauseImage_pausefocus();
		}
		else
		{
			onkeyPause();
			keycode_pause_play_flag = true;
			pvrtimeshitfview.paly_pause_flag = true;
			
			pvrtimeshitfview.setPlayer_PauseImage_playfocus();
		}
	}
	
	public void stopAbLoopPlayback()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "stopAbLoopPlayback() pvrMngIst is null");
			
			return;
		}
		
		pvrABLoopStartTime = pvrABLoopEndTime = INVALID_TIME;
		setPvrABLoop = PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_NONE;
		pvrMngIst.stopPlaybackLoop();
		pvrtimeshitfview.SetABLoopProgressWidth(0);
		pvrtimeshitfview.SetABLoopProgressbarMax(0);
        pvrtimeshitfview.SetABLoopProgressLayoutParams();
        pvrtimeshitfview.SetABLoopProgressGone();
	}
	
	public void onClickStop()
	{
		if(curPvrMode == PVR_MODE.E_PVR_MODE_NONE)
        {  
            return;
        }
		
		if(null == pvrMngIst)
		{
			Log.e(TAG, "onClickStop() pvrMngIst is null");
			
			return;
		}
		
		//针对处于快进快退时进行处理
		if((-1 != pvrtimeshitfview.revpicture) || (-1 != pvrtimeshitfview.ffpicture))
		{
			if(pvrMngIst.isPlaybacking())
			{
				pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
			}
		}
		
		if(-1 != pvrtimeshitfview.revpicture)
		{
			pvrtimeshitfview.revpicture = -1;
			pvrtimeshitfview.setPlayer_revImageunfocus();
		}
		
		if(-1 != pvrtimeshitfview.ffpicture)
		{
			pvrtimeshitfview.ffpicture = -1;
			pvrtimeshitfview.setPlayer_ffImageunfocus();
		}
		
		if(isBrowserCalled && pvrMngIst.isPlaybacking())
		{
			if ((pvrABLoopStartTime != INVALID_TIME) && 
            		(INVALID_TIME != pvrABLoopEndTime))
			{
				stopAbLoopPlayback();
	            
	            try 
	            {
	            	Thread.sleep(300);           	
	            } 
	            catch (InterruptedException e) 
	            {
	            	// TODO Auto-generated catch block
	            	e.printStackTrace();         	
	            }
			}
			
			pvrMngIst.stopPlayback();
		}
		
		if (curPvrMode == PVR_MODE.E_PVR_MODE_PLAYBACK ||
				curPvrMode == PVR_MODE.E_PVR_MODE_RECORD || 
				curPvrMode == PVR_MODE.E_PVR_MODE_TIME_SHIFT)
		{
			if(pvrMngIst.isPlaybacking())
			{
				if ((pvrABLoopStartTime != INVALID_TIME) && 
                		(INVALID_TIME != pvrABLoopEndTime))
                {
					stopAbLoopPlayback();
					keycode_pause_play_flag = true;
					pvrtimeshitfview.paly_pause_flag = true;
					pvrtimeshitfview.setPlayer_PauseImage_playunfocus();
					pvrtimeshitfview.setPlayer_ABRepeatImageSetAUnFocus();
					pvrtimeshitfview.SetNotPlaybackUnfocus();
					
					return;
                }
				else if(setPvrABLoop == PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_A)
                { 
					stopAbLoopPlayback();
    	            pvrMngIst.stopPlayback();
    	            
    	            keycode_pause_play_flag = true;
    	            pvrtimeshitfview.paly_pause_flag = true;
    	            pvrtimeshitfview.setPlayer_PauseImage_playunfocus();
    	            pvrtimeshitfview.setPlayer_ABRepeatImageSetAUnFocus();
    	            pvrtimeshitfview.SetNotPlaybackUnfocus();
    	            
    	            return;
                }
				else if(isBrowserCalled) //仅仅是播放
				{
					return;
				}
			}
		}
		
		if (curPvrMode == PVR_MODE.E_PVR_MODE_RECORD)
		{
			if(pvrMngIst.isPlaybacking())
			{
				if ((pvrABLoopStartTime != INVALID_TIME) && 
	            		(INVALID_TIME != pvrABLoopEndTime))
				{
					stopAbLoopPlayback();
				}
				
				pvrMngIst.stopPlayback();
				keycode_pause_play_flag = true;
				pvrtimeshitfview.paly_pause_flag = true;
				pvrtimeshitfview.SetRecordTextProgressbar("PVR", 0);
				String currentTimeString = pvrMngIst.getTimeString(0);
				pvrtimeshitfview.SetCurrentTimeText(currentTimeString);
				pvrtimeshitfview.setPlayer_PauseImage_playunfocus();
				pvrtimeshitfview.setPlayer_ABRepeatImageSetAUnFocus();
				pvrtimeshitfview.SetNotPlaybackUnfocus();
				
				return;
			}
			
			if(pvrMngIst.isRecording())
			{
				//弹出提示停止录制对话框
				onHandleExit();
				return;
			}
		}
		else if (curPvrMode == PVR_MODE.E_PVR_MODE_TIME_SHIFT)
		{
			//弹出提示停止时移对话框
			onHandleExit();
			return;
		}
		else if (curPvrMode == PVR_MODE.E_PVR_MODE_PLAYBACK)
		{
			if(pvrMngIst.isPlaybacking())
			{
				if ((pvrABLoopStartTime != INVALID_TIME) && 
	            		(INVALID_TIME != pvrABLoopEndTime))
				{
					stopAbLoopPlayback();
				}
				
				pvrMngIst.stopPlayback();
				pvrtimeshitfview.SetRecordTextProgressbar("PVR", 0);
				String currentTimeString = pvrMngIst.getTimeString(0);
				pvrtimeshitfview.SetCurrentTimeText(currentTimeString);
				keycode_pause_play_flag = true;
				pvrtimeshitfview.paly_pause_flag = true;
				pvrtimeshitfview.setPlayer_PauseImage_playunfocus();
				pvrtimeshitfview.setPlayer_ABRepeatImageSetAUnFocus();
				pvrtimeshitfview.SetNotPlaybackUnfocus();
				
				//this.OnHandleExitokButton();				
				//this.dismissUI();
			}
			
			if(pvrMngIst.isRecording())
			{
				pvrABLoopStartTime = pvrABLoopEndTime = INVALID_TIME;
				
				if (curPvrMode == PVR_MODE.E_PVR_MODE_PLAYBACK)
                {
                    curPvrMode = PVR_MODE.E_PVR_MODE_RECORD;
                }
				
				if(homeflag)
				{
					//home处理退出录制
				}
				else
				{
					return;
				}
			}
			else
			{
				if (isBrowserCalled && !homeflag)
				{
					this.OnHandleExitokButton();				
					this.dismissUI();
					return;
				}
			}
			
			return;
		}
		else
		{
			if (isBrowserCalled && !homeflag)
			{
				//启动返回录制列表
				
				return;
			}
		}
		
		return;
	}
	
	public void onClickPlayABLoop()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "onClickPlayABLoop() pvrMngIst is null");
			
			return;
		}
		
		if((curPvrMode == PVR_MODE.E_PVR_MODE_NONE) || 
				(!pvrMngIst.isPlaybacking()))
		{
			return;
		}
		
		if(-1 == pvrtimeshitfview.ablooppicture)
        {
			pvrtimeshitfview.ablooppicture = 0;
        }
		
		if((-1 != pvrtimeshitfview.revpicture) || (-1 != pvrtimeshitfview.ffpicture))
		{
			if(pvrMngIst.isPlaybacking())
			{
				pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
			}
		}
		
		if(-1 != pvrtimeshitfview.revpicture)
		{
			pvrtimeshitfview.revpicture = -1;
			pvrtimeshitfview.setPlayer_revImageunfocus();
		}
		
		if(-1 != pvrtimeshitfview.ffpicture)
		{
			pvrtimeshitfview.ffpicture = -1;
			pvrtimeshitfview.setPlayer_ffImageunfocus();
		}
		
		if(pvrMngIst.isPlaybacking())
		{
			PVR_PLAYBACK_SPEED speed = null;
			speed = pvrMngIst.getPlaybackSpeed();
			if(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X != speed)
			{
				pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
			}
			
			speed = pvrMngIst.getPlaybackSpeed();
			if(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X == speed)
			{
				OnClick_ABLoop();
			}
		}
	}
	
	private void OnClick_ABLoop()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "OnClick_ABLoop() pvrMngIst is null");
			
			return;
		}
		
		if (setPvrABLoop == PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_NONE)
		{
			pvrABLoopStartTime = pvrMngIst.getCurPlaybackTimeInSecond();
			setPvrABLoop = PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_A;
			pvrtimeshitfview.setPlayer_ABRepeatImageSetBFocus();
			A_progress = pvrtimeshitfview.GetRecordProgress();
			int x = pvrtimeshitfview.GetRecordProgressWidth() * A_progress / (pvrtimeshitfview.GetRecordProgressMax() == 0 ? 1 : pvrtimeshitfview.GetRecordProgressMax());
			pvrtimeshitfview.SetABLoopProgressLeftMargin(x);
			looptime = 0;
			pvrtimeshitfview.SetABLoopProgressbarMax(looptime);
			pvrtimeshitfview.SetABLoopProgress(looptime);
			pvrtimeshitfview.SetABLoopProgressLayoutParams();
			pvrtimeshitfview.SetABLoopProgressVisibility();
		}
		else if (setPvrABLoop == PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_A)
		{
			pvrABLoopEndTime = pvrMngIst.getCurPlaybackTimeInSecond();
			if((pvrABLoopEndTime - pvrABLoopStartTime) <= 2)
			{				
				return;
			}
			
			pvrMngIst.startPlaybackLoop(pvrABLoopStartTime, pvrABLoopEndTime);
            setPvrABLoop = PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_AB;
            pvrtimeshitfview.setPlayer_ABRepeatImageSetExitABFocus();
            int x = (pvrtimeshitfview.GetRecordProgressMax() - A_progress) * pvrtimeshitfview.GetRecordProgressWidth() / (pvrtimeshitfview.GetRecordProgressMax() == 0 ? 1 : pvrtimeshitfview.GetRecordProgressMax());
            pvrtimeshitfview.SetABLoopProgressWidth(x);
            pvrtimeshitfview.SetABLoopProgressLayoutParams();
            looptime++;
            pvrtimeshitfview.SetABLoopProgressbarMax(looptime);
            currentlooptime = 0;
		}
		else
		{
			pvrMngIst.stopPlaybackLoop();
			setPvrABLoop = PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_NONE;
			pvrtimeshitfview.setPlayer_ABRepeatImageSetAFocusAb3();
			pvrtimeshitfview.SetABLoopProgressWidth(0);
			pvrtimeshitfview.SetABLoopProgressbarMax(0);
			pvrtimeshitfview.SetABLoopProgressGone();
			
			pvrABLoopStartTime = INVALID_TIME;
            pvrABLoopEndTime = INVALID_TIME;
		}
	}
	
	public void OnkeyPlayRev()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "OnkeyPlayRev() pvrMngIst is null");
			
			return;
		}
		
		if(pvrMngIst.isPlaybacking())
		{
			//如果当前是属于快进，则先处理快进为正常播放
			if(-1 != pvrtimeshitfview.ffpicture)
			{
				pvrtimeshitfview.ffpicture = -1;
				pvrtimeshitfview.setPlayer_ffImageunfocus();
				pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
			}
			
			PVR_PLAYBACK_SPEED speed = null;
			speed = pvrMngIst.getPlaybackSpeed();
			switch(speed)
			{
			case E_PVR_PLAYBACK_SPEED_1XFB:
            case E_PVR_PLAYBACK_SPEED_1X:
            {
            	while( PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_2XFB != pvrMngIst.getPlaybackSpeed())
            	{
            		pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_2XFB);
            		try 
	        		{
	    				Thread.sleep(500);
	    			}
	        		catch (InterruptedException e) 
	        		{
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
            	}
            	
            	pvrtimeshitfview.revpicture = 1;
            	pvrtimeshitfview.setPlayer_revImage2Xfocus();
            }
            
            break;
            
            case E_PVR_PLAYBACK_SPEED_2XFB:
            {
            	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_4XFB);
            	pvrtimeshitfview.revpicture = 2;
            	pvrtimeshitfview.setPlayer_revImage4Xfocus();
            }
            
            break;
            
            case E_PVR_PLAYBACK_SPEED_4XFB:
            {
            	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_8XFB);
            	pvrtimeshitfview.revpicture = 3;
            	pvrtimeshitfview.setPlayer_revImage8Xfocus();
            }
            
            break;
            
            case E_PVR_PLAYBACK_SPEED_8XFB:
            {
            	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_16XFB);
            	pvrtimeshitfview.revpicture = 4;
            	pvrtimeshitfview.setPlayer_revImage16Xfocus();
            }
            
            break;
            
            case E_PVR_PLAYBACK_SPEED_16XFB:
            {
            	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
            	pvrtimeshitfview.revpicture = -1;
            	pvrtimeshitfview.setPlayer_revImagefocus();
            }
            
            break;
            
            default:
            {
            	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
            	pvrtimeshitfview.revpicture = -1;
            	pvrtimeshitfview.setPlayer_revImagefocus();
            }
            
            break;
            
			}
		}
	}
	
	public void OnkeyPlayFF()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "OnkeyPlayFF() pvrMngIst is null");
			
			return;
		}
		
		if(pvrMngIst.isPlaybacking())
		{
			//如果当前是属于快退，则先处理快退为正常播放
			if(-1 != pvrtimeshitfview.revpicture)
			{
				pvrtimeshitfview.revpicture = -1;
				pvrtimeshitfview.setPlayer_revImageunfocus();
				pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
			}
			
			PVR_PLAYBACK_SPEED speed = null;
			speed = pvrMngIst.getPlaybackSpeed();
			switch(speed)
			{
            case E_PVR_PLAYBACK_SPEED_1X:
            {
            	while( PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_2XFF != pvrMngIst.getPlaybackSpeed())
            	{
            		pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_2XFF);
            		try 
	        		{
	    				Thread.sleep(500);
	    			}
	        		catch (InterruptedException e) 
	        		{
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
            	}
            	
            	pvrtimeshitfview.ffpicture = 1;
            	pvrtimeshitfview.setPlayer_ffImage2Xfocus();
            }
            
            break;
            
            case E_PVR_PLAYBACK_SPEED_2XFF:
            {
            	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_4XFF);
            	pvrtimeshitfview.ffpicture = 2;
            	pvrtimeshitfview.setPlayer_ffImage4Xfocus();
            }
            
            break;
            
            case E_PVR_PLAYBACK_SPEED_4XFF:
            {
            	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_8XFF);
            	pvrtimeshitfview.ffpicture = 3;
            	pvrtimeshitfview.setPlayer_ffImage8Xfocus();
            }
            
            break;
            
            case E_PVR_PLAYBACK_SPEED_8XFF:
            {
            	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_16XFF);
            	pvrtimeshitfview.ffpicture = 4;
            	pvrtimeshitfview.setPlayer_ffImage16Xfocus();
            }
            
            break;
            
            case E_PVR_PLAYBACK_SPEED_16XFF:
            {
            	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
            	pvrtimeshitfview.ffpicture = -1;
            	pvrtimeshitfview.setPlayer_ffImagefocus();
            }
            
            break;
            
            default:
            {
            	pvrMngIst.setPlaybackSpeed(PVR_PLAYBACK_SPEED.E_PVR_PLAYBACK_SPEED_1X);
            	pvrtimeshitfview.ffpicture = -1;
            	pvrtimeshitfview.setPlayer_ffImagefocus();
            }
            
            break;
            
			}
		}
	}
	
	public void onHandleExit()
	{
		pvrtimeshitfview.OnHandleExit(curPvrMode);
	}
	
	public void OnHandleExitokButton()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "OnHandleExitokButton() pvrMngIst is null");
			
			return;
		}
		
		if(pvrMngIst.isPlaybacking())
		{
			if ((pvrABLoopStartTime != INVALID_TIME) && 
            		(INVALID_TIME != pvrABLoopEndTime))
			{
				stopAbLoopPlayback();
	            
	            try 
	            {
	            	Thread.sleep(300);           	
	            } 
	            catch (InterruptedException e) 
	            {
	            	// TODO Auto-generated catch block
	            	e.printStackTrace();         	
	            }
			}
			
			pvrMngIst.stopPlayback();
		}
		
		if(pvrMngIst.isTimeShiftRecording())
		{
			pvrMngIst.stopTimeShift();
			
			return;
		}
		
		if(pvrMngIst.isRecording())
		{
			saveRecordAndExit();
		}
	}
	
	public void onBackward()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "onBackward() pvrMngIst is null");
			
			return;
		}
		
		pvrMngIst.onKeyplayBackward();
	}
	
	public void onForward()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "onForward() pvrMngIst is null");
			
			return;
		}
		
		pvrMngIst.onKeyplayForward();
	}
	
	public void saveRecordAndExit()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "saveRecordAndExit() pvrMngIst is null");
			
			return;
		}
		
		//退出录制进度条提示
		
		pvrMngIst.stopRecord();
	}
	
	public boolean onHandleOnkeydownShowStopDlg()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "onHandleOnkeydownShowStopDlg() pvrMngIst is null");
			
			return false;
		}
		
		if(pvrMngIst.isPlaybacking())
		{
			if ((pvrABLoopStartTime != INVALID_TIME) && 
					(INVALID_TIME != pvrABLoopEndTime))
			{
				stopAbLoopPlayback();
				
				try 
	            {
					Thread.sleep(300);					
	            } 
	            catch (InterruptedException e) 
	            {
	            	// TODO Auto-generated catch block
	            	e.printStackTrace();	            	
	            }
			}
			
			pvrMngIst.stopPlayback();
		}				
		
		if(curPvrMode == PVR_MODE.E_PVR_MODE_TIME_SHIFT)
		{
			pvrMngIst.stopTimeShift();
		}
		
		if(pvrMngIst.isRecording())
		{
			saveRecordAndExit();
		}
		
		quickKeyListener.onQuickKeyDownListener(tPreviousEvent.getKeyCode(), tPreviousEvent);
		
		//结束，关闭录制对话框
		dismissUI();
		
		return true;
	}
	
	public boolean CheckNeedToStopRecord(KeyEvent tEvent)
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "CheckNeedToStopRecord() pvrMngIst is null");
			
			return false;
		}
		
		do
		{			
			int keyCode = tEvent.getKeyCode();
			if(showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
			
			tPreviousEvent = tEvent;
			
			if (curPvrMode == PVR_MODE.E_PVR_MODE_PLAYBACK &&
					(keyCode != KeyEvent.KEYCODE_HOME &&
					keyCode != KeyEvent.KEYCODE_TV_INPUT &&
					keyCode != KeyEvent.KEYCODE_CHANNEL_UP &&
					keyCode != KeyEvent.KEYCODE_CHANNEL_DOWN))
			{
				if(pvrMngIst.isPlaybacking())
				{
					if ((pvrABLoopStartTime != INVALID_TIME) &&
							(INVALID_TIME != pvrABLoopEndTime))
					{
						stopAbLoopPlayback();
						try 
						{
							Thread.sleep(300);							
						} 
						catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();							
						}						
					}
					//else if() 播放文件处理，非录制或者时移
					
					pvrMngIst.stopPlayback();
					pvrtimeshitfview.SetRecordTextProgressbar("PVR", 0);
					String currentTimeString = pvrMngIst.getTimeString(0);
					pvrtimeshitfview.SetCurrentTimeText(currentTimeString);
					keycode_pause_play_flag = true;
					pvrtimeshitfview.paly_pause_flag = true;
					pvrtimeshitfview.setPlayer_PauseImage_playunfocus();
					pvrtimeshitfview.setPlayer_ABRepeatImageSetAUnFocus();
					
					if (isBrowserCalled)
					{
						
					}	
				}
				
				if(pvrMngIst.isRecording())
				{
					if (curPvrMode == PVR_MODE.E_PVR_MODE_PLAYBACK)
					{
						curPvrMode = PVR_MODE.E_PVR_MODE_RECORD;
					}
				}
			}
			else
			{
				onHandleOnkeydownShowStopDlg();
			}
		} while(false);
		
		return true;
	}
	
	public boolean onJumpPlaybackTime()
	{
		if(null == pvrMngIst)
		{
			Log.e(TAG, "onJumpPlaybackTime() pvrMngIst is null");
			
			return false;
		}
		
		int timesecond = pvrtimeshitfview.playTimeDlg.GetJimeTime();
		int totaltime = 0;
		totaltime= (pvrMngIst.isRecording()) ?
				pvrMngIst.getCurRecordTimeInSecond() : 
				pvrMngIst.getRecordedFileDurationTime(
				pvrMngIst.getCurPlaybackingFileName());
		
		if(timesecond < totaltime)
		{
			pvrMngIst.jumpPlaybackTime(timesecond);
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public boolean setDialogListener(DialogListener dialogListener) 
	{
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj)
	{
		// TODO Auto-generated method stub
		String flag = (String)obj;
		
		if(null == flag)
		{
			return false;
		}
		
		if(flag.equals("record"))
		{
			curPvrMode = PVR_MODE.E_PVR_MODE_RECORD;
		}
		else if(flag.equals("timeshitf"))
		{
			curPvrMode = PVR_MODE.E_PVR_MODE_TIME_SHIFT;
		}
		else if(flag.equals("playback"))
		{
			curPvrMode = PVR_MODE.E_PVR_MODE_PLAYBACK;
			isBrowserCalled = true;
		}
		else
		{
			curPvrMode = PVR_MODE.E_PVR_MODE_NONE;
		}
		
		Log.e(TAG, "processCmd()::curPvrMode = " + curPvrMode);
		pvrtimeshitfview.setTitle(curPvrMode);
		
		switch (cmd) 
		{
		case DIALOG_SHOW:
			super.showUI();
			
			//判断是否已经选择了盘
			if((0 != selectPath.length()) || isBrowserCalled)
			{
				if(isBrowserCalled)
				{
					selectPath = pvrMngIst.getPvrMountPath();
					selectLabel = pvrMngIst.getVolumeLabel(selectPath);
					recordDiskPath = selectPath;
					recordDiskLable = selectLabel;
				}
				else
				{
					recordDiskPath = selectPath;
					recordDiskLable = selectLabel;
				}
				
				File file = new File(recordDiskPath);
				if(file.exists())
				{
					//是否为FAT32格式
					String fat = "FAT";
					if(isBrowserCalled ||recordDiskLable.regionMatches(5, fat, 0, 3))
					{
						if(curPvrMode == PVR_MODE.E_PVR_MODE_RECORD)
						{
							doPVRRecord(true);
							new PlayBackProgress().start();						
						}
						else if(curPvrMode == PVR_MODE.E_PVR_MODE_TIME_SHIFT)
						{
							doPVRTimeShift(true);
							new PlayBackProgress().start();							
						}
						else if(curPvrMode == PVR_MODE.E_PVR_MODE_PLAYBACK)
						{
							keycode_pause_play_flag = false;
							pvrtimeshitfview.setPlayer_PauseImage_pausefocus();
							TvPluginControler.getInstance().getCommonManager().setVideo(true);
							TvPluginControler.getInstance().getPvrManager().scaleToFullScreen();
							TvPluginControler.getInstance().getCommonManager().setVideo(false);
							doBrowserPlayback();
							new BrowserCalledPlayBackProgress().start();
						}
						else
						{
						}						
					}
					else
					{
						//提示不支持的格式
						pvrtimeshitfview.OnHandleUnSupportDist();
					}					
				}
			}
			else
			{
				//提示没有选择U盘
				pvrtimeshitfview.OnNoDist();
			}
			
			break;
		case DIALOG_HIDE:		
			break;
		case DIALOG_DISMISS:
			Log.e(TAG, "processCmd() DIALOG_DISMISS");
			super.dismissUI();
			break;
		}
		
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Log.i("gky", getClass().getSimpleName()+"onKeyDown "+event);
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_BACK:	
		case KeyEvent.KEYCODE_HOME:
		case KeyEvent.KEYCODE_TV_INPUT:
		case KeyEvent.KEYCODE_MEDIA_PRIORITY:
		{
			onClickStop();
			return true;
		}
		
		case KeyEvent.KEYCODE_MENU:
		{
			showmenu_flag = (!showmenu_flag);
			pvrtimeshitfview.showmenu(showmenu_flag);	
		}
		
		break;
		
		case KeyEvent.KEYCODE_PROG_YELLOW:
		case KeyEvent.KEYCODE_FACTORY_SOURCE_ADD:
			break;
		
		case KeyEvent.KEYCODE_INFO:
		{
			if(showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
			
			SourceInfoDialog sourceInfoDialog = new SourceInfoDialog(keyCode);
			sourceInfoDialog.setDialogListener(null);
			sourceInfoDialog.processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO, DialogCmd.DIALOG_SHOW, false);
		}
		
		return true;
		
		case KeyEvent.KEYCODE_SUBTITLE_SETTING:
		{
			if(showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
			
			MenuItem data = new MenuItem(TvConfigDefs.TV_CFG_SUBTITLE_LANGUAGE, "system_setting", true, null);
			TvListViewDialog listViewDialog = new TvListViewDialog();
			listViewDialog.setDialogListener(new DialogListener() 
			{				
				@Override
				public boolean onShowDialogDone(int ID) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public int onDismissDialogDone(Object... obj) {
					// TODO Auto-generated method stub
					return 0;
				}
			});
			listViewDialog.processCmd(TvConfigTypes.TV_DIALOG_LISTVIEW, DialogCmd.DIALOG_SHOW, data);
		}		
		return true;
		
		case KeyEvent.KEYCODE_AUDIO_SETTING:
		{
			if(showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
			
			MenuItem data = new MenuItem(TvConfigDefs.TV_CFG_AUDIO_LANGUAGE_SETTING, "sound_setting", true, null);
			TvListViewDialog listViewDialog = new TvListViewDialog();
			listViewDialog.setDialogListener(null);
			listViewDialog.processCmd(TvConfigTypes.TV_DIALOG_LISTVIEW, DialogCmd.DIALOG_SHOW, data);		
		}		
		return true;
		
		case KeyEvent.KEYCODE_MEDIA_BOOKING:
		{
            if(showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
			TvTeletextDialog teletextDialog = new TvTeletextDialog();
			teletextDialog.setDialogListener(null);
			teletextDialog.processCmd(TvConfigTypes.TV_DIALOG_TELETEXT, DialogCmd.DIALOG_SHOW, null);
		}		
		return true;
		
		case KeyEvent.KEYCODE_MEDIA_FAVORITES:
		{
            if(showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
			
			ChannelEditDialog ChannelEditDialog = new ChannelEditDialog();
			ChannelEditDialog.setDialogListener(null);
			ChannelEditDialog.processCmd(null, DialogCmd.DIALOG_SHOW, EnumChOpType.E_CH_OPTION_FAV);		
		}		
		return true;
		
		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
		case KeyEvent.KEYCODE_MEDIA_PLAY:
		{
			pvrtimeshitfview.SetPlayer_PauseImage_requestFocus();
			onClickPlayPause();
			
			if(!showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
		}
		
		return true;
		
		case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
		{
			pvrtimeshitfview.player_ffImageRequestFocus();
			OnkeyPlayFF();
			
			if(!showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
		}
		
		return true;
		
		case KeyEvent.KEYCODE_MEDIA_REWIND:
		{
			pvrtimeshitfview.player_revImageRequestFocus();
			OnkeyPlayRev();
			
			if(!showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
		}
		
		return true;
		
		case KeyEvent.KEYCODE_MEDIA_STOP:
		{
			pvrtimeshitfview.player_stopImageRequestFocus();
			onClickStop();
			
			if(!showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
		}
		
		return true;
		
		case KeyEvent.KEYCODE_MEDIA_NEXT:
		{
			if(!showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
			
			pvrtimeshitfview.player_forwardImageRequestFocus();
			onForward();
		}
		
		return true;
		
		case KeyEvent.KEYCODE_MEDIA_PREVIOUS:	
		{
			if(!showmenu_flag)
			{
				showmenu_flag = (!showmenu_flag);
				pvrtimeshitfview.showmenu(showmenu_flag);
			}
			
			pvrtimeshitfview.player_backwardImageRequestFocus();
			onBackward();
		}
		
		return true;
		
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
		case KeyEvent.KEYCODE_VOLUME_MUTE:
			return false;	
		}
		
		if (pvrtimeshitfview.onHandleOnkeydownStopRecord(curPvrMode, event) == true)
        {
            return true;
        }
		
		if(tPreviousEvent == null)
		{
			Log.e(TAG, "tPreviousEvent is null");
			
			return false;
		}
		
		if(quickKeyListener == null)
		{
			Log.e(TAG, "quickKeyListener is null");
			
			return false;
		}
		
		return quickKeyListener.onQuickKeyDownListener(tPreviousEvent.getKeyCode(), tPreviousEvent); //super.onKeyDown(keyCode, event);
	}	
}
