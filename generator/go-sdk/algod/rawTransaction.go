package algod

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// RawTransaction /v2/transactions
// Broadcasts a raw transaction to the network.
type RawTransaction struct {
	c      *Client
	rawtxn []byte
}

// Do performs HTTP request
func (s *RawTransaction) Do(ctx context.Context,
	headers ...*common.Header) (response models.PostTransactionsResponse, err error) {
	err = s.c.post(ctx, &response,
		"/v2/transactions", s.rawtxn, headers)
	return
}
