package gui;

import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Console output for the Console Stage. OutputStream wrapper that simply
 * redirected the System.out.println to here.
 * 
 * @author Jay
 *
 */
public class ConsoleOutput extends OutputStream {

	// TextArea is the text box, StringBuilder is to build the input
	private final TextArea output;
	private final StringBuilder sb = new StringBuilder();
	private String title;

	/**
	 * Get textarea pointer and title
	 * @param out
	 * @param title
	 */
	public ConsoleOutput(final TextArea out, String title) {
		this.output = out;
		this.title = title;
		sb.append(title + "> ");
	}

	/**
	 * Unused
	 */
	@Override
	public void flush() {
	}

	/**
	 * Unused
	 */
	@Override
	public void close() {
	}

	/**
	 * Writes out to the text area
	 */
	@Override
	public void write(int b) throws IOException {
		/*
		 * if (b == '\r') { return; }
		 */

		if (b == '\n') {
			final String text = sb.toString() + "\n";
			Platform.runLater(new Runnable() {
				public void run() {
					output.appendText(text);
				}
			});
			sb.setLength(0);
			sb.append(title + "> ");
			return;
		}

		sb.append((char) b);
	}
}
