package e.dante.sts;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

public class GlobalFunctions {
    private InfoHelper.Callback activity;

    public GlobalFunctions(InfoHelper.Callback activity) {
        this.activity = activity;
    }

//    public SpannableString makeSpans(String input) {
//        Log.d("makeSpans", "word: " + "init");
//        SpannableString ss = new SpannableString(input);
//
//        for (String word : MainActivity.globalKeywordList) {
//            Log.d("makeSpans", "word: " + word);
//            for (int index = input.toLowerCase().indexOf(word.toLowerCase());
//                 index >= 0;
//                 index = input.toLowerCase().indexOf(word.toLowerCase(), index + 1)) {
//                ss.setSpan(makeClickableSpan(word), index, index + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }
//
//        return ss;
//    }

    private ClickableSpan makeClickableSpan(final String filter) {
        return new ClickableSpan() {
            @Override
            public void onClick(View view) {
                new InfoHelper().getInfo(activity, "Keywords", filter);
            }
        };
    }
}
