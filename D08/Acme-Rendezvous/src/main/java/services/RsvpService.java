
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RsvpRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Rendezvous;
import domain.Rsvp;

@Service
@Transactional
public class RsvpService {

	// Managed repository
	@Autowired
	private RsvpRepository	rspvRepository;

	// Supporting services
	@Autowired
	private UserService		userService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private RendezvousService rendezvousService;


	// Constructor
	public RsvpService() {
		super();
	}

	// Simple CRUD methods
	public Rsvp create(final Rendezvous rendezvous) {
		Rsvp result;

		result = new Rsvp();
		result.setAttendant(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()));
		result.setRendezvous(rendezvous);
		result.setStatus("ACCEPTED");

		return result;
	}

	public Collection<Rsvp> findAll() {
		Collection<Rsvp> result;

		result = this.rspvRepository.findAll();

		return result;
	}

	public Rsvp findOne(final int rsvpId) {
		Rsvp result;

		Assert.isTrue(rsvpId != 0);

		result = this.rspvRepository.findOne(rsvpId);

		return result;
	}
	
	public Rsvp findOneToDisplay(final int rsvpId) {
		Rsvp result;
		
		Assert.isTrue(rsvpId != 0);
		
		result = this.rspvRepository.findOne(rsvpId);
		
		isOlderThan18Rsvp(result);
		
		return result;
	}

	public Rsvp save(final Rsvp rsvp) {
		Rsvp result;
		Rsvp saved;

		Assert.notNull(rsvp);
		Assert.isTrue(rsvp.getAttendant().getUserAccount().equals(LoginService.getPrincipal()));

		if (rsvp.getId() == 0){
			Assert.isTrue(this.rspvRepository.findByAttendantUserIdAndRendezvousId(rsvp.getAttendant().getId(), rsvp.getRendezvous().getId()) == null);
			isOlderThan18Rsvp(rsvp);
			Assert.isTrue(!rsvp.getAttendant().equals(rsvp.getRendezvous().getCreator()));
			Assert.isTrue(!rsvp.getRendezvous().getIsDeleted());
		}else{
			saved = this.findOne(rsvp.getId());
			// Comprobar que no se ha cambiado ni el attendant ni el rendezvous, solo se cambia el status.
			Assert.isTrue(saved.getAttendant().equals(rsvp.getAttendant()) && saved.getRendezvous().equals(rsvp.getRendezvous()));
			// Solo se puede actualizar si el rendezvous no se ha pasado. Tampoco se puede actualizar si rendezvous est� eliminado
			Assert.isTrue(rsvp.getRendezvous().getMoment().compareTo(new Date()) > 0 || rsvp.getRendezvous().getIsDeleted());
		}

		result = this.rspvRepository.save(rsvp);

		return result;
	}

	public Rsvp saveFromCreator(final Rsvp rsvp) {
		Rsvp result;

		Assert.notNull(rsvp);
		Assert.isTrue(rsvp.getAttendant().getUserAccount().equals(LoginService.getPrincipal()));

		result = this.rspvRepository.save(rsvp);

		return result;
	}

	public void deleteFromRendezvous(final Rsvp rsvp) {
		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");

		Assert.notNull(rsvp);
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		this.rspvRepository.delete(rsvp);

	}
	
	public void delete(final Rsvp rsvp) {

		this.rspvRepository.delete(rsvp);

	}

	// Other business methods
	public Collection<Rsvp> findByAttendantUserAccountId(final int userAccountId, final int page, final int size) {
		Collection<Rsvp> result;

		Assert.isTrue(userAccountId != 0);
		Assert.isTrue(LoginService.getPrincipal().getId() == userAccountId);

		result = this.rspvRepository.findByAttendantUserAccountId(userAccountId, this.getPageable(page, size)).getContent();

		return result;
	}
	
	public Integer countByAttendantUserAccountId(final int userAccountId) {
		Integer result;

		Assert.isTrue(userAccountId != 0);
		Assert.isTrue(LoginService.getPrincipal().getId() == userAccountId);

		result = this.rspvRepository.countByAttendantUserAccountId(userAccountId);

		return result;
	}

	public Collection<Rsvp> findByRendezvousIdToDisplay(final int rendezvousId, final int page, final int size) {
		Collection<Rsvp> result;
		Rendezvous rendezvous;

		Assert.isTrue(rendezvousId != 0);

		result = this.rspvRepository.findByRendezvousId(rendezvousId, this.getPageable(page, size)).getContent();

		rendezvous = this.rendezvousService.findOne(rendezvousId);
		
		isOlderThan18Rendezvous(rendezvous);
		
		return result;
	}
	
	public Integer countByRendezvousId(final int rendezvousId) {
		Integer result;

		Assert.isTrue(rendezvousId != 0);

		result = this.rspvRepository.countByRendezvousId(rendezvousId);

		return result;
	}

	public Collection<Rsvp> findByCreatorUserAccountId(final int userAccountId) {
		Collection<Rsvp> result;

		Assert.isTrue(userAccountId != 0);
		Assert.isTrue(this.userService.findByUserAccountId(userAccountId).getUserAccount().equals(LoginService.getPrincipal()));

		result = this.rspvRepository.findByCreatorUserAccountId(userAccountId);

		return result;
	}

	public Rsvp findByAttendantUserIdAndRendezvousId(final int userId, final int rendezvousId) {
		Rsvp result;

		Assert.isTrue(userId != 0 && rendezvousId != 0);

		result = this.rspvRepository.findByAttendantUserIdAndRendezvousId(userId, rendezvousId);

		return result;
	}
	
	// Auxiliar methods
	private Pageable getPageable(final int page, final int size) {
		Pageable result;
		
		if (page == 0 || size <= 0)
			result = new PageRequest(0, 5);
		else
			result = new PageRequest(page - 1, size);
		
		return result;
	}
	
	private void isOlderThan18Rsvp(final Rsvp rsvp) {
		Boolean canPermit;
		Calendar birthDatePlus18Years;
		Actor actor;
		Authority authority;
		Authority authority2;

		authority = new Authority();
		authority.setAuthority("USER");

		authority2 = new Authority();
		authority2.setAuthority("ADMIN");
		
		canPermit = false;
		if (LoginService.isAuthenticated())
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
				birthDatePlus18Years = Calendar.getInstance();
				birthDatePlus18Years.setTime(actor.getBirthdate());
				birthDatePlus18Years.add(Calendar.YEAR, 18);
				if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
					canPermit = true;
			} else if (LoginService.getPrincipal().getAuthorities().contains(authority2))
				canPermit = true;

		Assert.isTrue(rsvp.getRendezvous().getAdultOnly() == false || rsvp.getRendezvous().getAdultOnly() == true && canPermit);
	}
	
	private void isOlderThan18Rendezvous(final Rendezvous rendezvous) {
		Boolean canPermit;
		Calendar birthDatePlus18Years;
		Actor actor;
		Authority authority;
		Authority authority2;

		authority = new Authority();
		authority.setAuthority("USER");

		authority2 = new Authority();
		authority2.setAuthority("ADMIN");
		
		canPermit = false;
		if (LoginService.isAuthenticated())
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
				birthDatePlus18Years = Calendar.getInstance();
				birthDatePlus18Years.setTime(actor.getBirthdate());
				birthDatePlus18Years.add(Calendar.YEAR, 18);
				if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
					canPermit = true;
			} else if (LoginService.getPrincipal().getAuthorities().contains(authority2))
				canPermit = true;

		Assert.isTrue(rendezvous.getAdultOnly() == false || rendezvous.getAdultOnly() == true && canPermit);
	}

}
