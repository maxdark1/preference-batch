package ca.homedepot.preference.dto;

import lombok.Data;


/**
 * EmailParametersDTO
 */
@Data
public class EmailParametersDTO
{
	private String subject;

	private String productName;

	private String productImage;

	private String productUrl;

	private Float productRating;

	private Integer numberOfReviews;

	private String environment;

	private String manfactureName;

	private Integer currentYear;

}

