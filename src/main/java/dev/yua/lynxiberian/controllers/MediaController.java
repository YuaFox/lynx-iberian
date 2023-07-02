package dev.yua.lynxiberian.controllers;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dev.yua.lynxiberian.models.Media;
import dev.yua.lynxiberian.repositories.MediaRepository;
import dev.yua.lynxiberian.utils.ImageRender;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    @Autowired
    private MediaRepository repository;


    @GetMapping({"","/"})
    public List<Media> index() {
        Iterator<Media> source = this.repository.findAll().iterator();
        List<Media> target = new ArrayList<>();
        source.forEachRemaining(target::add);
        return target;
    }

    @GetMapping(path = "/{id}")
    public Media get(@PathVariable(name="id") long id) {
        return this.repository.findById(id).get();
    }

    @GetMapping(path = "/{id}/file")
    public void getFile(@PathVariable(name="id") long id, HttpServletResponse response) throws IOException {
        Media media = this.repository.findById(id).get();
        response.setContentType(
                URLConnection.guessContentTypeFromName(media.getPath())
        );
        org.apache.commons.io.IOUtils.copy(new FileInputStream(media.getPath()), response.getOutputStream());
    }

    @GetMapping(path = "/{id}/file/render/full")
    public void getFileRendered(@PathVariable(name="id") long id, HttpServletResponse response) throws IOException, FontFormatException {
        Media media = this.repository.findById(id).get();
        String path = media.getPath();
        File render = ImageRender.render(new File(media.getPath()), media.getSource());
        response.setContentType(
                URLConnection.guessContentTypeFromName(media.getPath())
        );
        org.apache.commons.io.IOUtils.copy(new FileInputStream(render), response.getOutputStream());
    }

    /*
    @DeleteMapping(path = "/{name}")
    public ResponseEntity<String> delete(@PathVariable(name="name") String name){
        Bucket bucket = repository.getBucketByName(name);
        if(bucket != null){
            repository.delete(bucket);
            return ResponseEntity.status(HttpStatus.OK).body("Ok");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bucket does not exists");
        }
    }
    */
}