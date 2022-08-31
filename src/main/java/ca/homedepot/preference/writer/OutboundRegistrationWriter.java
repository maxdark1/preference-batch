package ca.homedepot.preference.writer;

import ca.homedepot.preference.model.OutboundRegistration;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class OutboundRegistrationWriter implements ItemWriter<OutboundRegistration> {

    @Override
    public void write(List<? extends OutboundRegistration> items) throws Exception {

    }
}
