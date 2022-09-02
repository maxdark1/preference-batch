package ca.homedepot.preference.writer;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.dto.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class RegistrationAPIWriter implements ItemWriter<RegistrationRequest> {

    private WebClient webClient;

    @Value("${service.preference.baseurl}")
    public String baseUrl;

    public RegistrationAPIWriter()
    {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public void setWebClient(WebClient webClient)
    {
        this.webClient = webClient;
    }


    @Override
    public void write(List<? extends RegistrationRequest> items) throws Exception {

        String path = baseUrl + PreferenceBatchConstants.PREFERENCE_CENTER_REGISTRATION_URL;

        RegistrationResponse response = webClient.post().uri(uriBuilder -> {
                    URI uri = uriBuilder.path(path).build();
                    return uri;
                })
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(items), new ParameterizedTypeReference<List<RegistrationRequest>>() {
                })
                .retrieve().bodyToMono(RegistrationResponse.class).block();

        log.info("Service Response {} :", response.toString());

    }
}
