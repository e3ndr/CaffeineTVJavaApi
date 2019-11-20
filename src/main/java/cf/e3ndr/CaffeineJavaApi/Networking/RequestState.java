package cf.e3ndr.CaffeineJavaApi.Networking;

public enum RequestState {
	VALID,
	INVALID;
	
	private String data;
	public void store(String data) {
		this.data = data;
	}
	
	public String getData() {
		return this.data;
	}
	
}
