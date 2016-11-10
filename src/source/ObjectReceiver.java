package source;

import java.io.IOException;

public interface ObjectReceiver {
	public void receive(Object o) throws IOException;
}
