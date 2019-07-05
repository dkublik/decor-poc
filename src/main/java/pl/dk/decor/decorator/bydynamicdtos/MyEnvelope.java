package pl.dk.decor.decorator.bydynamicdtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class MyEnvelope extends DynamicDto {

    private List<MyVideo> items;
}
