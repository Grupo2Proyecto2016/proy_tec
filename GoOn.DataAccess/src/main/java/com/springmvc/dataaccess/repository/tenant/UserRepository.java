package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			entityManager.persist(user);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
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

	public List<Usuario> GetEmployees() //ADMIN NO ES INCLUIDO EN EL RESULTADO
	{
		List<Usuario> result = new ArrayList<>();
		Query q = entityManager.createQuery("FROM Usuario WHERE es_empleado = TRUE AND rol_id_rol NOT IN (1, 4)");
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
