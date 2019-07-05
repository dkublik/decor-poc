package pl.dk.decor.decorator.bypatterns;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping("/deco1")
@AllArgsConstructor
class DecoratingController1 {

    private final RestTemplate restTemplate;

    private final PlayheadService playheadService;

    private final ObjectMapper objectMapper;

    private static final String ORIGINAL_URL = "http://localhost:8080/external/videos?ids={ids}";

    @GetMapping(value = "/videos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String getAllVideosDecorated(@RequestParam(name = "ids") List<Integer> ids) {
        String response = getOriginalResponse(ids, String.class);
        response = addTimeStamps(response, ids);
        return response;
    }

    private String addTimeStamps(String response, List<Integer> ids) {
        for (Integer id: ids) {
            response = addTimeStamp(response, id);
        }
        return response;
    }

    private String addTimeStamp(String response, int id) {
        String patternString = "\\\"url\\\":\\\"https?://.*" + id + ".*?\\\"";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            String group = matcher.group();
            int lastWatchSec = playheadService.getLastWatchSec(id);
            response = response.replace(group, group.substring(0, group.length() - 1) + "&t=" + lastWatchSec + "s\"");
        }
        return response;
    }

    private <T> T getOriginalResponse(List<Integer> ids, Class<T> clazz) {
        String delimetedIds = ids.stream().map(id -> String.valueOf(id)).collect(joining(","));
        return restTemplate.getForObject(ORIGINAL_URL, clazz, Map.of("ids", delimetedIds));
    }
}
