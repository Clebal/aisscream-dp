
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.RsvpRepository;
import domain.Rsvp;

@Component
@Transactional
public class StringToRsvpConverter implements Converter<String, Rsvp> {

	@Autowired
	private RsvpRepository	rsvpRepository;


	@Override
	public Rsvp convert(final String text) {
		Rsvp result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.rsvpRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
