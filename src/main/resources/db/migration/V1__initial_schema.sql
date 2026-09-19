CREATE SEQUENCE users_seq
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE books_seq
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE book_entries_seq
    START WITH 1
    INCREMENT BY 1;


CREATE TABLE users (
       id INTEGER NOT NULL,
       username VARCHAR(255) NOT NULL,
       password VARCHAR(255) NOT NULL,
       role VARCHAR(255) NOT NULL,

       CONSTRAINT pk_users PRIMARY KEY (id),
       CONSTRAINT uk_users_username UNIQUE (username),
       CONSTRAINT ck_users_role
           CHECK (role IN ('USER', 'ADMIN'))
);


CREATE TABLE books (
       id INTEGER NOT NULL,
       title VARCHAR(255) NOT NULL,
       published_date VARCHAR(255),
       description TEXT,
       isbn10 VARCHAR(255),
       isbn13 VARCHAR(255),
       google_book_id VARCHAR(255),
       image_url VARCHAR(2000),

       CONSTRAINT pk_books PRIMARY KEY (id),
       CONSTRAINT uk_books_isbn10 UNIQUE (isbn10),
       CONSTRAINT uk_books_isbn13 UNIQUE (isbn13),
       CONSTRAINT uk_books_google_book_id UNIQUE (google_book_id)
);


CREATE TABLE book_entries (
      id INTEGER NOT NULL,
      book_id INTEGER NOT NULL,
      user_id INTEGER NOT NULL,
      reading_status VARCHAR(255) NOT NULL,
      rating INTEGER,

      CONSTRAINT pk_book_entries PRIMARY KEY (id),

      CONSTRAINT fk_book_entries_book
          FOREIGN KEY (book_id)
              REFERENCES books (id),

      CONSTRAINT fk_book_entries_user
          FOREIGN KEY (user_id)
              REFERENCES users (id),

      CONSTRAINT uk_user_book
          UNIQUE (user_id, book_id),

      CONSTRAINT ck_book_entries_rating
          CHECK (rating IS NULL OR rating BETWEEN 1 AND 5),

      CONSTRAINT ck_book_entries_reading_status
          CHECK (
              reading_status IN (
                     'PLAN_TO_READ',
                     'READING',
                     'COMPLETED',
                     'DROPPED'
                  )
              )
);


CREATE TABLE book_authors (
      book_id INTEGER NOT NULL,
      author VARCHAR(255),

      CONSTRAINT fk_book_authors_book
          FOREIGN KEY (book_id)
              REFERENCES books (id)
);

CREATE INDEX idx_book_authors_book_id
    ON book_authors (book_id);