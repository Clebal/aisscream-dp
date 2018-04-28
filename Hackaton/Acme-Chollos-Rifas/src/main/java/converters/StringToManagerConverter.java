package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ModeratorRepository;

import domain.Moderator;

@Component
@Transactional
public class StringToManagerConverter implements Converter<String, Moderator> {

	@Autowired
	ModeratorRepository	managerRepository;


	@Override
	public Moderator convert(final String text) {
		Moderator result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.managerRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
