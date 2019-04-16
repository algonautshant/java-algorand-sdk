/*
 * for KMD HTTP API
 * API for KMD (Key Management Daemon)
 *
 * OpenAPI spec version: 0.0.1
 * Contact: contact@algorand.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.algorand.algosdk.kmd.client.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.ObjectUtils;

/**
 * APIV1POSTMultisigTransactionSignRequest is the request for &#x60;POST /v1/multisig/sign&#x60;
 */
@ApiModel(description = "APIV1POSTMultisigTransactionSignRequest is the request for `POST /v1/multisig/sign`")

public class SignMultisigRequest {
  @SerializedName("partial_multisig")
  private MultisigSig partialMultisig = null;

  @SerializedName("public_key")
  private PublicKey publicKey = null;

  @SerializedName("transaction")
  private byte[] transaction = null;

  @SerializedName("wallet_handle_token")
  private String walletHandleToken = null;

  @SerializedName("wallet_password")
  private String walletPassword = null;

  public SignMultisigRequest partialMultisig(MultisigSig partialMultisig) {
    this.partialMultisig = partialMultisig;
    return this;
  }

   /**
   * Get partialMultisig
   * @return partialMultisig
  **/
  @ApiModelProperty(value = "")
  public MultisigSig getPartialMultisig() {
    return partialMultisig;
  }

  public void setPartialMultisig(MultisigSig partialMultisig) {
    this.partialMultisig = partialMultisig;
  }

  public SignMultisigRequest publicKey(PublicKey publicKey) {
    this.publicKey = publicKey;
    return this;
  }

   /**
   * Get publicKey
   * @return publicKey
  **/
  @ApiModelProperty(value = "")
  public PublicKey getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(PublicKey publicKey) {
    this.publicKey = publicKey;
  }

  public SignMultisigRequest transaction(byte[] transaction) {
    this.transaction = transaction;
    return this;
  }

   /**
   * Get transaction
   * @return transaction
  **/
  @ApiModelProperty(value = "")
  public byte[] getTransaction() {
    return transaction;
  }

  public void setTransaction(byte[] transaction) {
    this.transaction = transaction;
  }

  public SignMultisigRequest walletHandleToken(String walletHandleToken) {
    this.walletHandleToken = walletHandleToken;
    return this;
  }

   /**
   * Get walletHandleToken
   * @return walletHandleToken
  **/
  @ApiModelProperty(value = "")
  public String getWalletHandleToken() {
    return walletHandleToken;
  }

  public void setWalletHandleToken(String walletHandleToken) {
    this.walletHandleToken = walletHandleToken;
  }

  public SignMultisigRequest walletPassword(String walletPassword) {
    this.walletPassword = walletPassword;
    return this;
  }

   /**
   * Get walletPassword
   * @return walletPassword
  **/
  @ApiModelProperty(value = "")
  public String getWalletPassword() {
    return walletPassword;
  }

  public void setWalletPassword(String walletPassword) {
    this.walletPassword = walletPassword;
  }


  @Override
  public boolean equals(java.lang.Object o) {
  if (this == o) {
    return true;
  }
  if (o == null || getClass() != o.getClass()) {
    return false;
  }
    SignMultisigRequest signMultisigRequest = (SignMultisigRequest) o;
    return ObjectUtils.equals(this.partialMultisig, signMultisigRequest.partialMultisig) &&
    ObjectUtils.equals(this.publicKey, signMultisigRequest.publicKey) &&
    ObjectUtils.equals(this.transaction, signMultisigRequest.transaction) &&
    ObjectUtils.equals(this.walletHandleToken, signMultisigRequest.walletHandleToken) &&
    ObjectUtils.equals(this.walletPassword, signMultisigRequest.walletPassword);
  }

  @Override
  public int hashCode() {
    return ObjectUtils.hashCodeMulti(partialMultisig, publicKey, transaction, walletHandleToken, walletPassword);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignMultisigRequest {\n");
    
    sb.append("    partialMultisig: ").append(toIndentedString(partialMultisig)).append("\n");
    sb.append("    publicKey: ").append(toIndentedString(publicKey)).append("\n");
    sb.append("    transaction: ").append(toIndentedString(transaction)).append("\n");
    sb.append("    walletHandleToken: ").append(toIndentedString(walletHandleToken)).append("\n");
    sb.append("    walletPassword: ").append(toIndentedString(walletPassword)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
