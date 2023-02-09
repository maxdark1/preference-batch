package ca.homedepot.preference.model;

import lombok.Generated;

@Generated
public class Counters
{
	public String fileName;

	public String date;
	public int quantityRecords;
	public int quantityLoaded;
	public int quantityFailed;

	public Counters(int quantityRecords, int quantityLoaded, int quantityFailed)
	{
		this.quantityRecords = quantityRecords;
		this.quantityLoaded = quantityLoaded;
		this.quantityFailed = quantityFailed;
	}
}
