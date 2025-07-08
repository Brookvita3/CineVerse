  CREATE TABLE actor (
      id UUID PRIMARY KEY,
      name VARCHAR(255) NOT NULL
  );

  CREATE TABLE movie (
      id UUID PRIMARY KEY,
      title VARCHAR(255) NOT NULL,
      description TEXT NOT NULL,
      release_date TIMESTAMP,
      thumbnail_url VARCHAR(255),
      video_file_name VARCHAR(255),
      is_public BOOLEAN,
      created_at TIMESTAMP,
      updated_at TIMESTAMP
  );

  CREATE TABLE movie_genres (
      movie_id UUID REFERENCES movie(id) ON DELETE CASCADE,
      genres VARCHAR(50)
  );

  CREATE TABLE movie_actor (
      movie_id UUID REFERENCES movie(id) ON DELETE CASCADE,
      actor_id UUID REFERENCES actor(id) ON DELETE CASCADE,
      PRIMARY KEY (movie_id, actor_id)
  );

  ALTER TABLE movie
  ADD COLUMN status VARCHAR(20);
