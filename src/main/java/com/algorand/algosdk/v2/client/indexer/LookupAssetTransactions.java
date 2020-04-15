package com.algorand.algosdk.v2.client.indexer;

import com.algorand.algosdk.v2.client.common.Client;
import com.algorand.algosdk.v2.client.common.Query;
import com.algorand.algosdk.v2.client.common.QueryData;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.TransactionsResponse;


/**
 * Lookup transactions for an asset. /assets/{asset-id}/transactions 
 */
public class LookupAssetTransactions extends Query {
	private String address;
	private String addressRole;
	private String afterTime;
	private Long assetId;
	private String beforeTime;
	private Long currencyGreaterThan;
	private Long currencyLessThan;
	private Boolean excludeCloseTo;
	private Long limit;
	private Long maxRound;
	private Long minRound;
	private String next;
	private String notePrefix;
	private Long round;
	private String sigType;
	private String txId;
	private String txType;


	public LookupAssetTransactions(Client client, Long assetId) {
		super(client, "get");
		this.assetId = assetId;
	}

	/**
	 * Only include transactions with this address in one of the transaction fields. 
	 */
	public LookupAssetTransactions setAddress(String address) {
		this.address = address;
		return this;
	}

	/**
	 * Combine with the address parameter to define what type of address to search for. 
	 */
	public LookupAssetTransactions setAddressRole(String addressRole) {
		this.addressRole = addressRole;
		return this;
	}

	/**
	 * Include results after the given time. Must be an RFC 3339 formatted string. 
	 */
	public LookupAssetTransactions setAfterTime(String afterTime) {
		this.afterTime = afterTime;
		return this;
	}

	/**
	 * Include results before the given time. Must be an RFC 3339 formatted string. 
	 */
	public LookupAssetTransactions setBeforeTime(String beforeTime) {
		this.beforeTime = beforeTime;
		return this;
	}

	/**
	 * Results should have an amount greater than this value. MicroAlgos are the 
	 * default currency unless an asset-id is provided, in which case the asset will be 
	 * used. 
	 */
	public LookupAssetTransactions setCurrencyGreaterThan(Long currencyGreaterThan) {
		this.currencyGreaterThan = currencyGreaterThan;
		return this;
	}

	/**
	 * Results should have an amount less than this value. MicroAlgos are the default 
	 * currency unless an asset-id is provided, in which case the asset will be used. 
	 */
	public LookupAssetTransactions setCurrencyLessThan(Long currencyLessThan) {
		this.currencyLessThan = currencyLessThan;
		return this;
	}

	/**
	 * Combine with address and address-role parameters to define what type of address 
	 * to search for. The close to fields are normally treated as a receiver, if you 
	 * would like to exclude them set this parameter to true. 
	 */
	public LookupAssetTransactions setExcludeCloseTo(Boolean excludeCloseTo) {
		this.excludeCloseTo = excludeCloseTo;
		return this;
	}

	/**
	 * Maximum number of results to return. 
	 */
	public LookupAssetTransactions setLimit(Long limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * Include results at or before the specified max-round. 
	 */
	public LookupAssetTransactions setMaxRound(Long maxRound) {
		this.maxRound = maxRound;
		return this;
	}

	/**
	 * Include results at or after the specified min-round. 
	 */
	public LookupAssetTransactions setMinRound(Long minRound) {
		this.minRound = minRound;
		return this;
	}

	/**
	 * The next page of results. Use the next token provided by the previous results. 
	 */
	public LookupAssetTransactions setNext(String next) {
		this.next = next;
		return this;
	}

	/**
	 * Specifies a prefix which must be contained in the note field. 
	 */
	public LookupAssetTransactions setNotePrefix(String notePrefix) {
		this.notePrefix = notePrefix;
		return this;
	}

	/**
	 * Include results for the specified round. 
	 */
	public LookupAssetTransactions setRound(Long round) {
		this.round = round;
		return this;
	}

	/**
	 * SigType filters just results using the specified type of signature: * sig - 
	 * Standard * msig - MultiSig * lsig - LogicSig 
	 */
	public LookupAssetTransactions setSigType(String sigType) {
		this.sigType = sigType;
		return this;
	}

	/**
	 * Lookup the specific transaction by ID. 
	 */
	public LookupAssetTransactions setTxId(String txId) {
		this.txId = txId;
		return this;
	}
	public LookupAssetTransactions setTxType(String txType) {
		this.txType = txType;
		return this;
	}

	@Override
	public Response<TransactionsResponse> execute() throws Exception {
		Response<TransactionsResponse> resp = baseExecute();
		resp.setValueType(TransactionsResponse.class);
		return resp;
	}
	public QueryData getRequestString() {
		QueryData qd = new QueryData();
		if (this.address != null) {
			qd.addQuery("address", String.valueOf(address));
		}
		if (this.addressRole != null) {
			qd.addQuery("addressRole", String.valueOf(addressRole));
		}
		if (this.afterTime != null) {
			qd.addQuery("afterTime", String.valueOf(afterTime));
		}
		if (this.beforeTime != null) {
			qd.addQuery("beforeTime", String.valueOf(beforeTime));
		}
		if (this.currencyGreaterThan != null) {
			qd.addQuery("currencyGreaterThan", String.valueOf(currencyGreaterThan));
		}
		if (this.currencyLessThan != null) {
			qd.addQuery("currencyLessThan", String.valueOf(currencyLessThan));
		}
		if (this.excludeCloseTo != null) {
			qd.addQuery("excludeCloseTo", String.valueOf(excludeCloseTo));
		}
		if (this.limit != null) {
			qd.addQuery("limit", String.valueOf(limit));
		}
		if (this.maxRound != null) {
			qd.addQuery("maxRound", String.valueOf(maxRound));
		}
		if (this.minRound != null) {
			qd.addQuery("minRound", String.valueOf(minRound));
		}
		if (this.next != null) {
			qd.addQuery("next", String.valueOf(next));
		}
		if (this.notePrefix != null) {
			qd.addQuery("notePrefix", String.valueOf(notePrefix));
		}
		if (this.round != null) {
			qd.addQuery("round", String.valueOf(round));
		}
		if (this.sigType != null) {
			qd.addQuery("sigType", String.valueOf(sigType));
		}
		if (this.txId != null) {
			qd.addQuery("txId", String.valueOf(txId));
		}
		if (this.txType != null) {
			qd.addQuery("txType", String.valueOf(txType));
		}
		qd.addPathSegment(String.valueOf("assets"));
		qd.addPathSegment(String.valueOf(assetId));
		qd.addPathSegment(String.valueOf("transactions"));

		return qd;
	}
}