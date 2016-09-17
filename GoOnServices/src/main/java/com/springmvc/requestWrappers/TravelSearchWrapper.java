package com.springmvc.requestWrappers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TravelSearchWrapper
{
	public Date dateFrom;
	public Date dateTo;
	public List<Integer> origins;
	public List<Integer> destinations;
	public int ticketsCount;
}
