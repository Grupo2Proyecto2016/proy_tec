package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Parametro;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.enums.UserRol;

public class ParametroRepository
{
	EntityManager entityManager;
	
	public ParametroRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public Parametro FindById(int id)
	{
		Parametro parameter = null;
		parameter = entityManager.find(Parametro.class, id);
		return parameter;
	}
	
	public void InsertParameter(Parametro parameter)
	{
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			entityManager.persist(parameter);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}

	public List<Parametro> GetAll()
	{
		List<Parametro> result = new ArrayList<>();
		Query q = entityManager.createQuery("FROM Parametro");
		try
		{
			result = q.getResultList();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return result;
	}

	public void UpdateParameter(Parametro parameterToUpdate)
	{
		Parametro parameter = FindById(parameterToUpdate.getId());
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			parameter.setValor(parameterToUpdate.getValor());
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
}
