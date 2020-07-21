package algod

import (
	"context"
	"fmt"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// GetPendingTransactionsByAddress /v2/accounts/{address}/transactions/pending
// Get the list of pending transactions by address, sorted by priority, in
// decreasing order, truncated at the end at MAX. If MAX = 0, returns all pending
// transactions.
type GetPendingTransactionsByAddress struct {
	c       *Client
	p       models.GetPendingTransactionsByAddressParams
	address string
}

// Max truncated number of transactions to display. If max=0, returns all pending
// txns.
func (s *GetPendingTransactionsByAddress) Max(max uint64) *GetPendingTransactionsByAddress {
	s.p.Max = max
	return s
}

// Do performs HTTP request
func (s *GetPendingTransactionsByAddress) Do(ctx context.Context,
	headers ...*common.Header) (response models.PendingTransactionsResponse, err error) {
	err = s.c.get(ctx, &response,
		fmt.Sprintf("/v2/accounts/%s/transactions/pending", s.address), s.p, headers)
	return
}
