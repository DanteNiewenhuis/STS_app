package e.dante.sts;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataScraper extends AsyncTask<Void, Void, Void>{
    private DatabaseReference mDatabase;

    @Override
    protected Void doInBackground(Void... voids) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getCards();
        getRelics();
        getPotions();
        getKeywords();

        //TODO implement datascraper
//        getEvents();
//        getEnemies();

        return null;
    }

    private void getCards() {
        ArrayList<String> links = new ArrayList<>();
        links.add("Red_Cards");
        links.add("Green_Cards");
        links.add("Colorless_cards");

        String url;
        String cardId;
        for (int s = 0; s < links.size(); s++) {
            url = "https://slaythespire.gamepedia.com/" + links.get(s);
            try {
                Document doc = Jsoup.connect(url).get();

                Element table = doc.select("table").get(0);
                Elements rows = table.select("tr");

                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cols = row.select("td");

                    if (cols.size() > 0) {
                        Card card = new Card();
                        if (s == 0) {
                            card.setColor("Red");
                        }
                        if (s == 1) {
                            card.setColor("Green");
                        }
                        if (s == 2) {
                            card.setColor("Colorless");
                        }

                        card.setName(cols.get(0).text());
                        card.setImgUrl(cols.get(1).select("img").attr("src"));
                        card.setRarity(cols.get(2).text());
                        card.setType(cols.get(3).text());
                        card.setCost(cols.get(4).text());
                        card.setDescription(cols.get(5).text());

                        cardId = mDatabase.push().getKey();
                        mDatabase.child("Cards").child(cardId).setValue(card);
                    }
                }

            } catch (IOException e) {
                Log.d("ERROR", e.getMessage());
            }
        }
    }

    private void getRelics() {
        String url = "https://slaythespire.gamepedia.com/Relics";
        String relicId;
        try {
            Document doc = Jsoup.connect(url).get();

            Element table = doc.select("table").get(0);
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                if (cols.size() > 0) {
                    Relic relic = new Relic();

                    relic.setImgUrl(cols.get(0).select("img").attr("src"));
                    relic.setName(cols.get(1).text());
                    relic.setRarity(cols.get(2).text());
                    relic.setDescription(cols.get(3).text());

                    relicId = mDatabase.push().getKey();
                    mDatabase.child("Relics").child(relicId).setValue(relic);
                }
            }

        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    private void getPotions() {
        String url = "https://slaythespire.gamepedia.com/Potions";
        String potionId;
        try {
            Document doc = Jsoup.connect(url).get();

            Element table = doc.select("table").get(0);
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                if (cols.size() > 0) {
                    Potion potion = new Potion();

                    potion.setImgUrl(cols.get(0).select("img").attr("src"));
                    potion.setName(cols.get(1).text());
                    potion.setDescription(cols.get(2).text());

                    potionId = mDatabase.push().getKey();
                    mDatabase.child("Potions").child(potionId).setValue(potion);
                }
            }

        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    private void getKeywords() {
        String url = "https://slaythespire.gamepedia.com/Potions";
        String keywordId;
        try {
            Document doc = Jsoup.connect(url).get();

            Elements list = doc.select("ul");

            Pattern p1 = Pattern.compile("[^-]*");
            Pattern p2 = Pattern.compile("[^-]*(.*)");
            for (int i = 1; i < list.size(); i++) {
                String item = list.get(i).text();

                Matcher m1 = p1.matcher(item);
                Matcher m2 = p2.matcher(item);

                String name = m1.group(0);
                String description = m2.group(0);

                keywordId = mDatabase.push().getKey();
                mDatabase.child("Keywords").child(keywordId).child("name").setValue(name);
                mDatabase.child("Keywords").child(keywordId).child("description").setValue(description);
            }

        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }
    }
}
