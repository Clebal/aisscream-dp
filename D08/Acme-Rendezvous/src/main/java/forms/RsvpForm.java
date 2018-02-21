
package forms;

import java.util.Map;

import domain.Answer;
import domain.Question;

public class RsvpForm {

	private Integer					rendezvousId;
	private Map<Question, Answer>	questionsAndAnswer;


	public Integer getRendezvousId() {
		return this.rendezvousId;
	}

	public void setRendezvousId(final Integer rendezvousId) {
		this.rendezvousId = rendezvousId;
	}

	public Map<Question, Answer> getQuestionsAndAnswer() {
		return this.questionsAndAnswer;
	}

	public void setQuestionsAndAnswer(final Map<Question, Answer> questionsAndAnswer) {
		this.questionsAndAnswer = questionsAndAnswer;
	}

}
