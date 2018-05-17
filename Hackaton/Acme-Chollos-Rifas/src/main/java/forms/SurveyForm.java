
package forms;

import java.util.Collection;

import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.AutoPopulatingList;

import domain.Surveyer;
import forms.QuestionForm;

public class SurveyForm {

	private int		id;
	
	private String title;

	private Collection<QuestionForm> questions = new AutoPopulatingList<QuestionForm>(QuestionForm.class);

	private String toActor;
	
	private	Boolean hasAds;
		
	private Integer		minimumPoints;
	
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
	@Pattern(regexp = "^(USER|SPONSOR)$")
	public String getToActor() {
		return toActor;
	}

	public void setToActor(final String toActor) {
		this.toActor = toActor;
	}

	public Boolean getHasAds() {
		return hasAds;
	}

	public void setHasAds(final Boolean hasAds) {
		this.hasAds = hasAds;
	}

	public Integer getMinimumPoints() {
		return minimumPoints;
	}

	public void setMinimumPoints(final Integer minimumPoints) {
		this.minimumPoints = minimumPoints;
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

	public Collection<QuestionForm> getQuestions() {
		return questions;
	}

	public void setQuestions(final Collection<QuestionForm> questions) {
		this.questions = questions;
	}
	
}
