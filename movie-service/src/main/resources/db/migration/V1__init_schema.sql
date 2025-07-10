CREATE TABLE actor (
  id VARCHAR(7) PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE movie (
  id VARCHAR(7) PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  release_date TIMESTAMP,
  thumbnail_url VARCHAR(255),
  movie_file_name VARCHAR(255),
  is_public BOOLEAN,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  status VARCHAR(20)
);

CREATE TABLE movie_genres (
  movie_id VARCHAR(7) REFERENCES movie(id) ON DELETE CASCADE,
  genres VARCHAR(50)
);

CREATE TABLE movie_actor (
  movie_id VARCHAR(7) REFERENCES movie(id) ON DELETE CASCADE,
  actor_id VARCHAR(7) REFERENCES actor(id) ON DELETE CASCADE,
  PRIMARY KEY (movie_id, actor_id)
);
