package ca.homedepot.preference.util.validation;

import ca.homedepot.preference.model.InboundRegistration;

public class RegistrationValidator {

    public boolean isRequiredFieldsFills(InboundRegistration inboundRegistration){

        return inboundRegistration.getLanguage_pref() != null && inboundRegistration.getAsOfDate() != null && inboundRegistration.getEmailPermission() != null &&
                inboundRegistration.getMailPermission() != null && inboundRegistration.getEmailPrefHdCa() != null && inboundRegistration.getEmailPrefGardenClub() != null ;


    }
}
