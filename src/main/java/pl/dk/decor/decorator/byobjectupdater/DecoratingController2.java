package pl.dk.decor.decorator.byobjectupdater;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pl.dk.decor.decorator.PlayheadService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping("/deco2")
@AllArgsConstructor
class DecoratingController2 {

    private final RestTemplate restTemplate;

    private final PlayheadService playheadService;

    private final ObjectMapper objectMapper;

    private static final String ORIGINAL_URL = "http://localhost:8080/external/videos?ids={ids}";

    /**
     * Unfortunately merges, not replaces elements in list, which doubles the results
     */
    @GetMapping(value = "/videos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JsonNode getAllVideosDecorated(@RequestParam(name = "ids") List<Integer> ids) throws IOException {
        JsonNode response = getOriginalResponse(ids, JsonNode.class);
        MyEnvelope myEnvelope = objectMapper.treeToValue(response, MyEnvelope.class);
        addTimeStamps(myEnvelope);
        return merge(response, objectMapper.valueToTree(myEnvelope));
    }

    private MyEnvelope addTimeStamps(MyEnvelope envelope) {
        for (MyVideo video: envelope.getItems()) {
            int lastWatchSec = playheadService.getLastWatchSec(video.getId());
            video.setUrl(video.getUrl() + "&t=" + lastWatchSec + "s");
        }
        return envelope;
    }

    private JsonNode merge(JsonNode original, JsonNode override) throws IOException {
        ObjectReader updater = objectMapper.readerForUpdating(original);
        return updater.readValue(override);
    }


    private <T> T getOriginalResponse(List<Integer> ids, Class<T> clazz) {
        String delimetedIds = ids.stream().map(id -> String.valueOf(id)).collect(joining(","));
        return restTemplate.getForObject(ORIGINAL_URL, clazz, Map.of("ids", delimetedIds));
    }
}
