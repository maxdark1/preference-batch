package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.InternalOutboundProcessorDto;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InternalOutboundPreparedStatement implements ItemPreparedStatementSetter<InternalOutboundProcessorDto> {
    @Override
    public void setValues(InternalOutboundProcessorDto item, PreparedStatement ps) throws SQLException {
        ps.setString( 1, item.getEmailAddr());
        ps.setString( 2, item.getCanPtcEffectiveDate());
        ps.setString( 3, item.getCanPtcSourceId());
        ps.setString( 4, item.getEmailStatus());
        ps.setString( 5, item.getCanPtcFlag());
        ps.setString( 6, item.getFirstName());
        ps.setString( 7, item.getLastName());
        ps.setString( 8, item.getLanguagePreference());
        ps.setString( 9, item.getEarlyOptInDate());
        ps.setString( 10, item.getCndCompliantFlag());
        ps.setString( 11, item.getHdCaFlag());
        ps.setString( 12, item.getHdCaGardenClubFlag());
        ps.setString( 13, item.getHdCaProFlag());
        ps.setString( 14, item.getPostalCode());
        ps.setString( 15, item.getCity());
        //ps.setString( 16, item.getCustomerNbr());
        ps.setString( 17, item.getProvince());
    }
}
