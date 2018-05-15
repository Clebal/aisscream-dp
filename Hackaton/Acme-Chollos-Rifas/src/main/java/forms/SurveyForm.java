
package forms;

import java.util.List;

import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.AutoPopulatingList;

import domain.Surveyer;
import forms.QuestionForm;

public class SurveyForm {

	private int		id;
	
	private String title;

	private List<QuestionForm> questions = new AutoPopulatingList<QuestionForm>(QuestionForm.class);

	private Surveyer surveyer;
	
	public SurveyForm() {
		super();
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Surveyer getSurveyer() {
		return surveyer;
	}

	public void setSurveyer(final Surveyer surveyer) {
		this.surveyer = surveyer;
	}

	public List<QuestionForm> getQuestions() {
		return questions;
	}

	public void setQuestions(final List<QuestionForm> questions) {
		this.questions = questions;
	}
	
}
