package e.dante.sts;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

public class GlobalFunctions {
    private FragmentManager fragmentManager;

    public GlobalFunctions(Activity activity) {
        this.fragmentManager = activity.getFragmentManager();
    }

    public SpannableString makeSpans(String input) {
        SpannableString ss = new SpannableString(input);
        String[] keywords = {"block", "innate", "exhaust"};

        for (String word: keywords) {
            for (int index = input.toLowerCase().indexOf(word.toLowerCase());
                 index >= 0;
                 index = input.toLowerCase().indexOf(word.toLowerCase(), index + 1))
            {
                ss.setSpan(makeClickableSpan(word), index, index+word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return ss;
    }

    private ClickableSpan makeClickableSpan(final String filter) {
        return new ClickableSpan() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new InfoFragment();
                Bundle extra = new Bundle();
                extra.putString("filter", filter);
                extra.putString("type", "Keywords");
                dialog.setArguments(extra);
                dialog.show(fragmentManager, "dialog");
            }
        };
    }


}
