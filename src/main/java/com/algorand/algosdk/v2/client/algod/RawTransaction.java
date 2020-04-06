package com.algorand.algosdk.v2.client.algod;

import java.io.IOException;

import com.algorand.algosdk.v2.client.connect.Client;
import com.algorand.algosdk.v2.client.connect.Query;
import com.fasterxml.jackson.databind.ObjectMapper;


/*
	
 */
public class RawTransaction extends Query {
	private String rawtxn;

	private boolean rawtxnIsSet;

	public RawTransaction(Client client) {
		super(client);
	}
	public RawTransaction setRawtxn(String rawtxn) {
		this.rawtxn = rawtxn;
		this.rawtxnIsSet = true;
		return this;
	}

	public String lookup() {
		String response;
		try {
			response = request();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		String resp;
		try {
			resp = mapper.readValue(response, String.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return resp;
	}
	protected String getRequestString() {
		StringBuffer sb = new StringBuffer();
		sb.append("/");
		sb.append("v2");
		sb.append("/");
		sb.append("transactions");
		sb.append("?");

		boolean added = false;

		if (this.rawtxnIsSet) {
			if (added) {
				sb.append("&");
			}
			sb.append("rawtxn=");
			sb.append(rawtxn);
			added = true;
		}

		return sb.toString();
	}
}