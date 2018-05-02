package services;

import java.util.ArrayList;
import java.util.Calendar;
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

import domain.CreditCard;
import domain.Identifier;
import domain.Raffle;
import domain.Ticket;
import domain.User;
import forms.TicketForm;

import repositories.TicketRepository;
import security.LoginService;

@Service
@Transactional
public class TicketService {

	// Managed repository
	@Autowired
	private TicketRepository ticketRepository;
	
	// Supporting services
	@Autowired
	private IdentifierService	identifierService;
	
	@Autowired
	private Validator			validator;
	
	@Autowired
	private UserService			userService;
	
	// Constructor
	public TicketService() {
		super();
	}
	
	// Simple CRUD methods
	public Ticket create(final Raffle raffle, final User user, final CreditCard creditCard) {
		Ticket result;
		
		result = new Ticket();
		
		result.setRaffle(raffle);
		result.setUser(user);
		result.setCreditCard(creditCard);
		
		return result;
	}
	
	public Collection<Ticket> findAll() {
		Collection<Ticket> result;
		
		result = this.ticketRepository.findAll();
		
		return result;
	}
	
	public Ticket findOne(final int ticketId) {
		Ticket result;
		
		Assert.isTrue(ticketId != 0);
		
		result = this.ticketRepository.findOne(ticketId);
		
		return result;
	}
	
	public Ticket save(final Ticket ticket) {
		Ticket result;
		
		Assert.notNull(ticket);
		
		// El usuario debe estar logeado
		Assert.isTrue(LoginService.isAuthenticated());
		// El usuario que ha comprado el ticket debe ser el que est� autenticado
		Assert.isTrue(ticket.getUser().getUserAccount().equals(LoginService.getPrincipal()));
		
		// Asignar c�digo �nico
		ticket.setCode(this.generateUniqueCode(ticket.getRaffle()));
		
		// El c�digo �nico code no puede ser nulo
		Assert.notNull(ticket.getCode());
		
		// Guardar
		result = this.ticketRepository.save(ticket);
		
		return result;
	}
	
	// Delete
	
	// Other business methods 
	public void save(final Collection<Ticket> tickets) {
		for(Ticket t: tickets) {
			this.save(t);
		}
	}
	
	public Integer countByCreditCardId(final int creditCardId) {
		Integer result;
		
		result = this.ticketRepository.countByCreditCardId(creditCardId);
		
		return result;
	}
	
	public Page<Ticket> findByUserAccountId(final int userAccountId, int page, int size) {
		Page<Ticket> result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.ticketRepository.findByUserAccountId(userAccountId, this.getPageable(page, size));
		
		return result;
	}
	
	public Page<Ticket> findByRaffleIdAndUserAccountId(final int raffleId, final int userAccountId, int page, int size) {
		Page<Ticket> result;
		
		Assert.isTrue(userAccountId != 0 && raffleId != 0);
		
		result = this.ticketRepository.findByRaffleIdAndUserAccountId(raffleId, userAccountId, this.getPageable(page, size));
		
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
	
	public Collection<Ticket> reconstruct(final TicketForm ticketForm, final BindingResult binding) {
		Collection<Ticket> result;
		Ticket ticket;
		User user;
		
		Assert.notNull(ticketForm);
		
		result = new ArrayList<Ticket>();
		
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		
		ticketForm.setUser(user);
		
		if(binding != null) this.validator.validate(ticketForm, binding);
		
		for(int i = 0; i < ticketForm.getAmount(); i++) {
			ticket = this.create(ticketForm.getRaffle(), ticketForm.getUser(), ticketForm.getCreditCard());			
			result.add(ticket);
		}
		
		return result;
	}
	
	private String generateUniqueCode(final Raffle raffle) {
		String result;
		final String[] wordcharacters = {
			"A", "b", "C", "d", "E", "F", "G", "z", "I", "J", "K", "8", "M", "N", "k", "3", "Q", "W", "S", "s", "U", "u", "W", "0", "Z",
			"a", "B", "c", "D", "e", "f", "g", "h", "i", "j", "O", "l", "L", "n", "o", "p", "q", "r", "T", "t", "V", "v", "w", "y", "H",
			"Y", "1", "2", "P", "4", "5", "6", "7", "m", "9"
		};
		Identifier identifier;
		Calendar calendar;
		String day;
		String month;
		String year;

		calendar = Calendar.getInstance();

		year = Integer.toString(calendar.get(Calendar.YEAR));
		year = year.substring(2, 4);

		month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
		if (month.length() == 1) month = "0" + month;

		day = Integer.toString(calendar.get(Calendar.DATE));
		if (day.length() == 1) day = "0" + day;

		result = raffle.getId() + year + month + day;
		
		identifier = this.identifierService.findIdentifier();

		result += wordcharacters[identifier.getFivethCounter()] + wordcharacters[identifier.getFourthCounter()] + wordcharacters[identifier.getThirdCounter()] + wordcharacters[identifier.getSecondCounter()] + wordcharacters[identifier.getFirstCounter()];

		if (identifier.getFirstCounter() < wordcharacters.length) {
			identifier.setFirstCounter(identifier.getFirstCounter() + 1);
			if (identifier.getFirstCounter() >= wordcharacters.length) {
				identifier.setFirstCounter(0);
				if (identifier.getSecondCounter() < wordcharacters.length) {
					identifier.setSecondCounter(identifier.getSecondCounter() + 1);
					if (identifier.getSecondCounter() >= wordcharacters.length) {
						identifier.setSecondCounter(0);
						if (identifier.getThirdCounter() < wordcharacters.length) {
							identifier.setThirdCounter(identifier.getThirdCounter() + 1);
							if (identifier.getThirdCounter() >= wordcharacters.length) {
								identifier.setThirdCounter(0);
								if (identifier.getFourthCounter() < wordcharacters.length) {
									identifier.setFourthCounter(identifier.getFourthCounter() + 1);
									if (identifier.getFourthCounter() >= wordcharacters.length){
										identifier.setFourthCounter(0);
										if (identifier.getFivethCounter() < wordcharacters.length) {
											identifier.setFivethCounter(identifier.getFivethCounter() + 1);
											if (identifier.getFivethCounter() >= wordcharacters.length)
												identifier.setFivethCounter(0);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		this.identifierService.save(identifier);

		return result;
	}
	
	
}
