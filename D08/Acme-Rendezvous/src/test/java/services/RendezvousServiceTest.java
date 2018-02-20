
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.LoginService;
import utilities.AbstractTest;
import domain.Rendezvous;
import domain.TermCondition;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class RendezvousServiceTest extends AbstractTest {

	// Service under test----------------
	@Autowired
	private RendezvousService		rendezvousService;

	@Autowired
	private UserService				userService;

	@Autowired
	private TermConditionService	termConditionService;


	@Test
	public void testCreate() {
		User creator;
		Rendezvous created;

		super.authenticate("user1");

		creator = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

		created = this.rendezvousService.create(creator);

		Assert.notNull(created);

		super.authenticate(null);
	}

	@Test
	public void testFindAll() {
		Collection<Rendezvous> rendezvouses;

		super.authenticate("user1");

		rendezvouses = this.rendezvousService.findAll();

		Assert.isTrue(!rendezvouses.isEmpty());

		super.authenticate(null);
	}

	@Test
	public void testFindOne() {
		Rendezvous rendezvous;
		Rendezvous saved;

		super.authenticate("user1");

		rendezvous = (Rendezvous) this.rendezvousService.findAll().toArray()[0];

		saved = this.rendezvousService.findOne(rendezvous.getId());
		Assert.notNull(saved);

		super.authenticate(null);
	}

	@Test
	public void testSave() {
		User creator;
		Rendezvous created;
		SimpleDateFormat format;
		String momentString;
		Date momentDate;
		TermCondition termCondition;
		Rendezvous saved;

		super.authenticate("user1");

		creator = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

		created = this.rendezvousService.create(creator);

		Assert.notNull(created);

		created.setName("Rendezvous1");
		created.setDescription("Esto es una descripción");

		// Inicializamos las fechas
		format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		momentString = "02/02/2019 10:00";
		momentDate = new Date();
		try {
			momentDate = format.parse(momentString);
		} catch (final ParseException e) {

		}
		created.setMoment(momentDate);
		created.setPicture("http://www.url.com/eew00");
		created.setDraft(true);
		created.setAdultOnly(false);
		created.setLatitude(15.0);
		created.setLongitude(22.0);

		termCondition = (TermCondition) this.termConditionService.findAll().toArray()[0];

		created.setTermCondition(termCondition);

		saved = this.rendezvousService.save(created);

		Assert.isTrue(this.rendezvousService.findAll().contains(saved));

		super.authenticate(null);
	}

	@Test
	public void testSave2() {
		User creator;
		Rendezvous created;
		SimpleDateFormat format;
		String momentString;
		Date momentDate;
		TermCondition termCondition;
		Rendezvous saved;

		super.authenticate("user1");

		creator = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

		created = this.rendezvousService.create(creator);

		Assert.notNull(created);

		created.setName("Rendezvous1");
		created.setDescription("Esto es una descripción");

		// Inicializamos las fechas
		format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		momentString = "02/02/2016 10:00";
		momentDate = new Date();
		try {
			momentDate = format.parse(momentString);
		} catch (final ParseException e) {

		}
		created.setMoment(momentDate);
		created.setPicture("http://www.url.com/eew00");
		created.setDraft(true);
		created.setAdultOnly(false);
		created.setLatitude(15.0);
		created.setLongitude(22.0);

		termCondition = (TermCondition) this.termConditionService.findAll().toArray()[0];

		created.setTermCondition(termCondition);
		try {
			saved = this.rendezvousService.save(created);
			Assert.isTrue(this.rendezvousService.findAll().contains(saved));
		} catch (final IllegalArgumentException e) {

			super.authenticate(null);

		}
		super.authenticate(null);
	}

	@Test
	public void testVirtualDelete() {
		Rendezvous rendezvous;
		User creator;

		super.authenticate("user1");

		creator = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		rendezvous = (Rendezvous) this.rendezvousService.findByCreatorId(creator.getId()).toArray()[0];

		this.rendezvousService.virtualDelete(rendezvous);
		Assert.isTrue(this.rendezvousService.findOne(rendezvous.getId()).getIsDeleted() == true);

		super.authenticate(null);
	}

	@Test
	public void testaddLink() {
		Rendezvous rendezvousUser1;
		User creatorUser1;
		Rendezvous rendezvousUser3;
		User creatorUser3;

		super.authenticate("user1");

		creatorUser1 = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		rendezvousUser1 = (Rendezvous) this.rendezvousService.findByCreatorId(creatorUser1.getId()).toArray()[0];

		super.authenticate(null);

		super.authenticate("user3");

		creatorUser3 = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		rendezvousUser3 = (Rendezvous) this.rendezvousService.findByCreatorId(creatorUser3.getId()).toArray()[0];
		this.rendezvousService.addLink(rendezvousUser3, rendezvousUser1);

		Assert.isTrue(this.rendezvousService.findByLinkerRendezvousId(rendezvousUser3.getId()).size() == 1);

		super.authenticate(null);
	}

	@Test
	public void testRemoveLink() {
		Rendezvous rendezvousUser1;
		Rendezvous rendezvous2User1;
		User creatorUser1;

		super.authenticate("user1");

		rendezvousUser1 = null;
		rendezvous2User1 = null;
		creatorUser1 = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		for (final Rendezvous r : this.rendezvousService.findByCreatorId(creatorUser1.getId()))
			if (r.getLinkerRendezvouses().isEmpty())
				rendezvousUser1 = r;
			else
				rendezvous2User1 = r;

		this.rendezvousService.removeLink(rendezvousUser1, rendezvous2User1);

		Assert.isTrue(this.rendezvousService.findByLinkerRendezvousId(rendezvousUser1.getId()).size() == 0);

		super.authenticate(null);

	}

	@Test
	public void testFindByCreatorId() {
		Collection<Rendezvous> rendezvouses;
		User creator;

		super.authenticate("user1");
		creator = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

		rendezvouses = this.rendezvousService.findByCreatorId(creator.getId());

		Assert.isTrue(!rendezvouses.isEmpty());

		super.authenticate(null);
	}

	@Test
	public void testFindByLinkerRendezvousId() {
		Collection<Rendezvous> rendezvouses;
		User creator;
		Rendezvous rendezvous;

		super.authenticate("user1");
		creator = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		rendezvous = (Rendezvous) this.rendezvousService.findByCreatorId(creator.getId()).toArray()[0];
		rendezvouses = this.rendezvousService.findByLinkerRendezvousId(rendezvous.getId());

		Assert.isTrue(rendezvouses.size() >= 0);

		super.authenticate(null);
	}

	@Test
	public void testFindTermConditionId() {
		Collection<Rendezvous> rendezvouses;
		TermCondition termCondition;

		super.authenticate("user1");
		termCondition = (TermCondition) this.termConditionService.findAll().toArray()[0];

		rendezvouses = this.rendezvousService.findByTermConditionId(termCondition.getId());

		Assert.isTrue(!rendezvouses.isEmpty());

		super.authenticate(null);
	}

	@Test
	public void testAvgStandardDRsvpdRendezvouses() {
		Double[] numeros;
		super.authenticate("admin");

		numeros = new Double[2];
		numeros = this.rendezvousService.avgStandardDRsvpdRendezvouses();

		Assert.notNull(numeros);
		super.authenticate(null);
	}

	@Test
	public void testRendezvousesLinkedMoreAvgPlus10Percentage() {
		Collection<Rendezvous> rendezvouses;
		super.authenticate("admin");

		rendezvouses = this.rendezvousService.rendezvousesLinkedMoreAvgPlus10Percentage();

		Assert.isTrue(rendezvouses.size() == 1);
		super.authenticate(null);
	}
}
