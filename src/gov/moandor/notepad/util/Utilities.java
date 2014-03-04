package gov.moandor.notepad.util;

import gov.moandor.notepad.BuildConfig;
import gov.moandor.notepad.R;
import gov.moandor.notepad.bean.Article;

import java.io.Closeable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Utilities {
    public static void closeSilently(Closeable c) {
        try {
            c.close();
        } catch (Exception e) {
            Logger.logExcpetion(e);
        }
    }
    
    public static String getListTime(long timeMillis) {
        Calendar now = Calendar.getInstance();
        Calendar msg = Calendar.getInstance();
        msg.setTimeInMillis(timeMillis);
        if (msg.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                && msg.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) {
            return GlobalContext.getInstance().getString(R.string.today);
        } else {
            DateFormat dateFormat = new SimpleDateFormat("M-d", Locale.getDefault());
            return dateFormat.format(timeMillis);
        }
    }
    
    public static List<Article> sortByTime(List<Article> articles) {
        List<Article> result = new ArrayList<Article>();
        for (Article article : articles) {
            if (result.size() == 0) {
                result.add(article);
            } else if (article.lastModified >= result.get(0).lastModified) {
                result.add(0, article);
            } else if (article.lastModified <= result.get(result.size() - 1).lastModified) {
                result.add(result.size(), article);
            } else {
                int count = result.size() - 1;
                for (int i = 0; i < count; i++) {
                    if (result.get(i + 1).lastModified <= article.lastModified
                            && article.lastModified <= result.get(i).lastModified) {
                        result.add(i + 1, article);
                        break;
                    }
                }
            }
        }
        return result;
    }
}
