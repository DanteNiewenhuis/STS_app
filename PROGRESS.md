## Dag 1: 4 juni

README.md gemaakt, Al mijn classes in android studio gemaakt.

## Dag 2: 5 juni

DESIGN.md begonnen, begonnen met de scraper en de firebase database.
Een aantal paginas al gemaakt.

## Dag 3: 6 juni

bijna alle pagina's zijn nu op basis niveau af. DESIGN.md afgemaakt.
scraper is af, hij pushed nu alles naar de firebase database.
PROBLEEM: ItemClickListener doet het bij de cardActivity het niet.

## Dag 4: 7 juni

OPLOSSING: ITEMCLICKLISTENER deed het niet omdat in de listitems ook nog clickbare dingen waren.
alle paginas voor het prototype zijn nu klaar.

## Dag 5: 8 juni

PROBLEEM: app zou veel sneller moeten kunnen lopen.
OPLOSSING: Ik ga proberen de scraper te veranderen zodat hij niet alles naar firebase 
pushed maar als json in de app zelf opslaat, en de fotos download zodat hij niet alles van internet hoeft te halen.
EDIT: Dit heb ik uiteindelijk toch niet gedaan omdat dit de app veel groter zou maken en niet veel sneller zou maken.

## Dag 6: 11 juni

Accounts in Firebase geimplementeerd, het is nu mogelijk om in te loggen en een account te maken.
Probleem: door de hoeveelheid paginas in mijn app was mijn initiele design niet meer handig.
Oplossing: implementeer een naviagion drawer, hier ben ik vandaag mee begonnen.

## Dag 7: 12 juni
account interacties in de header van de navigation drawer verwerken zodat er vanuit elke pagina 
makkelijk kan worden ingelogd.
Probleem: Dit blijkt erg moeilijk te zijn omdat het niet mogelijk is om fragments te veranderen 
in de header aangezien dit niet een activity of fragment is.
Oplossing: bij elke verandering de huidige header verwijderen en er een nieuwe neerzetten. 
Deze oplossing werkt maar betekent wel dat mijn main activity vrij groot is.

## Dag 8: 13 juni
begonnen met de cardfragment filterbaar en sorteerbaar maken. 
Probleem: Dit is moeilijk te doen via firebase zelf omdat de database het niet toelaat om meer dan één 
voorwaarde te stellen aan een zoekopdracht
Oplossing: Filter/Sorteer de verkregen data zelf op de mobiel.

Ik wou ook kijken of ik mijn lijsten zo kan maken dat alleen de eerste paar resultaten worden geladen 
en er meer wordt geladen als er naar beneden wordt gescrolled.
Probleem: Dit is niet echt mogelijk als ik het filteren op de mobiel zelf doen
Oplossing: gewoon de oude versie houden

## dag 9: 14 juni
begonnen met het maken van clickable spans, dit zijn stukje van tekst die geklikt kunnen worden om 
meer informatie te krijgen over het object.
Probleem: Ik heb een lijst nodig van alle dingen die klikbaar moeten worden
Oplossing: momenteel nog geen

## dag 10: 15 juni
code verspreid over alle paginas, dus alle features die aan de cardpagina zijn toegevoegd ook aan relics toevoegen enz.

## dag 11: 18 juni
scraper updaten om de nieuw gevonden database te gebruiken. Omdat ik nu de volledige plaatjes van de kaarten heb ga 
ik de layout van mijn Cardview veranderen naar een grid. Ik ga hier een recycleview voor gebruiken

## dag 12: 19 juni
de detailview van de kaarten interactief maken zodat het niet alleen maar placeholders zijn.
Probleem: Het Fragment van dat de content van de pagina was werkte nog niet goed want de eerste fragment bleek altijd staan
Oplossing: implaats van een fragment moest er een FrameView staan in de contentpage.

## dag 13: 20 juni
Globale functie en Globale variabele classes aangemaakt. Card detail afgemaakt. aantal kleine aanpassing gedaan.

## dag 14: 21 juni
Alle paginas die nog niet af zijn af maken. 

## dag 15: 22 juni
een global opinion pagina maken voor en Card/Relic.
Probleem: het is vrij onhandig om snel alle info van een opinion van een card van iemand te krijgen.
Oplossing: herindelen van de firebase database in Opinions en Info. in de Info staan alleen de informatie van de objecten uit het spel. In de Opinions staan de meningen van de users erover.

## dag 16: 25 juni
detail layout updaten zodat het drie schermen wordt om te zorgen dat het overzichtelijker is.
Probleem: mijn gradle wilt niet echt meewerken :(
Oplossing: veel syncen en een paar versies van imports aanpassen

## dag 17: 26 juni
search, filter en sort opties in de toonbalk krijgen. Bugs fixen.

## dag 18: 27 juni
bugs wegwerken, UI updaten. Code opschonen

## dag 19: 28 juni
bugs wegwerken, REPORT.md schrijven. Code opschonen
