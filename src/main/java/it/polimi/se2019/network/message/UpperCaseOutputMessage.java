package it.polimi.se2019.network.message;

// ********** JUST FOR TEST ************
public class UpperCaseOutputMessage extends Message {

	private String content;


	public UpperCaseOutputMessage(String content) {
		super(MessageType.OUTPUT_FOR_UPPER_CASE);
		this.content = content;
	}


	public String getContent() {
		return content;
	}
}

