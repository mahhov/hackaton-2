package network;

import list.Findable;

public class Partner implements Findable {
	String name;
	String ip;
	int port;
	long lastSend;
	boolean hosting;

	Partner(String name, String ip, int port, boolean hosting) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.hosting = hosting;
		renew();
	}

	public boolean identical(Object p) {
		if (p == null)
			return false;
		Partner pp = (Partner) p;
		return this.ip.equals(pp.ip) && this.name.equals(pp.name)
				&& this.port == pp.port && this.hosting == pp.hosting;
	}

	boolean old() {
		return (System.currentTimeMillis() - lastSend) > Listener.MAX_PARTNER_AGE;
	}

	void renew() {
		lastSend = System.currentTimeMillis();
	}

}
