package com.fteams.sstrain.android;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.fteams.sstrain.SSTrain;
import com.fteams.sstrain.config.GlobalConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            GlobalConfiguration.appVersionName = pInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
        initialize(new SSTrain(), config);
	}
}
