package algod

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// GetPendingTransactions /v2/transactions/pending
// Get the list of pending transactions, sorted by priority, in decreasing order,
// truncated at the end at MAX. If MAX = 0, returns all pending transactions.
type GetPendingTransactions struct {
	c *Client
	p models.GetPendingTransactionsParams
}

// Max truncated number of transactions to display. If max=0, returns all pending
// txns.
func (s *GetPendingTransactions) Max(max uint64) *GetPendingTransactions {
	s.p.Max = max
	return s
}

// Do performs HTTP request
func (s *GetPendingTransactions) Do(ctx context.Context,
	headers ...*common.Header) (response models.PendingTransactionsResponse, err error) {
	err = s.c.get(ctx, &response,
		"/v2/transactions/pending", s.p, headers)
	return
}
