package com.springmvc.requestWrappers;

import java.util.Calendar;
import java.util.List;

public class TravelSearchWrapper
{
	public Calendar dateFrom;
	public Calendar dateTo;
	public List<Integer> origins;
	public List<Integer> destinations;
	public int ticketsCount;
}
