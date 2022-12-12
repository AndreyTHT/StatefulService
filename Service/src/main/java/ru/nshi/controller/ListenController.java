package ru.nshi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.nshi.error.InvalidParametersException;
import ru.nshi.error.SongNotFoundException;
import ru.nshi.model.Song;
import ru.nshi.model.ErrorResponse;
import ru.nshi.model.ListenSong;
import ru.nshi.model.ListenSongs;
import ru.nshi.service.SongService;

@RestController
@RequestMapping(
        path = "/songs",
        produces = MimeTypeUtils.APPLICATION_JSON_VALUE
)
public class ListenController {
    @Autowired
    private SongService service;

    @GetMapping("/listen")
    public List<Song> getSortedSongsByAuditions(
            @RequestParam(name = "limit", defaultValue = "5") Integer resultCount
    ) throws InvalidParametersException {
        if (resultCount == null || resultCount < 1) {
            throw new InvalidParametersException(
                    "resultCount can not be null or less than one"
            );
        }

        var songs = service.getSongs();
        songs.sort((a, b) -> b.getAuditions() - a.getAuditions());
        return songs.subList(0, Math.min(resultCount, songs.size()));
    }

    @PostMapping("/listen")
    public List<Song> listenSongByIds(
            @RequestBody ListenSongs listenSongs
    ) throws InvalidParametersException {
        if (listenSongs.getAuditions() == null ||
                listenSongs.getAuditions() < 1) {
            throw new InvalidParametersException(
                    "Song auditions can not be less than one or null"
            );
        }

        var songs = new ArrayList<Song>(listenSongs.getSongs().size());
        for (int songId : listenSongs.getSongs()) {
            Song song;
            try {
                song = service.getById(songId);
                song.setAuditions(
                        song.getAuditions() + listenSongs.getAuditions()
                );
                service.updateById(songId, song);
            } catch (SongNotFoundException ex) {
                continue;
            }

            songs.add(song);
        }

        return songs;
    }

    @PostMapping("/{id}/listen")
    public Song listenSongById(
            @PathVariable Integer id,
            @RequestBody ListenSong listenSong
    ) throws SongNotFoundException, InvalidParametersException {
        if (listenSong.getAuditions() == null ||
                listenSong.getAuditions() < 1) {
            throw new InvalidParametersException(
                    "Song auditions can not be less than one or null"
            );
        }

        var song = service.getById(id);
        song.setAuditions(
                song.getAuditions() + listenSong.getAuditions()
        );
        service.updateById(id, song);

        return song;
    }

    @ExceptionHandler(SongNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(SongNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(InvalidParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFoundException(InvalidParametersException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}

