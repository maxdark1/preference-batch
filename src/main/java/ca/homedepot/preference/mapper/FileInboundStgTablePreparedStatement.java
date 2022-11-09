package ca.homedepot.preference.mapper;

import ca.homedepot.preference.model.FileInboundStgTable;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FileInboundStgTablePreparedStatement implements ItemPreparedStatementSetter<FileInboundStgTable>
{
	@Override
	public void setValues(FileInboundStgTable item, PreparedStatement ps) throws SQLException
	{
		ps.setBigDecimal(1, item.getFileId());
		ps.setString(2, item.getStatus());
		ps.setBigDecimal(3, item.getSourceId());
		ps.setString(4, item.getSrcPhoneNumber());
		ps.setString(5, item.getSrcFirstName());
		ps.setString(6, item.getSrcMiddleInitial());
		ps.setString(7, item.getSrcLastName());
		ps.setString(8, item.getSrcAddress1());
		ps.setString(9, item.getSrcAddress2());
		ps.setString(10, item.getSrcCity());
		ps.setString(11, item.getSrcState());
		ps.setString(12, item.getSrcPostalCode());
		ps.setString(13, item.getSrcLanguagePref());
		ps.setString(14, item.getSrcEmailAddress());
		ps.setString(15, item.getSrcTitleName());
		ps.setString(16, item.getPhonePref());
		ps.setString(17, item.getEmailAddressPref());
		ps.setString(18, item.getMailAddressPref());
		ps.setTimestamp(19, new Timestamp(item.getSrcDate().getTime()));
		ps.setBigDecimal(20, item.getEmailStatus());
		ps.setString(21, item.getSrcPhoneExtension());
		ps.setString(22, item.getEmailPrefHdCa());
		ps.setString(23, item.getEmailPrefGardenClub());
		ps.setString(24, item.getEmailPrefPro());
		ps.setString(25, item.getEmailPrefNewMover());
		ps.setString(26, item.getCellSmsFlag());
		ps.setString(27, item.getBusinessName());
		ps.setString(28, item.getCustomerNbr());
		ps.setString(29, item.getOrgName());
		ps.setString(30, item.getStoreNbr());
		ps.setString(31, item.getCustTypeCd());
		ps.setString(32, item.getContent1());
		ps.setString(33, item.getValue1());
		ps.setString(34, item.getContent2());
		ps.setString(35, item.getValue2());
		ps.setString(36, item.getContent3());
		ps.setString(37, item.getValue3());
		ps.setString(38, item.getContent4());
		ps.setString(39, item.getValue4());
		ps.setString(40, item.getContent5());
		ps.setString(41, item.getValue5());
		ps.setString(42, item.getContent6());
		ps.setString(43, item.getValue6());
		ps.setString(44, item.getContent7());
		ps.setString(45, item.getValue7());
		ps.setString(46, item.getContent8());
		ps.setString(47, item.getValue8());
		ps.setString(48, item.getContent9());
		ps.setString(49, item.getValue9());
		ps.setString(50, item.getContent10());
		ps.setString(51, item.getValue10());
		ps.setString(52, item.getContent11());
		ps.setString(53, item.getValue11());
		ps.setString(54, item.getContent12());
		ps.setString(55, item.getValue12());
		ps.setString(56, item.getContent13());
		ps.setString(57, item.getValue13());
		ps.setString(58, item.getContent14());
		ps.setString(59, item.getValue14());
		ps.setString(60, item.getContent15());
		ps.setString(61, item.getValue15());
		ps.setString(62, item.getContent16());
		ps.setString(63, item.getValue16());
		ps.setString(64, item.getContent17());
		ps.setString(65, item.getValue17());
		ps.setString(66, item.getContent18());
		ps.setString(67, item.getValue18());
		ps.setString(68, item.getContent19());
		ps.setString(69, item.getValue19());
		ps.setString(70, item.getContent20());
		ps.setString(71, item.getValue20());
		ps.setString(72, item.getInsertedBy());
		ps.setTimestamp(73, new Timestamp(item.getInsertedDate().getTime()));
	}
}
