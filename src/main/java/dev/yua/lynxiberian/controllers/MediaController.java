package dev.yua.lynxiberian.controllers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;
import java.util.List;
import dev.yua.lynxiberian.models.Media;
import dev.yua.lynxiberian.repositories.MediaRepository;
import dev.yua.lynxiberian.utils.ImageRender;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    @Autowired
    private MediaRepository repository;

    @GetMapping({"","/"})
    public List<Media> index(
            @RequestParam(value = "bucket", defaultValue = "*") String bucket,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        if(bucket.equals("*")){
            return repository.findAll(PageRequest.of(page, 20)).toList();
        }
        return repository.findAll(PageRequest.of(page, 20)).stream().filter(
                (Media media) -> media.getBucket().getName().equals(bucket)
        ).toList();
    }

    @GetMapping("/random")
    public Media random(
            @RequestParam(value = "bucket", defaultValue = "*") String bucket
    ) {
        if(bucket.equals("*")){
            return repository.getRandomMedia();
        }else{
            return null;
        }
    }

    @GetMapping("/size")
    public JSONObject size(
            @RequestParam(value = "bucket", defaultValue = "*") String bucket
    ) {
        if(bucket.equals("*")){
            return new JSONObject().put("size", repository.getSize());
        }else{
            return new JSONObject().put("size", 0);
        }
    }

    @GetMapping(path = "/{id}")
    public Media get(@PathVariable(name="id") long id) {
        return this.repository.findById(id).get();
    }

    @DeleteMapping(path = "/{id}")
    public void delete(
            @PathVariable(name="id") long id,
            HttpServletResponse response
    ) {
        Media m = this.repository.findById(id).get();
        this.repository.delete(m);
        response.setStatus(200);
    }

    @GetMapping(path = "/{id}/file")
    public void getFile(
            @PathVariable(name="id") long id,
            @RequestParam(value = "format", defaultValue = "original") String format,
            @RequestParam(value = "text", defaultValue = "none") String text,
            HttpServletResponse response
    ) throws IOException, FontFormatException {
        Media media = this.repository.findById(id).get();
        BufferedImage image = ImageIO.read(new File(media.getPath()));
        if(format.equals("vertical")){
            image = ImageRender.renderVertical(image);
        }

        if(text.equals("source")){
            image = ImageRender.renderText(image, media.getSource());
        }

        response.setContentType(
                URLConnection.guessContentTypeFromName(media.getPath())
        );

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        org.apache.commons.io.IOUtils.copy(new ByteArrayInputStream(os.toByteArray()), response.getOutputStream());
    }
}