package com.springmvc.dataaccess.repository.tenant;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.main.EntityMapper;
import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Vehiculo;
import com.springmvc.entities.tenant.Viaje;
import com.springmvc.entities.tenant.ViajeUbicacion;
import com.springmvc.entities.tenant.ViajesBuscados;

public class ViajeUbicacionRepository {

	EntityManager entityManager;
	
	public ViajeUbicacionRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public ViajeUbicacion GetLast(long travelId)
	{
		ViajeUbicacion travelLoc = null;
		Query q = entityManager.createQuery("FROM ViajeUbicacion v WHERE v.viaje.id_viaje = :idv ORDER BY v.fecha DESC");
		q.setParameter("idv", travelId);
		q.setMaxResults(1);
		try
		{
			travelLoc = (ViajeUbicacion)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return travelLoc;
	}

	public void InsertTravelLoc(ViajeUbicacion travelLoc) 
	{
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();	
			entityManager.persist(travelLoc);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
}
