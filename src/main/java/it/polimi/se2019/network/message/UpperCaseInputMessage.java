package it.polimi.se2019.network.message;

// ********** JUST FOR TEST ************
public class UpperCaseInputMessage extends Message {

	private String content;


	public UpperCaseInputMessage(String content) {
		super(MessageType.INPUT_FOR_UPPER_CASE);
		this.content = content;
	}


	public String getContent() {
		return content;
	}
}
