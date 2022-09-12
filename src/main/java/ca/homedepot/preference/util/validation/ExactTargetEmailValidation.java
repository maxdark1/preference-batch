package ca.homedepot.preference.util.validation;

public class ExactTargetEmailValidation
{

    public static String getExactTargetStatus(String status)
    {

        if(status.equalsIgnoreCase("unsubscribed"))
            return "98";
        // In case is 'held'
        return "50";

    }

    public static String getSourceId(String reason)
    {
        String reasonUp = reason.toUpperCase();

        if(reasonUp.contains("AOL"))
            return "189";
        if (reasonUp.contains("SCAMCOP") || reasonUp.contains("SPAM COP REPORT"))
            return "190";

        return "188";
    }
}
