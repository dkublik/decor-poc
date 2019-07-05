package pl.dk.decor.decorator.bydynamicdtos;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pl.dk.decor.decorator.PlayheadService;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping("/deco3")
@AllArgsConstructor
class DecoratingController3 {

    private final RestTemplate restTemplate;

    private final PlayheadService playheadService;

    private static final String ORIGINAL_URL = "http://localhost:8080/external/videos?ids={ids}";

    @GetMapping(value = "/videos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    MyEnvelope getAllVideosDecorated(@RequestParam(name = "ids") List<Integer> ids) {
        MyEnvelope response = getOriginalResponse(ids, MyEnvelope.class);
        addTimeStamps(response);
        return response;
    }

    private MyEnvelope addTimeStamps(MyEnvelope envelope) {
        for (MyVideo video: envelope.getItems()) {
            int lastWatchSec = playheadService.getLastWatchSec(video.getId());
            video.setUrl(video.getUrl() + "&t=" + lastWatchSec + "s");
        }
        return envelope;
    }

    private <T> T getOriginalResponse(List<Integer> ids, Class<T> clazz) {
        String delimetedIds = ids.stream().map(id -> String.valueOf(id)).collect(joining(","));
        return restTemplate.getForObject(ORIGINAL_URL, clazz, Map.of("ids", delimetedIds));
    }
}
