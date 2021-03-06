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

import e.dante.sts.Cards.Card;
import e.dante.sts.Event.Event;
import e.dante.sts.Global.GlobalFunctions;
import e.dante.sts.Potion.Potion;
import e.dante.sts.Relics.Relic;

public class DataScraper extends AsyncTask<Void, Void, Void> {
    private DatabaseReference mDatabase;
    private GlobalFunctions globalFunctions;

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d("Scraper", "init");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        globalFunctions = new GlobalFunctions();
//        getCards();
        getCurses();
        getStatuses();
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

                        String link = cols.get(0).select("a").get(0).attr("abs:href");
                        cardId = card.getName();

                        if (link.equals("http://slay-the-spire.wikia.com/wiki/Defend")) {
                            link += "_(" + links.get(s).substring(0, links.get(s).length() - 6) + ")";
                        }

                        if (link.equals("http://slay-the-spire.wikia.com/wiki/Strike")) {
                            link += "_(" + links.get(s).substring(0, links.get(s).length() - 6) + ")";
                        }

                        card.setRarity(cols.get(2).text());
                        card.setType(cols.get(3).text());

                        Log.d("Scraper", "cardId: " + cardId);
                        Log.d("Scraper", "link: " + link);
                        String url2 = "http://slay-the-spire.wikia.com/wiki/" +
                                cardId.replaceAll(" ", "_");
                        Document doc2 = Jsoup.connect(link).get();

                        //TODO scrape upgraded and normal description and cost from here!!!
                        Element div = doc2.getElementById("mw-content-text");
                        Elements divs = div.select("div.pi-data-value");

                        String imgUrl = div.select("figure").get(0).select("a")
                                .attr("href");
                        card.setImgUrl(imgUrl.substring(0, imgUrl.indexOf("latest")) + "latest/");

                        if (cardId.equals("Doppelganger")) {
                            card.setCost("X");
                            card.setDescription(divs.get(3).text());
                            card.setUpgradeCost("X");
                            card.setUpgradeDescription(divs.get(4).text());
                        } else {
                            card.setCost(divs.get(3).text());
                            card.setDescription(divs.get(4).text());
                            card.setUpgradeCost(divs.get(5).text());
                            card.setUpgradeDescription(divs.get(6).text());
                        }

