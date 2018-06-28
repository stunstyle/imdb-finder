# imdb-finder
client-server search engine app for IMDB

## how to run
1. Run IMDBServer:
`java -cp target/imdb-finder-1.0-SNAPSHOT.jar com.stunstyle.imdb.finder.IMDBServer`

2. Connect one or more clients:
`java -cp target/imdb-finder-1.0-SNAPSHOT.jar com.stunstyle.imdb.finder.IMDBClient`

## supported commands
- get-movie <movie_name> --fields=[field_1,field_2]

   returns info about movie in JSON format
   if movie is not in local cache, it is downloaded via the OMDB API
- get-movies --order=[asc|desc] --genres=[genre_1, genre_2] --actors=[actor_1, actor_2]

   returns info about all movies featuring selected actors
   NOTE: works only for movies located in local cache
- get-tv-series <name> --season=<value>

   returns all episodes of <name> TV series' <value> season
- get-movie-poster <name>

   downloads movie <name>'s poster to local cache
- clear-cache

   clears local cache
