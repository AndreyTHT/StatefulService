package ru.nshi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.nshi.error.SongNotFoundException;
import ru.nshi.model.Song;
import ru.nshi.repository.SongRepository;

@Service
public class SongService {
    @Autowired
    private SongRepository repo;

    public List<Song> getSongs() {
        return repo.findAll();
    }

    public Song create(Song song) {
        return repo.create(song);
    }

    public Song getById(Integer id) throws SongNotFoundException {
        return repo.getById(id);
    }

    public Song updateById(Integer id, Song song) throws SongNotFoundException {
        return repo.updateById(id, song);
    }

    public Song deleteById(Integer id) throws SongNotFoundException {
        return repo.deleteById(id);
    }
}