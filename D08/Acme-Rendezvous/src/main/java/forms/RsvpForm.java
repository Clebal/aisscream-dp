
package forms;

import java.util.Map;

public class RsvpForm {

	private Integer					rendezvousId;
	private Map<Integer, String>	questions;
	private Map<Integer, String>	answers;


	public Integer getRendezvousId() {
		return this.rendezvousId;
	}

	public void setRendezvousId(final Integer rendezvousId) {
		this.rendezvousId = rendezvousId;
	}

	public Map<Integer, String> getQuestions() {
		return this.questions;
	}

	public void setQuestions(final Map<Integer, String> questions) {
		this.questions = questions;
	}

	public Map<Integer, String> getAnswers() {
		return this.answers;
	}

	public void setAnswers(final Map<Integer, String> answers) {
		this.answers = answers;
	}

}
