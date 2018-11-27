CREATE TABLE users (
    user_id  int AUTO_INCREMENT,
    username     text,
    user_password text,
    firstname    text,
    lastname     text,
    editor   boolean,
    login    boolean,
    last_login datetime,
    PRIMARY KEY(user_id)
);
 
CREATE TABLE music (
    nmusic       int AUTO_INCREMENT,
    m_name         text,
    day_of_creation date,
    lyrics       text,
    artist_nartist   int NOT NULL,
    users_user_id    int NOT NULL,
    PRIMARY KEY(nmusic)
);
 
CREATE TABLE artist (
    nartist  int AUTO_INCREMENT,
    a_name         text,
    date_of_birth date,
    a_description  text,
    PRIMARY KEY(nartist)
);
 
CREATE TABLE album (
    nalbum       int AUTO_INCREMENT,
    album_name text,
    nmusic       int,
    nartist  boolean,
    day_of_creation date,
    a_description  text,
    editor       text,
    artist_nartist   int NOT NULL,
    PRIMARY KEY(nalbum)
);
 
CREATE TABLE critique (
    c_text         text,
    rating   smallint,
    album_nalbum     int,
    users_user_id int,
    PRIMARY KEY(album_nalbum,users_user_id)
);
 
CREATE TABLE band (
    artist_nartist int,
    PRIMARY KEY(artist_nartist)
);
 
CREATE TABLE singer (
    artist_nartist int,
    PRIMARY KEY(artist_nartist)
);
 
CREATE TABLE composer (
    music_nmusic     int NOT NULL,
    artist_nartist int,
    PRIMARY KEY(artist_nartist)
);
 
CREATE TABLE shared (
    id_shareduser int,
    users_user_id int,
    PRIMARY KEY(id_shareduser,users_user_id)
);
 
CREATE TABLE playlist (
    nplaylist    int AUTO_INCREMENT,
    p_name text,
    private  boolean,
    users_user_id int,
    PRIMARY KEY(nplaylist,users_user_id)
);
 
CREATE TABLE url (
    url      text,
    music_nmusic int,
    PRIMARY KEY(music_nmusic)
);
 
CREATE TABLE playlist_music (
    playlist_nplaylist   int,
    music_nmusic         int,
    PRIMARY KEY(playlist_nplaylist,music_nmusic)
);
 
CREATE TABLE music_album (
    music_nmusic int,
    album_nalbum int,
    PRIMARY KEY(music_nmusic,album_nalbum)
);
 
ALTER TABLE users ENGINE=InnoDB;
ALTER TABLE music ENGINE=InnoDB;
ALTER TABLE music_album ENGINE=InnoDB;
ALTER TABLE album ENGINE=InnoDB;
ALTER TABLE artist ENGINE=InnoDB;
ALTER TABLE band ENGINE=InnoDB;
ALTER TABLE composer ENGINE=InnoDB;
ALTER TABLE critique ENGINE=InnoDB;
ALTER TABLE playlist ENGINE=InnoDB;
ALTER TABLE playlist_music ENGINE=InnoDB;
ALTER TABLE shared ENGINE=InnoDB;
ALTER TABLE singer ENGINE=InnoDB;
ALTER TABLE url ENGINE=InnoDB;
 
 
ALTER TABLE music ADD CONSTRAINT music_fk1 FOREIGN KEY (artist_nartist) REFERENCES artist(nartist);
ALTER TABLE music ADD CONSTRAINT music_fk2 FOREIGN KEY (users_user_id) REFERENCES users(user_id);
ALTER TABLE album ADD CONSTRAINT album_fk1 FOREIGN KEY (artist_nartist) REFERENCES artist(nartist);
ALTER TABLE critique ADD CONSTRAINT critique_fk1 FOREIGN KEY (album_nalbum) REFERENCES album(nalbum) ON DELETE CASCADE;
ALTER TABLE critique ADD CONSTRAINT critique_fk2 FOREIGN KEY (users_user_id) REFERENCES users(user_id) ON DELETE CASCADE;
ALTER TABLE band ADD CONSTRAINT band_fk1 FOREIGN KEY (artist_nartist) REFERENCES artist(nartist);
ALTER TABLE singer ADD CONSTRAINT singer_fk1 FOREIGN KEY (artist_nartist) REFERENCES artist(nartist);
ALTER TABLE composer ADD CONSTRAINT composer_fk1 FOREIGN KEY (music_nmusic) REFERENCES music(nmusic);
ALTER TABLE composer ADD CONSTRAINT composer_fk2 FOREIGN KEY (artist_nartist) REFERENCES artist(nartist);
ALTER TABLE shared ADD CONSTRAINT shared_fk1 FOREIGN KEY (users_user_id) REFERENCES users(user_id);
ALTER TABLE playlist ADD CONSTRAINT playlist_fk1 FOREIGN KEY (users_user_id) REFERENCES users(user_id);
ALTER TABLE url ADD CONSTRAINT url_fk1 FOREIGN KEY (music_nmusic) REFERENCES music(nmusic);
ALTER TABLE playlist_music ADD CONSTRAINT playlist_music_fk1 FOREIGN KEY (playlist_nplaylist) REFERENCES playlist(nplaylist) ON DELETE CASCADE;
ALTER TABLE playlist_music ADD CONSTRAINT playlist_music_fk2 FOREIGN KEY (music_nmusic) REFERENCES music(nmusic) ON DELETE CASCADE;
ALTER TABLE music_album ADD CONSTRAINT music_album_fk1 FOREIGN KEY (music_nmusic) REFERENCES music(nmusic);
ALTER TABLE music_album ADD CONSTRAINT music_album_fk2 FOREIGN KEY (album_nalbum) REFERENCES album(nalbum);

START TRANSACTION;
INSERT INTO users (username, user_password, firstname, lastname) VALUES ("user1", "pass1", "name1", "name2");
INSERT INTO users (username, user_password, firstname, lastname) VALUES ("user2", "pass2", "name1", "name2");
INSERT INTO users (username, user_password, firstname, lastname) VALUES ("user3", "pass3", "name1", "name2");

INSERT INTO artist (a_name) VALUES ("artist1");
INSERT INTO artist (a_name) VALUES ("artist2");
INSERT INTO artist (a_name) VALUES ("artist3");

INSERT INTO music (m_name, artist_nartist, users_user_id) VALUES ("music1", 1, 1);
INSERT INTO music (m_name, artist_nartist, users_user_id) VALUES ("music2", 1, 1);
INSERT INTO music (m_name, artist_nartist, users_user_id) VALUES ("music3", 1, 1);
INSERT INTO music (m_name, artist_nartist, users_user_id) VALUES ("music4", 1, 1);

INSERT INTO playlist (p_name, private, users_user_id) VALUES ("play1", 1, 1);
INSERT INTO playlist (p_name, private, users_user_id) VALUES ("play2", 1, 2);
COMMIT;