package algod

import (
	"context"
	"fmt"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// PendingTransactionInformation /v2/transactions/pending/{txid}
// Given a transaction id of a recently submitted transaction, it returns
// information about it. There are several cases when this might succeed:
// - transaction committed (committed round > 0) - transaction still in the pool
// (committed round = 0, pool error = "") - transaction removed from pool due to
// error (committed round = 0, pool error != "")
// Or the transaction may have happened sufficiently long ago that the node no
// longer remembers it, and this will return an error.
type PendingTransactionInformation struct {
	c    *Client
	txid string
}

// Do performs HTTP request
func (s *PendingTransactionInformation) Do(ctx context.Context,
	headers ...*common.Header) (response models.PendingTransactionResponse, err error) {
	err = s.c.get(ctx, &response,
		fmt.Sprintf("/v2/transactions/pending/%s", s.txid), nil, headers)
	return
}
