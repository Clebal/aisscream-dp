
package forms;

import java.util.Map;

import utilities.NotBlankMap;

public class RsvpForm {

	private int						rendezvousId;
	private Map<Integer, String>	questions;
	private Map<Integer, String>	answers;


	public int getRendezvousId() {
		return this.rendezvousId;
	}

	public void setRendezvousId(final int rendezvousId) {
		this.rendezvousId = rendezvousId;
	}

	public Map<Integer, String> getQuestions() {
		return this.questions;
	}

	public void setQuestions(final Map<Integer, String> questions) {
		this.questions = questions;
	}

	@NotBlankMap
	public Map<Integer, String> getAnswers() {
		return this.answers;
	}

	public void setAnswers(final Map<Integer, String> answers) {
		this.answers = answers;
	}

}
