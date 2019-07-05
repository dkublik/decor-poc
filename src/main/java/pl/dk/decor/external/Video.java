package pl.dk.decor.external;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Video {

    private int id;

    private String description;

    private String url;
}
