package com.algorand.algosdk.v2.client.indexer;

import com.algorand.algosdk.v2.client.common.Client;
import com.algorand.algosdk.v2.client.common.Query;
import com.algorand.algosdk.v2.client.common.QueryData;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.TransactionsResponse;


/**
 * Search for transactions. /transactions 
 */
public class SearchForTransactions extends Query {

	private String address;
	public String address() {
		return this.address;
	}
	private String addressRole;
	public String addressRole() {
		return this.addressRole;
	}
	private String afterTime;
	public String afterTime() {
		return this.afterTime;
	}
	private Long assetId;
	public Long assetId() {
		return this.assetId;
	}
	private String beforeTime;
	public String beforeTime() {
		return this.beforeTime;
	}
	private Long currencyGreaterThan;
	public Long currencyGreaterThan() {
		return this.currencyGreaterThan;
	}
	private Long currencyLessThan;
	public Long currencyLessThan() {
		return this.currencyLessThan;
	}
	private Boolean excludeCloseTo;
	public Boolean excludeCloseTo() {
		return this.excludeCloseTo;
	}
	private Long limit;
	public Long limit() {
		return this.limit;
	}
	private Long maxRound;
	public Long maxRound() {
		return this.maxRound;
	}
	private Long minRound;
	public Long minRound() {
		return this.minRound;
	}
	private String next;
	public String next() {
		return this.next;
	}
	private String notePrefix;
	public String notePrefix() {
		return this.notePrefix;
	}
	private Long round;
	public Long round() {
		return this.round;
	}
	private String sigType;
	public String sigType() {
		return this.sigType;
	}
	private String txId;
	public String txId() {
		return this.txId;
	}
	private String txType;
	public String txType() {
		return this.txType;
	}

	public SearchForTransactions(Client client) {
		super(client, "get");
	}

	/**
	 * Only include transactions with this address in one of the transaction fields. 
	 */
	public SearchForTransactions address(String address) {
		this.address = address;
		addQuery("address", String.valueOf(address));
		return this;
	}

	/**
	 * Combine with the address parameter to define what type of address to search for. 
	 */
	public SearchForTransactions addressRole(String addressRole) {
		this.addressRole = addressRole;
		addQuery("address-role", String.valueOf(addressRole));
		return this;
	}

	/**
	 * Include results after the given time. Must be an RFC 3339 formatted string. 
	 */
	public SearchForTransactions afterTime(String afterTime) {
		this.afterTime = afterTime;
		addQuery("after-time", String.valueOf(afterTime));
		return this;
	}

	/**
	 * Asset ID 
	 */
	public SearchForTransactions assetId(Long assetId) {
		this.assetId = assetId;
		addQuery("asset-id", String.valueOf(assetId));
		return this;
	}

	/**
	 * Include results before the given time. Must be an RFC 3339 formatted string. 
	 */
	public SearchForTransactions beforeTime(String beforeTime) {
		this.beforeTime = beforeTime;
		addQuery("before-time", String.valueOf(beforeTime));
		return this;
	}

	/**
	 * Results should have an amount greater than this value. MicroAlgos are the 
	 * default currency unless an asset-id is provided, in which case the asset will be 
	 * used. 
	 */
	public SearchForTransactions currencyGreaterThan(Long currencyGreaterThan) {
		this.currencyGreaterThan = currencyGreaterThan;
		addQuery("currency-greater-than", String.valueOf(currencyGreaterThan));
		return this;
	}

	/**
	 * Results should have an amount less than this value. MicroAlgos are the default 
	 * currency unless an asset-id is provided, in which case the asset will be used. 
	 */
	public SearchForTransactions currencyLessThan(Long currencyLessThan) {
		this.currencyLessThan = currencyLessThan;
		addQuery("currency-less-than", String.valueOf(currencyLessThan));
		return this;
	}

	/**
	 * Combine with address and address-role parameters to define what type of address 
	 * to search for. The close to fields are normally treated as a receiver, if you 
	 * would like to exclude them set this parameter to true. 
	 */
	public SearchForTransactions excludeCloseTo(Boolean excludeCloseTo) {
		this.excludeCloseTo = excludeCloseTo;
		addQuery("exclude-close-to", String.valueOf(excludeCloseTo));
		return this;
	}

	/**
	 * Maximum number of results to return. 
	 */
	public SearchForTransactions limit(Long limit) {
		this.limit = limit;
		addQuery("limit", String.valueOf(limit));
		return this;
	}

	/**
	 * Include results at or before the specified max-round. 
	 */
	public SearchForTransactions maxRound(Long maxRound) {
		this.maxRound = maxRound;
		addQuery("max-round", String.valueOf(maxRound));
		return this;
	}

	/**
	 * Include results at or after the specified min-round. 
	 */
	public SearchForTransactions minRound(Long minRound) {
		this.minRound = minRound;
		addQuery("min-round", String.valueOf(minRound));
		return this;
	}

	/**
	 * The next page of results. Use the next token provided by the previous results. 
	 */
	public SearchForTransactions next(String next) {
		this.next = next;
		addQuery("next", String.valueOf(next));
		return this;
	}

	/**
	 * Specifies a prefix which must be contained in the note field. 
	 */
	public SearchForTransactions notePrefix(String notePrefix) {
		this.notePrefix = notePrefix;
		addQuery("note-prefix", String.valueOf(notePrefix));
		return this;
	}

	/**
	 * Include results for the specified round. 
	 */
	public SearchForTransactions round(Long round) {
		this.round = round;
		addQuery("round", String.valueOf(round));
		return this;
	}

	/**
	 * SigType filters just results using the specified type of signature: * sig - 
	 * Standard * msig - MultiSig * lsig - LogicSig 
	 */
	public SearchForTransactions sigType(String sigType) {
		this.sigType = sigType;
		addQuery("sig-type", String.valueOf(sigType));
		return this;
	}

	/**
	 * Lookup the specific transaction by ID. 
	 */
	public SearchForTransactions txId(String txId) {
		this.txId = txId;
		addQuery("tx-id", String.valueOf(txId));
		return this;
	}
	public SearchForTransactions txType(String txType) {
		this.txType = txType;
		addQuery("tx-type", String.valueOf(txType));
		return this;
	}

	@Override
	public Response<TransactionsResponse> execute() throws Exception {
		Response<TransactionsResponse> resp = baseExecute();
		resp.setValueType(TransactionsResponse.class);
		return resp;
	}
	protected QueryData getRequestString() {
		addPathSegment(String.valueOf("transactions"));

		return qd;
	}
}