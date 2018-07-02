# imdb-finder
> A Java console client-server search engine app for the International Movie Database.
<hr>

## Prerequisites
You need JRE 9+ installed to run this project.
Download the latest JRE here:

http://www.oracle.com/technetwork/java/javase/downloads/index.html
## Installation
Clone this repository to your computer:

`git clone https://github.com/stunstyle/imdb-finder`

## How to run
1. Run IMDBServer:


`java -cp target/imdb-finder-1.0-SNAPSHOT.jar com.stunstyle.imdb.finder.IMDBServer`

2. Connect one or more clients:


`java -cp target/imdb-finder-1.0-SNAPSHOT.jar com.stunstyle.imdb.finder.IMDBClient`

## Usage
These commands need to be entered in a client's console.
- get-movie <movie_name> --fields=[field_1,field_2]

   *returns info about movie in JSON format
   
   if movie is not in local cache, it is downloaded via the OMDB API*
   
   ![get-movie-example](assets/get-movie-example.png?raw=true "Example for get-movie")

- get-movies --order=[asc|desc] --genres=[genre_1, genre_2] --actors=[actor_1, actor_2]

   *returns info about all movies featuring selected actors
   
   NOTE: works only for movies located in local cache*
   
   ![get-movies-example](assets/get-movies-example.png?raw=true "Example for get-movies")
- get-tv-series <name> --season=<value>

   *returns all episodes of <name> TV series' <value> season*
  
   ![get-tv-series-example](assets/get-movies-example.png?raw=true "Example for get-tv-series")
- get-movie-poster <name>

   *downloads movie <name>'s poster to local cache*
- clear-cache

   *clears local cache*
