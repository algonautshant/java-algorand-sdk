package com.algorand.algosdk.v2.client.algod;

import com.algorand.algosdk.v2.client.common.Client;
import com.algorand.algosdk.v2.client.common.HttpMethod;
import com.algorand.algosdk.v2.client.common.Query;
import com.algorand.algosdk.v2.client.common.QueryData;
import com.algorand.algosdk.v2.client.common.Response;


/**
 * Given a catchpoint, it starts catching up to this catchpoint 
 * /v2/catchup/{catchpoint} 
 */
public class StartCatchup extends Query {

	private String catchpoint;

	/**
	 * @param catchpoint A catch point 
	 */
	public StartCatchup(Client client, String catchpoint) {
		super(client, new HttpMethod("put"));
		this.catchpoint = catchpoint;
	}

	@Override
	public Response<String> execute() throws Exception {
		Response<String> resp = baseExecute();
		resp.setValueType(String.class);
		return resp;
	}

	protected QueryData getRequestString() {
		addPathSegment(String.valueOf("v2"));
		addPathSegment(String.valueOf("catchup"));
		addPathSegment(String.valueOf(catchpoint));

		return qd;
	}
}