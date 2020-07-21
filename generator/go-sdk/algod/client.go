package algod

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

const indexerAuthHeader = "X-Indexer-API-Token"

type Client common.Client

// get performs a GET request to the specific path against the server
func (c *Client) get(ctx context.Context, response interface{}, path string, request interface{}, headers []*common.Header) error {
	return (*common.Client)(c).Get(ctx, response, path, request, headers)
}

// MakeClient is the factory for constructing an IndexerClient for a given endpoint.
func MakeClient(address string, apiToken string) (c *Client, err error) {
	commonClient, err := common.MakeClient(address, indexerAuthHeader, apiToken)
	c = (*Client)(commonClient)
	return
}

// /v2/accounts/{address}
// Given a specific account public key, this call returns the accounts status,
// balance and spendable amounts
func (c *Client) AccountInformation(address string) *AccountInformation {
	return &AccountInformation{c: c, address: address}
}

// /v2/applications/{application-id}
// Given a application id, it returns application information including creator,
// approval and clear programs, global and local schemas, and global state.
func (c *Client) GetApplicationByID(applicationId uint64) *GetApplicationByID {
	return &GetApplicationByID{c: c, applicationId: applicationId}
}

// /v2/assets/{asset-id}
// Given a asset id, it returns asset information including creator, name, total
// supply and special addresses.
func (c *Client) GetAssetByID(assetId uint64) *GetAssetByID {
	return &GetAssetByID{c: c, assetId: assetId}
}

// /v2/blocks/{round}
// Get the block for the given round.
func (c *Client) GetBlock(round uint64) *GetBlock {
	return &GetBlock{c: c, round: round}
}

// /v2/transactions/pending
// Get the list of pending transactions, sorted by priority, in decreasing order,
// truncated at the end at MAX. If MAX = 0, returns all pending transactions.
func (c *Client) GetPendingTransactions() *GetPendingTransactions {
	return &GetPendingTransactions{c: c}
}

// /v2/accounts/{address}/transactions/pending
// Get the list of pending transactions by address, sorted by priority, in
// decreasing order, truncated at the end at MAX. If MAX = 0, returns all pending
// transactions.
func (c *Client) GetPendingTransactionsByAddress(address string) *GetPendingTransactionsByAddress {
	return &GetPendingTransactionsByAddress{c: c, address: address}
}

// /v2/status
// Gets the current node status.
func (c *Client) GetStatus() *GetStatus {
	return &GetStatus{c: c}
}

// /v2/ledger/supply
// Get the current supply reported by the ledger.
func (c *Client) GetSupply() *GetSupply {
	return &GetSupply{c: c}
}

// /health
// Returns OK if healthy.
func (c *Client) HealthCheck() *HealthCheck {
	return &HealthCheck{c: c}
}

// /metrics
// Return metrics about algod functioning.
func (c *Client) Metrics() *Metrics {
	return &Metrics{c: c}
}

// /v2/transactions/pending/{txid}
// Given a transaction id of a recently submitted transaction, it returns
// information about it. There are several cases when this might succeed:
// - transaction committed (committed round > 0) - transaction still in the pool
// (committed round = 0, pool error = "") - transaction removed from pool due to
// error (committed round = 0, pool error != "")
// Or the transaction may have happened sufficiently long ago that the node no
// longer remembers it, and this will return an error.
func (c *Client) PendingTransactionInformation(txid string) *PendingTransactionInformation {
	return &PendingTransactionInformation{c: c, txid: txid}
}

// /v2/transactions
// Broadcasts a raw transaction to the network.
func (c *Client) RawTransaction(rawtxn []byte) *RawTransaction {
	return &RawTransaction{c: c, rawtxn: rawtxn}
}

// /swagger.json
// Returns the entire swagger spec in json.
func (c *Client) SwaggerJSON() *SwaggerJSON {
	return &SwaggerJSON{c: c}
}

// /v2/teal/compile
// Given TEAL source code in plain text, return base64 encoded program bytes and
// base32 SHA512_256 hash of program bytes (Address style).
func (c *Client) TealCompile(source []byte) *TealCompile {
	return &TealCompile{c: c, source: source}
}

// /v2/teal/dryrun
// Executes TEAL program(s) in context and returns debugging information about the
// execution.
func (c *Client) TealDryrun(request models.DryrunRequest) *TealDryrun {
	return &TealDryrun{c: c, request: request}
}

// /v2/transactions/params
// Get parameters for constructing a new transaction
func (c *Client) TransactionParams() *TransactionParams {
	return &TransactionParams{c: c}
}

// /v2/status/wait-for-block-after/{round}
// Waits for a block to appear after round {round} and returns the node's status at
// the time.
func (c *Client) WaitForBlock(round uint64) *WaitForBlock {
	return &WaitForBlock{c: c, round: round}
}

