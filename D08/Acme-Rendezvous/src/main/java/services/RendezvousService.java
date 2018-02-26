
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RendezvousRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class RendezvousService {

	// Managed repository
	@Autowired
	private RendezvousRepository	rendezvousRepository;

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Validator				validator;


	// Constructor
	public RendezvousService() {
		super();
	}

	// Simple CRUD methods----------
	public Rendezvous create(final User creator) {
		Rendezvous result;
		//Authority authority;
		List<Rendezvous> linkerRendezvouses;

		Assert.notNull(creator);
		//		authority = new Authority();
		//		authority.setAuthority("USER");
		//		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		linkerRendezvouses = new ArrayList<Rendezvous>();
		result = new Rendezvous();
		result.setCreator(creator);
		result.setIsDeleted(false);
		result.setDraft(true);
		result.setAdultOnly(false);
		result.setLinkerRendezvouses(linkerRendezvouses);

		return result;
	}

	public Collection<Rendezvous> findAll() {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findAll();

		return result;
	}

	public Rendezvous findOne(final int rendezvousId) {
		Rendezvous result;

		Assert.isTrue(rendezvousId != 0);

		result = this.rendezvousRepository.findOne(rendezvousId);

		return result;
	}

	public Rendezvous findOneToEdit(final int rendezvousId) {
		Rendezvous result;
		User user;
		Authority authority;
		Authority authority2;

		authority = new Authority();
		authority.setAuthority("USER");

		authority2 = new Authority();
		authority2.setAuthority("ADMIN");

		Assert.isTrue(rendezvousId != 0);
		result = this.rendezvousRepository.findOne(rendezvousId);
		Assert.notNull(result);
		if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
			user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

			Assert.isTrue(result.getCreator().getId() == user.getId());
			Assert.isTrue(result.getDraft() == true && result.getIsDeleted() == false);
		} else if (LoginService.getPrincipal().getAuthorities().contains(authority2))
			Assert.isTrue(result.getIsDeleted() == false);

		return result;
	}

	public Rendezvous findOneToDisplay(final int rendezvousId) {
		Rendezvous result;
		Actor actor;
		Calendar birthDatePlus18Years;
		Boolean canPermit;
		Authority authority;
		Authority authority2;

		authority = new Authority();
		authority.setAuthority("USER");

		authority2 = new Authority();
		authority2.setAuthority("ADMIN");

		Assert.isTrue(rendezvousId != 0);

		result = this.rendezvousRepository.findOne(rendezvousId);

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

		Assert.isTrue(result.getAdultOnly() == false || result.getAdultOnly() == true && canPermit);

		return result;
	}
	public Rendezvous save(final Rendezvous rendezvous) {
		Rendezvous result;
		User user;
		Date currentMoment;
		Calendar birthDatePlus18Years;
		Boolean canPermit;

		Assert.notNull(rendezvous);

		currentMoment = new Date();
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

		//only can update o create a rendezvous its creator
		Assert.isTrue(rendezvous.getCreator().equals(user));

		canPermit = false;
		birthDatePlus18Years = Calendar.getInstance();
		birthDatePlus18Years.setTime(user.getBirthdate());
		birthDatePlus18Years.add(Calendar.YEAR, 18);
		if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
			canPermit = true;

		if (canPermit == false)
			Assert.isTrue(rendezvous.getAdultOnly() == false);

		//If you are creating a rendezvous the moment must be future, but if you are updated it the moment must be future or if this doesn´t change 
		// then the moment must be the same
		if (rendezvous.getId() == 0) {
			Assert.isTrue(rendezvous.getMoment().compareTo(currentMoment) > 0);
			Assert.isTrue(rendezvous.getIsDeleted() == false);
		} else if (this.findOne(rendezvous.getId()).getMoment().compareTo(rendezvous.getMoment()) != 0)
			Assert.isTrue(rendezvous.getMoment().compareTo(currentMoment) > 0);
		//If you are updating, the rendezvous can not be deletd and must be in draft mode
		if (rendezvous.getId() != 0) {
			Assert.isTrue(this.findOne(rendezvous.getId()).getIsDeleted() == false);
			Assert.isTrue(this.findOne(rendezvous.getId()).getDraft() == true);
		}

		if (rendezvous.getLongitude() != null || rendezvous.getLatitude() != null) {
			Assert.isTrue(rendezvous.getLongitude() != null);
			Assert.isTrue(rendezvous.getLatitude() != null);
		}

		result = this.rendezvousRepository.save(rendezvous);

		//If you are creating the rendezvous, the creator must have a RSVP to that rendezvous

		return result;
	}

	//	public void delete(final Rendezvous rendezvous) {
	//		final Authority authority;
	//		Rendezvous rendezvousForDelete;
	//
	//		authority.setAuthority("ADMIN");
	//
	//		Assert.notNull(rendezvous);
	//
	//		//only can deleted it an admin
	//		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
	//
	//		//Delete the comments
	//		for (final Comment comment : this.commentService.findWithoutFatherByRendezvousId(rendezvous.getId()))
	//			this.commentService.deleteFromRendezvous(rendezvous.getId()); //Este metodo te borra los comentarios para ese rendezvous teniendo en cuenta de borrar pri
	//		//Delete answers
	//		for (final Answer answer : this.answerService.findByRendezvousId(rendezvous.getId()))
	//			this.answerService.deleteFromRendezvous(answer);
	//
	//		//Deletes rsvps
	//		for (final Rsvp rsvp : this.rsvpService.findByRendezvousId(rendezvous.getId()))
	//			this.rsvpService.deleteFromRendezvous(rsvp);
	//
	//		//Delete announcemments
	//		for (final Announcement announcement : this.announcementService.findByRendezvousId(rendezvous.getId()))
	//			this.announcementService.deleteFromRendezvous(announcement);
	//
	//		rendezvousForDelete = this.findOne(rendezvous.getId());
	//		this.rendezvousRepository.delete(rendezvousForDelete);
	//
	//	}

	public void virtualDelete(final Rendezvous rendezvous) {
		Assert.notNull(rendezvous);
		//Assert.isTrue(rendezvous.getCreator().getUserAccount().getId() == LoginService.getPrincipal().getId());
		Assert.isTrue(this.rendezvousRepository.findOne(rendezvous.getId()).getIsDeleted() == false);

		rendezvous.setIsDeleted(true);

		this.rendezvousRepository.save(rendezvous);

	}

	public void addLink(final Rendezvous myRendezvous, final Rendezvous linkedRendezvous) {
		Assert.notNull(myRendezvous);
		Assert.notNull(linkedRendezvous);
		Assert.isTrue(myRendezvous.getCreator().getUserAccount().getId() == LoginService.getPrincipal().getId());
		Assert.isTrue(!linkedRendezvous.getLinkerRendezvouses().contains(myRendezvous));
		Assert.isTrue(myRendezvous.getIsDeleted() == false && linkedRendezvous.getIsDeleted() == false);

		linkedRendezvous.getLinkerRendezvouses().add(myRendezvous);

		this.rendezvousRepository.save(linkedRendezvous);
	}

	public void removeLink(final Rendezvous myRendezvous, final Rendezvous linkedRendezvous) {
		Assert.notNull(myRendezvous);
		Assert.notNull(linkedRendezvous);
		Assert.isTrue(myRendezvous.getCreator().getUserAccount().getId() == LoginService.getPrincipal().getId());
		Assert.isTrue(linkedRendezvous.getLinkerRendezvouses().contains(myRendezvous));
		Assert.isTrue(myRendezvous.getIsDeleted() == false && linkedRendezvous.getIsDeleted() == false);

		linkedRendezvous.getLinkerRendezvouses().remove(myRendezvous);

		this.rendezvousRepository.save(linkedRendezvous);
	}

	public Collection<Rendezvous> findByCreatorId(final int creatorId, final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.isTrue(creatorId != 0);
		result = this.rendezvousRepository.findByCreatorId(creatorId, pageable).getContent();

		return result;
	}

	public Integer countByCreatorId(final int creatorId) {
		Integer result;

		Assert.isTrue(creatorId != 0);
		result = this.rendezvousRepository.countByCreatorId(creatorId);

		return result;
	}

	public Collection<Rendezvous> findByCreatorIdAllPublics(final int creatorId, final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.isTrue(creatorId != 0);
		result = this.rendezvousRepository.findByCreatorIdAllPublics(creatorId, pageable).getContent();

		return result;
	}

	public Integer countByCreatorIdAllPublics(final int creatorId) {
		Integer result;

		Assert.isTrue(creatorId != 0);
		result = this.rendezvousRepository.countByCreatorIdAllPublics(creatorId);

		return result;
	}

	public Collection<Rendezvous> findByAttendantId(final int attendantId, final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.isTrue(attendantId != 0);
		result = this.rendezvousRepository.findByAttendantId(attendantId, pageable).getContent();

		return result;
	}

	public Integer countByAttendantId(final int attendantId) {
		Integer result;

		Assert.isTrue(attendantId != 0);
		result = this.rendezvousRepository.countByAttendantId(attendantId);

		return result;
	}

	public Collection<Rendezvous> findByAttendantIdAllPublics(final int attendantId, final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.isTrue(attendantId != 0);
		result = this.rendezvousRepository.findByAttendantIdAllPublics(attendantId, pageable).getContent();

		return result;
	}

	public Integer countByAttendantIdAllPublics(final int attendantId) {
		Integer result;

		Assert.isTrue(attendantId != 0);
		result = this.rendezvousRepository.countByAttendantIdAllPublics(attendantId);

		return result;
	}

	public Collection<Rendezvous> findByLinkerRendezvousId(final int linkerRendezvousId, final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.isTrue(linkerRendezvousId != 0);
		result = this.rendezvousRepository.findByLinkerRendezvousId(linkerRendezvousId, pageable).getContent();

		return result;
	}

	public Integer countByLinkerRendezvousId(final int linkerRendezvousId) {
		Integer result;

		Assert.isTrue(linkerRendezvousId != 0);
		result = this.rendezvousRepository.countByLinkerRendezvousId(linkerRendezvousId);

		return result;
	}

	public Collection<Rendezvous> findByLinkerRendezvousIdAndAllpublics(final int linkerRendezvousId, final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.isTrue(linkerRendezvousId != 0);
		result = this.rendezvousRepository.findByLinkerRendezvousIdAndAllPublics(linkerRendezvousId, pageable).getContent();

		return result;
	}

	public Integer countByLinkerRendezvousIdAndAllpublics(final int linkerRendezvousId) {
		Integer result;

		Assert.isTrue(linkerRendezvousId != 0);
		result = this.rendezvousRepository.countByLinkerRendezvousIdAndAllPublics(linkerRendezvousId);

		return result;
	}

	public Collection<Rendezvous> findLinkerRendezvousesAllPublicsByRendezvousId(final int rendezvousId, final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.isTrue(rendezvousId != 0);
		result = this.rendezvousRepository.findLinkerRendezvousesAllPublicsByRendezvousId(rendezvousId, pageable).getContent();

		return result;
	}

	public Integer countLinkerRendezvousesAllPublicsByRendezvousId(final int rendezvousId) {
		Integer result;

		Assert.isTrue(rendezvousId != 0);
		result = this.rendezvousRepository.countLinkerRendezvousesAllPublicsByRendezvousId(rendezvousId);

		return result;
	}

	public Collection<Rendezvous> findLinkerRendezvousesByRendezvousId(final int rendezvousId, final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.isTrue(rendezvousId != 0);
		result = this.rendezvousRepository.findLinkerRendezvousesByRendezvousId(rendezvousId, pageable).getContent();

		return result;
	}

	public Integer countLinkerRendezvousesByRendezvousId(final int rendezvousId) {
		Integer result;

		Assert.isTrue(rendezvousId != 0);
		result = this.rendezvousRepository.countLinkerRendezvousesByRendezvousId(rendezvousId);

		return result;
	}

	public Collection<Rendezvous> findAllPublics(final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		result = this.rendezvousRepository.findAllPublics(pageable).getContent();

		return result;
	}

	public Integer countAllPublics() {
		Integer result;

		result = this.rendezvousRepository.countAllPublics();

		return result;
	}

	public Collection<Rendezvous> findAllPaginated(final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		result = this.rendezvousRepository.findAllPaginated(pageable).getContent();

		return result;
	}

	public Integer countAllPaginated() {
		Integer result;

		result = this.rendezvousRepository.countAllPaginated();

		return result;
	}

	public Collection<Rendezvous> findNotLinkedByRendezvous(final Rendezvous rendezvous, final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.notNull(rendezvous);
		result = this.rendezvousRepository.findNotLinkedByRendezvous(rendezvous, pageable).getContent();

		return result;
	}

	public Integer countNotLinkedByRendezvous(final Rendezvous rendezvous) {
		Integer result;

		Assert.notNull(rendezvous);
		result = this.rendezvousRepository.countNotLinkedByRendezvous(rendezvous);

		return result;
	}

	public Collection<Rendezvous> findNotLinkedByRendezvousAllPublics(final Rendezvous rendezvous, final int page, final int size) {
		Collection<Rendezvous> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.notNull(rendezvous);
		result = this.rendezvousRepository.findNotLinkedByRendezvousAllPublics(rendezvous, pageable).getContent();

		return result;
	}

	public Integer countNotLinkedByRendezvousAllPublics(final Rendezvous rendezvous) {
		Integer result;

		Assert.notNull(rendezvous);
		result = this.rendezvousRepository.countNotLinkedByRendezvousAllPublics(rendezvous);

		return result;
	}

	public Double[] avgStandardDRsvpdCreatedPerUser() {
		Double[] result;
		Authority authority;

		//Solo puede acceder admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		result = this.rendezvousRepository.avgStandardDRsvpdCreatedPerUser();

		return result;
	}

	public Double ratioCreatorsVsTotal() {
		Double result;
		Authority authority;

		//Solo puede acceder admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		result = this.rendezvousRepository.ratioCreatorsVsTotal();

		return result;
	}

	public Double[] avgStandardDRendezvousesRsvpdPerUser() {
		Double[] result;
		Authority authority;

		//Solo puede acceder admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		result = this.rendezvousRepository.avgStandardDRendezvousesRsvpdPerUser();

		return result;
	}

	public Collection<Rendezvous> top10Rendezvouses() {
		Collection<Object[]> listId;
		List<Rendezvous> result;
		User creator;
		Rendezvous r;

		result = new ArrayList<Rendezvous>();

		listId = this.rendezvousRepository.top10Rendezvouses();

		for (final Object[] o : listId) {
			creator = this.userService.findOne((int) o[o.length - 1]);
			r = this.create(creator);
			r.setId((int) o[0]);
			r.setVersion((int) o[1]);
			r.setAdultOnly((boolean) o[2]);
			r.setDescription((String) o[3]);
			r.setDraft((boolean) o[4]);
			r.setIsDeleted((boolean) o[5]);
			r.setLatitude((Double) o[6]);
			r.setLongitude((Double) o[7]);
			r.setMoment((Date) o[8]);
			r.setName((String) o[9]);
			r.setPicture((String) o[10]);
			r.setCreator(creator);

			result.add(r);
		}

		return result;
	}

	public Collection<Rendezvous> rendezvousesNumberAnnouncementsPlus75Percentage(final int page, final int size) {
		Collection<Rendezvous> result;
		Authority authority;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		//Solo puede acceder admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		result = this.rendezvousRepository.rendezvousesNumberAnnouncementsPlus75Percentage(pageable).getContent();

		return result;
	}

	public Integer countRendezvousesNumberAnnouncementsPlus75Percentage() {
		Integer result;

		result = this.rendezvousRepository.countRendezvousesNumberAnnouncementsPlus75Percentage();

		return result;
	}

	public Collection<Rendezvous> rendezvousesLinkedMoreAvgPlus10Percentage(final int page, final int size) {
		Collection<Rendezvous> result;
		Authority authority;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		//Solo puede acceder admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		result = this.rendezvousRepository.rendezvousesLinkedMoreAvgPlus10Percentage(pageable).getContent();

		return result;
	}

	public Integer countRendezvousesLinkedMoreAvgPlus10Percentage() {
		Integer result;

		result = this.rendezvousRepository.countRendezvousesLinkedMoreAvgPlus10Percentage();

		return result;
	}

	public Rendezvous reconstruct(final Rendezvous rendezvous, final BindingResult binding) {
		Rendezvous result;
		Rendezvous aux;

		if (rendezvous.getId() == 0)
			result = rendezvous;
		else {
			result = rendezvous;
			aux = this.rendezvousRepository.findOne(rendezvous.getId());
			result.setVersion(aux.getVersion());
			result.setCreator(aux.getCreator());
			result.setIsDeleted(aux.getIsDeleted());
			result.setLinkerRendezvouses(aux.getLinkerRendezvouses());
			result.setName(rendezvous.getName());
			result.setDescription(rendezvous.getDescription());
			result.setMoment(rendezvous.getMoment());
			result.setPicture(rendezvous.getPicture());
			result.setDraft(rendezvous.getDraft());
			result.setAdultOnly(rendezvous.getAdultOnly());
			result.setLatitude(rendezvous.getLatitude());
			result.setLongitude(rendezvous.getLongitude());
		}

		this.validator.validate(result, binding);

		return result;
	}

}
