package ru.nshi.repository;

import java.util.List;

import ru.nshi.error.SongNotFoundException;
import ru.nshi.model.Song;

public interface SongRepository {
    List<Song> findAll();

    Song create(Song song);

    Song getById(Integer id) throws SongNotFoundException;

    Song updateById(Integer id, Song song) throws SongNotFoundException;

    Song deleteById(Integer id) throws SongNotFoundException;
}

