package it.polimi.se2019.view;

import java.util.Scanner;

public class CLIView implements RemoteViewInterface {

	public CLIView() {
	}

	@Override
	public String askNickname() {
		System.out.println("Enter your nickname.");
		Scanner s = new Scanner(System.in);
		return s.nextLine();
	}

	@Override
	public void displayText(String text) {
		System.out.println(text);
	}
}