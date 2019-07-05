package pl.dk.decor.external;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
class Envelope {

    private Instant created = Instant.now();

    private String description;

    private List<Video> items;

    Envelope(String description, List<Video> items) {
        this.description = description;
        this.items = items;
    }
}
