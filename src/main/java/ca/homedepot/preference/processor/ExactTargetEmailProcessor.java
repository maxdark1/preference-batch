package ca.homedepot.preference.processor;

import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.model.OutEmailOptOuts;
import ca.homedepot.preference.model.OutboundRegistration;
import org.springframework.batch.item.ItemProcessor;

public class ExactTargetEmailProcessor implements ItemProcessor<EmailOptOuts, OutEmailOptOuts> {
    @Override
    public OutEmailOptOuts process(EmailOptOuts item) throws Exception {

        System.out.println(item.toString());
        return null;
    }
}
