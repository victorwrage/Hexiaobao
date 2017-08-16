
package com.zdv.hexiaobao;

import android.app.Activity;
import android.app.Application;

import com.socks.library.KLog;
import com.zdv.hexiaobao.bean.DaoMaster;
import com.zdv.hexiaobao.bean.DaoSession;
import com.zdv.hexiaobao.utils.UncaughtExceptionCatcher;

import org.greenrobot.greendao.database.Database;

import java.util.LinkedList;
import java.util.List;

/** 
 * @ClassName VApplication 
 * @Description TODO  Application基类
 * @Version 1.0
 * @Creation 2013-8-10 上午10:09:20 
 * @Mender xiaoyl
 * @Modification 2013-8-10 上午10:09:20 
 **/
public class HeApplication extends Application {
	protected static HeApplication instance;
	private List<Activity> activityList = new LinkedList<Activity>();
	private String myState;
	public static boolean isExit = false;
	/** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
	public static final boolean ENCRYPTED = false;
	private DaoSession daoSession;
	public HeApplication() {

	}

	
	public static HeApplication getInstance() {
		if (null == instance) {
			instance = new HeApplication();
		}
		return instance;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "history-db-encrypted" : "history-db");
		Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
		daoSession = new DaoMaster(db).newSession();
		try 
	    {
			UncaughtExceptionCatcher.getInstance(getApplicationContext());
	    }
	    catch (Exception localException)
	    {
	       KLog.e(localException.getMessage());
	    }
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}

	public String getState() {
		return myState;
	}

	public void setState(String s) {
		myState = s;
	}
	
	/**
	 * 
	 * @param activity
	 */
	public void addActivitys(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 退出应用程序
	 */
	public void exitApplication() {
		isExit = true;
		for (Activity a : activityList) {
			a.finish();
		}
		System.exit(0);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
