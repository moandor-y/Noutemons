package gov.moandor.notepad.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {
    public long id;
    public String text;
    public String title;
    public long lastModified;
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(text);
        dest.writeString(title);
        dest.writeLong(lastModified);
    }
    
    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            Article result = new Article();
            result.id = source.readLong();
            result.text = source.readString();
            result.title = source.readString();
            result.lastModified = source.readLong();
            return result;
        }
        
        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
