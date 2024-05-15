package vn.hanguyen.tmdb.data.movie

import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.model.MovieProduction
import vn.hanguyen.tmdb.model.MoviesList


/**
 * Hardcoded movies
 */

val movie1 = Movie(
    id = 823464,
    title = "Godzilla x Kong: The New Empire",
    posterUrl = "https://image.tmdb.org/t/p/original/tMefBSflR6PGQLv7WvFPpKLZkyk.jpg",
    overview = "Following their explosive showdown, Godzilla and Kong must reunite against a colossal undiscovered threat hidden within our world, challenging their very existence – and our own.",
    releaseYear = "2022",
    voteAverage = 6.91f,
    production = MovieProduction(name = "Legendary Pictures", imageUrl = "https://image.tmdb.org/t/p/original/8M99Dkt23MjQMTTWukq4m5XsEuo.png", country = "US")
)
val movie2 = Movie(
    id = 823465,
    title = "Godzilla x Kong: The New Empire",
    posterUrl = "https://image.tmdb.org/t/p/original/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg",
    overview = "Following their explosive showdown, Godzilla and Kong must reunite against a colossal undiscovered threat hidden within our world, challenging their very existence – and our own.",
    releaseYear = "2022",
    voteAverage = 6.91f,
    production = MovieProduction(name = "Legendary Pictures", imageUrl = "https://image.tmdb.org/t/p/original/8M99Dkt23MjQMTTWukq4m5XsEuo.png", country = "US")
)
val movie3 = Movie(
    id = 823466,
    title = "Godzilla x Kong: The New Empire",
    posterUrl = "https://image.tmdb.org/t/p/original/5cCfqeUH2f5Gnu7Lh9xepY9TB6x.jpg",
    overview = "Following their explosive showdown, Godzilla and Kong must reunite against a colossal undiscovered threat hidden within our world, challenging their very existence – and our own.",
    releaseYear = "2022",
    voteAverage = 6.91f,
    production = MovieProduction(name = "Legendary Pictures", imageUrl = "https://image.tmdb.org/t/p/original/8M99Dkt23MjQMTTWukq4m5XsEuo.png", country = "US")
)
val movie4 = Movie(
    id = 823467,
    title = "Godzilla x Kong: The New Empire",
    posterUrl = "https://image.tmdb.org/t/p/original/s5znBQmprDJJ553IMQfwEVlfroH.jpg",
    overview = "Following their explosive showdown, Godzilla and Kong must reunite against a colossal undiscovered threat hidden within our world, challenging their very existence – and our own.",
    releaseYear = "2022",
    voteAverage = 6.91f,
    production = MovieProduction(name = "Legendary Pictures", imageUrl = "https://image.tmdb.org/t/p/original/8M99Dkt23MjQMTTWukq4m5XsEuo.png", country = "US")
)
val movie5 = Movie(
    id = 823468,
    title = "Godzilla x Kong: The New Empire",
    posterUrl = "https://image.tmdb.org/t/p/original/tMefBSflR6PGQLv7WvFPpKLZkyk.jpg",
    overview = "Following their explosive showdown, Godzilla and Kong must reunite against a colossal undiscovered threat hidden within our world, challenging their very existence – and our own.",
    releaseYear = "2022",
    voteAverage = 6.91f,
    production = MovieProduction(name = "Legendary Pictures", imageUrl = "https://image.tmdb.org/t/p/original/8M99Dkt23MjQMTTWukq4m5XsEuo.png", country = "US")
)

val movies: MoviesList =
    MoviesList(
        trendingMovies = listOf(movie1, movie2, movie3),
        searchResultMovies = listOf(movie1, movie4, movie5),
    )
