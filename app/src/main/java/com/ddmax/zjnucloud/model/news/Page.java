package com.ddmax.zjnucloud.model.news;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Page implements Parcelable {

	public abstract String getTitle();

	public abstract String getUrl();

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {
		return getTitle();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Page)) return false;
		Page page = (Page) o;
		return getUrl().equals(page.getUrl());
	}

	@Override
	public int hashCode() {
		return getUrl().hashCode();
	}

	public static class SimplePage extends Page {
		private final String mTitle;
		private final String mUrl;

		public SimplePage(String title, String url) {
			mTitle = title;
			mUrl = url;
		}

		@Override
		public String getTitle() {
			return mTitle;
		}

		@Override
		public String getUrl() {
			return mUrl;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.mTitle);
			dest.writeString(this.mUrl);
		}

		protected SimplePage(Parcel in) {
			this.mTitle = in.readString();
			this.mUrl = in.readString();
		}

		public static final Creator<SimplePage> CREATOR = new Creator<SimplePage>() {
			public SimplePage createFromParcel(Parcel source) {
				return new SimplePage(source);
			}

			public SimplePage[] newArray(int size) {
				return new SimplePage[size];
			}
		};
	}
}
