package com.acme.entities;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

/**
 * @author josueribeiro@github.com
 */
public class PersonTest {

	static EntityManagerFactory emf;
	EntityManager em;

	@BeforeClass
	public static void setUp() {
		emf = Persistence.createEntityManagerFactory("pu-sqlite-jpa");
	}

	@Before
	public void initEntityManager() {
		em = emf.createEntityManager();
		em.getTransaction().begin();
	}

	@Test
	public void testPersist() {
		try {

			// Persist in database
			Person person = new Person();
			person.setName("person2");
			em.persist(person);
			em.getTransaction().commit();

			// Find by id
			Person personDB = em.find(Person.class, person.getId());

			Assert.assertEquals(person.getName(), personDB.getName());

		} catch (Throwable e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testUpdate() {
		try {

			// Find by id in database
			Person person = em.find(Person.class, 10); // See file import.sql
			person.setAge(22);

			// Persist in database
			em.merge(person);
			em.getTransaction().commit();

			// Find by id
			Person personDB = em.find(Person.class, 10);
			Assert.assertEquals(person.getAge(), personDB.getAge());

		} catch (Throwable e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testFindById() {
		try {

			// Find by id in database
			Integer personId = 10;
			Person person = em.find(Person.class, personId); // See file import.sql

			Assert.assertEquals(personId, person.getId());

		} catch (Throwable e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testFindByReference() {
		try {

			// Find by id in database
			Integer personId = 10;
			Person person = em.getReference(Person.class, personId); // See file import.sql

			Assert.assertEquals(personId, person.getId());

		} catch (Throwable e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testFindByReferenceEntityNotFound() {
		try {

			// Find by id in database
			Integer personId = 111; // id not exists
			Person person = em.getReference(Person.class, personId);

			if (person.getId().equals(personId)) {
				Assert.fail("A exception EntityNotFoundException should be throw");
			}

		} catch (EntityNotFoundException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testRemove() {
		try {

			// Find by id in database and remove
			Integer personId = 10;
			Person person = em.find(Person.class, personId); // See file import.sql
			em.remove(person);
			em.getTransaction().commit(); // TODO java.sql.SQLException: database is locked (sometimes)

			// Find by id
			Person personDB = em.find(Person.class, personId); // See file import.sql

			Assert.assertNull(personDB);

		} catch (Throwable e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@After
	public void closeEntityManager() {
		em.close();
	}

	@AfterClass
	public static void closeEntityManagerFactory() {
		emf.close();
	}

}
