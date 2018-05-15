
package forms;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.AutoPopulatingList;

import domain.Answer;

public class QuestionForm {
	
	private String text;
	
	private List<Answer> answers = new AutoPopulatingList<Answer>(Answer.class); 

	@NotBlank
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
}
