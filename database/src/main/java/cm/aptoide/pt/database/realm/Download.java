/*
 * Copyright (c) 2016.
 * Modified by Neurophobic Animal on 24/05/2016.
 */

package cm.aptoide.pt.database.realm;

import android.content.Context;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cm.aptoide.pt.database.R;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sithengineer on 17/05/16.
 */
public class Download extends RealmObject {

	public static final int INVALID_STATUS = 0;
	public static final int COMPLETED = 1;
	public static final int BLOCK_COMPLETE = 2;
	public static final int CONNECTED = 3;
	public static final int PENDING = 4;
	public static final int PROGRESS = 5;
	public static final int PAUSED = 6;
	public static final int WARN = 7;
	public static final int STARTED = 8;
	public static final int ERROR = 9;
	public static final int FILE_MISSING = 10;
	public static final int RETRY = 11;
	public static final int NOT_DOWNLOADED = 12;
	RealmList<FileToDownload> filesToDownload;
	@DownloadState
	int overallDownloadStatus = 0;
	int overallProgress = 0;
	@PrimaryKey
	private long appId;
	private String appName;

	public static String getStatusName(@DownloadState int status, Context context) {
		String toReturn;
		switch (status) {
			case COMPLETED:
				toReturn = context.getString(R.string.download_completed);
				break;
			case ERROR:
				toReturn = context.getString(R.string.simple_error_occured);
				break;
			case PAUSED:
				toReturn = context.getString(R.string.download_paused);
				break;
			case PROGRESS:
				toReturn = context.getString(R.string.download_progress);
				break;
			case BLOCK_COMPLETE:
			case CONNECTED:
			case FILE_MISSING:
			case INVALID_STATUS:
			case NOT_DOWNLOADED:
			case PENDING:
			case RETRY:
			case STARTED:
			case WARN:
			default:
				toReturn = "";
		}
		return toReturn;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public RealmList<FileToDownload> getFilesToDownload() {
		return filesToDownload;
	}

	public void setFilesToDownload(RealmList<FileToDownload> filesToDownload) {
		this.filesToDownload = filesToDownload;
	}

	public
	@DownloadState
	int getOverallDownloadStatus() {
		return overallDownloadStatus;
	}

	public void setOverallDownloadStatus(@DownloadState int overallDownloadStatus) {
		this.overallDownloadStatus = overallDownloadStatus;
	}

	public int getOverallProgress() {
		return overallProgress;
	}

	public void setOverallProgress(int overallProgress) {
		this.overallProgress = overallProgress;
	}

	public long getAppId() {
		return appId;
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	@Override
	public Download clone() throws CloneNotSupportedException {
		Download clone = new Download();
		clone.setAppId(this.getAppId());
		clone.setOverallDownloadStatus(this.getOverallDownloadStatus());
		if (this.getAppName() != null) {
			clone.setAppName(new String(this.getAppName()));
		}
		clone.setFilesToDownload(cloneDownloadFiles(this.getFilesToDownload()));
		return clone;
	}

	private RealmList<FileToDownload> cloneDownloadFiles(RealmList<FileToDownload> filesToDownload) {
		RealmList<FileToDownload> clone = new RealmList<>();
		for (final FileToDownload fileToDownload : filesToDownload) {
			try {
				clone.add(fileToDownload.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return clone;
	}

	@IntDef({INVALID_STATUS, COMPLETED, BLOCK_COMPLETE, CONNECTED, PENDING, PROGRESS, PAUSED, WARN, STARTED, ERROR, FILE_MISSING, RETRY, NOT_DOWNLOADED})
	@Retention(RetentionPolicy.SOURCE)

	public @interface DownloadState {

	}
}
