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

public class DataScraper extends AsyncTask<Void, Void, ArrayList<Card>>{

    @Override
    protected ArrayList<Card> doInBackground(Void... voids) {
        ArrayList<String> links = new ArrayList<>();
        links.add("Red_Cards");
        links.add("Green_Cards");
        links.add("Colorless_cards");

        String url;
        ArrayList<Card> cards = new ArrayList<>();
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
//
                        cards.add(card);
                    }
                }

            } catch (IOException e) {
                Log.d("ERROR", e.getMessage());
            }
        }
        return cards;
    }

    @Override
    protected void onPostExecute(ArrayList<Card> cards) {
        super.onPostExecute(cards);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String cardId;

        for (int i = 0; i < cards.size(); i++) {
            cardId = mDatabase.push().getKey();
            mDatabase.child("Cards").child(cardId).setValue(cards.get(i));
        }
    }
}
