package pl.dk.decor.decorator.bydynamicdtos;

import lombok.Data;

@Data
class MyVideo extends DynamicDto {

    private int id;

    private String url;
}
