package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Usuario;

public class UserRepository
{
	EntityManager entityManager;
	
	public UserRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public Usuario FindByUsername(String username)
	{
		Usuario user = null;
		Query q = entityManager.createQuery("FROM Usuario WHERE usrname = :name");
		q.setParameter("name", username);
		try
		{
			user = (Usuario)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return user;
	}
	
	public void InsertUser(Usuario user)
	{
		try
		{
			entityManager.getTransaction().begin();
			entityManager.persist(user);
			entityManager.flush();
			entityManager.getTransaction().commit();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}
}
