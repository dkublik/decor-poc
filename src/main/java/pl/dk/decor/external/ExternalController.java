package pl.dk.decor.external;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/external")
class ExternalController {

    @GetMapping("/videos")
    Envelope getAllVideos(@RequestParam(name = "ids") List<Integer> ids) {
        List<Video> videos = ids.stream()
            .map(id -> new Video(id, "video " + id, "http://myvideos/video?id=" + id))
            .collect(toList());
        return new Envelope("test response", videos);
    }
}
