# Final report

## App description
My app is a tool to help the user play the game Slay the Spire.
Slay the Spire is a Card game that is now in the beta phase.
The only sources of information about this game are found on the internet through wiki sites.
This is usefull but these sites can be quite hard to navigate through and forces people to switch screens to 
a browser during playing because most people don't have two monitors. 
My app tries to solve these problems by being an easy to use tool where beside getting all the game information
it also gives the user the ability to make notes, save combos and see the opinion of other users.

![HIER MOET NOG EEN FOTO](doc/app_design_1.jpeg)


## Technical Design
The app works through a Firebase app with all the necessary information which is scraped from 
https://slay-the-spire.wikia.com/wiki/Slay_the_spire_Wiki. 
This is a wikia page which is fully user created and I have added missing data to this page as well 
to make it complete. Next to the data from the game there is also a section in the database with 
the opinions of the users. This is the place where the scores, notes and combos of all the users are saved.

The design of the app is a standard navigation drawer layour with changing Fragments. This means that 
there is only one Activity used in the whole app with a frame where the fragments will be placed when 
a draweritem is clicked. The draweritems all lead to overviewpages of the selected section.
The implemented sections are Cards, Relics, Potions, Keywords and Events.
It is also possible to log in, sign up, reset password and change password through he header of the 
navigation drawer.

The Cardpage/RelicPage consists of a Grid of all the Cards/Relics in the game. 
This is done by calling the CardsHelper/RelicsHelper class and running getCards()/getRelics. 
This funcion gathers a list of all the Cards/Relics in the database and uses a callback to 
let the Cardpage/Relicpage fill the list with these Cards/Relics. 
in the toolbar of the Cardpage/Relicpage it is possible to search, filter and sort the 
grid to your likeness. 

When clicked on an Card or Relic it will take you to the detail page. The Card/Relic is gathered
through the CardHelper/RelicHelper by calling getSingleCard()/getSingleRelic().
The detail page has a bottomnavigation with three subpages. The first subpage is the info page where the user can 
find the information about the card/relic. The second subpage is the opinion, this is the place where 
a user can place their own notes, combos and anti-combos. The third subpage is the global page where a user 
can see the opinions of all the users about a card/relic. The detailpage also works with fragments to switch between subpages.

The Potions page is only a simple List of all the potions in the game. This is done by calling the PotionsHelper class
and running getPotions(). This funcion gathers a list of all the potions in the database and uses a callback to 
let the potionspage fill the list with these potions.

The Keyword- and Event page consists of a simple Expendable List of all the Keywords/Events in the game. 
This is done by calling the KeywordsHelper/EventsHelper class and running getKeywords()/getEvents. 
This funcion gathers a list of all the Keywords/Events in the database and uses a callback to 
let the Keywordpage/Eventpage fill the list with these Keywords/Events.

## Challenges and Solutions

One of the initial problems in the process of making this app was the database.
The first site I found to scrape was very unorganized and was incomplete.
I fixed this problem by finding another site which was more up to date. 
If I had more time I would have looked into getting the data directly from the 
game. 

Another problem that I faced was the constant redesigning of my complete app. This was mostly done 
because I had very little experience with designing apps or building them and thus made a bad initial 
design both technical and visual. One of the examples of this is that I found out half way through the 
process that using recycleviews in my app would be much better than using listviews/gridviews. This however
meant that I needed to rebuild a big part of my app. 
Another problem was that my initial design of the detail page was very full and thereby hard to use. 
This made it so I have to reformat the page into three sections.

## Final thoughts
The app is at an okey state at the moment by I am not really satisfied with the result. This is mostly because 
I think there app could have more features and could work better if I worked more efficienty. 
