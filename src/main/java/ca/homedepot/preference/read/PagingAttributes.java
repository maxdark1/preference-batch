package ca.homedepot.preference.read;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Setter
@Getter
public class PagingAttributes
{

	/**
	 * Paging attributes for CRM
	 */
	@Value("${preference.centre.outboundCRM.pageSize}")
	private Integer pageSizeCRM;

	@Value("${preference.centre.outboundCRM.fetchSize}")
	private Integer fetchSizeCRM;

	/**
	 * Paging Attributes for Citi
	 */
	@Value("${preference.centre.outboundCiti.pageSize}")
	private Integer pageSizeCiti;

	@Value("${preference.centre.outboundCiti.fetchSize}")
	private Integer fetchSizeCiti;

	/**
	 * Paging Attributes for SFMC
	 */
	@Value("${preference.centre.outboundSalesforce.pageSize}")
	private Integer pageSizeSFMC;

	@Value("${preference.centre.outboundSalesforce.fetchSize}")
	private Integer fetchSizeSFMC;


	/**
	 * Paging Attributes for Internal
	 */
	@Value("${preference.centre.outboundInternal.pageSize}")
	private Integer pageSizeInternal;

	@Value("${preference.centre.outboundInternal.fetchSize}")
	private Integer fetchSizeInternal;

	/**
	 * Paging Attributes for Loyalty Compliant Weekly
	 */
	@Value("${preference.centre.outboundLoyalty.pageSize}")
	private Integer pageSizeLoyalty;

	@Value("${preference.centre.outboundInternal.fetchSize}")
	private Integer fetchSizeLoyalty;

	/**
	 *
	 */
	@Value("${preference.centre.outboundFlexAttributes.pageSize}")
	private Integer pageSizeFlexAttributes;

	@Value("${preference.centre.outboundFlexAttributes.fetchSize}")
	private Integer fetchSizeFlexAttributes;

	public PostgresPagingQueryProvider queryProvider(String key, String query)
	{
		Map<String, Order> orderKeys = new HashMap<>();
		orderKeys.put(key, Order.ASCENDING);
		PostgresPagingQueryProvider postgresPagingQueryProvider = new PostgresPagingQueryProvider();
		postgresPagingQueryProvider.setSelectClause("*");
		postgresPagingQueryProvider.setFromClause(getFromClause(query));
		postgresPagingQueryProvider.setSortKeys(orderKeys);
		return postgresPagingQueryProvider;
	}

	private String getFromClause(String query)
	{
		return "(" + query + ") AS RETURN_TABLE";
	}

}