                        cardId = globalFunctions.name_to_dName(cardId);
                        mDatabase.child("Cards").child(cardId).setValue(card);
                        mDatabase.child("Cards").child(cardId).child("yourScore").setValue(null);
                        mDatabase.child("Cards").child(cardId).child("averageScore").setValue(null);
                        mDatabase.child("Cards").child(cardId).child("notes").setValue(null);
                    }
                }

            } catch (IOException e) {
                Log.d("ERROR", e.getMessage());
            }
        }
    }

    private void getCurses() {
        try {
            Document doc = Jsoup.connect("http://slay-the-spire.wikia.com/wiki/Neutral_cards").get();

            Element table = doc.select("table").get(1);
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                if (cols.size() > 0) {
                    Card card = new Card();
                    card.setHero("Curse");

                    card.setName(cols.get(0).text());

                    String link = cols.get(0).select("a").get(0).attr("abs:href");
                    Document doc2 = Jsoup.connect(link).get();

                    //TODO scrape upgraded and normal description and cost from here!!!
                    Element div = doc2.getElementById("mw-content-text");
                    Elements divs = div.select("div.pi-data-value");

                    String imgUrl = div.select("figure").get(0).select("a")
                            .attr("href");
                    card.setImgUrl(imgUrl.substring(0, imgUrl.indexOf("latest")) + "latest/");

                    card.setCost("U");
                    card.setRarity(divs.get(2).text());
                    card.setCost(divs.get(3).text());
                    card.setDescription(divs.get(4).text());
                    card.setUpgradeCost(null);
                    card.setUpgradeDescription(null);

                    String cardId = globalFunctions.name_to_dName(card.getName());
                    mDatabase.child("Cards").child(cardId).setValue(card);
                    mDatabase.child("Cards").child(cardId).child("yourScore").setValue(null);
                    mDatabase.child("Cards").child(cardId).child("averageScore").setValue(null);
                    mDatabase.child("Cards").child(cardId).child("notes").setValue(null);
                }
            }

        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    private void getStatuses() {
        try {
            Document doc = Jsoup.connect("http://slay-the-spire.wikia.com/wiki/Neutral_cards").get();

            Element table = doc.select("table").get(2);
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                if (cols.size() > 0) {
                    Card card = new Card();
                    card.setHero("Status");

                    card.setName(cols.get(0).text());

                    String link = cols.get(0).select("a").get(0).attr("abs:href");
                    Log.d("Status", "Link: " + link);
                    Document doc2 = Jsoup.connect(link).get();

                    //TODO scrape upgraded and normal description and cost from here!!!
                    Element div = doc2.getElementById("mw-content-text");
                    Elements divs = div.select("div.pi-data-value");

                    String imgUrl = div.select("figure").get(0).select("a")
                            .attr("href");
                    card.setImgUrl(imgUrl.substring(0, imgUrl.indexOf("latest")) + "latest/");

                    card.setRarity(divs.get(2).text());
                    card.setCost(divs.get(3).text());
                    card.setDescription(divs.get(4).text());
                    card.setUpgradeCost(null);
                    card.setUpgradeDescription(null);

                    String cardId = globalFunctions.name_to_dName(card.getName());
                    mDatabase.child("Cards").child(cardId).setValue(card);
                    mDatabase.child("Cards").child(cardId).child("yourScore").setValue(null);
                    mDatabase.child("Cards").child(cardId).child("averageScore").setValue(null);
                    mDatabase.child("Cards").child(cardId).child("notes").setValue(null);
                }
            }

        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    private void getRelics() {
        Log.d("Scraper", "Relics: start");
        String url = "http://slay-the-spire.wikia.com/wiki/Relics";
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

                    relic.setName(cols.get(1).text());
                    relic.setRarity(cols.get(2).text().split(" ")[0]);
                    relic.setDescription(cols.get(3).text());

                    relicId = globalFunctions.name_to_dName(relic.getName());
                    Log.d("RelicScraper", "relic: " + relicId);
                    String url2 = "http://slay-the-spire.wikia.com/wiki/" + relicId.replaceAll(" ", "_");
                    Document doc2 = Jsoup.connect(url2).get();

                    Element div = doc2.getElementById("mw-content-text");
                    Element img = div.select("img").get(0);
                    try {
                        Element info = div.select("p").get(0);
                        relic.setInfo(info.text());
                    } catch (IndexOutOfBoundsException e) {
                        relic.setInfo("");
                    }
                    Element div2 = div.select("div.pi-data-value").get(3);

                    relic.setImgUrl(img.attr("src"));
                    relic.setHero(div2.text());
                    mDatabase.child("Relics").child(relicId).setValue(relic);
                }
            }

        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }

        Log.d("Scraper", "Relics: done");
    }

    private void getPotions() {
        String url = "http://slay-the-spire.wikia.com/wiki/Potions";
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

                    potion.setImgUrl(cols.get(0).select("a").attr("href"));
                    potion.setName(cols.get(1).text());
                    potion.setRarity(cols.get(2).text());
                    potion.setDescription(cols.get(3).text());

                    potionId = globalFunctions.name_to_dName(potion.getName());
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

                keywordId = globalFunctions.name_to_dName(name);
                mDatabase.child("Keywords").child(keywordId).child("name").setValue(name);
                mDatabase.child("Keywords").child(keywordId).child("description").setValue(des);
            }

        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    private void getEvents() {
        String url = "http://slay-the-spire.wikia.com/wiki/Events";
        String eventId;
        try {
            Document doc = Jsoup.connect(url).get();
            Element div = doc.getElementById("mw-content-text");
            Elements lists = div.select("ul");

            for (int i = 0; i < 4; i++) {
                Element list = lists.get(i);
                Elements items = list.select("li");

                for (int j = 0; j < items.size(); j++) {
                    Event event = new Event();
                    String name = items.get(j).text();
                    Log.d("EVENTSCRAPER", "name: " + name);
                    event.setAct(i + 1);
                    event.setName(name);

                    String link = items.get(j).select("a").get(0).attr("abs:href");
                    Log.d("EVENTSCRAPER", "link: " + link);

                    Document doc2 = Jsoup.connect(link).get();
                    Element div2 = doc2.getElementById("mw-content-text");
                    event.setOptions(div2.html());

                    eventId = globalFunctions.name_to_dName(event.getName());
                    mDatabase.child("Events").child(eventId).setValue(event);
                }
            }

        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }
    }
}
