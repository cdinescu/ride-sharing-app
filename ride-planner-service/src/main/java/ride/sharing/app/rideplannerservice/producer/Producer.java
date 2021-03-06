package ride.sharing.app.rideplannerservice.producer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@EnableBinding(Source.class)
@AllArgsConstructor
@Data
public class Producer {

    private Source mySource;
}
