package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Usuario;
import com.springmvc.enums.UserRol;

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

	public Usuario FindByRole(UserRol rol) 
	{
		Usuario user = null;
		Query q = entityManager.createQuery("FROM Usuario WHERE rol_id_rol = :rol");
		q.setParameter("rol", rol.getValue());
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

	public List<Usuario> GetEmployees() 
	{
		List<Usuario> result = new ArrayList<>();
		Query q = entityManager.createQuery("FROM Usuario WHERE es_empleado = TRUE");
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
}
