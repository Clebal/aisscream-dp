package converters;

import java.net.URLEncoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import security.Authority;

@Component
@Transactional
public class AuthorityToStringConverter implements Converter<Authority, String> {

	
	@Override
	public String convert(Authority authority) {
		String result;
		StringBuilder builder;
		
		if(authority == null) {
			result = null;
		}else{
			try{
				builder = new StringBuilder();
				builder.append(URLEncoder.encode(authority.getAuthority(), "UTF-8"));
				result = builder.toString();
			} catch(Throwable oops) {
				throw new RuntimeException(oops);
			}
		}
		
		return result;
	}
	
}
