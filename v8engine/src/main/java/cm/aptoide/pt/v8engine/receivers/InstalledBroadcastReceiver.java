/*
 * Copyright (c) 2016.
 * Modified by SithEngineer on 15/07/2016.
 */

package cm.aptoide.pt.v8engine.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.util.Log;

import cm.aptoide.pt.database.Database;
import cm.aptoide.pt.database.realm.Installed;
import cm.aptoide.pt.database.realm.Rollback;
import cm.aptoide.pt.database.realm.Update;
import cm.aptoide.pt.utils.AptoideUtils;
import cm.aptoide.pt.v8engine.V8Engine;
import io.realm.Realm;

/**
 * Created by neuro on 24-05-2016.
 */
public class InstalledBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "InstalledReceiver";
	private Realm realm;

	@Override
	public void onReceive(Context context, Intent intent) {
		loadRealm();

		String action = intent.getAction();
		String packageName = intent.getData().getEncodedSchemeSpecificPart();

		switch (action) {
			case Intent.ACTION_PACKAGE_ADDED:
				onPackageAdded(packageName);
				break;
			case Intent.ACTION_PACKAGE_REPLACED:
				onPackageReplaced(packageName);
				break;
			case Intent.ACTION_PACKAGE_REMOVED:
				onPackageRemoved(packageName);
				break;
		}

		closeRealm();
	}

	private void loadRealm() {
		if (realm == null) {
			realm = Database.get();
		}
	}

	private void closeRealm() {
		if (realm != null) {
			realm.close();
		}
	}

	protected void onPackageAdded(String packageName) {
		Log.d(TAG, "Package added: " + packageName);

		databaseOnPackageAdded(packageName);
	}

	protected void onPackageReplaced(String packageName) {
		Log.d(TAG, "Packaged replaced: " + packageName);

		databaseOnPackageReplaced(packageName);
	}

	protected void onPackageRemoved(String packageName) {
		Log.d(TAG, "Packaged removed: " + packageName);

		databaseOnPackageRemoved(packageName);
	}

	private void databaseOnPackageAdded(String packageName) {
		PackageInfo packageInfo = AptoideUtils.SystemU.getPackageInfo(packageName);

		Database.save(new Installed(packageInfo, V8Engine.getContext().getPackageManager()), realm);
	}

	private void databaseOnPackageReplaced(String packageName) {
		Update update = Database.UpdatesQ.get(packageName, realm);

		PackageInfo packageInfo = AptoideUtils.SystemU.getPackageInfo(packageName);
		if (update != null) {
			if (packageInfo.versionCode >= update.getVersionCode()) {
				Database.delete(update, realm);
			}
		}

		Installed installed = Database.InstalledQ.get(packageName, realm);

		if (installed != null) {
			installed.update(packageInfo, realm);
			Database.save(installed, realm);
		}
	}

	private void databaseOnPackageRemoved(String packageName) {
		Database.InstalledQ.delete(packageName, realm);
		Database.UpdatesQ.delete(packageName, realm);

		Rollback rollback = Database.RollbackQ.get(packageName, Rollback.Action.UNINSTALL, realm);
		if (rollback != null) {
			rollback.confirm(realm);
		}
	}
}
