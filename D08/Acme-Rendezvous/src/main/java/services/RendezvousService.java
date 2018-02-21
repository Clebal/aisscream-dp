
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RendezvousRepository;
import security.Authority;
import security.LoginService;
import domain.Rendezvous;
import domain.Rsvp;
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
	private RsvpService				rsvpService;


	// Constructor
	public RendezvousService() {
		super();
	}

	// Simple CRUD methods----------
	public Rendezvous create(final User creator) {
		Rendezvous result;
		Authority authority;
		List<Rendezvous> linkerRendezvouses;

		Assert.notNull(creator);
		authority = new Authority();
		authority.setAuthority("USER");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		linkerRendezvouses = new ArrayList<Rendezvous>();
		result = new Rendezvous();
		result.setCreator(creator);
		result.setIsDeleted(false);
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

	public Rendezvous save(final Rendezvous rendezvous) {
		Rendezvous result;
		User user;
		Date currentMoment;
		Rsvp creatorRsvp;

		Assert.notNull(rendezvous);

		currentMoment = new Date();
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

		//only can update o create a rendezvous its creator
		Assert.isTrue(rendezvous.getCreator().equals(user));

		//If you are creating a rendezvous the moment must be future, but if you are updated it the moment must be future or if this doesn´t change 
		// then the moment must be the same
		if (rendezvous.getId() == 0) {
			Assert.isTrue(rendezvous.getMoment().compareTo(currentMoment) > 0);
			Assert.isTrue(rendezvous.getIsDeleted() == false);
		} else if (!this.findOne(rendezvous.getId()).getMoment().equals(rendezvous.getMoment()))
			Assert.isTrue(rendezvous.getMoment().compareTo(currentMoment) > 0);
		//If you are updating, the rendezvous can not be deletd and must be in draft mode
		if (rendezvous.getId() != 0) {
			Assert.isTrue(this.findOne(rendezvous.getId()).getIsDeleted() == false);
			Assert.isTrue(rendezvous.getDraft() == true);
		}

		result = this.rendezvousRepository.save(rendezvous);

		//If you are creating the rendezvous, the creator must have a RSVP to that rendezvous
		if (rendezvous.getId() == 0) {
			creatorRsvp = this.rsvpService.create(result.getCreator(), result);
			creatorRsvp.setStatus("ACCEPTED");
			this.rsvpService.saveFromCreator(creatorRsvp);
		}

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

		rendezvous.setIsDeleted(true);

		this.rendezvousRepository.save(rendezvous);

	}

	public void addLink(final Rendezvous myRendezvous, final Rendezvous linkedRendezvous) {
		Assert.notNull(myRendezvous);
		Assert.notNull(linkedRendezvous);
		Assert.isTrue(myRendezvous.getCreator().getUserAccount().getId() == LoginService.getPrincipal().getId());
		Assert.isTrue(!linkedRendezvous.getLinkerRendezvouses().contains(myRendezvous));

		linkedRendezvous.getLinkerRendezvouses().add(myRendezvous);

		this.rendezvousRepository.save(linkedRendezvous);
	}

	public void removeLink(final Rendezvous myRendezvous, final Rendezvous linkedRendezvous) {
		Assert.notNull(myRendezvous);
		Assert.notNull(linkedRendezvous);
		Assert.isTrue(myRendezvous.getCreator().getUserAccount().getId() == LoginService.getPrincipal().getId());
		Assert.isTrue(linkedRendezvous.getLinkerRendezvouses().contains(myRendezvous));

		linkedRendezvous.getLinkerRendezvouses().remove(myRendezvous);

		this.rendezvousRepository.save(linkedRendezvous);
	}

	public Collection<Rendezvous> findByCreatorId(final int creatorId) {
		Collection<Rendezvous> result;

		Assert.isTrue(creatorId != 0);
		result = this.rendezvousRepository.findByCreatorId(creatorId);

		return result;
	}

	public Collection<Rendezvous> findByLinkerRendezvousId(final int linkerRendezvousId) {
		Collection<Rendezvous> result;

		Assert.isTrue(linkerRendezvousId != 0);
		result = this.rendezvousRepository.findByLinkerRendezvousId(linkerRendezvousId);

		return result;
	}

	public Double[] avgStandardDRsvpdRendezvouses() {
		Double[] result;
		Authority authority;

		//Solo puede acceder admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		result = this.rendezvousRepository.avgStandardDRsvpdRendezvouses();

		return result;
	}

	public Collection<Rendezvous> rendezvousesLinkedMoreAvgPlus10Percentage() {
		Collection<Rendezvous> result;
		Authority authority;

		//Solo puede acceder admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		result = this.rendezvousRepository.rendezvousesLinkedMoreAvgPlus10Percentage();

		return result;
	}

}
