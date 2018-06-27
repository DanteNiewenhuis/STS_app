package e.dante.sts;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

public class GlobalFunctions implements InfoHelper.Callback{
    private FragmentManager fragmentManager;

    public GlobalFunctions(){}

    public GlobalFunctions(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public SpannableString makeSpans(Spanned ss) {
        return makeSpans(ss.toString());
    }

    public SpannableString makeSpans(String input) {
        Log.d("makeSpans", "input: " + input);
        SpannableString ss = new SpannableString(input);

        int endIndex;
        for (String word : Globals.getInstance().getCards()) {
            for (int index = input.toLowerCase().indexOf(word.toLowerCase());
                 index >= 0;
                 index = input.toLowerCase().indexOf(word.toLowerCase(), index + 1)) {
                endIndex = input.indexOf(" ", index);
                if (endIndex == -1) {
                    endIndex = index + word.length();
                }
                ss.setSpan(makeClickableSpan("Cards", word), index, endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        for (String word : Globals.getInstance().getRelics()) {
            for (int index = input.toLowerCase().indexOf(word.toLowerCase());
                 index >= 0;
                 index = input.toLowerCase().indexOf(word.toLowerCase(), index + 1)) {
                endIndex = input.indexOf(" ", index);
                if (endIndex == -1) {
                    endIndex = index + word.length();
                }
                ss.setSpan(makeClickableSpan("Relics", word), index, endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        for (String word : Globals.getInstance().getPotions()) {
            for (int index = input.toLowerCase().indexOf(word.toLowerCase());
                 index >= 0;
                 index = input.toLowerCase().indexOf(word.toLowerCase(), index + 1)) {
                endIndex = input.indexOf(" ", index);
                if (endIndex == -1) {
                    endIndex = index + word.length();
                }
                ss.setSpan(makeClickableSpan("Potions", word), index, endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        for (String word : Globals.getInstance().getKeywords()) {
            for (int index = input.toLowerCase().indexOf(word.toLowerCase());
                 index >= 0;
                 index = input.toLowerCase().indexOf(word.toLowerCase(), index + 1)) {
                endIndex = input.indexOf(" ", index);
                if (endIndex == -1) {
                    endIndex = index + word.length();
                }
                ss.setSpan(makeClickableSpan("Keywords", word), index, endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        for (String word : Globals.getInstance().getEvents()) {
            for (int index = input.toLowerCase().indexOf(word.toLowerCase());
                 index >= 0;
                 index = input.toLowerCase().indexOf(word.toLowerCase(), index + 1)) {
                endIndex = input.indexOf(" ", index);
                if (endIndex == -1) {
                    endIndex = index + word.length();
                }
                ss.setSpan(makeClickableSpan("Events", word), index, endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return ss;
    }

    private ClickableSpan makeClickableSpan(final String type, final String filter) {
        return new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Log.d("ClickableSpan", "filter: " + filter);
                new InfoHelper().getInfo(GlobalFunctions.this, type, filter);
            }
        };
    }

    public SpannableString makeBold(String input, String start, String end) {
        Log.d("makeBold", "input: " + input);
        Log.d("makeBold", "start: " + start);
        Log.d("makeBold", "end: " + end);

        SpannableString ss = new SpannableString(input);

        int endIndex;
        for (int startIndex = input.toLowerCase().indexOf(start.toLowerCase());
            startIndex >= 0;
            startIndex = input.toLowerCase().indexOf(start.toLowerCase(), startIndex + 1)) {
            endIndex = input.toLowerCase().indexOf(end.toLowerCase(), startIndex + 1);
            if (endIndex == -1) {
                Log.d("makeBold", "break");
                break;
            }
            Log.d("makeBold", "startIndex: " + startIndex);
            Log.d("makeBold", "endIndex: " + endIndex);
            ss.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        return ss;
    }

    public String parseHTML(String html) {

        int endIndex;
        for (int startIndex = html.indexOf("<figure");
             startIndex >= 0;
             startIndex = html.indexOf("<figure")) {
            endIndex = html.indexOf("figure>", startIndex);
            if (endIndex == -1) {
                Log.d("makeBold", "break");
                break;
            }
            String sub = html.substring(startIndex, endIndex + 7);
            html = html.replace(sub, "");
        }

        for (int startIndex = html.indexOf("<span class=\"editsection");
             startIndex >= 0;
             startIndex = html.indexOf("<span class=\"editsection")) {
            endIndex = html.indexOf("span>", startIndex);
            if (endIndex == -1) {
                Log.d("makeBold", "break");
                break;
            }
            String sub = html.substring(startIndex, endIndex + 5);
            html = html.replace(sub, "");
        }

        return html;
    }

    public void getInfo(String comboName, String type){
        new InfoHelper().getInfo(this, comboName, type);
    }

    public void getNotes(String type, String name, String oldNote) {
        DialogFragment dialog = new InputFragment();
        Bundle extra = new Bundle();
        extra.putString("type", type);
        extra.putString("name", name);
        extra.putString("oldNote", oldNote);
        dialog.setArguments(extra);
        dialog.show(fragmentManager, "dialog");
    }
    @Override
    public void gotInfo(String name, String type, String des) {
        DialogFragment dialog = new InfoFragment();
        Bundle extra = new Bundle();
        extra.putSerializable("name", name);
        extra.putSerializable("type", type);
        extra.putSerializable("des", des);
        dialog.setArguments(extra);
        dialog.show(fragmentManager, "dialog");
    }

    @Override
    public void gotInfoError(String message) {

    }

    public String dName_to_name(String s) {
        s = s.replaceAll("_p_", "\\.");
        s = s.replaceAll("_d_", "\\$");
        s = s.replaceAll("_l_", "\\[");
        s = s.replaceAll("_r_", "]");
        s = s.replaceAll("_h_", "#");

        return s;
    }

    public String name_to_dName(String s) {
        s = s.replaceAll("\\.", "_p_");
        s = s.replaceAll("\\$", "_d_");
        s = s.replaceAll("\\[", "_l_");
        s = s.replaceAll("]", "_r_");
        s = s.replaceAll("#", "_h_");

        return s;
    }

    @SuppressWarnings("deprecation")
    public Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return makeSpans(Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY));
        } else {
            return makeSpans(Html.fromHtml(source));
        }
    }
}
