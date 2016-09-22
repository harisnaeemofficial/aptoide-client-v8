/*
 * Copyright (c) 2016.
 * Modified by SithEngineer on 02/09/2016.
 */

package cm.aptoide.pt.database.accessors;

import cm.aptoide.pt.database.schedulers.RealmSchedulers;
import cm.aptoide.pt.model.v7.listapp.App;
import java.util.List;

import cm.aptoide.pt.database.realm.Installed;
import java.util.List;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by sithengineer on 01/09/16.
 */
public class InstalledAccessor implements Accessor {

  private final Database database;

  protected InstalledAccessor(Database db) {
    this.database = db;
  }

  public Observable<List<Installed>> getAll() {
    return database.getAll(Installed.class);
  }

  public Observable<Installed> get(String packageName) {
    return database.get(Installed.class, Installed.PACKAGE_NAME, packageName);
  }

  public void delete(String packageName) {
    database.delete(Installed.class, Installed.PACKAGE_NAME, packageName);
  }

	public Observable<Boolean> isInstalled(String packageName) {
		return get(packageName).map(installed -> installed!=null);
	}

	public Observable<List<Installed>> get(String[] apps) {
		return Observable.fromCallable(() -> database.get())
				.flatMap(realm -> realm.where(Installed.class)
						.in(Installed.PACKAGE_NAME, apps)
						.findAll()
						.asObservable()
						.unsubscribeOn(RealmSchedulers.getScheduler()))
				.flatMap(installeds -> database.copyFromRealm(installeds))
				.subscribeOn(RealmSchedulers.getScheduler());
	}
}
