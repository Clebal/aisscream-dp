package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Company;
import domain.Raffle;
import domain.Ticket;

import repositories.RaffleRepository;
import security.Authority;
import security.LoginService;

@Service
@Transactional
public class RaffleService {

	// Managed repository
	@Autowired
	private RaffleRepository raffleRepository;
	
	// Supporting services
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private Validator		validator;
	
	// Constructor
	public RaffleService() {
		super();
	}
	
	// Simple CRUD methods
	public Raffle create(final Company company) {
		Raffle result;
		
		result = new Raffle();
		result.setCompany(company);
		
		return result;
	}
	
	public Collection<Raffle> findAll() {
		Collection<Raffle> result;
		
		result = this.raffleRepository.findAll();
		
		return result;
	}
	
	public Raffle findOne(final int raffleId) {
		Raffle result;
		
		Assert.isTrue(raffleId != 0);
		
		result = this.raffleRepository.findOne(raffleId);
		
		return result;
	}
	
	public Raffle save(final Raffle raffle) {
		Raffle result;
		Authority authority;
		
		Assert.notNull(raffle);
		
		authority = new Authority();
		authority.setAuthority("COMPANY");
		
		// La empresa está logeado
		Assert.isTrue(LoginService.isAuthenticated());
		
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		Assert.isTrue(raffle.getCompany().getUserAccount().equals(LoginService.getPrincipal()));
		
		// Guardar
		result = this.raffleRepository.save(raffle);
		
		return result;
	}
	
	// Delete
	public void delete(final Raffle raffle) {
		Raffle saved;
		Authority authority;
		Collection<Ticket> tickets;
		
		Assert.notNull(raffle);

		saved = this.raffleRepository.findOne(raffle.getId());
		
		authority = new Authority();
		authority.setAuthority("MODERATOR");
		if(LoginService.getPrincipal().getAuthorities().contains(authority)) {
			tickets = this.ticketService.findByRaffleId(raffle.getId());
			Assert.notNull(tickets);
			for(Ticket t: tickets)
				this.ticketService.delete(t);
		} else {
			Assert.isTrue(saved.getCompany().getUserAccount().equals(LoginService.getPrincipal()));
		}
		
		this.raffleRepository.delete(saved);
	}
	
	// Other business methods 
	public Page<Raffle> findAvailables(final int page, final int size) {
		Page<Raffle> result;
		
		result = this.raffleRepository.findAvailables(this.getPageable(page, size));
		
		return result;
	}
	
	public Page<Raffle> findByUserAccountId(final int userAccountId, final int page, final int size) {
		Page<Raffle> result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.raffleRepository.findByUserAccountId(userAccountId, this.getPageable(page, size));
		
		return result;
	}
	
	public Page<Raffle> findByCompanyAccountId(final int userAccountId, final int page, final int size) {
		Page<Raffle> result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.raffleRepository.findByCompanyAccountId(userAccountId, this.getPageable(page, size));
		
		return result;
	}
	
	public Page<Raffle> findOrderedByMaxDate(final int page, final int size) {
		Page<Raffle> result;
		
		result = this.raffleRepository.findOrderedByMaxDate(this.getPageable(page, size));
		
		return result;
	}
	
	public Page<Raffle> findAllPaginated(final int page, final int size) {
		Page<Raffle> result;
		
		result = this.raffleRepository.findAllPaginated(this.getPageable(page, size));
		
		return result;
	}
	
	public Raffle findOneToEdit(final int raffleId) {
		Raffle result;
		
		result = this.findOne(raffleId);
		Assert.notNull(result);
		
		// El que edite tiene que ser el creador del raffle
		Assert.isTrue(result.getCompany().getUserAccount().equals(LoginService.getPrincipal()));
		
		return result;
	}
	
	// Auxiliary methods
	private Pageable getPageable(final int page, final int size) {
		Pageable result;
		
		if (page == 0 || size <= 0)
			result = new PageRequest(0, 5);
		else
			result = new PageRequest(page - 1, size);
		
		return result;
	}
	
	public Raffle reconstruct(final Raffle raffle, final BindingResult binding) {
		Raffle result;
		Company company;
		
		if(raffle.getId() == 0) {
			company = this.companyService.findByUserAccountId(LoginService.getPrincipal().getId());
			Assert.notNull(company);
		} else {
			result = this.raffleRepository.findOne(raffle.getId());
			company = result.getCompany();
			raffle.setVersion(result.getVersion());
		}
		
		raffle.setCompany(company);

		this.validator.validate(raffle, binding);
		
		return raffle;
	}
	
}
