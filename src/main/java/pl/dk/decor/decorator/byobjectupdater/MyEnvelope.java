package pl.dk.decor.decorator.byobjectupdater;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class MyEnvelope {

    private List<MyVideo> items;
}
