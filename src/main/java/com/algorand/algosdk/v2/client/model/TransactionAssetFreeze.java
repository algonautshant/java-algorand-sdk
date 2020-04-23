package com.algorand.algosdk.v2.client.model;

import java.util.Objects;

import com.algorand.algosdk.v2.client.common.PathResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Fields for an asset freeze transaction. 
 * Definition: 
 * data/transactions/asset.go : AssetFreezeTxnFields 
 */
public class TransactionAssetFreeze extends PathResponse {

	/**
	 * (fadd) Address of the account whose asset is being frozen or thawed. 
	 */	@JsonProperty("address")
	public String address;

	/**
	 * (faid) ID of the asset being frozen or thawed. 
	 */	@JsonProperty("asset-id")
	public Long assetId;

	/**
	 * (afrz) The new freeze status. 
	 */	@JsonProperty("new-freeze-status")
	public Boolean newFreezeStatus;

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null) return false;

		TransactionAssetFreeze other = (TransactionAssetFreeze) o;
		if (!Objects.deepEquals(this.address, other.address)) return false;
		if (!Objects.deepEquals(this.assetId, other.assetId)) return false;
		if (!Objects.deepEquals(this.newFreezeStatus, other.newFreezeStatus)) return false;

		return true;
	}
}
