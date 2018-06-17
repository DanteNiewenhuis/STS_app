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
import java.util.List;

import e.dante.sts.Cards.Card;
import e.dante.sts.Event.Event;
import e.dante.sts.Potion.Potion;
import e.dante.sts.Relics.Relic;

public class DataScraper extends AsyncTask<Void, Void, Void> {
    private DatabaseReference mDatabase;

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d("Scraper", "init");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getCards();
//        getRelics();
//        getPotions();
//        getKeywords();
//        getEvents();

        //TODO EXTRA: add enemies to the database
//        getEnemies();

        return null;
    }

    private void getCards() {
        ArrayList<String> links = new ArrayList<>();
        links.add("Ironclad_cards");
        links.add("Silent_cards");
        links.add("Defect_cards");
        links.add("Neutral_cards");

        String url;
        String cardId;
        for (int s = 0; s < links.size(); s++) {
            Log.d("Scraper", "type: " + links.get(s));
            url = "http://slay-the-spire.wikia.com/wiki/" + links.get(s);
            Log.d("Scraper", "url: " + url);
            try {
                Document doc = Jsoup.connect(url).get();

                Element table = doc.select("table").get(0);
                Elements rows = table.select("tr");

                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cols = row.select("td");

                    if (cols.size() > 0) {
                        Card card = new Card();
                        card.setHero(links.get(s).substring(0, links.get(s).length() - 6));

                        card.setName(cols.get(0).text());
                        String imgUrl = cols.get(1).select("a").attr("href");
                        card.setImgUrl(imgUrl.substring(0, imgUrl.indexOf("latest")) + "latest/");
                        card.setRarity(cols.get(2).text());
                        card.setType(cols.get(3).text());
                        card.setCost(cols.get(4).text());
                        card.setDescription(cols.get(5).text());

                        String url2 = "http://slay-the-spire.wikia.com/wiki/" + card.getName();
                        Document doc2 = Jsoup.connect(url2).get();

                        //TODO scrape upgraded and normal description and cost from here!!!
                        Elements divs = doc2.select("div.pi-data-value pi-font");

                        card.setUpgradeCost(divs.get(5).text());
                        card.setUpgradeDescription(divs.get(6).text());

                        cardId = name_to_dName(card.getName());
                        if (cardId.equals("Strike")) {
                            if (card.getHero().equals("Ironclad")) {
                                cardId = cardId + "_i";
                            }
                            if (card.getHero().equals("Silent")) {
                                cardId = cardId + "_s";
                            }
                            if (card.getHero().equals("Defect")) {
                                cardId = cardId + "_d";
                            }
                        }
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

            for (int i = 0; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                if (cols.size() > 0) {
                    Relic relic = new Relic();

                    relic.setImgUrl(cols.get(0).select("img").attr("src"));
                    relic.setName(cols.get(1).text());
                    relic.setRarity(cols.get(2).text());
                    relic.setDescription(cols.get(3).text());

                    relicId = name_to_dName(relic.getName());
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

            for (int i = 0; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                if (cols.size() > 0) {
                    Potion potion = new Potion();

                    potion.setImgUrl(cols.get(0).select("img").attr("src"));
                    potion.setName(cols.get(1).text());
                    potion.setDescription(cols.get(2).text());

                    potionId = name_to_dName(potion.getName());
                    mDatabase.child("Potions").child(potionId).setValue(potion);
                }
            }

        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    private void getKeywords() {
        String url = "https://slaythespire.gamepedia.com/Keywords";
        String keywordId;
        try {
            Document doc = Jsoup.connect(url).get();

            Element list = doc.select("ul").get(0);
            Elements items = list.select("li");

            for (int i = 0; i < items.size(); i++) {
                String item = items.get(i).text();
                int index = item.indexOf("-");

                String name = "";
                String des = "";
                for (int j = 0; j < item.length(); j++) {
                    if (j < index - 1) {
                        name += item.charAt(j);
                    }
                    if (j > index + 1) {
                        des += item.charAt(j);
                    }
                }

                keywordId = name_to_dName(name);
                mDatabase.child("Keywords").child(keywordId).child("name").setValue(name);
                mDatabase.child("Keywords").child(keywordId).child("description").setValue(des);
            }

        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    private void getEvents() {
        String url = "https://slaythespire.gamepedia.com/Category:Act_";
        String eventId;
        for (int l = 1; l < 4; l++) {
            try {
                Document doc = Jsoup.connect(url + l + "_Events").get();
                Element div = doc.getElementById("mw-pages");
                Elements items = div.select("li");

                for (int i = 0; i < items.size(); i++) {
                    Event event = new Event();
                    String name = items.get(i).text();
                    event.setAct(l);
                    event.setName(name);

                    Document doc2 = Jsoup.connect("https://slaythespire.gamepedia.com/" +
                            name.replaceAll(" ", "_")).get();
                    Element div2 = doc2.getElementById("mw-content-text");
                    Elements items2 = div2.select("li");

                    List<String> options = new ArrayList<>();
                    for (int j = 0; j < items2.size(); j++) {
                        options.add(items2.get(j).text());
                    }
                    event.setOptions(options);

                    eventId = name_to_dName(event.getName());
                    mDatabase.child("Events").child(eventId).setValue(event);
                }

            } catch (IOException e) {
                Log.d("ERROR", e.getMessage());
            }
        }
    }

    private String name_to_dName(String s) {
        s = s.replaceAll("\\.", "_p_");
        s = s.replaceAll("\\$", "_d_");
        s = s.replaceAll("\\[", "_l_");
        s = s.replaceAll("]", "_r_");
        s = s.replaceAll("#", "_h_");

        return s;
    }

    private String dName_to_name(String s) {
        s = s.replaceAll("_p_", "\\.");
        s = s.replaceAll("_d_", "\\$");
        s = s.replaceAll("_l_", "\\[");
        s = s.replaceAll("_r_", "]");
        s = s.replaceAll("_h_", "#");

        return s;
    }
}
