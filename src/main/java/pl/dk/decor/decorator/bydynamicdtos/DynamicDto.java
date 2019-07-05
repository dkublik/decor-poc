package pl.dk.decor.decorator.bydynamicdtos;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

class DynamicDto {

    @JsonIgnore
    private Map<String, Object> dontCare = new HashMap<>();

    @JsonAnySetter
    void dontCare(String key, Object value) {
        dontCare.put(key, value);
    }

    @JsonAnyGetter
    Map<String, Object> dontCare() {
        return dontCare;
    }
}
