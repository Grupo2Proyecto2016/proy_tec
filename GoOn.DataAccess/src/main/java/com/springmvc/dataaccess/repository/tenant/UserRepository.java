package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.Date;
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
		Query q = entityManager.createQuery("FROM Usuario WHERE usrname = :name AND enabled = TRUE");
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
		Query q = entityManager.createQuery("FROM Usuario WHERE es_empleado = TRUE AND rol_id_rol NOT IN (1, 4) AND enabled = TRUE");
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

	public void UpdateUser(com.springmvc.entities.tenant.Usuario userUpdateData)
	{
		Usuario user = FindByUsername(userUpdateData.getUsrname());
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			user.setNombre(userUpdateData.getNombre());
			user.setApellido(userUpdateData.getApellido());
			user.setDireccion(userUpdateData.getDireccion());
			user.setTelefono(userUpdateData.getTelefono());
			user.setEmail(userUpdateData.getEmail());
			user.setFch_nacimiento(userUpdateData.getFch_nacimiento());
			user.setRol_id_rol(userUpdateData.getRol_id_rol());
			user.setSucursal(userUpdateData.getSucursal());
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}

	public void DeleteUser(String usrname)
	{
		Usuario user = FindByUsername(usrname);
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			user.setEnabled(false);
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}

	public void UpdateUserPassword(com.springmvc.entities.tenant.Usuario signedUser, String newPassword)
	{
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			signedUser.setUltimoResetPassword(new Date());
			signedUser.setPasswd(newPassword);
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
		
	}
}
